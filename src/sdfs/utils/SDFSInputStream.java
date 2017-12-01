/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.datanode.DataNode;
import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;

public class SDFSInputStream implements Closeable {
    private FileNode fileNode;
    private int currentPos = 0;

    public SDFSInputStream(String fileUri) throws Exception {
        NameNode nameNode = NameNode.getSingleNameNodeInstance();
        fileNode = nameNode.open(fileUri);
        if (fileNode == null) {
            throw new Exception();
        }
    }


    public int read(byte[] b) throws IOException {
        //todo your code here
        if (currentPos >= fileNode.getTotalSize()) {
            throw new IOException();
        }
        int leftBlockSize = fileNode.getTotalSize();
        int leftArraySize = b.length;
        int blockInfoIndex = 0;
        int blockInfoLength = fileNode.getBlockInfoLength();
        while (leftArraySize > 0 && leftBlockSize > 0 && blockInfoIndex < blockInfoLength) {
            byte[] tempData;
            if (leftBlockSize >= DataNode.BLOCK_SIZE && leftArraySize >= DataNode.BLOCK_SIZE) {
                tempData = new byte[DataNode.BLOCK_SIZE];

            }
        }
        for (int dataIndex = 0; dataIndex < b.length && leftBlockSize > 0; dataIndex++) {
            if (leftBlockSize >= DataNode.BLOCK_SIZE && leftArraySize >= DataNode.BLOCK_SIZE) {

            }
        }



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
