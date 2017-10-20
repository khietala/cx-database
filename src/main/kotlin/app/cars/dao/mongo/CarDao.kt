package app.cars.dao.mongo

import com.mongodb.*
import com.mongodb.client.MongoDatabase
import org.bson.conversions.Bson
import java.util.*

fun createMongoDb(): MongoDatabase {
    val host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
    if (host == null) {
        val mongoClient: MongoClient = MongoClient("localhost");
        return mongoClient.getDatabase("cx-database");
    }
    val port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"))
    val dbname = System.getenv("OPENSHIFT_APP_NAME")
    val username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME")
    val password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD")
    val mongoClientOptions = MongoClientOptions.builder().build()
    val mongoClient = MongoClient(ServerAddress(host, port), mongoClientOptions)
    mongoClient.setWriteConcern(WriteConcern.SAFE)
    return mongoClient.getDatabase(dbname);
}


class CarDao(val dbName: String = "cars.json") {
    private val db: MongoDatabase = createMongoDb()
    private val collection = db.getCollection(dbName)

    fun getAll(): Map<String, Car> {
        return collection.find()
                .map{
                    (it as Car).id to Car(it as BasicDBObject)}.toMap()
    }

    fun getById(id: String): Car {
        return app.cars.dao.mongo.Car(
                collection.find(BasicDBObject("_id", id))
                        as BasicDBObject)
    }

    fun create(car: Car): String {
        var newId = UUID.randomUUID().toString()
        return newId
    }

    fun edit(id: String, car: Car): Car {
        return collection.findOneAndUpdate(car as Bson, car as Bson) as Car
    }

}
