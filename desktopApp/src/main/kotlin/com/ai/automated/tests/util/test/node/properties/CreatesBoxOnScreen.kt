package com.ai.automated.tests.util.test.node.properties

//--------
interface CreatesBoxOnScreen {
    fun getBoxSize(): Int
    fun setBoxSize(width: Int)
    fun getStartOffset(): Int
    fun setStartOffset(offset: Int)
    fun getTopOffset(): Int
    fun setTopOffset(offset: Int)
    fun setParentContainerWidth(width: Int)
    fun getParentContainerWidth(): Int
}