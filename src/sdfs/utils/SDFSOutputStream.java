/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.datanode.DataNode;
import sdfs.namenode.DirNode;
import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;

import javax.print.attribute.standard.MediaSize;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.URISyntaxException;

public class SDFSOutputStream implements Closeable, Flushable {
    private FileNode fileNode;
    int bufferSize = DataNode.BLOCK_SIZE;
    int currentBufferPos = 0;
    byte[] buffer = new byte[bufferSize];



    public SDFSOutputStream(String fileUri) throws Exception {
        NameNode nameNode = NameNode.getSingleNameNodeInstance();
        fileNode = nameNode.create(fileUri);
        if (fileNode == null) {
            throw new Exception();
        }
    }

    public void write(byte[] b) throws IOException {
        //todo your code here
        // initial the block
        if (b == null || b.length == 0) {
            return;
        }

        if (bufferSize - currentBufferPos + 1 > b.length) {
            // write to buffer
            for (int i = 0; i < b.length; i++) {
                buffer[currentBufferPos] = b[i];
                currentBufferPos++;
            }
        } else {
            int dataPos = 0;
            while (dataPos < b.length) {
                for (; (currentBufferPos < bufferSize) && dataPos < b.length; currentBufferPos++) {
                    buffer[currentBufferPos] = b[dataPos];
                    dataPos++;
                }
                flush();
            }
        }

    }

    @Override
    public void flush() throws IOException {
        //todo your code here
        if (currentBufferPos == bufferSize) {
            boolean writeRes = fileNode.writeToBlockInfosOnce(buffer, currentBufferPos);
            System.out.println("write result: " + writeRes);
            currentBufferPos = 0;
        }
    }

    @Override
    public void close() throws IOException {
        //todo your code here
        if (currentBufferPos > 0 && currentBufferPos <= bufferSize) {
            // write to disk and close file node
            boolean writeRes = fileNode.writeToBlockInfosOnce(buffer, currentBufferPos);
            System.out.println("write result: " + writeRes);
            currentBufferPos = 0;
        }

        NameNode nameNode = NameNode.getSingleNameNodeInstance();
        try {
            nameNode.closeFileNode(fileNode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("file node save error");
        }
    }

}
