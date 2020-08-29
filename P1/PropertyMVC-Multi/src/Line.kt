import java.beans.PropertyChangeSupport
import java.lang.StringBuilder

class Line() {
    var position: Int = 0
        set(value) {
            changes.firePropertyChange("position", position, value)
            field = value
        }
    var text: String = ""
    var insert = false
    val changes: PropertyChangeSupport = PropertyChangeSupport(this)

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