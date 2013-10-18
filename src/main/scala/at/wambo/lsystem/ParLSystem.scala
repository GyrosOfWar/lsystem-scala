package at.wambo.lsystem

/**
 * User: martin tomasi
 * Date: 18.10.13
 * Time: 13:24
 */
case class ParLSystem(axiom: String, productions: Map[Char, Array[Char]]) {
  def getSuccessorFor(pred: Char): String = productions(pred).drop(1).mkString

  def getNumberOfSuccessorsFor(pred: Char): Int = productions(pred)(0).toInt

  def step: String = {
    def countModules(current: String): Array[Int] = {
      (current map getNumberOfSuccessorsFor).toArray
    }

    val scanned = countModules(axiom).scan(0)(_ + _)
    ""

  }

}

object ParLSystem {
  def sampleLSystem = ParLSystem("FX",
    Map('X' -> Array(4.toChar, 'X', '+', 'Y', 'F'),
      'Y' -> Array(4.toChar, 'F', 'X', '-', 'Y')).withDefaultValue(Array(0.toChar)))
}