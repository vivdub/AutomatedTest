package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper

//-----
class Click: CommandNode("click","Click","Use this to trigger click on an element"){
    init {
        configureNodeData(listOf(
            NodeData("Coordinates: ", NodeData.ValueType.Coordinate)
        ))
    }
    override var configurable: Boolean = true
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
}