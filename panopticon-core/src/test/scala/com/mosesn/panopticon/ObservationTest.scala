package com.mosesn.panopticon

import com.twitter.util.exp.Var
import java.awt.event.ActionEvent
import javax.swing.{AbstractButton, JButton}
import org.scalatest.FunSpec

class ObservationTest extends FunSpec {
  describe("Observation") {
    it("should observe jbuttons") {
      val button = new JButton()
      val obs = new Observation[AbstractButton, ActionEvent](button)
      var bool = false

      val flip = obs map { evt =>
        bool = !bool
        bool
      }

      intercept[NoSuchElementException] { flip() }

      button.doClick()

      assert(obs() != null)

      assert(flip() === true)

      button.doClick()
      assert(flip() === false)
    }
  }
}
