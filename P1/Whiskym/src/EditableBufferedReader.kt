import java.io.*
import java.lang.Integer.max
import java.lang.Integer.min

class EditableBufferedReader: BufferedReader {
    private var line: Line = Line() // Model
    var console = Console() // View

    // Keep the parent class constructors.
    constructor(input: Reader): super(input) {
        line.addObserver(console)
    }
    constructor(input: Reader, sz: Int): super(input, sz) {
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
        val lastPosition = line.position
        line.position = console.maxSize[1]*console.maxSize[0]
        var nextChar = this.read().toChar()
        if (nextChar == '[') { // Detect arrows
            line.position = lastPosition
            line.editableMode = true
            when (this.read().toChar()) {
                'D' -> if (line.position > 0) line.position -= 1 // Left arrow
                'C' -> if (line.position < line.text.length) line.position += 1 // Right arrow
                'A' -> if (console.cursorPosition[0] > 1) {
                    line.position = max(line.position - console.maxSize[1], 0)
                } // Up arrow
                'B' -> if (console.cursorPosition[0] <= (line.text.length + Constants.PROMPT.length) / console.maxSize[1]) {
                    line.position = min(line.position + console.maxSize[1], line.text.length * console.cursorPosition[0])
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
        else if (nextChar == ':') {
            print(":")
            nextChar = this.read().toChar()
            print(nextChar)
            when (nextChar) {
                'q' -> line.editableMode = false
                'i' -> line.position = lastPosition
                else -> line.position = lastPosition
            }
        }
        else {
            line.position = lastPosition
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
        while (true) { // Enter
            if (readChar == Constants.BACKSPACE || readChar == Constants.DELETE) { // Delete
                line.deleteChar(-1)
            }
            else if (readChar == Constants.CSI_SEQ) {
                this.detectCSI() // Detects the CSI sequence and modifies line.position.
                if (!line.editableMode) break
            }
            else {
                line.appendChar(readChar.toChar())
            }
            readChar = this.read()
        }
        console.stop()
        return line.text
    }
}
