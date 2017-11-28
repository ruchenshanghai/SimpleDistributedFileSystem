/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.namenode.FileNode;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public class SDFSOutputStream implements Closeable, Flushable {
    private FileNode fileNode;
    

    public void write(byte[] b) throws IOException {
        //todo your code here
    }

    @Override
    public void flush() throws IOException {
        //todo your code here
    }

    @Override
    public void close() throws IOException {
        //todo your code here
    }
}
