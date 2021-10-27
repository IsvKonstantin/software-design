interface LruCache<K, V> {
    val size: Int

    fun put(key: K, value: V)

    fun get(key: K): V?

    fun clear()
}