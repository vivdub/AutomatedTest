package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper

//-----
class CustomSwipe: CommandNode("swipe_custom","Custom Swipe","Use this to trigger swipe from and to the specified positions"){
    override var configurable: Boolean = true
    init {
        configureNodeData(listOf(
            NodeData("Start Position: ", NodeData.ValueType.Coordinate),
            NodeData("End Position: ", NodeData.ValueType.Coordinate)
        ))
    }

    //-----
    override fun run() {
        if(nodeData.size>=2){
            val p1 = nodeData[0] as Pair<*, *>
            val p2 = nodeData[1] as Pair<*, *>
            AdbHelper.swipe(p1.first as Int, p1.second as Int, p2.first as Int, p2.second as Int)
        }
    }
}