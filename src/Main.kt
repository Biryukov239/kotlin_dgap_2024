import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

const val filePath = "input.txt"
const val outputFileName = "output.txt"

class City(
    var measureMean: Double,
    var measureMin: Double,
    var measureMax: Double,
    var namesCount: Int
)

fun writeToFile(cityMap: MutableMap<String, City>) {
    try {
        val writer = File(outputFileName).writer()
        writer.use {
            for (key in cityMap.keys) {
                val city = cityMap.getValue(key)
                val line = "$key=${city.measureMin}/${city.measureMean}/${city.measureMax}\n"
                it.write(line)
            }
        }
    } catch (ex: Exception) {
        println("A writing error: ${ex.message}")
    }
}

fun updateMap(cityMap: MutableMap<String, City>, name: String, measurement: Double) {
    val currentCity = cityMap.getOrPut(name) {
        City(measurement, measurement, measurement, 0)
    }
    currentCity.namesCount += 1
    val sumOfMeasure = currentCity.measureMean * (currentCity.namesCount - 1) + measurement
    currentCity.measureMean =
        BigDecimal(sumOfMeasure / currentCity.namesCount).setScale(
            4,
            RoundingMode.HALF_UP
        ).toDouble()
    currentCity.measureMin = min(currentCity.measureMin, measurement)
    currentCity.measureMax = max(currentCity.measureMax, measurement)
}

fun readFromFile(cityMap: MutableMap<String, City>) {
    val file = File(filePath)
    try {
        file.reader().forEachLine {
            val (name, measurement) = it.split(';')
            updateMap(cityMap, name, measurement.toDouble())
        }
    } catch (ex: Exception) {
        println("A reading error: ${ex.message}")
    }
}

fun main() {
    val cityMap: MutableMap<String, City> = (mutableMapOf())
    readFromFile(cityMap)
    writeToFile(cityMap)
}