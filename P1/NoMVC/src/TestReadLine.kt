import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal object TestReadLine {
    @JvmStatic
    fun main(args: Array<String>) {
        val input: BufferedReader = EditableBufferedReader(InputStreamReader(System.`in`))
        var str = ""
        try {
            str = input.readLine();
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("\nline is: $str")
    }
}