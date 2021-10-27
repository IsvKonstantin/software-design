import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LruCacheImplTest {
    // LruCache implementation based on LinkedHashMap for testing purposes
    // accessOrder - the ordering mode - true for access-order
    class LinkedHashMapCache<K, V>(private val capacity: Int) : LinkedHashMap<K, V>(capacity, 1f, true) {

        override fun removeEldestEntry(eldest: Map.Entry<K, V>): Boolean {
            return size > capacity
        }
    }

    private lateinit var cache: LruCacheImpl<Int, Int>

    @BeforeEach
    fun createCache() {
        cache = LruCacheImpl(4)
    }

    @Test
    fun `empty LruCache test`() {
        assertEquals(0, cache.size)
        assertEquals(null, cache.get(0))
    }

    @Test
    fun `LruCache clear test`() {
        cache.put(1, 1)
        cache.put(2, 1)
        cache.put(3, 1)
        cache.put(4, 1)
        cache.clear()
        assertEquals(0, cache.size)
    }

    @Test
    fun `LruCache size test`() {
        assertEquals(0, cache.size)
        cache.put(1, 1)
        assertEquals(1, cache.size)
        cache.put(2, 1)
        cache.put(3, 1)
        cache.put(4, 1)
        assertEquals(4, cache.size)
        cache.put(5, 1)
        assertEquals(4, cache.size)
    }

    @Test
    fun `LruCache get existing entry test`() {
        cache.put(1, 1)
        assertEquals(1, cache.get(1))
    }

    @Test
    fun `LruCache put should update entry test`() {
        cache.put(1, 1)
        assertEquals(1, cache.size)
        cache.put(1, 1234)
        assertEquals(1, cache.size)
        assertEquals(1234, cache.get(1))
    }

    @Test
    fun `LruCache get non-existent entry test`() {
        assertEquals(null, cache.get(2))
        cache.put(1, 1)
        assertEquals(null, cache.get(2))
    }

    @Test
    fun `LruCache priority test`() {
        cache.put(1, 1)
        cache.put(2, 2)
        cache.put(3, 3)
        cache.put(4, 4)
        cache.put(5, 5)
        assertEquals(null, cache.get(1))
        assertEquals(2, cache.get(2))
        cache.put(6, 6)
        assertEquals(null, cache.get(3))
        listOf(2, 4, 5, 6).forEach { i ->
            assertEquals(i, cache.get(i))
        }
    }

    @Test
    fun `LruCache put with repeating keys test`() {
        cache.put(1, 1)
        assertEquals(1, cache.size)
        assertEquals(1, cache.get(1))
        cache.put(1, 1)
        assertEquals(1, cache.size)
        assertEquals(1, cache.get(1))
        cache.put(2, 2)
        assertEquals(2, cache.get(2))
        cache.put(2, 222)
        assertEquals(222, cache.get(2))
        cache.put(3, 3)
        assertEquals(3, cache.size)
    }

    @Test
    fun `LruCache random test, comparing with LruCache based on LinkedHashMap`() {
        val iterations = 1000000
        val keysRange = 0..20
        val valuesRange = 0..10
        val operationsRange = 0..1

        val cacheExpected: HashMap<Int, Int> = LinkedHashMapCache(1)
        val cacheActual: LruCache<Int, Int> = LruCacheImpl(1)
        val keys = (0..iterations).map { keysRange.random() }

        for (key in keys) {
            when ((operationsRange).random()) {
                0 -> {
                    assertEquals(cacheExpected[key], cacheActual.get(key))
                }
                1 -> {
                    val value = valuesRange.random()
                    cacheExpected[key] = value
                    cacheActual.put(key, value)
                }
            }
            assertEquals(cacheExpected.size, cacheActual.size)
        }
    }
}