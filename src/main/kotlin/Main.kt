package main

import app.resources.CarsResource
import app.util.Filters
import app.util.Path
import main.kotlin.app.cars.CarsController
//import spark.kotlin.before
//import spark.kotlin.get
//import spark.kotlin.port
import spark.Spark.*
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants


//import spark.debug.DebugScreen.*

object SparkKotlin {
    val LOGGER_NAME = "kotlin example"
    @JvmStatic fun main(args: Array<String>) {
        val carsController = CarsController()

        val ve = VelocityEngine()

        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.Log4JLogChute")

        ve.setProperty("runtime.log.logsystem.log4j.logger",
                LOGGER_NAME)

        ve.init()

        port(4567)
        staticFiles.location("/public")
        staticFiles.expireTime(600L)
//        enableDebugScreen()
        before("*", Filters.addTrailingSlashes)
        before("*") {request, response ->  Filters.handleLocaleChange(request, response)}


        get(Path.Web.INDEX) {request, response ->  "Hello World" }

        get(Path.Web.CARS) { req, res -> carsController.getCars(req)}
        get(Path.Web.CAR) { req, res -> carsController.getCar(req)}

        CarsResource.init()

    }
}
