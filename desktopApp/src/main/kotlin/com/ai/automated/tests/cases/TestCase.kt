package com.ai.automated.tests.cases

import com.ai.automated.tests.util.test.CommandNode
import java.util.UUID

//--------
data class TestCase (val title:String ,val description:String, val timestamp: String = ""){
    val id: String = UUID.randomUUID().toString()
    var successful = false
    val commandNodes:MutableList<CommandNode> = mutableListOf()
}