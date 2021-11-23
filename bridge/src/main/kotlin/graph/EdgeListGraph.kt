package graph

import drawing.DrawingApi
import java.io.File

class EdgeListGraph(private val drawingApi: DrawingApi) : Graph(drawingApi) {
    private val edges = mutableListOf<Edge>()
    private var size: Int = 0

    override fun readFromFile(filename: String) {
        val lines = File(filename).bufferedReader().readLines()

        size = lines.first().toInt()

        lines.drop(1).forEach { line ->
            val parsed = line.split(" ").map { it.toInt() }
            edges.add(Edge(parsed[0] - 1, parsed[1] - 1))
        }
    }

    override fun draw() {
        drawVertices(size)

        edges.forEach {
            drawEdge(it.from, it.to)
        }

        drawingApi.show()
    }

    private data class Edge(val from: Int, val to: Int)
}
