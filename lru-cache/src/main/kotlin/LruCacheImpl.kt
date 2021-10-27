class LruCacheImpl<K, V>(private val capacity: Int) : LruCache<K, V> {
    private val hashMap = hashMapOf<K, Node<K, V>>()
    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    init {
        assert(capacity > 0) { "Capacity should be positive" }
    }

    override val size: Int
        get() {
            assert(hashMap.size <= capacity) { "Cache size shouldn't exceed capacity" }
            return hashMap.size
        }

    override fun put(key: K, value: V) {
        assert(size <= capacity) { "Cache size shouldn't exceed capacity" }
        val node = Node(key, value)

        if (key in hashMap) {
            // remove node from cache
            val oldNode = hashMap.getValue(key)
            remove(oldNode)
        } else if (size == capacity) {
            assert(tail != null) { "Cache is not empty; therefore tail shouldn't be null" }
            // remove tail node if cache size equals capacity size
            tail?.let {
                remove(it)
                hashMap.remove(it.key)
            }
        }

        // move node to head (previously removing it from cache if necessary)
        hashMap[key] = node
        moveToHead(node)

        assert(tail != null && head != null) { "Cache is not empty; therefore tail & head shouldn't be null" }
        assert(size <= capacity) { "Cache size shouldn't exceed capacity" }
        assert(hashMap.containsKey(key)) { "Cache contains added / updated value for provided key" }
        assert(head?.value == value && head?.key == key) {
            "Added / updated value for provided key should be the most recent in cache"
        }
    }

    override fun get(key: K): V? {
        assert(size <= capacity) { "Cache size shouldn't exceed capacity" }

        return if (key !in hashMap) {
            // return null if cache doesn't contain such key
            null
        } else {
            val node = hashMap.getValue(key)

            // update order in cache
            remove(node)
            moveToHead(node)

            assert(tail != null && head != null) { "Cache is not empty; therefore tail & head shouldn't be null" }
            assert(size <= capacity) { "Cache size shouldn't exceed capacity" }
            assert(head?.key == key) { "Requested value for provided key should be the most recent in cache" }
            // return value corresponding to provided key
            node.value
        }
    }

    override fun clear() {
        // clear hash map, make head and tail null
        hashMap.clear()
        head = null
        tail = null

        assert(size == 0) { "Cache size should be zero" }
        assert(head == null && tail == null) { "Cache is empty; therefore tail & head should be null" }
    }

    private fun moveToHead(node: Node<K, V>) {
        // update head
        node.next = head
        head?.prev = node
        head = node
        head?.prev = null

        // update tail if provided node is the only node in cache
        if (size == 1) {
            tail = node
        }

        assert(head == node && node.prev == null) { "Provided node should become head node" }
    }

    private fun remove(node: Node<K, V>) {
        // update tail if provided node was tail
        if (node.next == null) {
            tail = node.prev
        }

        // update head if provided node was head
        if (node.prev == null) {
            head = node.next
        }

        // connect node's previous and next nodes
        node.next?.prev = node.prev
        node.prev?.next = node.next

        assert(node.next?.prev !== node && node.prev?.next !== node) { "Provided node should be properly removed" }
    }

    override fun toString(): String {
        return "{${buildString {
            hashMap.forEach { (_, Node) ->
                append("${Node.key}=${Node.value}, ")
            }
        }.dropLast(2)}}"
    }
}

data class Node<K, V>(val key: K, val value: V, var next: Node<K, V>? = null, var prev: Node<K, V>? = null)