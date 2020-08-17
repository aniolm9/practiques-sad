import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

internal object TestReadLine {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = EditableBufferedReader(InputStreamReader(System.`in`))
        var str = ""
        try {
            str = input.readLine();
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (input.save) {
            File("/tmp/foo").writeText(str)
        }
    }
}