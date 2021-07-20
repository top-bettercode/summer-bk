package top.bettercode.generator.dom.java.element

import top.bettercode.generator.dom.java.JavaType
import top.bettercode.generator.dom.java.JavaTypeOperator
import top.bettercode.generator.dom.java.StringOperator
import top.bettercode.generator.dom.java.StringOperator1

/**
 * This interface describes methods common to all Java compilation units (Java
 * classes, interfaces, and enums).
 *
 */
interface CompilationUnit {

    val formattedContent: String

    val importedTypes: MutableSet<JavaType>

    val staticImports: MutableSet<String>

    val superClass: JavaType?

    val isJavaInterface: Boolean

    val isJavaEnumeration: Boolean

    val type: JavaType

    val fileCommentLines: MutableList<String>

    fun comment(comment: StringOperator.() -> Unit) {
        comment(StringOperator(fileCommentLines))
    }

    fun import(import: JavaTypeOperator.() -> Unit) {
        import(JavaTypeOperator(importedTypes))
    }

    fun staticImport(staticImport: StringOperator1.() -> Unit) {
        staticImport(StringOperator1(this.staticImports))
    }

    /**
     * Comments will be written at the top of the file as is, we do not append any start or end remarks characters.
     *
     *
     * Note that in the Eclipse plugin, file comments will not be merged.
     *
     * @param commentLine
     * the remarks line
     */
    fun comment(vararg commentLine: String) {
        fileCommentLines.addAll(commentLine)
    }

    fun import(vararg fullTypeSpecification: String) {
        importedTypes.addAll(fullTypeSpecification.map { JavaType(it) }.filter { it.isExplicitlyImported && it.packageName != type.packageName })
    }

    fun import(vararg importedType: JavaType) {
        importedTypes.addAll(importedType.filter { it.isExplicitlyImported && it.packageName != type.packageName })
    }

    fun staticImport(vararg staticImport: String) {
        staticImports.addAll(staticImport)
    }
}
