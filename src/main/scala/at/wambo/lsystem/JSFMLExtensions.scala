package at.wambo.lsystem

import org.jsfml.system.{Vector2i, Vector2f}

/**
 * User: Martin Tomasi
 * Date: 14.10.13
 * Time: 00:12
 */
object JSFMLExtensions {

  /**
   * Defines some convenience operators and methods for the SFML Vector2f class.
   * @param thisVec Vector2f to operate on.
   */
  implicit class Vector2fExtensions(thisVec: Vector2f) {
    def +(thatVec: Vector2f) = Vector2f.add(thisVec, thatVec)

    def unary_-() = Vector2f.neg(thisVec)

    def -(thatVec: Vector2f) = Vector2f.sub(thisVec, thatVec)

    def *(thatVec: Vector2f) = Vector2f.componentwiseMul(thisVec, thatVec)

    def +(f: Float) = new Vector2f(thisVec.x + f, thisVec.y + f)

    def -(f: Float) = new Vector2f(thisVec.x - f, thisVec.y - f)

    def /(thatVec: Vector2f) = Vector2f.componentwiseDiv(thisVec, thatVec)

    /**
     * Dot product of two vectors v1 and v2.
     * @param thatVec other vector
     * @return v1.x * v2.x + v1.y + v2.y
     */
    def dot(thatVec: Vector2f) = thisVec.x * thatVec.x + thisVec.y * thatVec.y

  }

  /**
   * Defines some convenience operators and methods for the SFML Vector2i class.
   * @param thisVec Vector2i to operate on.
   */
  implicit class Vector2iExtensions(thisVec: Vector2i) {
    def +(thatVec: Vector2i) = Vector2i.add(thisVec, thatVec)

    def unary_-() = Vector2i.neg(thisVec)

    def -(thatVec: Vector2i) = Vector2i.sub(thisVec, thatVec)

    def *(thatVec: Vector2i) = Vector2i.componentwiseMul(thisVec, thatVec)

    def +(f: Int) = new Vector2i(thisVec.x + f, thisVec.y + f)

    def -(f: Int) = new Vector2i(thisVec.x - f, thisVec.y - f)

    def /(thatVec: Vector2i) = Vector2i.componentwiseDiv(thisVec, thatVec)

  }

}
