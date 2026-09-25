package com.ai.automated.tests.util.xml
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class XMLParser {

    fun parseUiDump(file: File): UiElement {
        val document = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)

        document.documentElement.normalize()

        return parseElement(document.documentElement)
    }

    private fun parseElement(element: Element): UiElement {

        val children = mutableListOf<UiElement>()

        for (i in 0 until element.childNodes.length) {
            val node = element.childNodes.item(i)

            if (node is Element && node.tagName == "node") {
                children += parseElement(node)
            }
        }

        return UiElement(
            text = element.getAttribute("text").takeIf { it.isNotEmpty() },
            resourceId = element.getAttribute("resource-id")
                .takeIf { it.isNotEmpty() },
            className = element.getAttribute("class")
                .takeIf { it.isNotEmpty() },
            contentDescription = element.getAttribute("content-desc")
                .takeIf { it.isNotEmpty() },
            clickable = element.getAttribute("clickable") == "true",
            enabled = element.getAttribute("enabled") != "false",
            bounds = element.getAttribute("bounds")
                .takeIf { it.isNotEmpty() },
            children = children
        )
    }
}