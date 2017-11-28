package sdfs;

import sdfs.namenode.DirNode;
import sdfs.namenode.NameNode;

import java.io.*;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        NameNode test = new NameNode();
        test.mkdir("dir2");



    }
}
