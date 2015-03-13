package com.johndoll.utilityfunctions.filetransfer.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIFileInt extends Remote{
    public byte[] getFileStream() throws RemoteException, IOException;
    public void setFileDownload(String fileName) throws RemoteException;
    public void setFileUpload(String fileName) throws RemoteException;
    public void sendFileStream(byte[] data) throws RemoteException;
    public String[] fileList() throws RemoteException;
}
