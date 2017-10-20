package app.cars

import app.cars.dao.json.CarDao
import app.cars.dao.json.Cars
import app.resources.CarsResourceTest
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import java.io.File


class CarDaoTest {
    var carDao = CarDao(fileName())

    @Before
    fun setup() {
        File(fileName()).delete()
    }

    @Test
    fun givenNameAndDataThenDatabaseCreated() {
        val records = Cars(cars = CarsResourceTest().createRecords())
        carDao.createDb(records)

        val recordsFromDb = Cars(cars = carDao.getAll())

        assertThat(recordsFromDb.cars).containsAllEntriesOf(records.cars)
    }

    @Test
    fun givenDatabaseAndEditThenEdited() {
        val records = Cars(cars = CarsResourceTest().createRecords())
        carDao.createDb(records)
        val recordsFromDb = Cars(cars = carDao.getAll())
        var edited = recordsFromDb.cars.get("2")?.copy(colour = "testcolour")?: defaultCar()

        carDao.edit("2", edited)

        assertThat(carDao.getById("2")).hasFieldOrPropertyWithValue("colour", "testcolour")
    }

    @Test
    fun givenDatabaseAndCreateThenCreated() {
        val records = Cars(cars = CarsResourceTest().createRecords())
        carDao.createDb(records)
        val recordsFromDb = Cars(cars = carDao.getAll())
        val newId = (recordsFromDb.cars.keys.map{it.toDouble()}.reduce{ a, b -> if (b  > a) a; else b;} + 1).toString()

        carDao.create(Car(id=newId))

        assertThat(carDao.getById(newId))
    }

    private fun fileName(): String {
        return "unitTestDb"
    }
}