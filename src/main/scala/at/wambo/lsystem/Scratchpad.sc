//import at.wambo.lsystem.{LSystems, LSystem}
//
//object Scratchpad {
//  val axiom = "F"
//  val iterations = 5
//  var color = 0
//  val rules = (c: Char, d: Double) => {
//    c match {
//      case 'F' => Some("FF-[-F+F+F]+[+F-F-F]")
//      case _ => None
//    }
//  }
////  "FF+C0[F+F-]C1".sliding(2).
////    map(s => (s(0), s(1))).
////    map(d => d match {
////    case ('C', x) => color = x - '0'; println(color); ('C', x)
////    case _ => null
////  }).filter(_ != null).toList
//  val a = "FF+C0[F+F-]C1".sliding(2).map(st => {
//    val (fst, snd) = (st(0), st(1))
//    fst match {
//      case 'C' => color = snd - '0'; println(color)
//      case _ =>
//    }
//  })
//}

import scala.collection.mutable

object Scratchpad {
  class Image(val xSize: Int = 800, val ySize: Int = 600) {
    private val data = Array.fill(xSize, ySize)(0)
  }

  class TurtleDrawing {
    private var position = (0, 0)
    private var angle = 0.0
    var color = (255, 255, 255)
    private val stack = new mutable.Stack[Double]

    def forward(distance: Int) {
      val newX = math.cos(angle) * distance
      val newY = math.sin(angle) * distance
      position = (newX.toInt, newY.toInt)
    }
    def anglePlus(deg: Double) { angle += deg }
    def angleMinus(deg: Double) { angle -= deg }
    def pushStack() {
      stack.push(angle)
      stack.push(position._1)
      stack.push(position._2)
    }
    def popStack() {
      val y = stack.pop()
      val x = stack.pop()
      val ang = stack.pop()
      position = (x.toInt, y.toInt)
      angle = ang
    }
  }



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

  val dr = new TurtleDrawing

  class LSystem {
    // TODO
  }

  def drawFunction(state: String): List[DrawAction] = {
    val buf = new mutable.ListBuffer[DrawAction]()
    for(c <- state) {
      c match {
        case 'F' => buf += Forward(distance = 5)
        case _ => throw new IllegalArgumentException("bleh")
      }
    }

    buf.toList
  }
//  val s = drawFunction("FF+-")
  println("fffddd")
}







