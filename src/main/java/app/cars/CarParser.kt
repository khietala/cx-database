package app.cars

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import java.io.IOException
import java.util.Collections
import java.util.Optional

object CarParser {
    class CarNode {
        @JsonProperty("id")
        var id: String? = null
        @JsonProperty("year")
        internal var year: String? = null
        @JsonProperty("model")
        internal var model: String? = null
        @JsonProperty("make")
        internal var make: String? = null
        @JsonProperty("variant")
        internal var variant: String? = null
        @JsonProperty("location")
        internal var location: String? = null
        @JsonProperty("engine")
        internal var engine: String? = null
        @JsonProperty("rego")
        internal var rego: String? = null
        @JsonProperty("gearbox")
        internal var gearbox: String? = null
        @JsonProperty("vin")
        internal var vin: String? = null
        @JsonProperty("owners")
        internal var owners: String? = null
        @JsonProperty("other")
        internal var other: String? = null
        @JsonProperty("colour")
        internal var colour: String? = null
    }

    @Throws(IOException::class)
    fun parse(jsonString: String): Collection<CarNode> {
        return if (jsonString.isEmpty()) {
            Collections.EMPTY_LIST as Collection<CarNode>
        } else {
            try {
                mapper().readValue(jsonString, Array<CarNode>::class.java).asList()
            } catch (e: IOException) {
                Collections.EMPTY_LIST as Collection<CarNode>
            }
        }
    }

    @Throws(IOException::class)
    fun parseOne(jsonString: String): CarParser.CarNode {
        return mapper().readValue(jsonString, CarParser.CarNode::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun marshall(carNodes: Collection<CarParser.CarNode>): String {
        return mapper().writeValueAsString(carNodes)

    }

    @Throws(JsonProcessingException::class)
    fun marshall(carNode: CarParser.CarNode): String {
        return mapper().writeValueAsString(carNode)
    }

    fun mapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        return objectMapper

    }
}