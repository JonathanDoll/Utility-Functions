package com.johndoll.utilityfunctions.filetransfer.sockets;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jonathan Doll
 */
public class SingleFileServer {

    private byte[] data;
    private String fileName;

    public SingleFileServer(File file) {
        fileName = file.getName();
        try {
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int d;
            while ((d = fin.read()) != -1) {
                out.write(d);
            }
            data = out.toByteArray();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void startConnection(int port) {
        try {
            ServerSocket server = new ServerSocket(port);

            Socket client = server.accept();

            OutputStream serverOut = client.getOutputStream();
            PrintWriter serverSpeak = new PrintWriter(client.getOutputStream(), true);

            serverSpeak.println(fileName);

            serverOut.write(data);
            serverOut.flush();

            client.close();
            server.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
