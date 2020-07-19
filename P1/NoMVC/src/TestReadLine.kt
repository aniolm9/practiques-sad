import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal object TestReadLine {
    @JvmStatic
    fun main(args: Array<String>) {
        val input: BufferedReader = EditableBufferedReader(InputStreamReader(System.`in`))
        //val str: String = ""
        var n = 0
        try {
            //str = in.readLine();
            n = input.read()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("\nline is: $n")
    }
}