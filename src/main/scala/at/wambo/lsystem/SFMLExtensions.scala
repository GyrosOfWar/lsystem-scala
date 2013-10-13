package at.wambo.lsystem

import org.jsfml.system.Vector2f

/**
 * User: Martin Tomasi
 * Date: 14.10.13
 * Time: 00:12
 */
object SFMLExtensions {

  implicit class Vector2fExtensions(thisVec: Vector2f) {
    def +(thatVec: Vector2f) = new Vector2f(thisVec.x + thatVec.x, thisVec.y + thatVec.y)

    def unary_-() = new Vector2f(-thisVec.x, -thisVec.y)

    def -(thatVec: Vector2f) = new Vector2f(thisVec.x - thatVec.x, thisVec.y - thatVec.y)

  }
}
