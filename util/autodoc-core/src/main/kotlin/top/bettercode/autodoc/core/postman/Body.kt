package top.bettercode.autodoc.core.postman


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("mode", "formdata")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Body(
        @JsonProperty("mode")
        var mode: String? = null,
        @JsonProperty("raw")
        var raw: String? = null,
        @JsonProperty("urlencoded")
        var urlencoded: List<Urlencoded>? = null,
        @JsonProperty("formdata")
        var formdata: List<Formdatum>? = null
)