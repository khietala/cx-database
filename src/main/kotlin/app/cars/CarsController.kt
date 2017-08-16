package main.kotlin.app.cars

import app.resources.CarsResource
import app.util.Path.Template.CARS_ALL
import app.util.Path.Template.CAR
import com.sun.javafx.tools.packager.Log
import main.kotlin.app.util.ViewUtil
import spark.Request
import spark.Response

class CarsController {

    fun getCars(request: Request): String {
        try {
            return ViewUtil.render(request, hashMapOf(Pair("cars", CarsResource.getAll().values)), CARS_ALL)
        } catch (e: Exception) {
            Log.debug(e)
            return ""
        }
    }

    fun getCar(request: Request): String {
        return ViewUtil.render(request,
                hashMapOf(Pair("car",
                        CarsResource.getById(request.params("id")))), CAR)
    }
}