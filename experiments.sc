import scala.util.Random

object AA {
  val a = Random.nextInt()
}
trait A {
  def a = AA.a
}

trait B extends A
trait C extends A

object Bb extends B {

}

object Cc extends C {

}


Bb.a
Cc.a

