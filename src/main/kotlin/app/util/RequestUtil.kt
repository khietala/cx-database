package app.util

import spark.*

object RequestUtil {

    fun getQueryLocale(request: Request): String? {
        return request.queryParams("locale")
    }

    fun getParamIsbn(request: Request): String {
        return request.params("isbn")
    }

    fun getQueryUsername(request: Request): String {
        return request.queryParams("username")
    }

    fun getQueryPassword(request: Request): String {
        return request.queryParams("password")
    }

    fun getQueryLoginRedirect(request: Request): String {
        return request.queryParams("loginRedirect")
    }

    fun getSessionLocale(request: Request): String? {
        return request.session().attribute<String>("locale")
    }

    fun getSessionCurrentUser(request: Request): String {
        return request.session().attribute<String>("currentUser")
    }

    fun removeSessionAttrLoggedOut(request: Request): Boolean {
        val loggedOut = request.session().attribute<Any>("loggedOut")
        request.session().removeAttribute("loggedOut")
        return loggedOut != null
    }

    fun removeSessionAttrLoginRedirect(request: Request): String {
        val loginRedirect = request.session().attribute<String>("loginRedirect")
        request.session().removeAttribute("loginRedirect")
        return loginRedirect
    }

    fun clientAcceptsHtml(request: Request): Boolean {
        val accept = request.headers("Accept")
        return accept != null && accept.contains("text/html")
    }

    fun clientAcceptsJson(request: Request): Boolean {
        val accept = request.headers("Accept")
        return accept != null && accept.contains("application/json")
    }

}
