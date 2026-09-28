package com.ai.automated.tests.cases

import java.util.UUID

//--------
data class TestCase (val title:String ,val description:String, val timestamp: String = ""){
    val id: String = UUID.randomUUID().toString()
    var successful = false
}