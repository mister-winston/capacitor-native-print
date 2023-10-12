package com.mrwinston.capacitor.print

import android.print.PrintAttributes
import android.util.Log
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "NativePrint")
class NativePrintPlugin : Plugin() {
    private val implementation = NativePrint()

    @PluginMethod
    fun print(call: PluginCall) {
        val name = call.getString("name") ?: return call.reject("Name is required")

        val webView = this.bridge.webView ?: return call.reject("WebView not found")

        implementation.print(
            this.context,
            this.activity,
            webView,
            name,
            this.extractPrintAttributes(call)
        ) { error, printJob ->
            if (printJob == null) {
                error?.printStackTrace()
                call.reject(error?.message ?: "No print job")
            } else {
                val result = JSObject()

                result.putSafe("isBlocked", printJob.isBlocked)
                result.putSafe("isCancelled", printJob.isCancelled)
                result.putSafe("isCompleted", printJob.isCompleted)
                result.putSafe("isFailed", printJob.isFailed)
                result.putSafe("isQueued", printJob.isQueued)
                result.putSafe("isStarted", printJob.isStarted)
                result.putSafe("copies", printJob.info.copies)
                result.putSafe("printerId", printJob.info.printerId.toString())
                result.putSafe("label", printJob.info.label)
                result.putSafe("creationTime", printJob.info.creationTime)
                result.putSafe("state", printJob.info.state)

                if (printJob.info.pages != null) {
                    val pages = JSArray()
                    for (page in printJob.info.pages!!) {
                        val pageObject = JSObject()
                        pageObject.putSafe("start", page.start)
                        pageObject.putSafe("end", page.end)
                        pages.put(pageObject)
                    }
                    result.putSafe("pages", pages)
                }

                call.resolve(result)
            }
        }
    }

    private fun extractPrintAttributes(call: PluginCall): PrintAttributes {
        val printAttributes = PrintAttributes.Builder()

        val monochrome = call.getBoolean("monochrome") ?: false
        val orientation = call.getString("orientation")?.lowercase() ?: "portrait"
        val pageSize = this.getPageSize(call.getString("pageSize") ?: "A4")

        when (orientation) {
            "portrait" -> pageSize.asPortrait()
            "landscape" -> pageSize.asLandscape()
        }

        printAttributes.setColorMode(
            if (monochrome) {
                PrintAttributes.COLOR_MODE_MONOCHROME
            } else {
                PrintAttributes.COLOR_MODE_COLOR
            }
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            printAttributes.setDuplexMode(
                if (pageSize.isPortrait) {
                    PrintAttributes.DUPLEX_MODE_SHORT_EDGE
                } else {
                    PrintAttributes.DUPLEX_MODE_LONG_EDGE
                }
            )
        }

        printAttributes.setMediaSize(pageSize)
        printAttributes.setMinMargins(PrintAttributes.Margins.NO_MARGINS)

        return printAttributes.build()
    }

    private fun getPageSize(name: String): PrintAttributes.MediaSize {
        return when (name.lowercase()) {
            "a0" -> PrintAttributes.MediaSize.ISO_A0
            "a1" -> PrintAttributes.MediaSize.ISO_A1
            "a2" -> PrintAttributes.MediaSize.ISO_A2
            "a3" -> PrintAttributes.MediaSize.ISO_A3
            "a4" -> PrintAttributes.MediaSize.ISO_A4
            "a5" -> PrintAttributes.MediaSize.ISO_A5
            "a6" -> PrintAttributes.MediaSize.ISO_A6
            "a7" -> PrintAttributes.MediaSize.ISO_A7
            "a8" -> PrintAttributes.MediaSize.ISO_A8
            "a9" -> PrintAttributes.MediaSize.ISO_A9
            "a10" -> PrintAttributes.MediaSize.ISO_A10
            "B0" -> PrintAttributes.MediaSize.ISO_B0
            "B1" -> PrintAttributes.MediaSize.ISO_B1
            "B2" -> PrintAttributes.MediaSize.ISO_B2
            "B3" -> PrintAttributes.MediaSize.ISO_B3
            "B4" -> PrintAttributes.MediaSize.ISO_B4
            "B5" -> PrintAttributes.MediaSize.ISO_B5
            "B6" -> PrintAttributes.MediaSize.ISO_B6
            "B7" -> PrintAttributes.MediaSize.ISO_B7
            "B8" -> PrintAttributes.MediaSize.ISO_B8
            "B9" -> PrintAttributes.MediaSize.ISO_B9
            "B10" -> PrintAttributes.MediaSize.ISO_B10
            "C0" -> PrintAttributes.MediaSize.ISO_C0
            "C1" -> PrintAttributes.MediaSize.ISO_C1
            "C2" -> PrintAttributes.MediaSize.ISO_C2
            "C3" -> PrintAttributes.MediaSize.ISO_C3
            "C4" -> PrintAttributes.MediaSize.ISO_C4
            "C5" -> PrintAttributes.MediaSize.ISO_C5
            "C6" -> PrintAttributes.MediaSize.ISO_C6
            "C7" -> PrintAttributes.MediaSize.ISO_C7
            "C8" -> PrintAttributes.MediaSize.ISO_C8
            "C9" -> PrintAttributes.MediaSize.ISO_C9
            "C10" -> PrintAttributes.MediaSize.ISO_C10
            "govt" -> PrintAttributes.MediaSize.NA_GOVT_LETTER
            "three_by_five" -> PrintAttributes.MediaSize.NA_INDEX_3X5
            "four_by_six" -> PrintAttributes.MediaSize.NA_INDEX_4X6
            "five_by_eight" -> PrintAttributes.MediaSize.NA_INDEX_5X8
            "junior_legal" -> PrintAttributes.MediaSize.NA_JUNIOR_LEGAL
            "ledger" -> PrintAttributes.MediaSize.NA_LEDGER
            "legal" -> PrintAttributes.MediaSize.NA_LEGAL
            "letter" -> PrintAttributes.MediaSize.NA_LETTER
            "monarch" -> PrintAttributes.MediaSize.NA_MONARCH
            "tabloid" -> PrintAttributes.MediaSize.NA_TABLOID
            "landscape" -> PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE
            "portrait" -> PrintAttributes.MediaSize.UNKNOWN_PORTRAIT
            else -> PrintAttributes.MediaSize.ISO_A4
        }
    }
}