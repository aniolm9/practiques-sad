import java.io.*
import java.lang.Integer.min
import java.util.*

class EditableBufferedReader: BufferedReader {
    private var lines = LinkedList<Line>()
    private var currentLine: Int
    var console = Console() // View
    private var insert = false

    // Keep the parent class constructors.
    constructor(input: Reader): super(input) {
        currentLine = 0
        addLine(currentLine)
    }
    constructor(input: Reader, sz: Int): super(input, sz) {
        currentLine = 0
        addLine(currentLine)
    }

    private fun addLine(index: Int) {
        val line = Line()
        lines.add(index, line)
        line.addObserver(console)
    }

    // Set console mode to "raw".
    private fun setRaw() {
        val cmd = arrayOf("/bin/sh", "-c", "stty -echo raw < /dev/tty")
        Runtime.getRuntime().exec(cmd).waitFor()
    }

    // Set console mode to "cooked".
    private fun unsetRaw() {
        val cmd = arrayOf("/bin/sh", "-c", "stty echo cooked < /dev/tty")
        Runtime.getRuntime().exec(cmd).waitFor()
    }

    // Method to detect the type of CSI sequence.
    private fun detectCSI() {
        if (this.read().toChar() == '[') { // Detect arrows
            when (this.read().toChar()) {
                'D' -> if (lines[currentLine].position > 0) {
                    lines[currentLine].position -= 1
                } // Left arrow
                'C' -> if (lines[currentLine].position < lines[currentLine].text.length) {
                    lines[currentLine].position += 1
                } // Right arrow
                'A' -> if (currentLine > 1) {
                    currentLine -= 1
                    lines[currentLine].position = min(lines[currentLine+1].position, lines[currentLine].text.length)
                } // Up arrow
                'B' -> if (currentLine < lines.size-1) {
                    currentLine += 1
                    lines[currentLine].position = min(lines[currentLine-1].position, lines[currentLine].text.length)
                } // Down arrow
                '5' -> { // Start
                    lines[currentLine].position = 0
                    this.read()
                }
                '6' -> { // End
                    lines[currentLine].position = lines[currentLine].text.length
                    this.read()
                }
                '2' -> { // Insert
                    insert = !insert
                    this.read()
                }
                '3' -> { // Supr
                    if (lines[currentLine].text.isNotEmpty())  {
                        lines[currentLine].deleteChar(0)
                    }
                    else if (lines.size > 1) {
                        lines.removeAt(currentLine)
                    }
                    this.read()
                }
            }
        }
    }

    // Override parent method and return just after introducing a character.
    override fun read(): Int {
        try {
            this.setRaw()
            return super.read()
        }
        finally {
            this.unsetRaw()
        }
    }

    // Override parent method to make the line editable.
    override fun readLine(): String {
        var readChar: Int
        var previousLine = currentLine
        while (previousLine == currentLine) { // Enter
            previousLine = currentLine
            readChar = this.read()
            if (readChar == Constants.BACKSPACE || readChar == Constants.DELETE) { // Delete
                if (lines[currentLine].text.isNotEmpty()) {
                    lines[currentLine].deleteChar(-1)
                }
                else if (lines.size > 1) {
                    lines.removeAt(currentLine)
                    currentLine -= 1
                    lines[currentLine].position = lines[currentLine].text.length
                }
            }
            else if (readChar == Constants.CSI_SEQ) {
                this.detectCSI() // Detects the CSI sequence and modifies line.position.
            }
            else if (readChar == Constants.ENTER) {
                lines[currentLine].appendChar('\n', insert)
                currentLine += 1
                addLine(currentLine)
            }
            else {
                lines[currentLine].appendChar(readChar.toChar(), insert)
            }
        }
        return lines[previousLine].text
    }

    fun readLines(): String {
        var str = readLine()
        while (str != "\n") {
            str = readLine()
            console.currentLine = this.currentLine
        }
        console.stop()
        return str
    }
}
