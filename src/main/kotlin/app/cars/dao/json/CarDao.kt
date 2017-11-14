package app.cars.dao.json

import app.cars.Car
import app.cars.CarParser
import app.cars.defaultCar
import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap


class CarDao(val dbType: String = "json", val dbName: String = "cars.json") {
    private var carsCache: Cars = fromFile(dbName)

    fun getAll(page: Int = 0, size: Int = 0): HashMap<String, Car> {
        return carsCache.cars.filter { IntRange((page -1 * size), (page * size)).contains(it.key) }
                as HashMap<String, Car>
    }

    fun getById(id: String): Car {
        return carsCache.cars.getOrDefault(id, defaultCar())
    }

    fun create(car: Car): String {
        var newId = UUID.randomUUID().toString()
        toFile(
                toJson(
                        Cars(cars = getAll().apply { put(newId, car) })), dbName)
        carsCache = fromFile(dbName)
        return newId
    }

    fun edit(id: String, car: Car) {
        toFile(
                toJson(
                        Cars(cars = getAll().entries
                                .map { if (it.key == car.id) it.key to car else it.key to it.value }
                                .toMap() as HashMap<String, Car>)), dbName)

        carsCache = fromFile(dbName)
    }

    fun <K, V> Map<K, V>.toMutableMap(): MutableMap<K, V> {
        return HashMap(this)
    }

    fun createDb(cars: Cars): Unit {
        toFile(toJson(cars), dbName)
        carsCache = fromFile(dbName)
    }

    fun createDb(cars: HashMap<String, Car>): Unit {
        createDb(Cars(cars = cars))
    }

    fun clear() {
        File(dbName).delete()
    }
}

data class Cars(val cars: HashMap<String, Car>)

//fun update(toFile: (String, String) -> Unit, fromFile: (String) -> Cars): Cars {
//    toFile()
//    return fromFile()
//}

fun fromFile(fileName: String = "cars.json"): Cars {
    return if (File(fileName).exists()) {
        fromJson(File(fileName).inputStream().bufferedReader().use { it.readText() })
    } else {
        File(fileName).createNewFile();
        Cars(cars = HashMap())
    }
}

fun toFile(jsonString: String, fileName: String = "cars.json"): Unit {
    File(fileName).printWriter().use{out -> out.print(jsonString)}
}

fun toJson(cars: Cars): String {
    return CarParser.marshall(cars)
}

fun fromJson(jsonString: String): Cars {
    val cars = CarParser.parse(jsonString).map {
        var car = it.copy(id = if (it.id != null && it.id != "") it.id else UUID.randomUUID().toString())
        car.id to car
    }.toMap()
    return Cars(cars = (if (cars != null && cars.size > 0) cars as HashMap<String, Car> else hashMapOf()))
}

