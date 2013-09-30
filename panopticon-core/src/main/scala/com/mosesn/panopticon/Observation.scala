package com.mosesn.panopticon

import com.twitter.util.{Closable, Future, Time}
import com.twitter.util.exp.Var
import java.util.concurrent.atomic.AtomicReference

class Observation[A, B](
  observee: A
)(
  implicit observatory: Observatory[A, B]
) extends Var[B] with EmptyStart[B] with Closable { self =>

  override protected def observe(depth: Int, f: B => Unit): Closable = {
    observatory.observe(observee)(f)
  }

  override def close(deadline: Time): Future[Unit] =
    closable.close(deadline)

  // FIXME MN: this is total cargo culting, figure out what the contract is
  override def flatMap[U](f: B => Var[U]): Var[U] = new Var[U] with EmptyStart[U] {
    override protected def observe(depth: Int, o: U => Unit) = {
      val inner = new AtomicReference(Closable.nop)
      val outer = self.observe(depth, { t =>
        inner.getAndSet(f(t).observe(o)).close()
      })

      Closable.sequence(
        outer,
        Closable.make { deadline =>
          inner.getAndSet(Closable.nop).close(deadline)
        }
      )
    }
  }
}

trait EmptyStart[A] { self: Var[A] =>
  @volatile var opt: Option[A] = None
  val closable = observe(v => opt = Some(v))

  override def apply(): A = opt.get
}
