import java.beans.PropertyChangeSupport
import java.lang.IndexOutOfBoundsException
import java.util.*

class Lines() {
    var lineArray = LinkedList<Line>()
    var currentLine: Int = 0
        set(value) {
            changes.firePropertyChange("currentLine", currentLine, value)
            changes.firePropertyChange("lineArray", LinkedList<Line>(), lineArray)
            field = value
        }
    val changes: PropertyChangeSupport = PropertyChangeSupport(this)

    fun addLine(index: Int, view: Console) {
        var text = ""
        // Start new lines with a newline at the end. If you are not at EOF not doing this causes undesired behavior.
        try {
            if (lineArray[currentLine].position == lineArray[currentLine].text.length - 1) text = "\n"
        }
        catch (e: IndexOutOfBoundsException) {}
        val line = Line(text)
        line.changes.addPropertyChangeListener(view)
        lineArray.add(index, line)
    }
}