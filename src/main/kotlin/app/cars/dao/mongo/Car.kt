package app.cars.dao.mongo

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.sun.corba.se.spi.ior.ObjectId
import java.util.*

class Car(dbObject: BasicDBObject) {
    val id: String = (dbObject.get("_id") as ObjectId).toString()
    val title: String = dbObject.getString("title")
    val isDone: Boolean = dbObject.getBoolean("done")
    val createdOn: Date = dbObject.getDate("createdOn")
}