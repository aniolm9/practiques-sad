import java.io.*

class EditableBufferedReader: BufferedReader {
    private val lines: MutableList<Line> = mutableListOf()
    private var line: Line // Model
    var console = Console() // View

    // Keep the parent class constructors.
    constructor(input: Reader): super(input) {
        addLine()
        line = lines[0]
    }
    constructor(input: Reader, sz: Int): super(input, sz) {
        addLine()
        line = lines[0]
    }

    private fun addLine() {
        val newLine = Line()
        newLine.addObserver(console)
        lines.add(newLine)
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
                'D' -> if (line.position > 0) line.position -= 1 // Left arrow
                'C' -> if (line.position < line.text.length) line.position += 1 // Right arrow
                'A' -> {

                } // Up arrow
                'B' -> {

                } // Down arrow
                '5' -> { // Start
                    line.position = 0
                    this.read()
                }
                '6' -> { // End
                    line.position = line.text.length
                    this.read()
                }
                '2' -> { // Insert
                    line.insert = !line.insert
                    this.read()
                }
                '3' -> { // Supr
                    line.deleteChar(0)
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
        var readChar: Int = this.read()
        while (readChar != Constants.ENTER) { // Enter
            console.maxSize = console.updateConsoleSize()
            if (readChar == Constants.BACKSPACE || readChar == Constants.DELETE) { // Delete
                line.deleteChar(-1)
            }
            else if (readChar == Constants.CSI_SEQ) {
                this.detectCSI() // Detects the CSI sequence and modifies line.position.
            }
            else {
                if (line.position + Constants.PROMPT.length + 1 < console.maxSize[1]) {
                    line.appendChar(readChar.toChar())
                }
            }
            readChar = this.read()
        }
        return line.text
    }
}
