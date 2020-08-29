import java.io.IOException
import java.io.InputStreamReader

internal object TestReadLine {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = EditableBufferedReader(InputStreamReader(System.`in`))
        try {
            input.readLines();
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}