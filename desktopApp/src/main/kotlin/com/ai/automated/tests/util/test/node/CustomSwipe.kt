package com.ai.automated.tests.util.test.node

//-----
class CustomSwipe: CommandNode("swipe_custom","Custom Swipe","Use this to trigger swipe from and to the specified positions"){
    override var configurable: Boolean = true
    init {
        configureNodeData(listOf(
            NodeData("Start Position: ", NodeData.ValueType.Coordinate),
            NodeData("End Position: ", NodeData.ValueType.Coordinate)
        ))
    }
}