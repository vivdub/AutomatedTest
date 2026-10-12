package com.ai.automated.tests.util.test.node

import java.lang.Thread.sleep

//-----
class Wait: CommandNode("wait", "Wait", "Wait for the specified time") {
    override var configurable = true
    init {
        configureNodeData(listOf(
            NodeData("Seconds: ", NodeData.ValueType.Input)
        ))
    }

    //-----
    override fun run() {
        val time = nodeData.first().toString().toLong()
        sleep(time*1000)
    }
}