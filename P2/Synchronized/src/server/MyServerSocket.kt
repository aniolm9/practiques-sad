package server

import MySocket
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketImpl

class MyServerSocket(port: Int): ServerSocket(port) {
    override fun accept(): MySocket {
        return if (this.isClosed) {
            throw SocketException("Socket is closed")
        } else if (!this.isBound) {
            throw SocketException("Socket is not bound yet")
        } else {
            val s = MySocket(null as SocketImpl?)
            implAccept(s)
            s
        }
    }
}