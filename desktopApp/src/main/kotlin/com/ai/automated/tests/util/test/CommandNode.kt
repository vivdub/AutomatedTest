package com.ai.automated.tests.util.test

//--------
/** This class provides a way to store the operations */
open class CommandNode (val code:String, @Transient val title:String, @Transient val description:String) {

    @Transient var requiredData: List<NodeData> = listOf()
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

        //--
        fun all() = listOf(Click, Back, Input, SwipeUp, SwipeDown, SwipeLeft, SwipeRight, SwipeCustom, SwipeCustom)
    }

    //-----
    fun clone(): CommandNode{
        val node = CommandNode(code, title, description)
        node.requiredData = requiredData
        return node
    }

    //-----
    class NodeData(var name:String, var type: ValueType){
        enum class ValueType{ Coordinate, Input }
    }

    open fun isConfigurable(): Boolean = false
    fun configureNodeData(data: List<NodeData>) {this.requiredData=data}


    //-----
    class Click: CommandNode("click","Click","Use this to trigger click on an element"){
        init {
            configureNodeData(listOf(
                NodeData("Coordinates: ", NodeData.ValueType.Coordinate)
            ))
        }
        override fun isConfigurable(): Boolean = true
    }

    //-----
    class Back: CommandNode("back","Back","Use this to trigger back button"){}

    //-----
    class Input: CommandNode("input","Input","Use this to input the text"){
        override fun isConfigurable(): Boolean = true
        init {
            configureNodeData(listOf(NodeData("Input Text: ", NodeData.ValueType.Input)))
        }
    }

    //-----
    class SwipeUp: CommandNode("swipe_up","Swipe Up","Use this to trigger swipe up action on the screen"){}
    class SwipeDown: CommandNode("swipe_down","Swipe Down","Use this to trigger swipe down action on the screen"){}
    class SwipeLeft: CommandNode("swipe_left","Swipe Left","Use this to trigger left swipe on the screen"){}
    class SwipeRight: CommandNode("swipe_right","Swipe Right","Use this to trigger right swipe on the screen"){}

    //-----
    class CustomSwipe: CommandNode("swipe_custom","Custom Swipe","Use this to trigger swipe from and to the specified positions"){
        override fun isConfigurable(): Boolean = true
        init {
            configureNodeData(listOf(
                NodeData("Start Position: ", NodeData.ValueType.Coordinate),
                NodeData("End Position: ", NodeData.ValueType.Coordinate)
            ))
        }
    }
}