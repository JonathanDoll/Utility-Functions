package com.johndoll.utilityfunctions;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Jonathan Doll
 */
public class SingleFileClient {

    private Socket client;
    private InputStream serverFileIn;
    private BufferedReader serverIn;

    public SingleFileClient(String ip, int port) {
        try {
            client = new Socket(ip, port);
            serverFileIn = client.getInputStream();
            serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void receiveFile(String folder) {
        try {
            String fileName = serverIn.readLine();
            FileOutputStream fout = new FileOutputStream(folder + "\\" + fileName);

            int d;
            while ((d = serverFileIn.read()) != -1) {
                fout.write(d);
            }
            fout.close();
            serverIn.close();
            client.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
