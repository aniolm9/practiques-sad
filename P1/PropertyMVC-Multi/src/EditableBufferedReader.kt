import java.io.BufferedReader
import java.io.File
import java.io.Reader

class EditableBufferedReader: BufferedReader {
    var lines = Lines() // Model
    var lineArray = lines.lineArray
    var console = Console() // View

    // Keep the parent class constructors.
    constructor(input: Reader): super(input) {
        lines.addLine(0, console)
        lines.changes.addPropertyChangeListener(console)
    }
    constructor(input: Reader, sz: Int): super(input, sz) {
        lines.addLine(0, console)
        lines.changes.addPropertyChangeListener(console)
    }

    private fun Boolean.toInt() = if (this) 1 else 0

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
                'D' -> if (lineArray[lines.currentLine].position > 0) {
                    lineArray[lines.currentLine].position -= 1
                } // Left arrow
                'C' -> if (lineArray[lines.currentLine].position < lineArray[lines.currentLine].text.length - lineArray[lines.currentLine].text.contains('\n').toInt()) {
                    lineArray[lines.currentLine].position += 1
                } // Right arrow
                'A' -> if (lines.currentLine >= 1) {
                    lines.currentLine -= 1
                    lineArray[lines.currentLine].position = Integer.min(lineArray[lines.currentLine + 1].position, lineArray[lines.currentLine].text.length)
                } // Up arrow
                'B' -> if (lines.currentLine < lineArray.size-1) {
                    lines.currentLine += 1
                    lineArray[lines.currentLine].position = Integer.min(lineArray[lines.currentLine - 1].position, lineArray[lines.currentLine].text.length)
                } // Down arrow
                '5' -> { // Start
                    lineArray[lines.currentLine].position = 0
                    this.read()
                }
                '6' -> { // End
                    lineArray[lines.currentLine].position = lineArray[lines.currentLine].text.length
                    this.read()
                }
                '2' -> { // Insert
                    lineArray[lines.currentLine].insert = !lineArray[lines.currentLine].insert
                    this.read()
                }
                '3' -> { // Supr
                    if (lineArray[lines.currentLine].text.isNotEmpty())  {
                        lineArray[lines.currentLine].deleteChar(0)
                    }
                    else if (lineArray.size > 1) {
                        lineArray.removeAt(lines.currentLine)
                    }
                    this.read()
                    lines.currentLine = lines.currentLine // Trigger
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
        var previousLine = lines.currentLine
        while (previousLine == lines.currentLine) { // Enter
            previousLine = lines.currentLine
            readChar = this.read()
            if (readChar == Constants.BACKSPACE || readChar == Constants.DELETE) { // Delete
                if (lineArray[lines.currentLine].text.isNotEmpty()) {
                    lineArray[lines.currentLine].deleteChar(-1)
                }
                else if (lineArray.size > 1) {
                    lineArray.removeAt(lines.currentLine)
                    lines.currentLine -= 1
                    lineArray[lines.currentLine].position = lineArray[lines.currentLine].text.length - 1
                }
            }
            else if (readChar == Constants.CSI_SEQ) {
                this.detectCSI() // Detects the CSI sequence and modifies line.position.
            }
            else if (readChar == Constants.ENTER) {
                lineArray[lines.currentLine].appendChar('\n')
                lines.currentLine += 1
                lines.addLine(lines.currentLine, console)
            }
            else {
                lineArray[lines.currentLine].appendChar(readChar.toChar())
            }
        }

        if (previousLine >= lineArray.size) {
            return ""
        }
        return lineArray[previousLine].text
    }

    fun readLines() {
        lines.currentLine = 0 // Trigger
        //File("/tmp/foo").writeText("")
        while (true) {
            //File("/tmp/foo").appendText(readLine())
            readLine()
        }
    }
}