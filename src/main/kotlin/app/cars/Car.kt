package app.cars

data class Car(val id: String,
               val year: String = "1980",
               val model: String = "CX",
               val make: String = "Citroen",
               val colour: String = "",
               val variant: String = "",
               val description: String = "",
               val error: String = "",
               val location: String = "",
               val owners: String = "",
               val other: String = "",
               val vin:String = "",
               val gearbox: String = "",
               val rego: String = "",
               val engine: String = "")

fun defaultCar(): Car {
    return Car(id="0", error = "Not Found")
}