/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.namenode.FileNode;

import java.io.Closeable;
import java.io.IOException;

public class SDFSInputStream implements Closeable {
    private FileNode fileNode;

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
