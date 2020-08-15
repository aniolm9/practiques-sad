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
            str = input.readLines()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        File("/tmp/foo").writeText(str)
    }
}