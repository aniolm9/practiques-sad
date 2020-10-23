package server

import MySocket
import java.io.IOException
import java.lang.Exception
import kotlin.system.exitProcess

class ChatServer(private var port: Int) {
    private var users = HashMap<String, MySocket>()
    private var threads = HashSet<ClientThread>()

    /* Method to run the chat server. */
    fun runServer() {
        val ss: MyServerSocket
        try {
            ss = MyServerSocket(port)
            println("Listening on port: $port")
        }
        catch (e: IOException) {
            println("Couldn't listen on port: $port")
            exitProcess(1)
        }
        while (true) {
            val socket: MySocket = ss.accept()
            val newClient = ClientThread(this, socket)
            // Add a thread for every client. Maybe we should set a max number of clients.
            threads.add(newClient)
            newClient.start()
        }
    }

    /* Method to forward a message to everybody except the sender. */
    fun broadcast(message: String, sender: String) {
        for (nick in users.keys) {
            if (nick != sender) {
                println("Broadcasting: $message")
                users[nick]?.writeMsg(message)
            }
        }
    }

    /* Synchronized method to add a new user to the HashMap. */
    @Synchronized fun addUser(nick: String, socket: MySocket): Boolean {
        if (!users.containsKey(nick)) {
            users[nick] = socket
            return true
        }
        return false
    }

    /* Synchronized method to remove a user from the HashMap. */
    @Synchronized fun removeUser(nick: String, thread: ClientThread) {
        if (users.remove(nick, users[nick] as MySocket)) threads.remove(thread)
    }
}

/* Main function to run the server. It takes a parameter "port". */
fun main(args: Array<String>) {
    val port: Int
    try {
        port = args[0].toInt()
    }
    catch (e: Exception) {
        println("Please provide a valid port number.")
        exitProcess(1)
    }
    val chatServer = ChatServer(port)
    chatServer.runServer()
}
