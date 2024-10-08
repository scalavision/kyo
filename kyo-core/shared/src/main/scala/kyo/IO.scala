package kyo

import kyo.Tag
import kyo.kernel.*

sealed trait IO extends Effect[Const[Unit], Const[Unit]]

object IO:

    inline def unit: Unit < IO = ()

    inline def apply[A, S](inline f: Safepoint ?=> A < S)(using inline frame: Frame): A < (IO & S) =
        Effect.suspendMap[Any](Tag[IO], ())(_ => f)

    inline def ensure[A, S](inline f: => Unit < IO)(v: A < S)(using inline frame: Frame): A < (IO & S) =
        IO(Safepoint.ensure(IO.run(f).eval)(v))

    def run[A: Flat, S](v: A < IO)(using Frame): A < Any =
        runLazy(v)

    def runLazy[A: Flat, S](v: A < (IO & S))(using Frame): A < S =
        Effect.handle(Tag[IO], v) {
            [C] => (_, cont) => cont(())
        }
end IO
