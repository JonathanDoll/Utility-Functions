package com.johndoll.utilityfunctions.filetransfer.rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jonathan Doll
 */

public class RMIFileTransfer implements RMIFileInt, Serializable {

    private FileInputStream fin;
    private FileOutputStream fout;
    private BufferedInputStream bfin;
    private BufferedOutputStream bfout;
    private String uploadFolder;
    private String downloadFolder;

    public RMIFileTransfer(String downloadLocation, String uploadLocation) {
        uploadFolder = uploadLocation;
        downloadFolder = downloadLocation;
    }

    public static void main(String[] args) {
        try {
            RMIFileTransfer rft = new RMIFileTransfer(args[0], args[1]);
            RMIFileInt stub = (RMIFileInt) UnicastRemoteObject.exportObject(rft, 0);
            Registry reg = LocateRegistry.getRegistry();
            reg.bind("RMIFileTransfer", stub);
            System.out.println("Server Ready");
        } catch (RemoteException e) {
            System.err.println(e);
        } catch (AlreadyBoundException e) {
            System.err.println(e);
        }
    }

    public String[] fileList() throws RemoteException {
        File folder = new File(downloadFolder);
        File[] list = folder.listFiles();
        String[] fileList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            fileList[i] = list[i].getName();
        }
        return fileList;
    }

    public void setFileDownload(String fileName) throws RemoteException {
        File file = new File(downloadFolder + "\\" + fileName);
        try {
            fin = new FileInputStream(file);
            bfin = new BufferedInputStream(fin);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public void setFileUpload(String fileName) throws RemoteException {
        int fileNum = 1;
        File file = new File(uploadFolder + "\\" + fileName);
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        fileName =  fileName.substring(0, fileName.lastIndexOf("."));
        while(file.exists()){
            file = new File(uploadFolder + "\\" + fileName + "(" + fileNum++ +  ")" + fileExt);
        }
        try {
            fout = new FileOutputStream(file);
            bfout = new BufferedOutputStream(fout);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public byte[] getFileStream() throws RemoteException, IOException {
        
        int b = 0;
        do{
        int j = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (j < 100000000 && (b = bfin.read()) != -1) {
            out.write(b);
            j++;
        }
        byte[] data = out.toByteArray();
        return data;
        }while(b != -1);
    }

    public void sendFileStream(byte[] data) throws RemoteException {
        try {
            bfout.write(data);
            if(data.length < 100000000){
                bfout.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
