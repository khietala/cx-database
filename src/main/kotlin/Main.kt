package main

import app.cars.CarDao
import app.cars.CarsController
import app.resources.CarsResource
import app.util.Filters
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import spark.Spark.*
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File


fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this::class.java.name) }
}

fun <T> loggerFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)

object SparkKotlin {
    val LOGGER_NAME = "kotlin example"
    @JvmStatic fun main(args: Array<String>) {

        val config: AppConfiguration = loadConfig("./src/main/resources/configuration/dev.yml")

        val carsController = CarsController()

        val ve = VelocityEngine()

        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.Log4JLogChute")

        ve.setProperty("runtime.log.logsystem.log4j.logger", LOGGER_NAME)

        ve.init()

        port(config.port)
        staticFiles.location("/public")
        staticFiles.expireTime(600L)
        before("*") {request, response ->  Filters.handleLocaleChange(request, response)}

        after("/api/*") {_, response ->  response.type("application/json")}

        CarsResource.init(carsController, CarDao(dbType = config.storageMethod))

    }
}

fun loadConfig(path: String): AppConfiguration {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return File(path).inputStream().bufferedReader().use {
        mapper.readValue(it, AppConfiguration::class.java)
    }
}

data class AppConfiguration(val storageMethod: String = "json", val port: Int = 4567)
