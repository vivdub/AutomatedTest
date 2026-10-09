package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper

/** This class provides a way to store the operations */
open class CommandNode (val code:String, @Transient val title:String, @Transient val description:String) {

    @Transient var requiredData: List<NodeData> = listOf()
    @Transient
    open var configurable = false
    var nodeData: MutableList<Any> = mutableListOf()

    companion object{
        val Click = Click()
        val Back = Back()
        val Input = Input()
        val SwipeUp = SwipeUp()
        val SwipeDown = SwipeDown()
        val SwipeLeft = SwipeLeft()
        val SwipeRight = SwipeRight()
        val SwipeCustom = CustomSwipe()
        val FindElement = FindElement()

        //--
        fun all() = listOf(Click, Back, Input, SwipeUp, SwipeDown, SwipeLeft, SwipeRight, SwipeCustom, SwipeCustom,
            FindElement
        )
    }

    //-----
    open fun clone(): CommandNode{
        val node = when (this) {
            is Click -> Click()
            is Back -> Back()
            is Input -> Input()
            is SwipeUp -> SwipeUp()
            is SwipeDown -> SwipeDown()
            is SwipeLeft -> SwipeLeft()
            is SwipeRight -> SwipeRight()
            is CustomSwipe -> CustomSwipe()
            is FindElement -> FindElement()
            else -> CommandNode(code, title, description)
        }
        node.nodeData.addAll(nodeData)
        node.requiredData = requiredData
        node.configurable = configurable
        return node
    }

    //-----
    class NodeData(var name:String, var type: ValueType){
        enum class ValueType{ Coordinate, Input }
    }

    open fun isConfigurable(): Boolean = configurable
    fun configureNodeData(data: List<NodeData>) {this.requiredData=data}

    //-----
    fun setData(values: List<Any>){
        nodeData.clear()
        nodeData.addAll(values)
    }

    //-----
    open fun run() {}



    //-----
    class SwipeUp: CommandNode("swipe_up","Swipe Up","Use this to trigger swipe up action on the screen"){
        override fun run() { AdbHelper.swipeUp() }
    }

    //-----
    class SwipeDown: CommandNode("swipe_down","Swipe Down","Use this to trigger swipe down action on the screen"){
        override fun run() {AdbHelper.swipeDown()}
    }

    //-----
    class SwipeLeft: CommandNode("swipe_left","Swipe Left","Use this to trigger left swipe on the screen"){
        override fun run() { AdbHelper.swipeLeft() }
    }

    //-----
    class SwipeRight: CommandNode("swipe_right","Swipe Right","Use this to trigger right swipe on the screen"){
        override fun run() { AdbHelper.swipeRight() }
    }

    //-----
    class CaptureScreen: CommandNode("capture_screen","Capture Screen","Capture current screen"){
        override fun run() { AdbHelper.dump() }
    }
}