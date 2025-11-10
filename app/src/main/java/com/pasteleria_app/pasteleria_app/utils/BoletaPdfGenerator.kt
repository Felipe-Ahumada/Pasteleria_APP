package com.pasteleria_app.pasteleria_app.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import java.io.File
import java.io.FileOutputStream

object BoletaPdfGenerator {

    fun generar(context: Context, orden: Orden): Uri {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        // --- Definir Estilos de Texto ---
        val titlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 18f
            color = 0xFF3E2E20.toInt()
        }

        val textPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = 12f
            color = 0xFF000000.toInt()
        }

        val boldTextPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 12f
            color = 0xFF000000.toInt()
        }

        val smallTextPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = 10f
            color = 0xFF555555.toInt()
        }

        // --- MODIFICADO: Añadido estilo itálico ---
        val smallItalicPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            textSize = 10f
            color = 0xFF555555.toInt()
        }
        // ------------------------------------------

        // --- Dibujar Contenido ---
        var y = 40f
        val x = 40f
        val lineHeight = 18f
        val largeLineHeight = 24f

        canvas.drawText("Pastelería Mil Sabores", x, y, titlePaint)
        y += largeLineHeight
        canvas.drawText("Detalle de tu Pedido", x, y, textPaint)
        y += largeLineHeight

        canvas.drawText("Pedido: ${orden.id}", x, y, boldTextPaint)
        y += lineHeight
        canvas.drawText("Tracking: ${orden.trackingId}", x, y, textPaint)
        y += lineHeight
        canvas.drawText("Fecha: ${formatTimestamp(orden.fechaCreacion)}", x, y, smallTextPaint)
        y += largeLineHeight

        canvas.drawText("--- Productos ---", x, y, boldTextPaint)
        y += largeLineHeight

        // ---- MODIFICADO AQUÍ ----
        // Items
        orden.items.forEach { item ->
            canvas.drawText("${item.nombreProducto} (x${item.cantidad})", x, y, textPaint)
            canvas.drawText(formateaCLP(item.subtotal), x + 400, y, textPaint)
            y += lineHeight

            // --- AÑADIDO ---
            if (!item.mensaje.isNullOrBlank()) {
                canvas.drawText("  ↳ Mensaje: \"${item.mensaje}\"", x, y, smallItalicPaint) // Usar smallItalicPaint
                y += lineHeight
            }
            // ----------------
        }
        // -------------------------
        y += lineHeight

        // Total
        canvas.drawText("TOTAL", x, y, boldTextPaint)
        canvas.drawText(formateaCLP(orden.total), x + 400, y, boldTextPaint)
        y += largeLineHeight

        canvas.drawText("--- Envío ---", x, y, boldTextPaint)
        y += largeLineHeight
        canvas.drawText("Tipo: ${orden.tipoEntrega}", x, y, textPaint)
        y += lineHeight
        canvas.drawText("Dirección: ${orden.direccionEntrega}", x, y, textPaint)
        y += lineHeight
        canvas.drawText("Fecha preferida: ${orden.fechaPreferida}", x, y, textPaint)
        y += largeLineHeight

        canvas.drawText("¡Gracias por tu compra!", x, y, boldTextPaint)

        // --- Finalizar y Guardar ---
        document.finishPage(page)

        val pdfDir = File(context.filesDir, "boletas").apply { mkdirs() }
        val pdfFile = File(pdfDir, "boleta_${orden.id}.pdf")

        try {
            FileOutputStream(pdfFile).use {
                document.writeTo(it)
            }
        } catch (e: java.io.IOException) {
            e.printStackTrace()
            throw RuntimeException("Error al escribir el archivo PDF", e)
        } finally {
            document.close()
        }

        val authority = "${context.packageName}.provider"
        return FileProvider.getUriForFile(context, authority, pdfFile)
    }
}