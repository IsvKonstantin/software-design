package dto

data class QueryParams(val engine: SearchEngine, val host: String, val port: Int)

enum class SearchEngine {
    GOOGLE, BING, YANDEX
}
