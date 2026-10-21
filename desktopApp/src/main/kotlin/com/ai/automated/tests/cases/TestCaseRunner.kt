package com.ai.automated.tests.cases

import com.ai.automated.tests.util.test.node.CommandNode
import com.ai.automated.tests.util.test.node.properties.CanReceiveDataFromPrevNode
import com.ai.automated.tests.util.test.node.properties.OutputsData
import com.ai.automated.tests.util.test.node.properties.VerifyingNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

//--------
class TestCaseRunner {

    data class VerificationResult(var total:Int, var passed:Int)

    var verificationResult: VerificationResult? = null

    //----
    fun execute(commands: List<CommandNode>, onRunningNode:(CommandNode?)->Unit){
        val dataMap = mutableMapOf<Int, Any?>()
        var start = 0L
        var verifyingNodes = 0
        var verifiedNodes = 0
        verificationResult = null
        CoroutineScope(Dispatchers.IO).launch {
            commands.forEachIndexed { index, command ->
                onRunningNode.invoke(command)
                start = System.currentTimeMillis()
                command.run()
                when (command) {
                    is OutputsData -> dataMap[index] = command.getOutputData()
                    is CanReceiveDataFromPrevNode -> if(command.isReceivingPrevNodeData() && index>0) {
                        dataMap[index-1]?.let{ command.setDataFromPrevNode(it) }
                        command.run()
                    }
                }
                if(command is VerifyingNode) {
                    verifyingNodes++
                    if(command.isVerified()) verifiedNodes++
                }
                if(System.currentTimeMillis() - start < 1000) sleep(1000)
            }
            if(verifyingNodes>0) verificationResult = VerificationResult(verifyingNodes, verifiedNodes)
            onRunningNode.invoke(null)
        }
    }
}