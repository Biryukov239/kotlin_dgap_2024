import java.net.URI

const val domain = "https://en.wikipedia.org/wiki/Example"

fun getLinks(domain: String): Sequence<String> {
    val regularExpression = Regex("<a\\s+href=\"([^\"]*)\"")
    val retrieved = regularExpression.findAll(URI(domain).toURL().readText())
    val links = retrieved.map {
        it.groupValues[1]
    }
    return links
}


fun main() {
    var cnt = 0
    getLinks(domain).forEach {
        if (it.startsWith("/wiki/")) {
            cnt++
            println(it)
        }
    }
    println(cnt)
}