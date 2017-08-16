package app.util

import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle

class MessageBundle(languageTag: String?) {

    private val messages: ResourceBundle

    init {
        val locale = if (languageTag != null) Locale(languageTag) else Locale.ENGLISH
        this.messages = ResourceBundle.getBundle("localization/messages", locale)
    }

    operator fun get(message: String): String {
        return messages.getString(message)
    }

    operator fun get(key: String, vararg args: Any): String {
        return MessageFormat.format(get(key), *args)
    }

}
