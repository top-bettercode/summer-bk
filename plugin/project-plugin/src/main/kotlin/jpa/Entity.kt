import top.bettercode.generator.dom.java.JavaType
import top.bettercode.generator.dom.java.element.InnerClass
import top.bettercode.generator.dom.java.element.JavaVisibility
import top.bettercode.generator.dom.java.element.Parameter

/**
 * @author Peter Wu
 */
class Entity : ModuleJavaGenerator() {

    override var cover: Boolean = true
    override val type: JavaType
        get() = entityType

    override fun content() {
        clazz {
            annotation("@org.hibernate.annotations.DynamicInsert")
            annotation("@org.hibernate.annotations.DynamicUpdate")
            annotation("@javax.persistence.Entity")
            annotation("@javax.persistence.Table(name = \"$tableName\")")
            import("org.springframework.data.jpa.domain.support.AuditingEntityListener")
            annotation("@javax.persistence.EntityListeners(AuditingEntityListener.class)")
            javadoc {
                +"/**"
                +" * $remarks 对应表名：$tableName"
                +" */"
            }
            implement {
                +"java.io.Serializable"
            }
            serialVersionUID()

            //constructor no args
            constructor {}
            //constructor with id
            constructor(Parameter(primaryKeyName, primaryKeyType)) {
                +"this.${primaryKeyName} = ${primaryKeyName};"
            }

            //primaryKey
            field(primaryKeyName, primaryKeyType) {
                if (primaryKeys.size == 1) {
                    if (primaryKey.remarks.isNotBlank() || !primaryKey.columnDef.isNullOrBlank())
                        javadoc {
                            +"/**"
                            +" * ${getRemark(primaryKey)}"
                            +" */"
                        }

                    annotation("@javax.persistence.Id")
                    if (primaryKey.autoIncrement) {
                        import("javax.persistence.GenerationType")
                        annotation("@javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)")
                    }
                } else {
                    javadoc {
                        +"/**"
                        +" * ${remarks}主键"
                        +" */"
                    }
                    annotation("@javax.persistence.EmbeddedId")
                }
            }
            //primaryKey getter
            method("get${primaryKeyName.capitalize()}", primaryKeyType) {
                javadoc {
                    +"/**"
                    +" * ${remarks}主键"
                    +" */"
                }
                +"return ${primaryKeyName};"
            }
            //primaryKey setter
            method("set${primaryKeyName.capitalize()}") {
                javadoc {
                    +"/**"
                    +" * ${remarks}主键"
                    +" */"
                }
                parameter {
                    type = primaryKeyType
                    name = primaryKeyName
                }
                +"this.${primaryKeyName} = ${primaryKeyName};"
            }

            otherColumns.forEach {
                //field
                field(it.javaName, it.javaType) {
                    if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                        javadoc {
                            +"/**"
                            +" * ${getRemark(it)}"
                            +" */"
                        }
                    var columnAnnotation =
                        "@javax.persistence.Column(name = \"${it.columnName}\", columnDefinition = \"${it.typeDesc}${it.defaultDesc}${if (it.extra.isBlank()) "" else " ${it.extra}"}\""
                    if (it.columnSize > 0 && it.columnSize != 255 || !it.nullable) {
                        if (it.columnSize > 0 && it.columnSize != 255) {
                            columnAnnotation += ", length = ${it.columnSize}"
                        }
                        if (!it.nullable) {
                            columnAnnotation += ", nullable = false"
                        }
                    }
                    columnAnnotation += ")"
                    annotation(columnAnnotation)
                    if (it.javaName == "createdDate") {
                        annotation("@org.springframework.data.annotation.CreatedDate")
                    }
                    if (it.extra.contains("ON UPDATE CURRENT_TIMESTAMP")) {
                        annotation("@org.springframework.data.annotation.LastModifiedDate")
                    }
                    if (it.javaName == "version") {
                        annotation("@javax.persistence.Version")
                    }
                    if (it.isSoftDelete) {
                        annotation("@top.bettercode.simpleframework.data.jpa.SoftDelete")
                    }
                }

                //getter
                method("get${it.javaName.capitalize()}", it.javaType) {
                    if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                        javadoc {
                            +"/**"
                            +" * ${getReturnRemark(it)}"
                            +" */"
                        }
                    +"return ${it.javaName};"
                }
                //setter
                method("set${it.javaName.capitalize()}") {
                    if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                        javadoc {
                            +"/**"
                            +" * ${getParamRemark(it)}"
                            +" */"
                        }
                    parameter {
                        type = it.javaType
                        name = it.javaName
                    }
                    +"this.${it.javaName} = ${it.javaName};"
                }
            }


            //toString
            method("toString", JavaType.stringInstance) {
                annotation("@Override")
                +"return \"${className}{\" +"
                +"    \"${primaryKeyName}='\" + $primaryKeyName + '\\'' +"
                otherColumns.forEachIndexed { i, it ->
                    +"    \"${if (i > 0) ", " else ""}${it.javaName}=${if (it.javaType == JavaType.stringInstance) "'" else ""}\" + ${it.javaName} ${if (it.javaType == JavaType.stringInstance) "+ '\\'' " else ""}+"
                }
                +"    '}';"
            }
            if (compositePrimaryKey) {
                val keySep = "_"
                import("javax.persistence.Embeddable")
                val innerClass = InnerClass(JavaType("${className}Key"))
                innerClass(innerClass)
                innerClass.apply {
                    visibility = JavaVisibility.PUBLIC
                    isStatic = true
                    annotation("@javax.persistence.Embeddable")
                    javadoc {
                        +"/**"
                        +" * $remarks 主键 对应表名：$tableName"
                        +" */"
                    }
                    implement {
                        +"java.io.Serializable"
                    }
                    serialVersionUID()

                    //constructor no args
                    constructor {}
                    //constructor with key String
                    import("org.springframework.util.Assert")
                    constructor(Parameter(primaryKeyName, JavaType.stringInstance)) {
                        +"Assert.hasText(${primaryKeyName},\"${primaryKeyName}不能为空\");"
                        +"String[] split = ${primaryKeyName}.split(\"${keySep}\");"
                        +"Assert.isTrue(split.length==${primaryKeys.size},\"${primaryKeyName}格式不对\");"
                        primaryKeys.forEachIndexed { index, column ->
                            +"this.${column.javaName} = ${column.setValue("split[${index}]")};"
                        }
                    }

                    constructor {
                        primaryKeys.forEach { column ->
                            parameter(column.javaType, column.javaName)
                            +"this.${column.javaName} = ${column.javaName};"
                        }
                    }

                    primaryKeys.forEach {
                        //field
                        field(it.javaName, it.javaType) {
                            if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                                javadoc {
                                    +"/**"
                                    +" * ${getRemark(it)}"
                                    +" */"
                                }

                            var columnAnnotation =
                                "@javax.persistence.Column(name = \"${it.columnName}\", columnDefinition = \"${it.typeDesc}${it.defaultDesc}${if (it.extra.isBlank()) "" else " ${it.extra}"}\""
                            if (it.columnSize > 0 && it.columnSize != 255 || !it.nullable) {
                                if (it.columnSize > 0 && it.columnSize != 255) {
                                    columnAnnotation += ", length = ${it.columnSize}"
                                }
                                if (!it.nullable) {
                                    columnAnnotation += ", nullable = false"
                                }
                            }
                            columnAnnotation += ")"
                            annotation(columnAnnotation)
                        }

                        //getter
                        method("get${it.javaName.capitalize()}", it.javaType) {
                            if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                                javadoc {
                                    +"/**"
                                    +" * ${getReturnRemark(it)}"
                                    +" */"
                                }
                            +"return ${it.javaName};"
                        }
                        //setter
                        method("set${it.javaName.capitalize()}") {
                            if (it.remarks.isNotBlank() || !it.columnDef.isNullOrBlank())
                                javadoc {
                                    +"/**"
                                    +" * ${getParamRemark(it)}"
                                    +" */"
                                }
                            parameter {
                                type = it.javaType
                                name = it.javaName
                            }
                            +"this.${it.javaName} = ${it.javaName};"
                        }
                    }
                    //equals
                    import("java.util.Objects")
                    method(
                        "equals",
                        JavaType.booleanPrimitiveInstance,
                        Parameter("o", JavaType.objectInstance)
                    ) {
                        annotation("@Override")
                        +"if (this == o) {"
                        +"return true;"
                        +"}"
                        +"if (!(o instanceof ${className}Key)) {"
                        +"return false;"
                        +"}"
                        +"${className}Key that = (${className}Key) o;"
                        val size = primaryKeys.size
                        primaryKeys.forEachIndexed { index, column ->
                            when (index) {
                                0 -> {
                                    +"return Objects.equals(${column.javaName}, that.${column.javaName}) &&"
                                }
                                size - 1 -> {
                                    +"    Objects.equals(${column.javaName}, that.${column.javaName});"
                                }
                                else -> {
                                    +"    Objects.equals(${column.javaName}, that.${column.javaName}) &&"
                                }
                            }
                        }
                    }

                    //hashCode
                    method("hashCode", JavaType.intPrimitiveInstance) {
                        annotation("@Override")
                        +"return Objects.hash(${primaryKeys.joinToString(", ") { it.javaName }});"
                    }

                    //toString
                    method("toString", JavaType.stringInstance) {
                        annotation("@Override")
                        +"return ${primaryKeys.joinToString(" + \"${keySep}\" + ") { it.javaName }};"
                    }
                }
            }
        }


    }


}