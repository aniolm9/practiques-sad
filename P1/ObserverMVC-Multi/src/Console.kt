import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class Console() : Observer {
    var maxSize: IntArray
    init {
        this.maxSize = updateConsoleSize()
        printLine("", 0)
    }

    fun updateConsoleSize(): IntArray {
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

    private fun printLine(text: String, position: Int) {
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        print(Constants.PROMPT)
        print(text)
        print("\u001b[1;" + (position + Constants.PROMPT.length + 1).toString() + "H") // Move the cursor to position.
    }

    override fun update(line: Observable, parameters: Any?) {
        if (line is Line) {
            printLine(line.text, line.position)
        }
    }
}
