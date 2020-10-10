package server

import MySocket

class ClientThread(var server: ChatServer, var socket: MySocket): Thread() {
    override fun run() {
        // Get the new user nickname.
        var clientMsg : String
        var serverMsg : String
        // TODO: Check nick uniqueness.
        val nick : String = socket.readMsg()
        server.addUser(nick, socket)
        serverMsg = "$nick connected to the server."
        server.broadcast(serverMsg, nick)

        // Forward messages until we get "bye".
        do {
            clientMsg = socket.readMsg()
            serverMsg = "[$nick] $clientMsg"
            server.broadcast(serverMsg, nick)
        } while (clientMsg.toLowerCase() != "bye")

        // Remove a user that exited the server
        server.removeUser(nick, this)
        socket.close()
        serverMsg = "$nick exited the server."
        server.broadcast(serverMsg, nick)
    }
}