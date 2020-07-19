import java.io.BufferedReader
import java.io.Reader

class EditableBufferedReader: BufferedReader {
    // Keep the parent class constructors.
    constructor(input: Reader): super(input)
    constructor(input: Reader, sz: Int): super(input, sz)

    // Set console mode to "raw".
    private fun setRaw() {
        val cmd = arrayOf("/bin/sh", "-c", "stty raw < /dev/tty")
        Runtime.getRuntime().exec(cmd).waitFor()
    }

    // Set console mode to "cooked".
    private fun unsetRaw() {
        val cmd = arrayOf("/bin/sh", "-c", "stty cooked < /dev/tty")
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
}