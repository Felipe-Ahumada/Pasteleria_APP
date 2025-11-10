package com.pasteleria_app.pasteleria_app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Formatea un número Int a moneda $CLP con puntos
fun formateaCLP(valor: Int): String =
    "$" + "%,d".format(valor).replace(',', '.')

// Formatea un Long (timestamp) a una fecha y hora legible
fun formatTimestamp(timestamp: Long, pattern: String = "dd/MM/yyyy, HH:mm"): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}