import java.lang.StringBuilder
import java.util.*

class Line() : Observable() {
    var position: Int = 0
    set(value) {
        field = value
        setChanged()
        notifyObservers()
    }
    var text: String = ""
    var insert = false

    fun appendChar(c: Char) {
        val strBuilder = StringBuilder(text)
        if (insert && position < text.length) {
            strBuilder.setCharAt(position, c)
        }
        else {
            strBuilder.insert(this.position, c)
        }
        this.text = strBuilder.toString()
        this.position = this.position+1
    }

    fun deleteChar(offset: Int) {
        if (this.position >= 0 && this.text.isNotEmpty()) {
            if (offset == 0 && this.position >= this.text.length) {
                return
            }
            this.text = this.text.removeRange(this.position+offset, this.position+1+offset)
            this.position += offset
        }
    }
}