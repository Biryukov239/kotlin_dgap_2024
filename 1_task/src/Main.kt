import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

const val filePath = "input.txt"
const val outputFileName = "output.txt"

class City(val name: String, val measurement: Double)

fun writeToFile(cityToMeasures: Map<String, List<Double>>) {
    try {
        val writer = File(outputFileName).writer()
        writer.use {
            for ((key, cityList) in cityToMeasures) {
                val average = BigDecimal(cityList.average()).setScale(4, RoundingMode.HALF_EVEN)
                val line = "$key=${cityList.min()}/${average}/${cityList.max()}\n"
                it.write(line)
            }
        }
    } catch (ex: Exception) {
        println("A writing error: ${ex.message}")
    }
}

fun readFromFile(cityList: MutableList<City>) {
    val file = File(filePath)
    try {
        file.reader().forEachLine {
            val (name, measurement) = it.split(';')
            val city = City(name, measurement.toDouble())
            cityList.add(city)
        }
    } catch (ex: Exception) {
        println("A reading error: ${ex.message}")
    }
}

fun main() {
    val cityList: MutableList<City> = mutableListOf()
    readFromFile(cityList)
    val cityToMeasures: Map<String, List<Double>> = cityList.groupBy({ it.name }) { it.measurement }
    writeToFile(cityToMeasures)
}