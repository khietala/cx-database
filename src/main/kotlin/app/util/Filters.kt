package app.util

import spark.*
import app.util.RequestUtil.getQueryLocale

object Filters {

    // If a user manually manipulates paths and forgets to add
    // a trailing slash, redirect the user to the correct path
    var addTrailingSlashes = { request: Request, response: Response ->
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/")
        }
    }

    // Locale change can be initiated from any page
    // The locale is extracted from the request and saved to the user's session
    var handleLocaleChange = { request: Request, response: Response ->
        if (getQueryLocale(request) != null) {
            request.session().attribute("locale", getQueryLocale(request))
            response.redirect(request.pathInfo())
        }
    }

    // Enable GZIP for all responses
    var addGzipHeader = { request: Request, response: Response -> response.header("Content-Encoding", "gzip") }

}
