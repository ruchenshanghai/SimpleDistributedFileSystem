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
    private int currentPos = 0;

    public SDFSInputStream(String fileUri) throws Exception {
        NameNode nameNode = new NameNode();
        fileNode = nameNode.open(fileUri);
        if (fileNode == null) {
            throw new Exception();
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
