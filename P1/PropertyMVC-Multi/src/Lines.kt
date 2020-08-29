import java.beans.PropertyChangeSupport
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
        val line = Line()
        line.changes.addPropertyChangeListener(view)
        lineArray.add(index, line)
    }
}