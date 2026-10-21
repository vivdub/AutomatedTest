package com.ai.automated.tests.util.test.node

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.ai.automated.tests.util.test.node.properties.CreatesBoxOnScreen
import com.ai.automated.tests.util.test.node.properties.OutputsData
import java.awt.image.BufferedImage

//--------
class CaptureScreen:CommandNode("capture_screen", "Capture the screen", "Capture the screen or an area on it."), OutputsData, CreatesBoxOnScreen {

    override var configurable = true

    // for the box
    private var left = 0
    private var top = 0
    private var width = 0
    private var containerWidth = 0
    @Transient private var image: Any? = null

    //----
    init {
        configureNodeData(listOf(
            NodeData("Size", NodeData.ValueType.Input, true)
        ))
    }

    //----
    override fun getBoxSize(): Int = width
    override fun setBoxSize(width: Int) { this.width = width }

    //----
    override fun getStartOffset(): Int = left
    override fun setStartOffset(offset: Int) { left = offset }

    //----
    override fun getTopOffset(): Int = top
    override fun setTopOffset(offset: Int) { top = offset }

    //----
    override fun getParentContainerWidth(): Int = containerWidth
    override fun setParentContainerWidth(width: Int) { containerWidth = width }

    //----
    override fun setOutputData(data: Any) {
        if(data is BufferedImage){
            image = when(nodeData.isEmpty()){
                true -> data.toComposeImageBitmap()
                else -> {
                    try{
                        val screenshot = data.toComposeImageBitmap()

                        val ratio = screenshot.width/(containerWidth*1f)
                        val actualLeft = (left * ratio).toInt()
                        val actualTop = (top * ratio).toInt()
                        val actualWidth = (width * ratio).toInt()

                        val pixels = IntArray(actualWidth*actualWidth)
                        data.toComposeImageBitmap().readPixels(pixels, actualLeft, actualTop, actualWidth, actualWidth)

                        val croppedImage = BufferedImage(actualWidth, actualWidth, BufferedImage.TYPE_INT_ARGB)
                        croppedImage.setRGB(0,0,actualWidth, actualWidth, pixels, 0, actualWidth)

                        croppedImage
                    }catch (e: Exception){null}
                }
            }
        }
    }

    //----
    override fun getOutputData(): Any? = image
}