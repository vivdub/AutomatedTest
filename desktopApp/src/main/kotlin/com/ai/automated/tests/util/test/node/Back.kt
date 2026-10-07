package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper

//-----
class Back: CommandNode("back","Back","Use this to trigger back button"){

    //-----
    override fun send(values: List<Any>) {
        AdbHelper.goBack()
    }
}