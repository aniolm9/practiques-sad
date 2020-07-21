import java.lang.StringBuilder

class Line {
    var position: Int = 0
    var text: String = ""

    fun appendChar(c: Char) {
        var strBuilder = StringBuilder(text)
        strBuilder.insert(this.position, c)
        this.text = strBuilder.toString()
        this.position = this.position+1
    }

    fun deleteChar() {
        if (this.position > 0) {
            this.text = this.text.removeRange(this.position-1, this.position)
            this.position -= 1
        }
    }

    fun print() {
        val prompt = "$ "
        print("\u001b[H\u001b[2J") // Clean (2J) and move the cursor to (0,0) (H).
        print(prompt)
        print(this.text)
        print("\u001b[1;" + (this.position + prompt.length + 1).toString() + "H") // Move the cursor to position.
    }
}