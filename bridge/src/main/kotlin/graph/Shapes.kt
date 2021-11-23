package graph

data class Point(val x: Double, val y: Double)

data class Line(val from: Point, val to: Point)

data class Circle(val center: Point, val radius: Double)
