package at.wambo.lsystem

import org.jsfml.graphics.VertexArray

/*
 * User: Martin
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
class LSystem(private val axiom: String,
              private var iterations: Int,
              private val angle: Double,
              private val distance: Int)
             (private val rules: (Char, Double) => Option[String]) {
  private val td = new TurtleDrawing()
  private val drawFunction = (c: Char) => c match {
    case 'X' | 'Y' => {}
    case 'F' => td.forward(distance)
    case 'G' => td.penUp = true; td.forward(distance); td.penUp = false
    case '+' => td.anglePlus(angle)
    case '-' => td.angleMinus(angle)
    case '[' => td.pushStack()
    case ']' => td.popStack()
    case _ => throw new IllegalArgumentException("Not a valid character: " + c)
  }

  /**
   * Accesses the list of VertexArrays of the TurtleDrawing
   * @return Vector[VertexArray] with the vertices to draw
   */
  def vertices: List[VertexArray] = td.vertices.toList

  /**
   * Draws the L-System to the TurtleDrawing.
   */
  def draw() {
    def stepRec(current: String, n: Int): String = {
      n match {
        case 0 => current
        case _ => stepRec(current.map {
          c => rules(c, math.random).getOrElse(c)
        } mkString, n-1)
      }
    }
    val state = stepRec(axiom, iterations)
    state map drawFunction
  }

  def redraw(iterationDelta: Int) {
    iterations += iterationDelta
    td.clear()
    draw()
  }
}

object LSystems {
  def FractalPlant(iterations: Int, distance: Int) = new LSystem("F", iterations, Math.toRadians(22), distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("FF-[-F+F+F]+[+F-F-F]")
      case _ => None
    }
  })

  def KochCurve(iterations: Int, distance: Int) = new LSystem("F", iterations, Math.PI / 2.0, distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("F+F-F-F+F")
      case _ => None
    }
  })

  def DragonCurve(iterations: Int, distance: Int) = new LSystem("FX", iterations, Math.PI / 2.0, distance)({
    (c: Char, d: Double) => c match {
      case 'X' => Some("X+YF")
      case 'Y' => Some("FX-Y")
      case _ => None
    }
  })

  def StochasticPlant(iterations: Int, distance: Int) = new LSystem("F", iterations, Math.toRadians(30), distance)({
    (c: Char, d: Double) => c match {
      case 'F' if d < 1.0 / 3.0 => Some("F[+F]F[-F]F")
      case 'F' if d < 2.0 / 3.0 => Some("F[+F]F-F+")
      case 'F' if d <= 3.0 / 3.0 => Some("F[-F]F[F+]")
      case _ => None
    }
  })

  def Carpet(iterations: Int, distance: Int) = new LSystem("F-F-F-F", iterations, Math.toRadians(90), distance)({
    (c: Char, d: Double) => c match {
      case 'F' => Some("F[F]-F+F[--F]+F-F")
      case _ => None
    }
  })

  def Tree(iterations: Int, distance: Int) = new LSystem("G", iterations, Math.toRadians(100), distance)({
    (c: Char, d: Double) => c match {
      case 'G' => Some("F[+++++G][-------G]-F[++++G][------G]-F[+++G][-----G]-FG")
      case _ => None
    }
  })
}