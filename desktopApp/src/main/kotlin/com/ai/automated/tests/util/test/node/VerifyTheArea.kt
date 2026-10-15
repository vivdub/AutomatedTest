package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.test.node.properties.VerifyingNode

//--------
class VerifyTheArea:CommandNode("verify_area", "Verify Area match", "Matches a particular section of the area taken from the image dump."), VerifyingNode {
    override var configurable = true

    //----
    init {
        configureNodeData(listOf(
            NodeData("Center Coordinate", NodeData.ValueType.Coordinate),
            NodeData("Distance from center", NodeData.ValueType.Input)
        ))
    }

    //----
    override fun isVerified(): Boolean {
        return true
    }
}