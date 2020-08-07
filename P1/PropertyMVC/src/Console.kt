import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class Console() : PropertyChangeListener {
    var position = 0
    var text = ""

    init {
        printLine("", 0)
    }

    private fun printLine(text: String, position: Int) {
        val prompt = "$ "
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        print(prompt)
        print(text)
        print("\u001b[1;" + (position + prompt.length + 1).toString() + "H") // Move the cursor to position.
    }

    override fun propertyChange(p0: PropertyChangeEvent?) {
        if (p0 != null) {
            if (p0.propertyName == "position") {
                position = p0.newValue as Int
            }
            else if (p0.propertyName == "text") {
                text = p0.newValue as String
            }
            printLine(text, position)
        }
    }
}