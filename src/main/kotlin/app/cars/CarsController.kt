package app.cars

import app.resources.CarsResource
import app.util.Path.Template.CARS_ALL
import app.util.Path.Template.CAR
import main.kotlin.app.util.ViewUtil
import main.loggerFor
import spark.Request

class CarsController {
    private val LOG = loggerFor(javaClass)


    fun getCars(request: Request): String {
        try {
            return ViewUtil.render(request, hashMapOf(Pair("cars", CarsResource.getAll().values)), CARS_ALL)
        } catch (e: Exception) {
            LOG.error(e.message)
            return ""
        }
    }

    fun getCar(request: Request): String {
        return ViewUtil.render(request,
                hashMapOf(Pair("car",
                        CarsResource.getById(request.params("id")))), CAR)
    }

    fun editCar(request: Request) {
        CarsResource.edit(request.params("id"),
                CarFromCarNode(CarParser.parseOne(request.body()))
        )
    }

}