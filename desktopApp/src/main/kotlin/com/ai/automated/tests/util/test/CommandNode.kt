package com.ai.automated.tests.util.test

//--------
/** This class provides a way to store the operations */
data class CommandNode (val code:String, @Transient val title:String, @Transient val description:String, val values: MutableList<String>, val configurable:Boolean = true) {
    companion object{
        val Click = CommandNode("click","Click","Use this to trigger click on an element", mutableListOf())
        val Back = CommandNode("back","Go Back","Use this to execute the back button", mutableListOf(), false)
        val Input = CommandNode("input","Input","Use this to input the text", mutableListOf())
        val SwipeUp = CommandNode("swipe_up","Swipe Up","Use this to trigger swipe up action on the screen", mutableListOf(), false)
        val SwipeDown = CommandNode("swipe_down","Swipe Down","Use this to trigger swipe down action on the screen", mutableListOf(), false)
        val SwipeLeft = CommandNode("swipe_left","Swipe Left","Use this to trigger left swipe on the screen", mutableListOf(), false)
        val SwipeRight = CommandNode("swipe_right","Swipe Right","Use this to trigger right swipe on the screen", mutableListOf(), false)
        val SwipeCustom = CommandNode("swipe_custom","Custom Swipe","Use this to trigger swipe from and to the specified positions", mutableListOf())

        //--
        fun all() = listOf(Click, Back, Input, SwipeUp, SwipeDown, SwipeLeft, SwipeRight, SwipeCustom, SwipeCustom)
    }
}