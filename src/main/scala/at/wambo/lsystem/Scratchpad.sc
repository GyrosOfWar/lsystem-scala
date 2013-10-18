import at.wambo.lsystem.ParLSystem

object Scratchpad {
  val pl = ParLSystem.sampleLSystem

  pl.getSuccessorFor('F')
  pl.getSuccessorFor('X')

  pl.getNumberOfSuccessorsFor('F')
  pl.getNumberOfSuccessorsFor('X')

}







