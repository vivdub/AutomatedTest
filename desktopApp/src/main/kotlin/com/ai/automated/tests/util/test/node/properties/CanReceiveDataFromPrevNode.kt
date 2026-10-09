package com.ai.automated.tests.util.test.node.properties

interface CanReceiveDataFromPrevNode {
    fun isReceivingPrevNodeData(): Boolean
    fun setReceivingPrevNodeData(yes: Boolean)
}