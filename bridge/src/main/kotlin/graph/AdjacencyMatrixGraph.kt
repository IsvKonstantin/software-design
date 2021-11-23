package graph

import drawing.DrawingApi
import java.io.File

class AdjacencyMatrixGraph(private val drawingApi: DrawingApi) : Graph(drawingApi) {
    private val adjacencyMatrix = mutableListOf<List<Int>>()
    private var size: Int = 0

    override fun readFromFile(filename: String) {
        File(filename).bufferedReader().forEachLine { line ->
            adjacencyMatrix.add(line.split(" ").map { it.toInt() })
        }
        size = adjacencyMatrix.size
    }

    override fun draw() {
        drawVertices(size)

        (0 until size).forEach { i ->
            (0 until size).forEach { j ->
                if (i < j && adjacencyMatrix[i][j] == 1) {
                    drawEdge(i, j)
                }
            }
        }

        drawingApi.show()
    }
}
