package app.resources

import app.cars.*
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap
import spark.Spark.awaitInitialization
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType
import org.junit.After
import org.skyscreamer.jsonassert.JSONAssert


class CarsResourceTest {
    val baseURI = "http://localhost:4567/"
    var carsResource: CarsResource = CarsResource
    var records = Cars(cars = HashMap())
    val carDao = CarDao("resourceTest")

    @Before
    fun setup() {
        carsResource.init(CarsController(), carDao)

        awaitInitialization()


        records = Cars(cars = CarsResourceTest().createRecords())
        carDao.createDb(records)

    }

    @After
    fun teardown() {
        carDao.clear()
    }

    @Test
    fun givenGetAllThenAllReturned() {
        val testUrl = "${baseURI}api/cars"
        var response = given()
                .accept(ContentType.ANY)
                .get(testUrl)

                .then()
                .statusCode(200)
                .log().all()
                //.body(sameJSONArrayAs(JSONArray(records.cars.values)))
                //.body(containsString(records.cars.values.toString()))
                //.body("$", hasItems(records.cars.entries.map { CarParser.marshall(CarNodeFromCar(it.value)) }))
                .extract()
                .response()

        JSONAssert.assertEquals(response.asString(), CarParser.marshall(records.cars.entries.map { CarNodeFromCar(it.value) }), false)
    }

    @Test
    fun givenGetByIdThenOneReturned() {
        val testUrl = "${baseURI}api/cars/2"
        val response = given()
                .accept(ContentType.JSON)
                .get(testUrl)

                .then()
                .statusCode(200).log().all()
                //.body(carMatches(records.cars.getOrDefault("2", defaultCar())))
                //.body(contains(records.cars.getOrDefault("2", defaultCar())))
                .extract()
                .response()
        JSONAssert.assertEquals(response.asString(), CarParser.marshall(CarNodeFromCar(records.cars.getOrDefault("2", defaultCar()))), true)
//        assertThat(CarParser.parseOne(response.asString()).id)
//                .isEqualTo(records.cars.getOrDefault("2", defaultCar()).id)
    }

    @Test
    fun updateRecord(): Unit {
        var testUrl = "${baseURI}api/cars/2"
        val records = createRecords()
        CarDao("unitTest").createDb(records)
        given()
                .contentType("application/json")
                .body(records.filter { it.key == "2" }.getOrDefault("0", defaultCar()).copy(model = "edited"))

                .put(testUrl)

                .then()
                .statusCode(200)

    }

    fun createRecords(): HashMap<String, Car> {
        return IntRange(1, 10)
                .map { Car(id = it.toString(), year = UUID.randomUUID().toString()) }
                .map { it.id to it }.toMap(HashMap<String, Car>())
    }
}

