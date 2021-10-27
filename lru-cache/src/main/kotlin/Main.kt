fun main() {
    val cache: LruCache<Int, Int> = LruCacheImpl(4)

    cache.put(1, 1)
    cache.put(2, 2)
    cache.put(3, 3)
    cache.put(4, 4)
    println("Number of entries = capacity (${cache.size}):\n$cache\n")

    cache.put(5, 5)
    println("Least used entry was removed in order to store new entry:\n$cache\n")

    cache.put(2, 22)
    cache.put(6, 6)
    println("Updating / adding / requesting entry will increase its priority:\n$cache\n")
}