import java.io.*
import java.net.Socket
import java.net.SocketAddress
import java.net.SocketImpl
import kotlin.system.exitProcess

class MySocket: Socket {
    /* Lazy initialization of both properties. This means that they are initialized
    the first time they are needed, not when the object is created (property delegation).
     */
    private val pw by lazy {
        PrintWriter(outputStream, true)
    }
    private val br by lazy {
        BufferedReader(InputStreamReader(inputStream))
    }

    /* Override the two constructors we need. */
    constructor(host: String, port: Int): super(host, port)
    constructor(impl: SocketImpl?): super(impl)

    /* Override connect(). It handles the IOException instead of throwing it. */
    override fun connect(endpoint: SocketAddress?, timeout: Int) {
        try {
            super.connect(endpoint, timeout)
        }
        catch (e: IOException) {
            println("Connection error.")
            exitProcess(1)
        }
    }

    /* Method to read a line from the socket BufferedReader. */
    fun readMsg(): String? {
        return br.readLine()
    }

    /* Method to write to the socket stream. */
    fun writeMsg(text: String) {
        pw.println(text)
    }

    /* Override close(). As in connect(), it handles the IOException. */
    override fun close() {
        try {
            super.close()
        }
        catch (e: IOException) {
            println("Could not close the socket.")
            exitProcess(1)
        }
    }
}
