class MyServerSocket : ServerSocket{
    constructor():super()
    fun threadServer(){
        var port = 4444;
        try {
            var  serverSocket:  ServerSocket = ServerSocket(port)
        } catch (e: IOException){
            println("port error")
        }
    }
    fun acceptClients(){
        var clients: Map<client,client.getSocket()>
        while(true){
            try{
                var socket:Socket = ServerSocket
                var client: Client() = Client(Socket)
                var thread: Thread(client)
                thread.start()
                clients.put(client,client.socket)
            } catch (e: IOException){
                println("acceptació falla a:" )
            }
        }
    }
}
