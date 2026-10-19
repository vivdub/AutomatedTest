package com.ai.automated.tests.util.test.node

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.VerifyingNode
import javax.swing.Spring.height

//--------
class VerifyTheArea: CommandNode("verify_area", "Verify Area match", "Matches a particular section of the area taken from the image dump."), VerifyingNode {

    override var configurable = true
    var left = 0
    var top = 0
    var width = 0

    //----
    init {
        configureNodeData(listOf(
            NodeData("Distance from center", NodeData.ValueType.Input)
        ))
    }

    //----
    override fun isVerified(): Boolean {
        return true
    }

    //----
    override fun setData(values: List<Any>) {
        super.setData(values)

    }

    //----
    private fun saveTheArea(){
        AdbHelper.takeScreenshot()?.let { screenshot ->
            val arr = IntArray(width * width)
            screenshot.toComposeImageBitmap().readPixels(arr, left,  top, width, width)
            ImageBitmap(width, width)
        }
    }
}