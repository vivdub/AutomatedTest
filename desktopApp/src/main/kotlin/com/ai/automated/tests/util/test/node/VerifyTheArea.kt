package com.ai.automated.tests.util.test.node

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.VerifyingNode
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

//--------
class VerifyTheArea : CommandNode(
    "verify_area",
    "Verify Area match",
    "Matches a particular section of the area taken from the image dump."
), VerifyingNode {

    override var configurable = true
    var left = 0
    var top = 0
    var width = 0

    //----
    init {
        configureNodeData(
            listOf(
                NodeData("Distance from center", NodeData.ValueType.Input)
            )
        )
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
    private fun saveTheArea() {
        AdbHelper.takeScreenshot()?.let { screenshot ->
            require(width > 0) {
                "The area width must be greater than zero."
            }
            require(left >= 0 && top >= 0) {
                "The area coordinates cannot be negative."
            }
            require(left + width <= screenshot.width && top + width <= screenshot.height) {
                "The requested area is outside the screenshot bounds."
            }

            val pixels = IntArray(width * width)
            screenshot.toComposeImageBitmap().readPixels(
                buffer = pixels,
                startX = left,
                startY = top,
                width = width,
                height = width
            )

            val areaImage = BufferedImage(
                width,
                width,
                BufferedImage.TYPE_INT_ARGB
            )
            areaImage.setRGB(
                0,
                0,
                width,
                width,
                pixels,
                0,
                width
            )

            val outputFile = File("data/dump/verify_area.png")
            outputFile.parentFile?.mkdirs()
            ImageIO.write(areaImage, "png", outputFile)
        }
    }
}
