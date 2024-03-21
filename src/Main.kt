import java.io.File
import java.io.FileWriter
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class City(
    var measureMean: Double,
    var measureMin: Double,
    var measureMax: Double,
    var namesCount: Int
)

class World(
    var cityMap: MutableMap<String, City>
)

fun writeToFile(world: World) {
    val fileName = "output.txt"
    try {
        val file = File(fileName)
        val writer = FileWriter(file)
        for (key in world.cityMap.keys) {
            val city = world.cityMap.getValue(key)
            val line = "$key=${city.measureMin}/${city.measureMean}/${city.measureMax}\n"
            writer.write(line)
        }
        writer.close()
    } catch (ex: Exception) {
        println("A writing error: ${ex.message}")
    }
}

fun updateMaps(world: World, name: String, measurement: Double) {
    if (world.cityMap[name] == null) {
        world.cityMap[name] = City(
            measurement,
            measurement,
            measurement,
            1
        )
        return
    }
    for (key in world.cityMap.keys) {
        val city = world.cityMap.getValue(key)
        if (name == key) {
            city.namesCount += 1
            world.cityMap[key] = City(
                BigDecimal(
                    (city.measureMean * (city.namesCount - 1) + measurement) /
                            city.namesCount
                ).setScale(
                    4,
                    RoundingMode.HALF_EVEN
                ).toDouble(),
                min(city.measureMin, measurement),
                max(city.measureMax, measurement),
                city.namesCount
            )
            break
        }
    }
}

fun readFromFile(world: World) {
    val filePath = "input.txt"
    val file = File(filePath)
    try {
        file.reader().forEachLine {
            for (i in it.indices) {
                if (it[i] == ';') {
                    val measurement = it.substring(i + 1, it.length).toDouble()
                    val name = it.substring(0, i)
                    updateMaps(world, name, measurement)
                }
            }
        }
    } catch (ex: Exception) {
        println("A reading error: ${ex.message}")
    }
}

fun main() {
    val world = World(mutableMapOf())
    readFromFile(world)
    writeToFile(world)
}