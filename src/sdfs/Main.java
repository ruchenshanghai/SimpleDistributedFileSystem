package sdfs;

import sdfs.client.Client;
import sdfs.datanode.DataNode;
import sdfs.namenode.*;
import sdfs.utils.SDFSOutputStream;

import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

//        Client testClient = new Client();
//        testClient.mkdir("dir1");
//        testClient.mkdir("dir2");
//        testClient.mkdir("dir1/dir11");
//        SDFSOutputStream testOut = testClient.create("dir1/file11");
//        byte[] testData = new byte[128 * 1024 + 5];
//        for (int i = 0; i < testData.length; i++) {
//            testData[i] = (byte) (Math.random() * 255);
//        }
//        testOut.write(testData);
//        testOut.close();



//        NameNode nameNode = NameNode.getSingleNameNodeInstance();
//        FileNode fileNode = nameNode.open("dir1/file11");
//        System.out.println(fileNode.getName());
    }
}
