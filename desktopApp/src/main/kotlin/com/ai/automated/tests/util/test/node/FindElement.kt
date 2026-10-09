package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.OutputsData
import com.ai.automated.tests.util.xml.XMLParser
import com.ai.automated.tests.util.xml.findByText

//-----
class FindElement: CommandNode("find_ele","Find Element","Use this to trigger swipe from and to the specified positions"), OutputsData{

    override var configurable = true
    private val parser = XMLParser()
    private var point = Pair(0,0)

    init {
        configureNodeData(listOf(
            NodeData("Text to find: ", NodeData.ValueType.Input)
        ))
    }

    //-----
    override fun run() {
        AdbHelper.dump()
        val file = AdbHelper.pullDump()
        val element = parser.parseUiDump(file)
        val points = mutableListOf<Int>()
        element.findByText(nodeData.first().toString())?.let { element ->
            element.bounds!!.substring(1, element.bounds.length - 1).split("][").forEach { bSplit ->
                bSplit.split(",").forEach { points.add(it.trim().toInt()) }
            }
        }
        point = Pair((points[0]+points[2])/2, (points[1]+points[3])/2)
        println("foundt at: $point")
    }

    //-----
    override fun setOutputData(data: Any) {if(data is Pair<*, *>)point= data as Pair<Int,Int>}
    override fun getOutputData(): Any = point

    //-----
    override fun clone(): CommandNode {
        return (super.clone() as FindElement).apply {
            setOutputData(point)
        }
    }
}