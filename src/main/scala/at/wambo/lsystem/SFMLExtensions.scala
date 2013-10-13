package at.wambo.lsystem

import org.jsfml.system.{Vector2i, Vector2f}

/**
 * User: Martin Tomasi
 * Date: 14.10.13
 * Time: 00:12
 */
object SFMLExtensions {

  implicit class Vector2fExtensions(thisVec: Vector2f) {
    def +(thatVec: Vector2f) = Vector2f.add(thisVec, thatVec)

    def unary_-() = new Vector2f(-thisVec.x, -thisVec.y)

    def -(thatVec: Vector2f) = Vector2f.sub(thisVec, thatVec)

    def *(thatVec: Vector2f) = Vector2f.componentwiseMul(thisVec, thatVec)

  }

  implicit class Vector2iExtensions(thisVec: Vector2i) {
    def +(thatVec: Vector2i) = Vector2i.add(thisVec, thatVec)

    def unary_-() = new Vector2i(-thisVec.x, -thisVec.y)

    def -(thatVec: Vector2i) = Vector2i.sub(thisVec, thatVec)

    def *(thatVec: Vector2i) = Vector2i.componentwiseMul(thisVec, thatVec)

  }

}
