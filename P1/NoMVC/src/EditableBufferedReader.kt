import java.io.BufferedReader
import java.io.Reader

class EditableBufferedReader: BufferedReader {
    private var line: Line = Line()

    // Keep the parent class constructors.
    constructor(input: Reader): super(input)
    constructor(input: Reader, sz: Int): super(input, sz)

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
        line.print() // Clean the command line.
        var readChar: Int = this.read()
        while (readChar != 13) { // Enter
            when(readChar) {
                8 -> line.deleteChar() // Backspace
                127 -> line.deleteChar() // Delete
                else -> line.appendChar(readChar.toChar())
            }
            line.print()
            readChar = this.read()
        }
        return line.text
    }
}