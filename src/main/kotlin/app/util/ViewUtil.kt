package main.kotlin.app.util

import app.util.MessageBundle
import app.util.Path
import app.util.RequestUtil
import org.eclipse.jetty.http.HttpStatus
import spark.ModelAndView
import spark.Request
import spark.Response
import org.apache.velocity.app.VelocityEngine
import spark.template.velocity.VelocityTemplateEngine

object ViewUtil {

    // Renders a template given a model and a request
    // The request is needed to check the user session for language settings
    // and to see if the user is logged in
    fun render(request: Request, model: MutableMap<String, Any>, templatePath: String): String {
        model.put("msg", MessageBundle(RequestUtil.getSessionLocale(request)))
        //model.put("currentUser", getSessionCurrentUser(request))
        model.put("WebPath", Path.Web::class.java) // Access application URLs from templates
        return strictVelocityEngine().render(ModelAndView(model, templatePath))
    }

    var notAcceptable = { request: Request, response: Response ->
        response.status(HttpStatus.NOT_ACCEPTABLE_406)
        //MessageBundle(getSessionLocale(request)).get("ERROR_406_NOT_ACCEPTABLE")
    }

    var notFound = { request: Request, response: Response ->
        response.status(HttpStatus.NOT_FOUND_404)
        render(request, HashMap(), Path.Template.CARS_ALL) // should be not found
    }

    private fun strictVelocityEngine(): VelocityTemplateEngine {
        val configuredEngine = VelocityEngine()
        configuredEngine.setProperty("runtime.references.strict", true)
        configuredEngine.setProperty("resource.loader", "class")
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        return VelocityTemplateEngine(configuredEngine)
    }
}
