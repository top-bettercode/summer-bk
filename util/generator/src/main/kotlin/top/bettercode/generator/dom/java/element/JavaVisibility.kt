package top.bettercode.generator.dom.java.element

enum class JavaVisibility(val value: String) {
    PUBLIC("public "),
    PRIVATE("private "),
    PROTECTED("protected "),
    DEFAULT("")
}