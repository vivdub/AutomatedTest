package com.ai.automated.tests.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.GraphAIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.prompt.Prompt
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLMProvider.Companion
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.message.SystemMessageBuilder
import ai.koog.prompt.params.LLMParams
import com.ai.automated.tests.agent.AppAIAgent

//----------
class OpenRouterAgent : AppAIAgent {

    private lateinit var agent: GraphAIAgent<String, String>
    private var systemMessage = "You are an expert in text operation like Formatting and Replying."
    private var id = "text"
    private var model = ""
    private var key = ""

    //------
    override fun setSystemMessage(message: String): AppAIAgent {
        systemMessage = message
        return this
    }

    //------
    override fun setId(id: String): AppAIAgent {
        this.id=id
        return this
    }

    //------
    override fun setModel(model: String): AppAIAgent {
        this.model = model
        return this
    }

    //------
    override fun setKey(key: String): AppAIAgent {
        this.key = key
        return this
    }

    //------
    override fun build(): AppAIAgent {
        val prompt = Prompt(messages = listOf(SystemMessageBuilder().addText(systemMessage).build()), id = id, params = LLMParams(maxTokens = 2048),)
        val agentConfig = AIAgentConfig(prompt = prompt, model = getModelWithId(model), maxAgentIterations = 10,)
        agent = AIAgent(
            promptExecutor = simpleOpenRouterExecutor(key),
            agentConfig = agentConfig
        )
        return this
    }

    //------
    fun getModelWithId(id:String) = LLModel(
        provider = LLMProvider.OpenRouter,
        id = id,
        capabilities = listOf(
            LLMCapability.Temperature,
            LLMCapability.Speculation,
            LLMCapability.Tools,
            LLMCapability.Completion,
            LLMCapability.Schema.JSON.Standard,
            LLMCapability.ToolChoice
        ),
        contextLength = 128_000,
    )

    //------
    /*override suspend fun run(action: Action, text: String): String {
        return try{
            agent.run(
                StringBuilder().append(action.instruction).append("\n").append(text).toString()
            )
        }catch (e:Exception){
            e.message.toString()
        }
    }*/
}