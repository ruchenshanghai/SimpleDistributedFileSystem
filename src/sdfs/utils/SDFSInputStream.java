/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;

public class SDFSInputStream implements Closeable {
    private FileNode fileNode;
    private NameNode nameNode;


    public SDFSInputStream(String fileUri) {
        try {
            nameNode = new NameNode();
            fileNode = nameNode.open(fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public int read(byte[] b) throws IOException {
        //todo your code here
        return 0;
    }

    @Override
    public void close() throws IOException {
        //todo your code here
    }

    public void seek(int newPos) throws IndexOutOfBoundsException, IOException {
        //todo your code here
    }
}
