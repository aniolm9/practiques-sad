import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class Console() : Observer {
    var maxSize = intArrayOf(0, 0)
    init {
        printLine("", 0)
        updateConsoleSize()
    }

    fun updateConsoleSize() {
        // Refresh LINES and COLUMNS.
        Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "shopt -u checkwinsize"))
        // Get LINES.
        var proc = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "tput lines"))
        val rows = BufferedReader(InputStreamReader(proc.inputStream)).readLine().toInt()
        // Get COLUMNS.
        proc = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", "tput cols"))
        val columns = BufferedReader(InputStreamReader(proc.inputStream)).readLine().toInt()
        this.maxSize = intArrayOf(rows, columns)
    }

    private fun printLine(text: String, position: Int) {
        val prompt = "$ "
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        print(prompt)
        print(text)
        print("\u001b[1;" + (position + prompt.length + 1).toString() + "H") // Move the cursor to position.
    }

    override fun update(line: Observable, parameters: Any?) {
        if (line is Line) {
            printLine(line.text, line.position)
        }
    }
}