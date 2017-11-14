package app.resources
import app.cars.*
import app.cars.dao.json.CarDao
import app.util.CorsHelper
import app.util.Path
import spark.Request
import spark.Spark.*

object CarsResource {
    lateinit var carsDao: CarDao
    fun init(carsController: CarsController, carsDao: CarDao) {
        CorsHelper.apply()
        this.carsDao = carsDao
        path(Path.Web.API_CARS) {
            get("") { request, _ -> CarParser.marshall(getAll(request).entries.map {  it.value }) }
            get("/:id") { request, _ -> CarParser.marshall(getById(request.params("id"))) }
            put("/:id", "application/json") { req, _ -> edit(req.params("id"), CarParser.parseOne(req.body())) }
            post("", "application/json") { req, _ -> create(CarParser.parseOne(req.body())) }
        }

        path(Path.Web.CARS) { get("") { req, _ -> carsController.getCars(req)} }
        path(Path.Web.CAR) { get("") { req, _ -> carsController.getCar(req)} }
    }

    fun getAll(request: Request):HashMap<String, Car>  {
        val page = request.queryParams("page") as Int
        val size = request.queryParams("size") as Int
        return carsDao.getAll(page = page, size = size)
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