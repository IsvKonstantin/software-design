import drawing.DrawingApi
import drawing.JavaAwtDrawingApi
import drawing.JavaFxDrawingApi
import graph.AdjacencyMatrixGraph
import graph.EdgeListGraph
import graph.Graph

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: <drawing api> <graph representation>" }

    val drawingApi: DrawingApi = when (args[0]) {
        "awt" -> JavaAwtDrawingApi()
        "fx" -> JavaFxDrawingApi()
        else -> error("Unknown drawing api: ${args[0]}")
    }

    lateinit var filename: String
    val graph: Graph = when (args[1]) {
        "adj" -> {
            filename = "src/main/resources/AdjacencyMatrix.txt"
            AdjacencyMatrixGraph(drawingApi)
        }
        "edge" -> {
            filename = "src/main/resources/EdgeList.txt"
            EdgeListGraph(drawingApi)
        }
        else -> error("Unknown graph representation: ${args[1]}")
    }

    graph.readFromFile(filename)
    graph.draw()
}