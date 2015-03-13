package com.johndoll.utilityfunctions.filetransfer.rmi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * @author Jonathan Doll
 */
public class RMIFileTransferClient extends javax.swing.JPanel {

    private JButton btnDownload;
    private JButton btnRefresh;
    private JButton btnUpload;
    private JList fileList;
    private JScrollPane jScrollPane1;
    private String fileName;
    private DefaultListModel dlm;
    private RMIFileInt stub;
    private JFileChooser jfc;


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        RMIFileTransferClient window = new RMIFileTransferClient(args[0]);
        frame.add(window);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 300);
        frame.setTitle("RMI File Transfer Client");
    }

    public RMIFileTransferClient(String serverAddress) {
        initComponents();
        try {
            Registry reg = LocateRegistry.getRegistry(serverAddress);
            stub = (RMIFileInt) reg.lookup("RMIFileTransfer");
            populateList(stub.fileList());
            fileList.setSelectedIndex(0);
            jfc = new JFileChooser();
        } catch (RemoteException e) {
            System.err.println(e);
        } catch (NotBoundException e) {
            System.err.println(e);
        }
    }

    private void initComponents() {
        btnDownload = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList();
        btnRefresh = new javax.swing.JButton();
        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        fileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(fileList);
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnDownload)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnRefresh)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnUpload)))
                        .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnDownload)
                                .addComponent(btnUpload)
                                .addComponent(btnRefresh))
                        .addGap(21, 21, 21))
        );
    }

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            fileName = dlm.getElementAt(fileList.getSelectedIndex()).toString();
            stub.setFileDownload(fileName);
            jfc.showSaveDialog(this);
            if(jfc.getSelectedFile() != null){
            FileOutputStream fout = new FileOutputStream(jfc.getSelectedFile() + fileName.substring(fileName.lastIndexOf(".")));
            byte[] data = stub.getFileStream();
            fout.write(data);
            fout.close();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        jfc.showOpenDialog(this);
        try {
            stub.setFileUpload(jfc.getSelectedFile().getName());
            FileInputStream fin = new FileInputStream(jfc.getSelectedFile());
            BufferedInputStream bfin = new BufferedInputStream(fin);
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            int b;
            while ((b = bfin.read()) != -1) {
                fout.write(b);
            }
            byte[] data = fout.toByteArray();
            stub.sendFileStream(data);
        } catch (RemoteException e) {
            System.err.println(e);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            populateList(stub.fileList());
        } catch (RemoteException e) {
            System.err.println(e);
        }
    }

    public void populateList(String[] list) {
        dlm = new DefaultListModel();
        fileList.setModel(dlm);
        for (String file : list) {
            dlm.addElement(file);
        }
    }

}
