package mlang.client.web
import org.scalajs.dom
import mlang.core.editor
import mlang.core.editor._
import mlang.core.utils.{Text, _}

import scalatags.JsDom.all._





object Main {
  def main(args: Array[String]): Unit = {
    new Editor()
  }
}

class Editor() extends editor.Editor {

  val textMeasure = div(cls := "text", color := "#00000000").render
  dom.document.body.appendChild(textMeasure)
  dom.console.log(textMeasure.clientWidth)
  //dom.document.body.removeChild(div)

  override def width: Float = 0F
  override def height: Float = 0F

  override def measure(text: StyledText): StyledTextMeasurement = {
    ???
  }

}
