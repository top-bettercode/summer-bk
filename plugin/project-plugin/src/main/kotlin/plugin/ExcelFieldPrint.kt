package plugin

import top.bettercode.generator.dsl.JavaGenerator

/**
 * @author Peter Wu
 */
open class ExcelFieldPrint : JavaGenerator() {
    override fun content() {
    }

    override fun doCall() {
        println("""private final ExcelField<$className, ?>[] excelFields = ArrayUtil.of(""")
        val cols = columns
        val size = cols.size
        cols.forEachIndexed { i, it ->
            val code =
                if (it.isCodeField) {
                    if (it.columnName.contains("_") || extension.softDeleteColumnName == it.columnName) ".code()" else ".code(${(className + it.javaName.capitalize())}Enum.ENUM_NAME)"
                } else {
                    ""
                }
            val propertyGetter =
                if (it.isPrimary && primaryKeys.size > 1) "${it.javaType.shortNameWithoutTypeArguments}.class, from -> from.get${primaryKeyName.capitalize()}().get${it.javaName.capitalize()}()" else "$className::get${it.javaName.capitalize()}"
            println("      ExcelField.of(\"${it.remarks.split(Regex("[:：,， (（]"))[0]}\", $propertyGetter)${code}${if (i == size - 1) "" else ","}")
        }
        println("""  );""")
    }
}