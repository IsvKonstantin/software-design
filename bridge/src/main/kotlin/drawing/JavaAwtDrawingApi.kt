package drawing

import graph.Circle
import graph.Line
import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

class JavaAwtDrawingApi : DrawingApi, Frame() {
    private val circles = mutableListOf<Ellipse2D.Double>()
    private val lines = mutableListOf<Line2D.Double>()

    override val drawingAreaWidth = 1000

    override val drawingAreaHeight = 1000

    override fun drawCircle(circle: Circle) {
        circles.add(
            Ellipse2D.Double(
                circle.center.x - circle.radius,
                circle.center.y - circle.radius,
                circle.radius * 2,
                circle.radius * 2
            )
        )
    }

    override fun drawLine(line: Line) {
        lines.add(
            Line2D.Double(
                line.from.x,
                line.from.y,
                line.to.x,
                line.to.y
            )
        )
    }

    override fun show() {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                dispose()
                exitProcess(0)
            }
        })

        setSize(drawingAreaWidth, drawingAreaHeight)
        isVisible = true
    }

    override fun paint(g: Graphics?) {
        val graphics2D = g as Graphics2D
        graphics2D.paint = Color.black

        lines.forEach {
            graphics2D.draw(it)
        }

        circles.forEach {
            graphics2D.fill(it)
        }
    }
}