package app.util


class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    object Web {
        val INDEX = "/index/"
        val LOGIN = "/login/"
        val LOGOUT = "/logout/"
        val CARS = "/cars/"
        val CAR = "/cars/:id/"
        val API_CARS = "/api/cars"
    }

    object Template {
        val CARS_ALL = "/velocity/cars/all.vm"
        val CAR = "/velocity/cars/one.vm"
    }

}
