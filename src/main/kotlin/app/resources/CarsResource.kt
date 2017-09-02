package app.resources
import app.cars.*
import app.util.Path
import spark.Spark.*

object CarsResource {
    lateinit var carsDao: CarDao
    fun init(carsController: CarsController, carsDao: CarDao): Unit {
        this.carsDao = carsDao
        path(Path.Web.API_CARS) {
            get("") { _, _ -> CarParser.marshall(getAll().entries.map {  it.value }) }
            get("/:id") { request, _ -> CarParser.marshall(getById(request.params("id"))) }
            put("/:id") { req, _ -> edit(req.params("id"), CarParser.parseOne(req.body())) }
            post("") { req, _ -> create(CarParser.parseOne(req.body())) }
        }

        path(Path.Web.INDEX) { get("") { _, _ ->  "Hello World" } }
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