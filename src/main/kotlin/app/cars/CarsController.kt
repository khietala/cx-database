package app.cars

import app.cars.CarParser
import app.resources.CarsResource
import app.util.Path.Template.CARS_ALL
import app.util.Path.Template.CAR
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.sun.javafx.tools.packager.Log
import main.kotlin.app.util.ViewUtil
import spark.Request

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

    fun editCar(request: Request) {
        CarsResource.edit(request.params("id"),
                CarFromCarNode(CarParser.parseOne(request.body()))
        )
    }

}