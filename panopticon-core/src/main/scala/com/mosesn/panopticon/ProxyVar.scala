package com.mosesn.panopticon

import com.twitter.util.Closable
import com.twitter.util.exp.Var

class ProxyVar[+A](proxy: Var[A]) extends Var[A] {
  override def apply(): A = proxy()

  override protected def observe(depth: Int, f: A => Unit): Closable = depth match {
    case 0 => proxy.observe(f)
    case _ => throw new Exception("don't call observe with a nontrivial depth on a proxyvar")
  }

  override def foreach(f: A => Unit) = proxy.foreach(f)

  override def map[B](f: A => B): Var[B] = proxy.map(f)

  override def flatMap[B](f: A => Var[B]): Var[B] = proxy.flatMap(f)
}
