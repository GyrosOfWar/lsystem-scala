package at.wambo.lsystem

import org.jsfml.graphics._
import org.jsfml.window.{Keyboard, Mouse, VideoMode}
import org.jsfml.window.event.Event
import org.jsfml.system.{Vector2f, Vector2i}


import collection.JavaConverters._

/**
 * User: Martin Tomasi
 * Date: 29.05.13
 * Time: 15:17
 */
object Main {
  val xSize = 1280
  val ySize = 800
  val zoomInFactor = 1.2f
  val zoomOutFactor = 0.8f
  val moveDist = 8.0f

  var mouseDrag = false
  var oldMousePos = new Vector2i(0, 0)
  var selectedLSys = 0
  var LSystemCount = 0
  var scaleFactor = 1.0f

  def handleEvents(window: RenderWindow, event: Event, view: View, currentLSys: LSystem) {
    event.`type` match {
      case Event.Type.CLOSED => window.close()
      case Event.Type.MOUSE_BUTTON_PRESSED => mouseDrag = true
      case Event.Type.MOUSE_BUTTON_RELEASED => mouseDrag = false
      case Event.Type.MOUSE_MOVED if mouseDrag => {
        val pos = Mouse.getPosition(window)
        val delta = new Vector2f(oldMousePos.x - pos.x, oldMousePos.y - pos.y)
        view.move(Vector2f.mul(delta, scaleFactor))
        oldMousePos = pos
      }
      case Event.Type.MOUSE_WHEEL_MOVED => {
        if (event.asMouseWheelEvent().delta == -1) {
          view.zoom(zoomInFactor)
          scaleFactor *= zoomInFactor
        }
        else {
          view.zoom(zoomOutFactor)
          scaleFactor *= zoomOutFactor
        }
      }
      case Event.Type.KEY_PRESSED => processKeyboard(event.asKeyEvent().key, view, currentLSys)
      case _ =>
    }
  }

  def processKeyboard(key: Keyboard.Key, view: View, ls: LSystem) {
    key match {
      case Keyboard.Key.W => view.move(0, moveDist * scaleFactor)
      case Keyboard.Key.S => view.move(0, -moveDist * scaleFactor)
      case Keyboard.Key.A => view.move(moveDist * scaleFactor, 0)
      case Keyboard.Key.D => view.move(-moveDist * scaleFactor, 0)
      case Keyboard.Key.O =>
        ls.redraw(-1)
        ls.scaleToView(xSize, ySize)
        view.setSize(xSize, ySize)
        view.setCenter(ls.getCenter)
      case Keyboard.Key.P =>
        ls.redraw(1)
        ls.scaleToView(xSize, ySize)
        view.setSize(xSize, ySize)
        view.setCenter(ls.getCenter)
      case Keyboard.Key.UP => selectedLSys = (selectedLSys + 1) % LSystemCount
      case Keyboard.Key.DOWN => {
        selectedLSys = selectedLSys - 1
        if (selectedLSys < 0) selectedLSys = LSystemCount - 1
      }
      case _ =>
    }
  }

  def timedCall[A](block: => A): A = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    println(s"Time: ${(t1 - t0) / 1000000.toDouble} ms")
    result
  }

  def main(args: Array[String]) {
    val window = new RenderWindow(new VideoMode(xSize, ySize), "L-System")
    window.setFramerateLimit(60)
    val fixed = window.getView
    val view = new View(fixed.getCenter, fixed.getSize)
    view.setSize(xSize, ySize)
    view.move(-xSize / 4.0f, -ySize)

    val koch = LSystem.KochCurve(6)
    val dragonCurve = LSystem.DragonCurve(12)
    val fractalPlant = LSystem.FractalPlant(4)
    val stochasticPlant = LSystem.StochasticPlant(6)
    val carpet = LSystem.Carpet(5)
    val lSystems = List(koch, dragonCurve, fractalPlant, stochasticPlant, carpet)
    LSystemCount = lSystems.length

    // Rotate the l-systems by -90 degrees
    var transform = new Transform()
    transform = Transform.rotate(transform, -90)
    val state = new RenderStates(BlendMode.NONE, transform, null, null)

    for (l <- lSystems) {
      // Draw each LSystem to their own TurtleDrawing
      l.draw()
      // Scale them to the same size (sort of)
      l.scaleToView(xSize, ySize)
      view.setCenter(l.getCenter)
    }

    // Main render loop
    while (window.isOpen) {
      // Handle all of the events
      val eventList = window.pollEvents().asScala
      for (e <- eventList) {
        handleEvents(window, e, view, lSystems(selectedLSys))
      }

      window clear Color.WHITE
      // Draw vertices of the LSystems
      for (v <- lSystems(selectedLSys).vertices) {
        if (v != null) {
          window.draw(v, state)
        }
      }

      // Set the view and swap the buffers
      window setView view
      window.display()
    }
  }
}
