package at.wambo.lsystem.tests

import org.scalameter.api._
import org.jsfml.system.Vector2i
import at.wambo.lsystem.JSFMLExtensions._

object JSFMLExtPerfTest extends PerformanceTest.Microbenchmark {
  val ranges = for {
    x <- Gen.range("xs")(0, 15000, 500)
    y <- Gen.range("ys")(15000, 0, -500)
  } yield new Vector2i(x, y)

  performance of "Vector2i" in {
    measure method "+" in {
      using(ranges) in {
        c => c + c
      }

    }
  }

}