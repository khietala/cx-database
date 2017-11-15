package app.resources
import app.cars.*
import app.cars.dao.json.CarDao
import app.cars.dao.json.Filter
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
        val queryParams = request.queryParams()
        val page = if (queryParams.contains("page")) request.queryParams("page").toInt() else 0
        val size = if (queryParams.contains("size")) request.queryParams("size").toInt() else 0
        val sort = if (queryParams.contains("sort")) request.queryParams("sort") else ""
        val order = if (queryParams.contains("order")) request.queryParams("order") else "asc"

        return carsDao.getAll(page = page, size = size, filter = Filter(field = sort, asc = order == "asc"))
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