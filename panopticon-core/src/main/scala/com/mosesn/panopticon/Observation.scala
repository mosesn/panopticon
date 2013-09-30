package com.mosesn.panopticon

import com.twitter.util.{Closable, Future, Time}
import com.twitter.util.exp.Var
import java.util.concurrent.atomic.AtomicReference

class Observation[A, B](
  observee: A
)(
  implicit observatory: Observatory[A, B]
) extends Var[B] with EmptyStart[B] { self =>

  override protected def observe(depth: Int, f: B => Unit): Closable = {
    observatory.observe(observee)(f)
  }

  // FIXME MN: this is total cargo culting, figure out what the contract is
  override def flatMap[C](f: B => Var[C]): Var[C] = new ProxyVar[C](super.flatMap(f)) with EmptyStart[C]
}

trait EmptyStart[A] extends Closable { self: Var[A] =>
  @volatile var opt: Option[A] = None
  val closable = observe(v => opt = Some(v))

  override def apply(): A = opt.get

  override def close(deadline: Time): Future[Unit] =
    closable.close(deadline)
}
