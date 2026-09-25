package com.ai.automated.tests.util.xml

data class UiElement(
    val text: String?,
    val resourceId: String?,
    val className: String?,
    val contentDescription: String?,
    val clickable: Boolean,
    val enabled: Boolean,
    val bounds: String?,
    val children: List<UiElement>
)

fun UiElement.findByText(text: String): UiElement? {
    var found = children.firstOrNull { it.text == text }
    if(found != null) return found
    children.forEach { child ->
        found = child.findByText(text)
        if(found != null) return found
    }
    return null
}