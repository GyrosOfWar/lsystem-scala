package at.wambo.lsystem

import org.jsfml.graphics.VertexArray
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
 * @param rules A function that returns Some[String] if given a production that has a defined result, else None.
 */
case class LSystem(axiom: String,
                   iterations: Int,
                   angle: Double,
                   distance: Int)
                  (rules: (Char, Double) => Option[String]) {
  private val td = new TurtleDrawing()

  /**
   * Accesses the list of VertexArrays of the TurtleDrawing
   * @return Seq[VertexArray] with the vertices to draw
   */
  def vertices: Seq[VertexArray] = td.vertices.toSeq

  /**
   * Does the given number of iterations based on the given axiom
   * @return state string after the given number of iterations.
   */
  def step: String = {
    @tailrec
    def stepRec(current: String, n: Int): String = n match {
      case 0 => current
      case _ => stepRec(current.map {
        c => rules(c, math.random) getOrElse c
      } mkString, n - 1)
    }

    stepRec(axiom, iterations)
  }

  /**
   * Function mapping characters in the state string to DrawActions.
   * @param c The character to process
   * @return An instance of DrawAction. Throws IllegalArgumentException if not a valid character.
   */
  def drawChar(c: Char): DrawAction = c match {
    case 'F' => Forward(distance)
    case 'G' => ForwardWithoutDraw(distance)
    case '+' => AnglePlus(angle)
    case '-' => AngleMinus(angle)
    case '[' => PushStack()
    case ']' => PopStack()
    case 'X' | 'Y' => NoOp()
    case _ => throw new IllegalArgumentException(s"Not a valid character: $c")
  }

  /**
   * Creates a sequence of DrawActions by mapping the drawChar function to the state string.
   * @return sequence of DrawActions
   */
  def drawList: IndexedSeq[DrawAction] = step map drawChar

  /**
   * Draws the DrawActions to the turtle drawing by executing its action.
   */
  def draw() = drawList foreach (_.action(td))

  /**
   * Returns the minimum and maximum values of all the vertex positions in the LSystem.
   * @return The first vector in the tuple is the maximum values, the second is the minimum values.
   */
  def getBounds: (Vector2f, Vector2f) = {
    (new Vector2f(td.xMax, td.yMax), new Vector2f(td.xMin, td.yMin))
  }

  /**
   * Scales the LSystem to the given window size.
   * @param xSize Window width
   * @param ySize Window height
   */
  def scaleToView(xSize: Int, ySize: Int) = td.scaleToView(xSize, ySize)

  // TODO implement
  def redraw(iterDelta: Int) = ???
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