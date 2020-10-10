package client

import MySocket
import java.lang.Exception
import kotlin.system.exitProcess

class ChatClient(private val host: String, private val port: Int) {
    lateinit var username: String

    fun runClient() {
        val socket: MySocket
        try {
            socket = MySocket(host, port)
        }
        catch (e: Exception) {
            println("Couldn't connect to the server.")
            exitProcess(1)
        }
        print("Enter user name: ")
        username = readLine().toString()
        WriteThread(socket, username).start()
        ReadThread(socket).start()
    }
}

fun main(args: Array<String>) {
    val host: String; val port: Int
    try {
        host = args[0]
        port = args[1].toInt()
    }
    catch (e: Exception) {
        println("Please provide a valid host and port number.")
        exitProcess(1)
    }
    val chatClient = ChatClient(host, port)
    chatClient.runClient()
}