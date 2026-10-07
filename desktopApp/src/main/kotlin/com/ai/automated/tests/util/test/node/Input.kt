package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper

//-----
class Input: CommandNode("input","Input","Use this to input the text"){
    override var configurable: Boolean = true
    init {
        configureNodeData(listOf(NodeData("Input Text: ", NodeData.ValueType.Input)))
    }

    //-----
    override fun send(values: List<Any>) {
        super.send(values)
        values.firstOrNull()?.let { value -> AdbHelper.input(value.toString()) }
    }
}