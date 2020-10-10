import java.io.*
import java.net.Socket
import java.net.SocketAddress
import java.net.SocketImpl
import kotlin.system.exitProcess

class MySocket: Socket {
    private val pw by lazy {
        PrintWriter(outputStream, true)
    }
    private val br by lazy {
        BufferedReader(InputStreamReader(inputStream))
    }

    constructor(host: String, port: Int): super(host, port)
    constructor(impl: SocketImpl?): super(impl)

    override fun connect(endpoint: SocketAddress?, timeout: Int) {
        try {
            super.connect(endpoint, timeout)
        }
        catch (e: IOException) {
            println("Connection error.")
            exitProcess(1)
        }
    }

    fun readMsg(): String {
        return br.readLine() ?: ""
    }

    fun writeMsg(text: String) {
        pw.println(text)
    }

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