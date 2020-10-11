package server

import MySocket

class ClientThread(var server: ChatServer, var socket: MySocket): Thread() {
    override fun run() {
        // Get the new user nickname.
        var clientMsg : String?
        var serverMsg : String?
        val nick = socket.readMsg()
        // This should not happen, but if it does we just close the socket and end the thread.
        if (nick == null) {
            socket.close()
            return
        }
        // TODO: Check nick uniqueness.
        server.addUser(nick, socket)
        serverMsg = "$nick connected to the server."
        server.broadcast(serverMsg, nick)

        // Forward messages until we get a null.
        do {
            clientMsg = socket.readMsg()
            serverMsg = "[$nick] $clientMsg"
            server.broadcast(serverMsg, nick)
        } while (clientMsg != null)

        // Remove a user that exited the server
        server.removeUser(nick, this)
        socket.close()
        serverMsg = "$nick exited the server."
        server.broadcast(serverMsg, nick)
    }
}
