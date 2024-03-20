import java.io.File
import java.io.FileWriter
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class City(val name: String, val measurement: Double)

class World(
    var measureMean: MutableMap<String, Double>,
    var measureMax: MutableMap<String, Double>,
    var measureMin: MutableMap<String, Double>,
    var namesCount: MutableMap<String, Int>
)

fun writeToFile(world: World) {
    val fileName = "output.txt"
    try {
        val file = File(fileName)
        val writer = FileWriter(file)
        for (key in world.measureMean.keys) {
            val line = "$key=${world.measureMin[key]}/${world.measureMean[key]}/${world.measureMax[key]}\n"
            writer.write(line)
        }
        writer.close()
    } catch (ex: Exception) {
        println("A writing error: ${ex.message}")
    }
}

fun updateMaps(world: World, city: City) {
    if (world.namesCount[city.name] == null) {
        world.namesCount[city.name] = 1
    }
    if (world.measureMean[city.name] == null) {
        world.measureMean[city.name] = city.measurement
        world.measureMin[city.name] = city.measurement
        world.measureMax[city.name] = city.measurement
        return
    }
    for (key in world.measureMean.keys) {
        if (city.name == key) {
            world.namesCount[key] = world.namesCount[key]!! + 1
            world.measureMin[key] = min(world.measureMin[key]!!, city.measurement)
            world.measureMax[key] = max(world.measureMax[key]!!, city.measurement)
            world.measureMean[key] =
                BigDecimal((world.measureMean[key]!! * (world.namesCount[key]!! - 1) + city.measurement) / world.namesCount[key]!!).setScale(
                    4,
                    RoundingMode.HALF_EVEN
                ).toDouble()
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
                    val city = City(
                        it.substring(0, i),
                        it.substring(i + 1, it.length).toDouble()
                    )
                    updateMaps(world, city)
                }
            }
        }
    } catch (ex: Exception) {
        println("A reading error: ${ex.message}")
    }
}

fun main() {
    val world = World(mutableMapOf(), mutableMapOf(), mutableMapOf(), mutableMapOf())
    readFromFile(world)
    writeToFile(world)
}