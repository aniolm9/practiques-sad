import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.concurrent.thread

class Console() : PropertyChangeListener {
    var position: Int = 0
    var currentLine: Int = 0
    var cursorPosition = intArrayOf(0, 0)
    var consoleSize: IntArray = intArrayOf(0, 0)
    var lines: LinkedList<*> = LinkedList<Line>()
    var runningThread = false

    init {
        consoleSize = updateConsoleSize()
        printLines()
        // Start a thread to check the window size.
        thread(start = runningThread) {
            var prevSize: IntArray
            while (runningThread) {
                prevSize = consoleSize
                consoleSize = updateConsoleSize()
                if (!prevSize.contentEquals(consoleSize)) {
                    cursorPosition[0] = position / consoleSize[1] + getPrintedLines(currentLine)
                    cursorPosition[1] = position % consoleSize[1]
                    printLines()
                }
                Thread.sleep(50)
            }
        }
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

    private fun getPrintedLines(endLine: Int): Int {
        var logicLinesCounter = 0
        var physicalLinesCounter = 0
        for (line in lines) {
            if (logicLinesCounter >= endLine) break
            physicalLinesCounter += (line as Line).text.length / consoleSize[1] + 1
            logicLinesCounter++
        }
        return physicalLinesCounter
    }

    private fun printLines() {
        print("\u001bc\u001b[3J\u001b[H\u001b[2J") // Reset, clean (2J) and move the cursor to (0,0) (H).
        // WIP
        val lastLine = Integer.min(consoleSize[0] - cursorPosition[0] + currentLine, lines.size)
        val firstLine = Integer.max(lastLine - consoleSize[0], 0)
        //
        for (line in lines.subList(firstLine, lastLine)) {
            print((line as Line).text)
        }
        print("\u001b[" + (cursorPosition[0] + 1).toString() + ";" + (cursorPosition[1] + 1).toString() + "H") // Move the cursor to position.
    }

    override fun propertyChange(p0: PropertyChangeEvent?) {
        if (p0 != null) {
            if (p0.propertyName == "position") {
                position = p0.newValue as Int
            }
            else if (p0.propertyName == "currentLine") {
                currentLine = p0.newValue as Int
                position = (lines[currentLine] as Line).position
                // Make sure to get at position = 0 when creating a new line.
                try {
                    // If we are creating a new line we'll get an IndexOutOfBoundsException.
                    if ((lines[currentLine] as Line).text == "") position = 0
                }
                catch (e: IndexOutOfBoundsException) {
                    position = 0
                }
            }
            else if (p0.propertyName == "lineArray") {
                lines = p0.newValue as LinkedList<*>
            }
            // WIP
            cursorPosition[0] = Integer.min(position / consoleSize[1] + getPrintedLines(currentLine), consoleSize[0] - 1)
            //
            cursorPosition[1] = position % consoleSize[1]
            printLines()
        }
    }
}