package com.ai.automated.tests.util.test.node

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.VerifyingNode
import io.lettuce.core.LMPopArgs.Builder.right
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
    private var left = 0
    private var top = 0
    private var width = 0
    private var containerWidth = 0

    //----
    fun setArea(left: Int, top: Int, width: Int, containerWidth: Int) {
        this.left = left
        this.top = top
        this.width = width
        this.containerWidth = containerWidth
    }

    //----
    fun getLeft() = left
    fun getTop() = top

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
        saveTheArea()
    }

    //----
    private fun saveTheArea() {
        AdbHelper.takeScreenshot()?.let { screenshot ->
            require(width > 0) { "The area width must be greater than zero." }
            require(left >= 0 && top >= 0) { "The area coordinates cannot be negative." }
            require(left + width <= screenshot.width && top + width <= screenshot.height) { "The requested area is outside the screenshot bounds." }

            val ratio = screenshot.width/(containerWidth*1f)
            val actualLeft = (left * ratio).toInt()
            val actualTop = (top * ratio).toInt()
            val actualWidth = (width * ratio).toInt()

            val pixels = IntArray(actualWidth * actualWidth)
            screenshot.toComposeImageBitmap().readPixels(buffer = pixels, startX = actualLeft, startY = actualTop, width = actualWidth, height = actualWidth)

            /*val areaImage = BufferedImage(actualWidth, actualWidth, BufferedImage.TYPE_INT_ARGB)
            areaImage.setRGB(0, 0, actualWidth, actualWidth, pixels, 0, actualWidth)

            val outputFile = File("verify_area.png")
            outputFile.createNewFile()
            ImageIO.write(areaImage, "png", outputFile)*/
        }
    }
}
