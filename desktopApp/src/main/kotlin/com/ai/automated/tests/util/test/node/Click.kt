package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.CanReceiveDataFromPrevNode

//-----
class Click: CommandNode("click","Click","Use this to trigger click on an element"), CanReceiveDataFromPrevNode {
    init {
        configureNodeData(listOf(
            NodeData("Coordinates: ", NodeData.ValueType.Coordinate)
        ))
    }
    override var configurable: Boolean = true
    var isReceivingDataFromPrevNode = false

    //-----
    override fun run() {
        nodeData.firstOrNull()?.let {value ->
            (value as Pair<*, *>).apply {
                val x = this.first as Int
                val y = this.second as Int
                AdbHelper.click(x,y)
            }
        }
    }

    //-----
    override fun clone(): CommandNode{
        return (super.clone() as Click).apply {
            setReceivingPrevNodeData(isReceivingDataFromPrevNode)
        }
    }

    //-----
    override fun isReceivingPrevNodeData(): Boolean = isReceivingDataFromPrevNode
    override fun setReceivingPrevNodeData(yes: Boolean) { isReceivingDataFromPrevNode = yes }
}