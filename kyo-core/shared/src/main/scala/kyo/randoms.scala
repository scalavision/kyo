package kyo

import scala.Console

import ios._
import envs._

object randoms {

  trait Random {
    def nextInt: Int > IOs
    def nextInt(n: Int): Int > IOs
    def nextLong: Long > IOs
    def nextDouble: Double > IOs
    def nextBoolean: Boolean > IOs
    def nextFloat: Float > IOs
    def nextGaussian: Double > IOs
  }

  object Random {
    given default: Random with {
      val random          = new java.util.Random
      def nextInt         = IOs(random.nextInt())
      def nextInt(n: Int) = IOs(random.nextInt(n))
      def nextLong        = IOs(random.nextLong())
      def nextDouble      = IOs(random.nextDouble())
      def nextBoolean     = IOs(random.nextBoolean())
      def nextFloat       = IOs(random.nextFloat())
      def nextGaussian    = IOs(random.nextGaussian())
    }
  }

  type Randoms = Envs[Random] with IOs

  object Randoms {
    type Iso = Randoms with IOs
    def run[T, S](r: Random)(f: => T > (Iso with S)): T > (IOs with S) =
      Envs[Random].run(r)(f)
    def run[T, S](f: => T > (Iso with S))(implicit c: Random): T > (IOs with S) =
      Envs[Random].run(c)(f)

    def nextInt: Int > Randoms = Envs[Random].get.map(_.nextInt)
    def nextInt[S](n: Int > (Iso with S)): Int > (S with Randoms) =
      n.map(n => Envs[Random].get.map(_.nextInt(n)))
    def nextLong: Long > Randoms       = Envs[Random].get.map(_.nextLong)
    def nextDouble: Double > Randoms   = Envs[Random].get.map(_.nextDouble)
    def nextBoolean: Boolean > Randoms = Envs[Random].get.map(_.nextBoolean)
    def nextFloat: Float > Randoms     = Envs[Random].get.map(_.nextFloat)
    def nextGaussian: Double > Randoms = Envs[Random].get.map(_.nextGaussian)
    def nextValue[T, S](seq: Seq[T] > S): T > (S with Randoms) =
      seq.map(s => nextInt(s.size).map(idx => s(idx)))
  }
}
