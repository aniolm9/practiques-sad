import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class Console() : PropertyChangeListener {
    var position: Int = 0
    var currentLine: Int = 0
    var consoleSize: IntArray = intArrayOf(0, 0)
    var lines: LinkedList<*> = LinkedList<Line>()

    init {
        consoleSize = updateConsoleSize()
        printLines()
    }

    private fun updateConsoleSize(): IntArray {
        // Refresh LINES and COLUMNS.
        Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "shopt -s checkwinsize"))
        // Get LINES.
        var proc = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "tput lines"))
        val rows = BufferedReader(InputStreamReader(proc.inputStream)).readLine().toInt()
        // Get COLUMNS.
        proc = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "tput cols"))
        val columns = BufferedReader(InputStreamReader(proc.inputStream)).readLine().toInt()
        // Disable refreshing.
        Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "shopt -u checkwinsize"))
        return intArrayOf(rows, columns)
    }

    private fun printLines() {
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        for (line in lines) {
            print((line as Line).text)
        }
        print("\u001b[" + (currentLine + 1).toString() + ";" + (position + 1).toString() + "H") // Move the cursor to position.
    }

    override fun propertyChange(p0: PropertyChangeEvent?) {
        if (p0 != null) {
            if (p0.propertyName == "position") {
                position = p0.newValue as Int
            }
            else if (p0.propertyName == "currentLine") {
                currentLine = p0.newValue as Int
            }
            else if (p0.propertyName == "lineArray") {
                lines = p0.newValue as LinkedList<*>
            }
            printLines()
        }
    }
}