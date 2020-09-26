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
        try {
            // Start new lines with a newline at the end. If you are not at EOF not doing this causes undesired behavior.
            if (lineArray[currentLine].position == lineArray[currentLine].text.length - 1) text = "\n"
            // Move the substring from the cursor position to the end to a new line after clicking enter.
            else {
                text = lineArray[currentLine].text.substring(lineArray[currentLine].position)
                lineArray[currentLine].text = lineArray[currentLine].text.replace(text, "")
            }
        }
        catch (e: IndexOutOfBoundsException) {}
        val line = Line(text)
        line.changes.addPropertyChangeListener(view)
        lineArray.add(index, line)
    }

    fun removeLine(index: Int) {
        if (index == currentLine+1) {
            try {
                lineArray[currentLine].text = lineArray[currentLine].text.replace("\n", "") + lineArray[currentLine + 1].text
                lineArray.removeAt(index)
            } catch (e: IndexOutOfBoundsException) {
            }
        }
        else if (index == currentLine) {
            lineArray.removeAt(currentLine)
            currentLine -= 1
            lineArray[currentLine].position = lineArray[currentLine].text.length - 1
        }
    }
}