package server

import MySocket

class ClientThread(private var server: ChatServer, private var socket: MySocket): Thread() {
    override fun run() {
        // Get the new user nickname.
        var clientMsg : String?
        var serverMsg : String?
        var nick = socket.readMsg()
        while (!server.addUser(nick.toString(), socket)) {
            socket.writeMsg("Username already in use.")
            nick = socket.readMsg()
        }
        // This should not happen, but if it does we just close the socket and end the thread.
        if (nick == null) {
            socket.close()
            return
        }
        // If the username doesn't exist, tell ok to the client and allow the connection.
        socket.writeMsg("OK")
        serverMsg = "$nick connected to the server."
        server.broadcast(serverMsg, nick)

        // Forward messages until we get a null. This means, the client closes the window.
        clientMsg = socket.readMsg()
        while (clientMsg != null) {
            serverMsg = "[$nick] $clientMsg"
            server.broadcast(serverMsg, nick)
            clientMsg = socket.readMsg()
        }

        // Remove a user that exited the server.
        server.removeUser(nick, this)
        socket.close()
        serverMsg = "$nick exited the server."
        server.broadcast(serverMsg, nick)
    }
}
