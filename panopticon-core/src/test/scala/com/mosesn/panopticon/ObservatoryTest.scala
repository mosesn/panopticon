package com.mosesn.panopticon

import javax.swing.JButton
import org.scalatest.FunSpec

class ObservatoryTest extends FunSpec {
  describe("AbstractButtonActionObservatory") {
    it("should observe jbuttons") {
      val button = new JButton()
      var bool = false
      DefaultAbstractButtonActionObservatory.observe(button) { _ =>
        bool = true
      }

      assert(bool === false)
      button.doClick()
      assert(bool === true)
    }
  }
}
