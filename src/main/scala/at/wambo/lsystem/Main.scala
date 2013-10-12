package at.wambo.lsystem

import org.jsfml.graphics._
import org.jsfml.window.{Keyboard, Mouse, VideoMode}
import org.jsfml.window.event.Event
import org.jsfml.system.{Vector2f, Vector2i}
import java.nio.file.Paths

/**
 * User: Martin
 * Date: 29.05.13
 * Time: 15:17
 */
object Main {
  var mouseDrag: Boolean = false
  var oldMousePos: Vector2i = new Vector2i(0, 0)
  val xSize = 1280
  val ySize = 800
  var selected = 0
  var LSystemCount: Int = 0
  var scaleFactor = 1.0f
  val moveDist = 8.0f
  val zoomInFactor = 1.2f
  val zoomOutFactor = 0.8f
  var grayscale = 0.0f

  def handleEvents(window: RenderWindow, event: Event, view: View, currentLSys: LSystem) {
    event.`type` match {
      case Event.Type.CLOSED => window.close()
      case Event.Type.MOUSE_BUTTON_PRESSED => mouseDrag = true
      case Event.Type.MOUSE_BUTTON_RELEASED => mouseDrag = false
      case Event.Type.MOUSE_MOVED if mouseDrag => {
        val pos = Mouse.getPosition(window)
        val delta = new Vector2f(oldMousePos.x - pos.x, oldMousePos.y - pos.y)
        //view.setCenter(-pos.x, -pos.y)
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
      case Keyboard.Key.O => ls.redraw(-1)
      case Keyboard.Key.P => ls.redraw(1)
      case Keyboard.Key.UP => selected = (selected + 1) % LSystemCount
      case Keyboard.Key.DOWN => {
        selected = selected - 1
        if (selected < 0) selected = LSystemCount - 1
      }
      case Keyboard.Key.G => grayscale = if (grayscale == 0.0f) 1.0f else 0.0f
      case _ => {}
    }
  }

  def main(args: Array[String]) {
    val window = new RenderWindow(new VideoMode(xSize, ySize), "L-System")
    window.setFramerateLimit(60)
    val fixed = window.getView
    val view = new View(fixed.getCenter, fixed.getSize)
    view.setCenter(xSize / 2.0f, ySize / 2.0f)
    val koch = LSystems.KochCurve(6, 10)
    val dragonCurve = LSystems.DragonCurve(9, 10)
    val fractalPlant = LSystems.FractalPlant(4, 10)
    val stochasticPlant = LSystems.StochasticPlant(6, 10)
    val carpet = LSystems.Carpet(5, 5)
    val tree = LSystems.Tree(5, 5)
    val lSystems = List(koch, dragonCurve, fractalPlant, stochasticPlant, carpet)
    LSystemCount = lSystems.length

    // Rotate the l-systems by -90 degrees
    var transform = new Transform()
    transform = Transform.rotate(transform, -90)

    // Load the shader
    val shader = new Shader()
    // ugh
    //    shader.loadFromFile(
    //      Paths.get("frag.glsl"),
    //      Paths.get("vert.glsl"))

    shader.loadFromFile(
      Paths.get(getClass.getClassLoader.getResource("frag.glsl").getPath),
      Paths.get(getClass.getClassLoader.getResource("vert.glsl").getPath))
    shader.setParameter("grayscale", grayscale)
    val state = new RenderStates(BlendMode.NONE, transform, null, shader)

    for (l <- lSystems) {
      l.draw()
    }
    while (window.isOpen) {
      val e = window.pollEvent()
      if (e != null) {
        handleEvents(window, e, view, lSystems(selected))
      }
      window.clear(Color.WHITE)
      shader.setParameter("grayscale", grayscale)
      for (v <- lSystems(selected).vertices) {
        if (v != null)
          window.draw(v, state)
      }
      window.setView(view)
      window.display()
    }
  }
}
