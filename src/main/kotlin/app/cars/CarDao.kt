package app.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import jdk.nashorn.internal.parser.JSONParser
import java.io.File
import java.lang.reflect.ParameterizedType
import java.util.*


class CarDao {
    private val carsCache: Cars = fromFile()

    fun getAll(): HashMap<String, Car> {
        return carsCache.cars
    }

    fun getById(id: String): Car {
        return carsCache.cars.getOrDefault(id, defaultCar())
    }
}

data class Cars(val cars: HashMap<String, Car>)

fun fromFile(): Cars {
    return fromJson(File("cars.json").inputStream().bufferedReader().use { it.readText() })
}

fun toJson(cars: Cars): String {
    val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            //.add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    return moshi.adapter(Cars::class.java).toJson(cars)
}

fun fromJson(jsonString: String): Cars {
    val carNodes = CarParser.parse(jsonString)
    //val type = Types.newParameterizedType(Map::class.java, Integer::class.java, Car::class.java)
    //val moshi = Moshi.Builder()
     //       .add(KotlinJsonAdapterFactory())
            //.add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
      //      .build()
    //val cars = null
    //val jsonAdapter: JsonAdapter<Map::class<Pair<>> = moshi.adapter(type)
    //val cars = moshi.adapter(List::class.java).fromJson(jsonString)?.map { Car(id = 1) }
    //val adapter: JsonAdapter<Map<Number, Car>> = moshi.adapter(type)
    //val cars = adapter.fromJson(jsonString)
    //val mapper = ObjectMapper()
    //val obj = mapper.readValue(jsonString, CarList::class.java)
    val cars = carNodes.map {
        val car = it
        car.id=UUID.randomUUID().toString()
        car.id to Car(
                id=car?.id ?: UUID.randomUUID().toString(),
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
        ) }.toMap()
     return Cars(cars= (if (cars != null)  cars as HashMap<String, Car> else hashMapOf()))
}