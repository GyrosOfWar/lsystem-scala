package at.wambo.lsystem

/**
 * User: Martin
 * Date: 14.10.13
 * Time: 11:19
 */
trait DrawAction {
  val symbol: Char
  val action: (TurtleDrawing) => Unit
}

case class Forward(distance: Int) extends DrawAction {
  val symbol = 'F'
  val action = (td: TurtleDrawing) => td.forward(distance)
}

case class AnglePlus(angle: Double) extends DrawAction {
  val symbol = '+'
  val action = (td: TurtleDrawing) => td.anglePlus(angle)
}

case class AngleMinus(angle: Double) extends DrawAction {
  val symbol = '-'
  val action = (td: TurtleDrawing) => td.angleMinus(angle)
}

case class PushStack() extends DrawAction {
  val symbol = '['
  val action = (td: TurtleDrawing) => td.pushStack()
}

case class PopStack() extends DrawAction {
  val symbol = ']'
  val action = (td: TurtleDrawing) => td.popStack()
}

case class ForwardWithoutDraw(distance: Int) extends DrawAction {
  val symbol = 'G'
  val action = (td: TurtleDrawing) => {
    td.penUp = true
    td.forward(distance)
    td.penUp = false
  }
}

case class NoOp() extends DrawAction {
  val symbol = 'X'
  val action = (td: TurtleDrawing) => ()
}