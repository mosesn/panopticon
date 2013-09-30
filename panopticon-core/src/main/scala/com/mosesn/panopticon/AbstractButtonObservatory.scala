package com.mosesn.panopticon

import com.twitter.util.{Closable, Future}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.AbstractButton

protected[panopticon] abstract class AbstractButtonActionObservatory extends
    Observatory[AbstractButton, ActionEvent] {
  override def observe(observee: AbstractButton)(action: ActionEvent => Unit): Closable = {
    val listener = new SimpleActionListener(action)
    observee.addActionListener(listener)
    Closable.make { time =>
      observee.removeActionListener(listener)
      Future.Unit
    }
  }
}

private[panopticon] class SimpleActionListener(action: ActionEvent => Unit) extends ActionListener {
  override def actionPerformed(event: ActionEvent) {
    action(event)
  }
}
