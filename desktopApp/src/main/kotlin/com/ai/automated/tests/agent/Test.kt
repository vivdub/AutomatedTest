package com.ai.writing.assistant.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.agent.functionalStrategy
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeExecuteTools
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.nodeLLMSendToolResults
import ai.koog.agents.core.dsl.extension.onTextMessage
import ai.koog.agents.core.dsl.extension.onToolCalls
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.prompt.Prompt
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openrouter.OpenRouterLLMClient
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.message.MessagePart
import ai.koog.prompt.message.SystemMessageBuilder
import ai.koog.prompt.params.LLMParams
import com.ai.writing.assistant.config.Credentials
import kotlinx.coroutines.runBlocking
import kotlin.collections.emptyList

class Test {
}

@Tool
@LLMDescription("Use this tool whenever you need to ask the human user a question. " +
        "The tool prints the question and waits for the user's answer. " +
        "You MUST use this tool to ask questions instead of writing a question " +
        "in your response. If your response ends with ? use this tool.")
fun askUser(@LLMDescription("Question from the agent") question: String): String {
    println("Tool: "+question)
    return readln()
}


@LLMDescription("Tools for performing math operations")
class MathTools : ToolSet {
    @Tool
    @LLMDescription("Adds two numbers and returns the result")
    fun add(a: Int, b: Int): Int {
        // This is not necessary, but it helps to see the tool call in the console output
        println("Adding $a and $b...")
        return a + b
    }
    @Tool
    @LLMDescription("Multiplies two numbers and returns the result")
    fun multiply(a: Int, b: Int): Int {
        // This is not necessary, but it helps to see the tool call in the console output
        println("Multiplying $a and $b...")
        return a * b
    }
}

fun main() = runBlocking {
    // Get the OpenRouter API key from the OPENROUTER_API_KEY environment variable
    val apiKey = Credentials.OPEN_ROUTER_API_KEY/*System.getenv("OPENROUTER_API_KEY")*/ ?: error("The API key is not set.")

    // Koog sends no max_tokens unless you set LLMParams.maxTokens, and OpenRouter
    // then reserves the model's full ceiling (16384 for gpt-4o). It checks your
    // balance against that reservation, not against actual usage, so a free-tier
    // account gets HTTP 402 before a single token is generated. Setting it
    // explicitly is what makes the request affordable.
    /*val agentConfig = AIAgentConfig(prompt = Prompt(messages = emptyList(), id = "test", params = LLMParams(maxTokens = 1024)),
        model = OpenRouterModels.GPT4o,
        maxAgentIterations = 10,
    )
    val agent2 = AIAgent(
        promptExecutor = simpleOpenRouterExecutor(apiKey),
        agentConfig = agentConfig
    )
    // Run the agent
    val result = agent2.run("You are an expert in internet memes. Be helpful, friendly, and answer user questions concisely, showing your knowledge of memes.")
    println(result)*/

    /*val agentConfig = AIAgentConfig(prompt = Prompt(messages = listOf(
        SystemMessageBuilder().addText(
            "You are an expert in internet memes. Be helpful, friendly, and answer user questions concisely, showing your knowledge of memes.").build()
    ), id = "test", params = LLMParams(maxTokens = 1024)),
        model = OpenRouterModels.GPT4o,
        maxAgentIterations = 10,
    )
    val basicAgent = AIAgent(
        promptExecutor = simpleOpenRouterExecutor(apiKey),
        agentConfig = agentConfig,
        toolRegistry = ToolRegistry {
            tool(::askUser)
        },
    )
    val userInput = """
    You must ask the user a question before answering.
    
    Ask the user what their favorite animal is.
    You MUST use the askUser tool to do this.
    Do not answer until you have called the tool.
    """.trimIndent()
    val result = basicAgent.run("The user said: $userInput")
    println(result)*/


    /*
    val calculatorAgentStrategy = strategy<String, String>("Simple calculator") {
        val nodeSendInput by nodeLLMRequest()
        val nodeExecuteTool by nodeExecuteTools()
        val nodeSendToolResult by nodeLLMSendToolResults()

        edge(nodeStart forwardTo nodeSendInput)
        edge(nodeSendInput forwardTo nodeFinish onTextMessage { true })
        edge(nodeSendInput forwardTo nodeExecuteTool onToolCalls { true })
        edge(nodeExecuteTool forwardTo nodeSendToolResult)
        edge(nodeSendToolResult forwardTo nodeFinish onTextMessage { true })
        edge(nodeSendToolResult forwardTo nodeExecuteTool onToolCalls { true })
    }

    val agentConfig = AIAgentConfig(prompt = Prompt(messages = listOf(
        SystemMessageBuilder().addText(
            "You are an expert calculator.").build()
    ), id = "test", params = LLMParams(maxTokens = 1024)),
        model = OpenRouterModels.GPT4o,
        maxAgentIterations = 10,
    )
    val mathAgent = AIAgent(
        promptExecutor = simpleOpenRouterExecutor(apiKey),
        agentConfig = agentConfig,
        strategy = calculatorAgentStrategy,
        toolRegistry = ToolRegistry {
            tools(MathTools())
        }
    )
    val result = mathAgent.run("Multiply 3 by 4, then multiply the result by 5, then add 10, then add 123.")
    println(result)*/




    val agentConfig = AIAgentConfig(prompt = Prompt(messages = listOf(
        SystemMessageBuilder().addText(
            "You are an expert calculator.").build()
    ), id = "test", params = LLMParams(maxTokens = 1024)),
        model = OpenRouterModels.GPT4o,
        maxAgentIterations = 10,
    )
    val strategy = functionalStrategy<String, String> { input ->
        val response = requestLLM(input)
        response.parts.filterIsInstance<MessagePart.Text>().joinToString("\n") { it.text }
    }

    val mathAgent = AIAgent(
        promptExecutor = simpleOpenRouterExecutor(apiKey),
        agentConfig = agentConfig,
        strategy = strategy
    )
    val result = mathAgent.run("Multiply 3 by 4, then multiply the result by 5, then add 10, then add 123.")
    println(result)
}