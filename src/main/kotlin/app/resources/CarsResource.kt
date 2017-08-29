package app.resources
import app.cars.*
import app.util.Path
import spark.Spark.*

object CarsResource {
    lateinit var carsDao: CarDao
    fun init(carsController: CarsController, carsDao: CarDao): Unit {
        this.carsDao = carsDao
        path(Path.Web.API_CARS) {
            get("") { _, _ -> getAll() }
            get("/:id") { request, _ -> getById(request.params("id")) }
            put("/:id") { req, _ -> edit(req.params("id"), CarFromCarNode(CarParser.parseOne(req.body()))) }
            post("") { req, _ -> create(CarFromCarNode(CarParser.parseOne(req.body()))) }
        }

        path(Path.Web.INDEX) { get("") { req, res ->  "Hello World" } }
        path(Path.Web.CARS) { get("") { req, res -> carsController.getCars(req)} }
        path(Path.Web.CAR) { get("") { req, res -> carsController.getCar(req)} }
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