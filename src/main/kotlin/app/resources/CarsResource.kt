package app.resources
import app.cars.*
import app.cars.dao.json.CarDao
import app.util.CorsHelper
import app.util.Path
import spark.Spark.*

object CarsResource {
    lateinit var carsDao: CarDao
    fun init(carsController: CarsController, carsDao: CarDao): Unit {
        CorsHelper.apply()
        this.carsDao = carsDao
        path(Path.Web.API_CARS) {
            get("") { _, _ -> CarParser.marshall(getAll().entries.map {  it.value }) }
            get("/:id") { request, _ -> CarParser.marshall(getById(request.params("id"))) }
            put("/:id", "application/json") { req, _ -> edit(req.params("id"), CarParser.parseOne(req.body())) }
            post("", "application/json") { req, _ -> create(CarParser.parseOne(req.body())) }
        }

        path(Path.Web.CARS) { get("") { req, _ -> carsController.getCars(req)} }
        path(Path.Web.CAR) { get("") { req, _ -> carsController.getCar(req)} }
    }

    fun getAll():HashMap<String, Car>  {
        return carsDao.getAll()
    }

    fun getById(id: String): Car {
        return carsDao.getById(id)
    }

    fun edit(id: String, car: Car) {
        carsDao.edit(id, car)
    }

    fun create(car: Car): String {
        return carsDao.create(car)
    }
}