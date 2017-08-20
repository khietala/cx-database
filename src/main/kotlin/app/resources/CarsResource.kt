package app.resources
import app.cars.Car
import app.cars.CarDao
import spark.Spark.*

val carsDao = CarDao()
object CarsResource {
    fun init(): Unit {
        path("/api/cars") {
            get("") { _, _ ->
                getAll()
            }

            get("/:id") { request, _ ->  getById(request.params("id")) }
        }
    }

    fun getAll():HashMap<String, Car>  {
        return carsDao.getAll()
    }

    fun getById(id: String): Car {
        return carsDao.getById(id)
    }

    fun edit(id: String, car: Car): Car {
        return car
    }
}