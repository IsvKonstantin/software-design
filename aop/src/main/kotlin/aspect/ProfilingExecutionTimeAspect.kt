package aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import java.util.*


@Aspect
class ProfilingExecutionTimeAspect {
    private val executions = HashMap<String, MutableList<Double>>()

    //    @Around("@annotation(aspect.Profile)")
    @Around("execution(* plane.PlaneManagerImpl.*(..)) || execution(* passenger.model.implementations.*.*(..))")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val executionStart = System.nanoTime()
        val methodName = "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}"
        val result = joinPoint.proceed(joinPoint.args)
        val time = System.nanoTime() - executionStart
        executions.getOrPut(methodName) { mutableListOf() }.add(time.toDouble() * 1e-6)

        return result
    }

    fun printStatistics() {
        val root = Node("aop")
        executions.forEach { (name, statistics) ->
            val parts = name.split(".").toMutableList()
            var node = root
            parts.forEach { part ->
                node = node.children.find { it.name == part } ?: Node(part).also {
                    node.children.add(it)
                }
            }
            node.statistics = statistics
        }

        println(root)
    }

    class Node(
        val name: String,
        val children: MutableList<Node> = mutableListOf(),
        var statistics: List<Double>? = null
    ) {
        override fun toString(): String {
            val buffer = StringBuilder()
            print(buffer, "", "")
            return buffer.toString()
        }

        private fun print(buffer: StringBuilder, prefix: String, childrenPrefix: String) {
            buffer.apply {
                append(prefix)
                if (statistics != null) {
                    val cnt = "cnt=%d".format(Locale("en"), statistics!!.size).padEnd(10)
                    val avg = "avg=%.2fms".format(Locale("en"), statistics!!.average()).padEnd(15)
                    val sum = "sum=%.2fms".format(Locale("en"), statistics!!.sum()).padEnd(15)

                    append("[$cnt $avg $sum]  $name")
                } else {
                    append(name)
                }
                append('\n')
            }

            val it = children.iterator()
            while (it.hasNext()) {
                val next = it.next()
                if (it.hasNext()) {
                    next.print(buffer, "$childrenPrefix├── ", "$childrenPrefix│   ")
                } else {
                    next.print(buffer, "$childrenPrefix└── ", "$childrenPrefix    ")
                }
            }
        }
    }
}