import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.concurrent.thread

class Console() : Observer {
    var maxSize: IntArray
    var cursorPosition = intArrayOf(0, Constants.PROMPT.length)
    var lastPosition: Int = 0
    var lastText = ""
    var runningThread = true
    init {
        this.maxSize = updateConsoleSize()
        printLine("")
        Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "tput smam"))
        // Start a thread to check the window size.
        thread(start = true) {
            var prevSize: IntArray
            while (runningThread) {
                prevSize = this.maxSize
                this.maxSize = this.updateConsoleSize()
                if (!prevSize.contentEquals(this.maxSize)) {
                    updateCursor(lastPosition)
                    this.printLine(lastText)
                }
                Thread.sleep(50)
            }
        }
    }

    fun stop() {
        runningThread = false
    }

    private fun updateCursor(position: Int) {
        cursorPosition[0] = ((position + Constants.PROMPT.length) / this.maxSize[1]) + 1
        cursorPosition[1] = (position + Constants.PROMPT.length) % this.maxSize[1]
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

    private fun printLine(text: String) {
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        print(Constants.PROMPT)
        print(text)
        print("\u001b[" + cursorPosition[0].toString() + ";" + (cursorPosition[1] + 1).toString() + "H") // Move the cursor to position.
    }

    override fun update(line: Observable, parameters: Any?) {
        if (line is Line) {
            lastPosition = line.position
            lastText = line.text
            updateCursor(line.position)
            printLine(line.text)
        }
    }
}
