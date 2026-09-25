package com.ai.automated.tests.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeExecuteTools
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.nodeLLMSendToolResults
import ai.koog.agents.core.dsl.extension.onTextMessage
import ai.koog.agents.core.dsl.extension.onToolCalls
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.Prompt
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.message.Message
import ai.koog.prompt.message.MessagePart
import ai.koog.prompt.message.SystemMessageBuilder
import ai.koog.prompt.params.LLMParams
import com.ai.automated.tests.agent.tools.ElementOperations
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.helper.AdbHelper.Companion.pullDump
import com.ai.automated.tests.util.xml.UiElement
import com.ai.automated.tests.util.xml.XMLParser
import com.ai.automated.tests.util.xml.findByText
import com.ai.writing.assistant.config.Credentials
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep

class AutomatedTest {

    private val parser = XMLParser()
    private lateinit var element: UiElement

    fun execute(): AutomatedTest{
        AdbHelper.apply {
            dump()
            val file = pullDump()
            element = parser.parseUiDump(file)
            /*element.findByText("Music")?.let {
                println("element found: ${it.bounds}")
            }*/
        }
        return this
    }

    suspend fun executeAgent(){
        val systemMessage = "You are an automated ui test agent, you have to divide the process into parts and execute the steps to perform the action and complete the test. " +
                "Before performing an operation you have to find the element. Example: if you have to click first find the element." +
                "If you have to type first find the element then type on it" +
                "Only return the text that is required, nothing more. If some action corresponding to node is missing then you can return that. " +
                "When every step is done, always answer with a short text summary of what you did. Never answer with an empty message."
        val agentConfig = AIAgentConfig(
            prompt = Prompt(
                messages = listOf(
                    SystemMessageBuilder().addText(systemMessage).build()
                ), id = "test", params = LLMParams(maxTokens = 1024)
            ),
            model = OpenRouterAgent().getModelWithId("openai/gpt-5.4-mini"),
            maxAgentIterations = 100,
        )
        val automatedAgentStrategy = strategy<String, String>("Automated AI Agent") {
            val nodeSendInput by nodeLLMRequest()
            val nodeExecuteTool by nodeExecuteTools()
            val nodeSendToolResult by nodeLLMSendToolResults()

            edge(nodeStart forwardTo nodeSendInput)
            // tool calls are checked first: a reply can carry both text and tool calls
            edge(nodeSendInput forwardTo nodeExecuteTool onToolCalls { true })
            edge(nodeSendInput forwardTo nodeFinish onTextMessage { true })
            edge(nodeExecuteTool forwardTo nodeSendToolResult)
            edge(nodeSendToolResult forwardTo nodeExecuteTool onToolCalls { true })
            edge(nodeSendToolResult forwardTo nodeFinish onTextMessage { true })
            // The model can reply with neither text nor tool calls (parts=[], finishReason=stop).
            // Without these catch-all edges the graph matches nothing and throws
            // AIAgentStuckInTheNodeException, so treat such a reply as "done".
            edge(nodeSendInput forwardTo nodeFinish transformed { it.finalText() })
            edge(nodeSendToolResult forwardTo nodeFinish transformed { it.finalText() })
        }

        val eleOperationsToolSet = ElementOperations(element){newDump -> parser.parseUiDump(newDump).also { element = it }}

        val agent = AIAgent(
            promptExecutor = simpleOpenRouterExecutor(Credentials.OPEN_ROUTER_API_KEY),
            agentConfig = agentConfig,
            strategy = automatedAgentStrategy,
            toolRegistry = ToolRegistry {
                tools(eleOperationsToolSet)
            }
        )
        val testCases = listOf(
            //TestCase("On the screen, find: Download, then click it. Wait for 2 seconds. Capture new screen. Verify if RazerRecordings exist on the screen. Go back."),
            //TestCase("On the screen, find: Music, then click it. Wait for 2 seconds. Capture new screen. Verify if RazerRecordings exist on the screen. Go back."),
            //TestCase("scroll down, wait for 1 second, capture screen, verify 'ui.xml' exist on screen"),
            TestCase("Click at (260,650), type 'viv@razer.com', click at (260, 720), type '12345', click (220,870), wait for 4 seconds, capture new screen, verify if 'Office' exists on screen.")
        )
        /*AdbHelper.apply {
            click(260,720)
            input("Hello world")
        }*/
        //val result = agent.run("On the login screen, find: email, type the email as test@test.com, find: password, type password as '12345', then find 'sign in' & click it")
        testCases.forEach {case ->
            eleOperationsToolSet.setRunningFor(case)
            val result = agent.run(case.description)
            println(result)
            println("******************** ${case.description} *****************")
            println("Passed: ${case.successful}")
        }
    }
}

//----------
/** Text of an assistant reply, with a readable placeholder when the model answered with nothing. */
private fun Message.Assistant.finalText(): String =
    parts.filterIsInstance<MessagePart.Text>()
        .joinToString("\n") { it.text }
        .ifBlank { "The model returned an empty reply (finishReason=$finishReason), treating the test as finished." }

fun main() = runBlocking {
    // Get the OpenRouter API key from the OPENROUTER_API_KEY environment variable
    AutomatedTest().execute().executeAgent()
    println()//executeAgent()
}