package com.ai.automated.tests.util.test.node.properties

interface CanReceiveDataFromPrevNode {
    /** Returns if this node is actually receiving data from prev node or not since its not required */
    fun isReceivingPrevNodeData(): Boolean
    fun setReceivingPrevNodeData(yes: Boolean)
    fun setDataFromPrevNode(data:Any)
}