package com.ai.automated.tests.cases

import com.ai.automated.tests.util.test.node.CommandNode
import java.util.UUID

data class TestCase(
    val title: String,
    val description: String,
    val timestamp: String = "",
    val id: String = UUID.randomUUID().toString(),
    var successful: Boolean = false,
    val commandNodes: MutableList<CommandNode> = mutableListOf()
)
