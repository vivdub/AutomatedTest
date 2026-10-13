package com.ai.automated.tests.util.test.node

import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.properties.VerifyingNode
import com.ai.automated.tests.util.xml.XMLParser
import com.ai.automated.tests.util.xml.findByText

//--------
class VerifyElementExistence: CommandNode("verify_existence", "Verify Existence", "Verify if the element exists on the UI."), VerifyingNode {

    override var configurable = true
    private val xmlParser = XMLParser()
    private var verified = false

    //----
    init {
        configureNodeData(listOf(NodeData("Element Name", NodeData.ValueType.Input)))
    }

    //----
    override fun run() {
        verified = false
        if(nodeData.isNotEmpty()){
            val text = nodeData.first().toString()
            AdbHelper.dump()
            val file = AdbHelper.pullDump()
            val element = xmlParser.parseUiDump(file).findByText(text)
            verified = element!=null
        }
    }

    //----
    override fun isVerified(): Boolean = verified
}