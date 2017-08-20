package app.resources

import app.cars.*
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap
import spark.Spark.awaitInitialization
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.RestAssured.responseSpecification
import org.hamcrest.Matcher
import org.hamcrest.Matchers.containsString
import org.json.JSONArray
import org.junit.After
import uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONArrayAs
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.TypeSafeMatcher


class CarsResourceTest {
    val baseURI = "http://localhost:4567/"
    var carsResource: CarsResource = CarsResource
    var records = Cars(cars = HashMap())

    @Before
    fun setup() {
        carsResource.init()
        awaitInitialization()

        val carDao = CarDao("resourceTest")
        records = Cars(cars = CarsResourceTest().createRecords())
        carDao.createDb(records)

    }

    @After
    fun teardown() {
        carsDao.clear()
    }

    @Test
    fun givenGetAllThenAllReturned() {
        var testUrl = "${baseURI}api/cars"
        given()
                .get(testUrl)

                .then()
                .statusCode(200)
                //.body(sameJSONArrayAs(JSONArray(records.cars.values)))
                .body(containsString(records.cars.values.toString()))
    }

    @Test
    fun givenGetByIdThenOneReturned() {
        var testUrl = "${baseURI}api/cars/2"
        given()
                .get(testUrl)

                .then()
                .statusCode(200)
                //.body(carMatches(records.cars.getOrDefault("2", defaultCar())))
                .body(contains(records.cars.getOrDefault("2", defaultCar())))
    }

    @Test
    fun updateRecord(): Unit {
        val records = createRecords()
        CarDao("unitTest").createDb(records )
        var toEdit = records
    }

    fun createRecords(): HashMap<String, Car> {
        return IntRange(1, 10)
                .map { Car(id = it.toString(), year = UUID.randomUUID().toString()) }
                .map { it.id to it }.toMap(HashMap<String, Car>())
    }

//    fun createDataBase(records: List<Car>) {
//        CarDao
//    }
}

//fun carMatches(car: Car): CarMatcher {
//    return CarMatcher()
//}
//
//class CarMatcher: TypeSafeMatcher<Car> {
//    init {}
//    fun matchesSafely(record: Car): Matcher<String> {
//        return result -> {
//            val car = CarParser.parseOne(result)
//            assertThat(car).isEqualToComparingFieldByField(record)
//        }
//    }
//}
