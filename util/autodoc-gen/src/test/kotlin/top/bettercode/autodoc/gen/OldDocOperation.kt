package top.bettercode.autodoc.gen

import top.bettercode.autodoc.core.Util
import top.bettercode.autodoc.core.model.Field
import top.bettercode.logging.operation.Operation
import top.bettercode.logging.operation.OperationRequest
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.File
import java.util.*

/**
 *
 * @author Peter Wu
 */
@JsonPropertyOrder("fields", "prerequest", "testExec", "description", "request", "response", "protocol")
@JsonIgnoreProperties("collectionName", "name", "duration")
class OldDocOperation(operation: Operation = Operation(),
                      var description: String = "",
                      var prerequest: List<String> = listOf(),
                      var testExec: List<String> = listOf(),
                      var fields: SortedSet<Field> = TreeSet()
) : Operation(operation.collectionName, operation.name, operation.protocol, if (operation.request::class == OperationRequest::class) OldDocOperationRequest(operation.request) else operation.request, operation.response) {

    @JsonIgnore
    lateinit var operationFile: File

    fun save() {
        operationFile.parentFile.mkdirs()
        operationFile.parentFile.mkdirs()
        println("${if (operationFile.exists()) "更新" else "创建"}：$operationFile")
        operationFile.writeText(Util.yamlMapper.writeValueAsString(this))
    }

    fun setOperation(operation: Operation) {
        collectionName = operation.collectionName
        name = operation.name
        protocol = operation.protocol
        request = if (operation.request::class == OperationRequest::class) OldDocOperationRequest(operation.request) else operation.request
        response = operation.response
    }

    override var request
        @JsonDeserialize(`as` = OldDocOperationRequest::class)
        get() = super.request
        set(value) {
            super.request = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OldDocOperation) return false

        if (collectionName != other.collectionName) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = collectionName.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}