package com.ngaming.ngamingcase.core.common

/** Karşılaşabileceğimiz hata çeşitleri. Ekran hangisi geldiyse ona göre mesaj gösteriyor. */
sealed interface AppError {

    data object Network : AppError

    data class Server(val code: Int) : AppError

    data class Validation(val field: Field) : AppError {
        enum class Field { TITLE, BODY }
    }

    data object Unknown : AppError
}
