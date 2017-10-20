package app.cars

import app.cars.dao.json.Cars
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import java.io.IOException
import java.util.*

object CarParser {
    private class CarNode {
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

        fun toCar(): Car {
            return CarFromCarNode(this)
        }
    }

    @Throws(IOException::class)
    fun parse(jsonString: String): Collection<Car> {
        return if (jsonString.isEmpty()) {
            Collections.EMPTY_LIST as Collection<Car>
        } else {
            try {
                mapper().readValue(jsonString, Array<CarNode>::class.java).map {  it.toCar() }
            } catch (e: IOException) {
                Collections.EMPTY_LIST as Collection<Car>
            }
        }
    }

    @Throws(IOException::class)
    fun parseOne(jsonString: String): Car {
        return CarFromCarNode(mapper().readValue(jsonString, CarNode::class.java))
    }

    @Throws(JsonProcessingException::class)
    fun marshall(cars: Cars): String {
        return mapper().writeValueAsString(cars.cars.map{it.value}.map { CarNodeFromCar(it) })
    }

    @Throws(JsonProcessingException::class)
    fun marshall(cars: Collection<Car>): String {
        return mapper().writeValueAsString(cars.map { CarNodeFromCar(it) })
    }

    @Throws(JsonProcessingException::class)
    fun marshall(car: Car): String {
        return mapper().writeValueAsString(CarNodeFromCar(car))
    }

    private fun mapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        return objectMapper

    }

    private fun CarFromCarNode(car: CarParser.CarNode?): Car {
        return Car(
                id = car?.id ?: UUID.randomUUID().toString(),
                model = car?.model ?: "",
                year = car?.year ?: "",
                variant = car?.variant ?: "",
                make = car?.make ?: "Citroen",
                location = car?.location ?: "",
                engine = car?.engine ?: "",
                rego = car?.rego ?: "",
                gearbox = car?.gearbox ?: "",
                vin = car?.vin ?: "",
                owners = car?.owners ?: "",
                other = car?.other ?: "",
                colour = car?.colour ?: ""
        )
    }

    private fun CarNodeFromCar(car: Car): CarParser.CarNode {
        var c = CarParser.CarNode()
        c.id = car.id
        c.model = car.model
        c.year = car.year
        c.variant = car.variant
        c.make = car.make
        c.location = car.location
        c.engine = car.engine
        c.rego = car.rego
        c.gearbox = car.gearbox
        c.vin = car.vin
        c.owners = car.owners
        c.other = car.other
        c.colour = car.colour
        return c
    }

}

