package com.ai.automated.tests.agent

//----------
interface AppAIAgent {

    //------
    fun setSystemMessage(message:String): AppAIAgent
    fun setId(id:String): AppAIAgent
    fun setModel(model:String): AppAIAgent
    fun setKey(key:String): AppAIAgent

    //------
    fun build(): AppAIAgent
}