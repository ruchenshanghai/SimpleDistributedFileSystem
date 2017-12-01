package sdfs;

import sdfs.client.Client;
import sdfs.datanode.DataNode;
import sdfs.namenode.*;
import sdfs.utils.SDFSInputStream;
import sdfs.utils.SDFSOutputStream;

import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Client client = new Client();
        if (args.length == 2 && args[0].equals("mkdir") && !args[1].equals("")) {
            // mkdir
            String fileUri = args[1];
            client.mkdir(fileUri);
        } else if (args.length == 3) {
            String method = args[0];
            switch (method) {
                case "put": {
                    String srcFileUri = args[1];
                    File srcFile = new File(srcFileUri);
                    String dstFileUri = args[2];
                    if (!srcFileUri.equals("") && !dstFileUri.equals("") && srcFile.isFile()) {
                        BufferedInputStream tempIn = new BufferedInputStream(new FileInputStream(srcFile));
                        SDFSOutputStream tempOut = client.create(dstFileUri);
                        while (tempIn.available() != 0) {
                            byte[] buffer = new byte[tempIn.available()];
                            tempIn.read(buffer);
                            tempOut.write(buffer);
                        }
                        tempOut.close();
                        tempIn.close();
                    }
                } break;
                case "get": {
                    String srcFileUri = args[1];
                    String dstFileUri = args[2];
                    File dstFile = new File(dstFileUri);
                    if (!srcFileUri.equals("") && !dstFileUri.equals("")) {
                        if (dstFile.exists()) {
                            dstFile.delete();
                            dstFile.createNewFile();
                        }
                        SDFSInputStream tempIn = client.open(srcFileUri);
                        BufferedOutputStream tempOut = new BufferedOutputStream(new FileOutputStream(dstFile));
                        byte[] buffer = new byte[DataNode.BLOCK_SIZE];
                        int tempReadCount = 0;
                        while ((tempReadCount = tempIn.read(buffer)) != 0) {
                            tempOut.write(buffer, 0, tempReadCount);
                        }
                        tempOut.close();
                        tempIn.close();
                    }
                } break;
            }
        }


//        Client testClient = new Client();
//        testClient.mkdir("dir1");
//        testClient.mkdir("dir1/dir11");
//        testClient.mkdir("dir2");
//        SDFSOutputStream testOut = testClient.create("dir1/dir11/file111");
//        byte[] testData = new byte[128 * 1024 + 5];
//        for (int i = 0; i < testData.length; i++) {
//            testData[i] = (byte) (Math.random() * 255);
//        }
//        testOut.write(testData);
//        testOut.close();
//
//        Client test = new Client();
//        SDFSInputStream temp = test.open("dir1/dir11/file111");
//        byte[] tempData = new byte[10];
//        temp.read(tempData);
//        System.out.println(tempData.toString());

    }
}
