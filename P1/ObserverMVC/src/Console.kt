import java.util.*

class Console() : Observer {
    init {
        printLine("", 0)
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