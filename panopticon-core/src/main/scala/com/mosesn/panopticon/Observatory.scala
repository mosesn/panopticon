package com.mosesn.panopticon

import com.twitter.util.{Closable, Future}
import java.util.{Observable, Observer}

trait Observatory[A, B] {
  def observe(observee: A)(action: B => Unit): Closable
}

class ObservableObservatory extends Observatory[Observable, Object] {
  override def observe(observee: Observable)(action: Object => Unit): Closable = {
    val observer = new SimpleObserver(action)
    observee.addObserver(observer)
    Closable.make { time =>
      observee.deleteObserver(observer)
      Future.Unit
    }
  }
}

class SimpleObserver(action: Object => Unit) extends Observer {
  override def update(observable: Observable, obj: Object) {
    action(obj)
  }
}
