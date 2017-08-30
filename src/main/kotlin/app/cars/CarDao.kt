package app.cars

import java.io.File
import java.util.*
import kotlin.collections.HashMap


class CarDao(val dbName: String = "cars.json") {
    private var carsCache: Cars = fromFile(dbName)

    fun getAll(): HashMap<String, Car> {
        return carsCache.cars
    }

    fun getById(id: String): Car {
        return carsCache.cars.getOrDefault(id, defaultCar())
    }

    fun create(car: Car): String {
        var cars = getAll().toMutableMap()
        var newId = UUID.randomUUID().toString()
        cars.put(newId, car)
        toFile(toJson(Cars(cars = cars as HashMap<String, Car>)), dbName)
        carsCache = fromFile(dbName)
        return newId
    }

    fun edit(id: String, car: Car) {
        var cars = getAll().toMutableMap().filter { it.key != id }.toMutableMap()
        cars.put(car.id, car)
        toFile(toJson(Cars(cars= cars as HashMap<String, Car>)), dbName)
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
        createDb(Cars(cars=cars))
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
    return CarParser.marshall(cars.cars.map{it.value}.map { CarNodeFromCar(it) })
}

fun fromJson(jsonString: String): Cars {
    val cars = CarParser.parse(jsonString).map {
        val car = it
        car.id = if (car.id != null && car.id != "") car.id else UUID.randomUUID().toString()
        car.id to CarFromCarNode(car)
    }.toMap()
    return Cars(cars= (if (cars != null && cars.size > 0)  cars as HashMap<String, Car> else hashMapOf()))
}

fun CarFromCarNode(car: CarParser.CarNode?): Car {
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
fun CarNodeFromCar(car: Car): CarParser.CarNode {
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
