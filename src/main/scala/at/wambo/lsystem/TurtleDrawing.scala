package at.wambo.lsystem

import org.jsfml.graphics._
import org.jsfml.system.Vector2f
import collection.mutable
import collection.mutable.ListBuffer
import collection.JavaConverters._
import at.wambo.lsystem.JSFMLExtensions._

/**
 * User: Martin Tomasi
 * Date: 27.05.13
 * Time: 16:50
 */
class TurtleDrawing() {

  val vertices = ListBuffer(new VertexArray(PrimitiveType.LINES))
  var color = Color.BLACK
  var penUp: Boolean = false
  private val stack = new mutable.ArrayStack[Double]
  private var position = new Vector2f(0, 0)
  private var angle: Double = 0.0
  private var verticesListIndex = 0

  /**
   * Moves the turtle forward.
   * @param distance distance in pixels to move forward
   */
  def forward(distance: Float) {
    if (vertices(verticesListIndex).size() >= 1023) {
      verticesListIndex += 1
      vertices += new VertexArray(PrimitiveType.LINES)
    }
    val newPos = new Vector2f(
      (position.x + Math.cos(angle) * distance).toFloat,
      (position.y + Math.sin(angle) * distance).toFloat)

    if (!penUp) {
      vertices(verticesListIndex).add(new Vertex(position, color))
      vertices(verticesListIndex).add(new Vertex(newPos, color))
    }
    position = newPos
  }

  /**
   * Increases the angle by a given amount.
   * @param d Amount to increase the angle with, in radians.
   */
  def anglePlus(d: Double) {
    angle += d
  }

  /**
   * Decreases the angle by a given amount.
   * @param d Amount to decrease the angle with, in radians.
   */
  def angleMinus(d: Double) {
    angle -= d
  }

  /**
   * Resets the TurtleDrawing to its initial state.
   */
  def clear() {
    vertices foreach (_.clear())
    stack.clear()
    angle = 0
    position = new Vector2f(0, 0)
    color = Color.BLACK
    verticesListIndex = 0
  }

  /**
   * Moves the turtle to a given direction,
   * without drawing anything.
   * @param x X coordinate for new position
   * @param y Y coordinate for new position
   */
  def moveTo(x: Float, y: Float) {
    position = new Vector2f(x, y)
  }

  /**
   * Moves the turtle to a given direction,
   * without drawing anything.
   * @param x X coordinate for new position
   * @param y Y coordinate for new position
   */
  def moveTo(x: Double, y: Double) {
    position = new Vector2f(x.toFloat, y.toFloat)
  }

  /**
   * Pushes the current position and angle to the stack
   */
  def pushStack() {
    stack push angle
    stack push position.x
    stack push position.y
  }

  /**
   * Gets the position and angle from the stack and
   * restores it.
   */
  def popStack() {
    val y = stack.pop().toFloat
    val x = stack.pop().toFloat
    val ang = stack.pop()

    angle = ang
    moveTo(x, y)
  }
}
