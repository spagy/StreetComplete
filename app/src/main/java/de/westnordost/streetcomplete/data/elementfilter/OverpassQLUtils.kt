package de.westnordost.streetcomplete.data.elementfilter

import de.westnordost.osmapi.map.data.BoundingBox
import java.text.NumberFormat
import java.util.*

const val DEFAULT_MAX_QUESTS = 2000

// by default we limit the number of quests created to something that does not cause
// performance problems
fun getQuestPrintStatement() = "out meta geom $DEFAULT_MAX_QUESTS;"

fun BoundingBox.toGlobalOverpassBBox() = "[bbox:${toOverpassBbox()}];"

fun BoundingBox.toOverpassBboxFilter() = "(${toOverpassBbox()})"

private fun BoundingBox.toOverpassBbox(): String {
    val df = NumberFormat.getNumberInstance(Locale.US)
    df.maximumFractionDigits = 7

    return df.format(minLatitude) + "," + df.format(minLongitude) + "," +
            df.format(maxLatitude) + "," + df.format(maxLongitude)
}

private val QUOTES_NOT_REQUIRED = Regex("[a-zA-Z_][a-zA-Z0-9_]*|-?[0-9]+")

fun String.quoteIfNecessary() =
    if (QUOTES_NOT_REQUIRED.matches(this)) this else quote()

fun String.quote() = "'${this.replace("'", "\'")}'"
