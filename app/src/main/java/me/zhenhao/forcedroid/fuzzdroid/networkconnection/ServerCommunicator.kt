package me.zhenhao.forcedroid.fuzzdroid.networkconnection

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.util.NetworkSettings
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Created by tom on 6/8/17.
 */
class ServerCommunicator(private val syncToken: Any) {

    val serverAnswers: Queue<ServerResponse> = ConcurrentLinkedQueue<ServerResponse>()

    fun getResultForRequest(request: IClientRequest): ServerResponse? {
        synchronized(syncToken) {
            val client = ClientThread(syncToken as Object, request)
            val thread = Thread(client)
            thread.start()
            try {
                syncToken.wait()
                val response = serverAnswers.poll()
                return response
            } catch (e: InterruptedException) {
                e.printStackTrace()
                return null
            }

        }
    }

    fun send(request: Collection<IClientRequest>,
             waitForResponse: Boolean) {
        val thread = Thread(Runnable {
            // No need to send empty requests
            if (request.isEmpty())
                return@Runnable

            var socket: Socket? = null
            val oos: ObjectOutputStream?
            val ois: ObjectInputStream?
            try {
                try {
                    socket = Socket(NetworkSettings.SERVER_IP, NetworkSettings.SERVERPORT_OBJECT_TRANSFER)
                    if (!socket.isConnected) {
                        socket.close()
                        throw RuntimeException("Socket is not established")
                    }

                    // Create the streams
                    oos = ObjectOutputStream(socket.getOutputStream())
                    ois = ObjectInputStream(socket.getInputStream())

                    // Send the requests to the server
                    Log.i(SharedClassesSettings.TAG, String.format("Sending %d events to server...",
                            request.size))
                    for (icr in request)
                        oos.writeObject(icr)
                    oos.flush()

                    // Wait for all objects to be acknowledged before closing
                    // the connection
                    Log.i(SharedClassesSettings.TAG, "Waiting for server confirmation ("
                            + Thread.currentThread().id + ")...")
                    for (i in request.indices) {
                        ois.readObject()
                        // Log.i(SharedClassesSettings.TAG, String.format("Received %d/%d confirmation responses", i+1, request.size()));
                    }

                    // Tell the server that we're ready to close the connection
                    Log.i(SharedClassesSettings.TAG, "All objects confirmed, closing connection...")
                    oos.writeObject(CloseConnectionRequest())
                    oos.flush()
                    socket.shutdownOutput()

                    // Make sure that the server isn't already dead as a doornail
                    ois.mark(1)
                    if (ois.read() != -1) {
                        ois.reset()

                        // Wait for the server to acknowledge that it's going away
                        Log.i(SharedClassesSettings.TAG, "Waiting for server shutdown confirmation...")
                        ois.readObject()
                        Log.i(SharedClassesSettings.TAG, "Confirmation received.")
                        // We close the socket anyway
                        // if (socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown())
                        //     socket.shutdownInput();

                        Log.i(SharedClassesSettings.TAG, "OK, request handling done")
                    }
                    Log.i(SharedClassesSettings.TAG, "Connection closed.")
                } finally {
                    socket?.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

            if (waitForResponse) {
                synchronized(syncToken as Object) {
                    syncToken.notify()
                }
            }

            Log.i(SharedClassesSettings.TAG, "End of SEND thread (" + Thread.currentThread().id + ").")
        })
        thread.start()

        // Wait for completion if we have to
        try {
            if (waitForResponse)
                synchronized(syncToken as Object) {
                    syncToken.wait()
                }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }


    private inner class ClientThread(private val syncToken: Object, private val request: IClientRequest?) : Runnable {

        override fun run() {
            if (request == null)
                throw RuntimeException("Server-Request should not be null!")
            var socket: Socket? = null
            val oos: ObjectOutputStream?
            val ois: ObjectInputStream?
            try {
                try {
                    socket = Socket(NetworkSettings.SERVER_IP, NetworkSettings.SERVERPORT_OBJECT_TRANSFER)
                    if (!socket.isConnected) {
                        socket.close()
                        throw RuntimeException("Socket is not established")
                    }

                    // Create our streams
                    oos = ObjectOutputStream(socket.getOutputStream())
                    ois = ObjectInputStream(socket.getInputStream())

                    // Send the request
                    Log.i(SharedClassesSettings.TAG, "Sending single event to server...")
                    sendRequest(oos)

                    // Wait for the response
                    Log.i(SharedClassesSettings.TAG, "Waiting for single server response...")

                    val response = getResponse(ois)

                    // Even in case the connection dies, take what we have and run.
                    Log.i(SharedClassesSettings.TAG, "OK, done.")
                    Log.i(SharedClassesSettings.TAG, response.toString())
                    serverAnswers.add(response)

                    // Tell the server that we're ready to close the connection
                    Log.i(SharedClassesSettings.TAG, "All objects confirmed, closing connection...")
                    oos.writeObject(CloseConnectionRequest())
                    oos.flush()
                    socket.shutdownOutput()

                    // Wait for the server to acknowledge that it's going away
                    // Make sure that the server isn't already dead as a doornail
                    ois.mark(1)
                    if (ois.read() != -1) {
                        ois.reset()

                        Log.i(SharedClassesSettings.TAG, "Waiting for server shutdown confirmation...")
                        ois.readObject()

                        // We close the socket anyway
                        // if (socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown())
                        //     socket.shutdownInput();
                    }
                } finally {
                    socket?.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

            synchronized(syncToken) {
                syncToken.notify()
            }

            Log.i(SharedClassesSettings.TAG, "End of CLIENT thread.")
        }


        @Throws(IOException::class)
        private fun sendRequest(out: ObjectOutputStream) {
            out.writeObject(request)
        }


        @Throws(ClassNotFoundException::class, IOException::class)
        private fun getResponse(input: ObjectInputStream): ServerResponse {
            val response = input.readObject() as ServerResponse
            return response
        }
    }


}