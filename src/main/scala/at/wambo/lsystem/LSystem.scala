package at.wambo.lsystem

import org.jsfml.graphics.VertexArray
import collection.JavaConverters._
import org.jsfml.system.Vector2f
import scala.annotation.tailrec

/*
 * User: Martin Tomasi
 * Date: 27.05.13
 * Time: 16:50
 */
/**
 * Represents a L-System and has a method to evaluate itself and
 * draw itself onto a TurtleDrawing.
 * @param axiom  Start string for the grammar.
 * @param iterations Number of iterations the L-System should have after draw() is called
 * @param angle Angle to increase or decrease on '+' or '-' in the state string
 * @param distance Distance in pixels to draw forward on when encountering a forward action in the state string
 * @param rules This is a representation of the rules of the L-System
 */
case class LSystem(axiom: String,
              iterations: Int,
              angle: Double,
              distance: Int)
             (rules: (Char, Double) => Option[String]) {
  private val td = new TurtleDrawing()

  /**
   * Accesses the list of VertexArrays of the TurtleDrawing
   * @return Vector[VertexArray] with the vertices to draw
   */
  def vertices: List[VertexArray] = td.vertices.toList

  def step: String = {
    @tailrec
    def stepRec(current: String, n: Int): String = n match {
      case 0 => current
      case _ => stepRec(current.map {
        c => rules(c, math.random).getOrElse(c)
      } mkString, n - 1)
    }

    stepRec(axiom, iterations)
  }

  def drawChar(c: Char): DrawAction = c match {
    case 'F' => Forward(distance)
    case '+' => AnglePlus(angle)
    case '-' => AngleMinus(angle)
    case '[' => PushStack()
    case ']' => PopStack()
    case 'X' | 'Y' => NoOp()
  }

  def drawList: IndexedSeq[DrawAction] = step map drawChar

  def draw() = drawList foreach(_.action(td))

  def getBounds(windowX: Float, windowY: Float): (Vector2f, Vector2f) = {
    (new Vector2f(td.xMax, td.yMax), new Vector2f(td.xMin, td.yMin))
  }

  def scaleToView(x: Int, y: Int) = td.scaleToView(x, y)
}

object LSystem {

  def FractalPlant(iterations: Int, distance: Int = 1) = LSystem("F", iterations, Math.toRadians(22), distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("FF-[-F+F+F]+[+F-F-F]")
      case _ => None
    }
  })

  def KochCurve(iterations: Int, distance: Int = 1) = LSystem("F", iterations, Math.PI / 2.0, distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("F+F-F-F+F")
      case _ => None
    }
  })

  def DragonCurve(iterations: Int, distance: Int = 1) = LSystem("FX", iterations, Math.PI / 2.0, distance)({
    (c: Char, d: Double) => c match {
      case 'X' => Some("X+YF")
      case 'Y' => Some("FX-Y")
      case _ => None
    }
  })

  def StochasticPlant(iterations: Int, distance: Int = 1) = LSystem("F", iterations, Math.toRadians(30), distance)({
    (c: Char, d: Double) => c match {
      case 'F' if d < 1.0 / 3.0 => Some("F[+F]F[-F]F")
      case 'F' if d < 2.0 / 3.0 => Some("F[+F]F-F+")
      case 'F' if d <= 3.0 / 3.0 => Some("F[-F]F[F+]")
      case _ => None
    }
  })

  def Carpet(iterations: Int, distance: Int = 1) = LSystem("F-F-F-F", iterations, Math.toRadians(90), distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("F[F]-F+F[--F]+F-F")
      case _ => None
    }
  })

  def Tree(iterations: Int, distance: Int = 1) = LSystem("G", iterations, Math.toRadians(100), distance)({
    (c: Char, d: Double) => c match {
      case 'G' => Some("F[+++++G][-------G]-F[++++G][------G]-F[+++G][-----G]-FG")
      case _ => None
    }
  })
}