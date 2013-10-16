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
  private var _xMax = Float.NegativeInfinity
  private var _yMax = Float.NegativeInfinity
  private var _xMin = Float.PositiveInfinity
  private var _yMin = Float.PositiveInfinity

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

    if (newPos.x > _xMax) _xMax = newPos.x
    if (newPos.y > _yMax) _yMax = newPos.y

    if (newPos.x < _xMin) _xMin = newPos.x
    if (newPos.y < _yMin) _yMin = newPos.y

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

  def scaleToView(xSize: Int, ySize: Int) {
    val allVertices = vertices.flatMap(_.asScala).toVector
    val min = new Vector2f(_xMin, _yMin)
    val max = new Vector2f(_xMax, _yMax)
    val size = new Vector2f(xSize.toFloat, ySize.toFloat)
    val scaled = allVertices.map(vertex => {
      val p = vertex.position
      val pos = (p - min) / (max - min) * size
      new Vertex(pos, vertex.color, vertex.texCoords)
    }).grouped(1024).toVector

    this.clear()

    scaled foreach (vec => {
      val vertexArray = new VertexArray(PrimitiveType.LINES)
      vertexArray.addAll(vec.asJava)
      vertices += vertexArray
    })

    val xs = scaled.flatten.map(_.position.x)
    val ys = scaled.flatten.map(_.position.y)

    this._xMax = xs.max
    this._yMax = ys.max
    this._xMin = xs.min
    this._yMin = ys.min

  }

  def getCenter = new Vector2f(xMax / 2.0f, yMax / 2.0f)

  def xMax = _xMax

  def xMin = _xMin

  def yMax = _yMax

  def yMin = _yMin

}
