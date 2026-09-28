package com.ai.automated.tests.agent.tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.xml.UiElement
import com.ai.automated.tests.util.xml.findByText
import java.io.File
import java.lang.Thread.sleep

//--------
@LLMDescription("Tools to capture/dump screen and discover the UI elements on the screen")
/** @param onNewScreenDump Invoked when screenshot of new screen is taken */
class ElementOperations(private var element: UiElement, val onNewScreenDump:(File)->UiElement) : ToolSet {

    private var currentTestCase: TestCase? = null

    //----
    fun setRunningFor(case: TestCase){ currentTestCase = case }

    //----
    @Tool
    @LLMDescription("Tool to find the UI element on the screen and return the coordinates")
    fun findElement(text:String): Pair<Int, Int>{
        val points = mutableListOf<Int>()
        element.findByText(text)?.let { element ->
            element.bounds!!.substring(1, element.bounds.length - 1).split("][").forEach { bSplit ->
                bSplit.split(",").forEach { points.add(it.trim().toInt()) }
            }
        }
        val mid = Pair((points[0]+points[2])/2, (points[1]+points[3])/2)
        println("Found element $text at $mid")
        return mid
    }

    //----
    @Tool
    @LLMDescription("Tool to click the element at a particular coordinates")
    fun click(x:Int, y:Int) {
        AdbHelper.click(x,y)
        println("Clicked on the element: $x, $y")
    }

    //----
    @Tool
    @LLMDescription("Tool to send the type event to the input box")
    fun type(text:String, x:Int, y:Int) {
        println("Typed $text on the element at $x, $y")
        AdbHelper.input(text)
    }

    //----
    @Tool
    @LLMDescription("Execute this when some tool is missing and let us know")
    fun void(text:String) {
        println("Missing: $text ")
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to verify if the view exists on the screen")
    fun verify(text:String): Boolean {
        println("Verify: $text")
        val result = element.findByText(text)!=null
        currentTestCase?.successful = result
        return result
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to scroll down the screen")
    fun scrollDown() {
        println("Scrolled down")
        AdbHelper.swipeUp()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to scroll up the screen")
    fun scrollUp() {
        println("Scrolled up")
        AdbHelper.swipeDown()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to swipe right the screen")
    fun swipeRight() {
        println("Swipe right")
        AdbHelper.swipeRight()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to swipe left the screen")
    fun swipeLeft() {
        println("Swipe left")
        AdbHelper.swipeLeft()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to execute the swipe action on the screen when start(x,y) and end(x,y) coordinates are provided")
    fun swipe(sx:Int, sy:Int, ex:Int, ey:Int) {
        println("Swiped from: $sx,$sy  to $ex, $ey")
        AdbHelper.swipeDown()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to go back the screen or hit the back button")
    fun goBack(){
        AdbHelper.goBack()
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to capture/dump the screen")
    fun capture() {
        println("Capturing screen")
        AdbHelper.apply {
            dump()
            val file = pullDump()
            element = onNewScreenDump.invoke(file)
        }
    }

    //----
    @Tool
    @LLMDescription("Execute this when you have to wait for a few seconds")
    fun wait(time: Int) {
        println("Waiting for $time")
        sleep(time*1000L)
        println("Wait complete")
    }
}