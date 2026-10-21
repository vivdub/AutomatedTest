package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.test.node.properties.CanReceiveDataFromPrevNode

//--------
class AIModel : CommandNode("ai_model", "AI model", "Interact with an AI model"), CanReceiveDataFromPrevNode {

    private var receiving = false
    private var data: Any? = null

    //----
    override fun isReceivingPrevNodeData(): Boolean = receiving
    override fun setReceivingPrevNodeData(yes: Boolean) { receiving = yes }

    //----
    override fun setDataFromPrevNode(data: Any) { this.data = data }
}