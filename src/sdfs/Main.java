package sdfs;

import sdfs.datanode.DataNode;
import sdfs.namenode.DirNode;
import sdfs.namenode.Entity;
import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;

import java.io.*;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

//        FileNode testObject = new FileNode(110, "fileName123321");
//        System.out.println(testObject.getID());
//        System.out.println(testObject.getName());
//        File testFile = new File("./fileNode");
//        try {
//            testFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FileOutputStream fos = new FileOutputStream(testFile);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(testObject);
//        oos.flush();
//        oos.close();
//        fos.close();
//
//        testFile = new File("./fileNode");
//        FileInputStream fis = new FileInputStream(testFile);
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        try {
//            FileNode testNode = (FileNode)(ois.readObject());
//            System.out.println(testNode.getID());
//            System.out.println(testNode.getName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        NameNode nameNode = new NameNode();
//        nameNode.mkdir("dir1");
//        nameNode.mkdir("dir2");
//        nameNode.mkdir("dir1/dir11");
//        FileNode newNode = nameNode.create("dir1/file11");
//        System.out.println(newNode);

//        NameNode nameNode = new NameNode();
//        FileNode newNode = nameNode.open("dir1/file11");
//        System.out.println(newNode);

        DataNode test = new DataNode();
        System.out.println(test.createNewBlock());
    }
}
