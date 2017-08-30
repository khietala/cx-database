package main

import app.cars.CarDao
import app.cars.CarsController
import app.resources.CarsResource
import app.util.Filters
import spark.Spark.*
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this::class.java.name) }
}

fun <T> loggerFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)

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
        //before("*", Filters.addTrailingSlashes)
        before("*") {request, response ->  Filters.handleLocaleChange(request, response)}

        after("/api/*") {_, response ->  response.type("application/json")}

        CarsResource.init(carsController, CarDao())

    }
}
