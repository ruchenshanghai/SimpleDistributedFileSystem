/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import sdfs.datanode.DataNode;
import sdfs.namenode.BlockInfo;
import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;

import java.io.Closeable;
import java.io.IOException;

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


    public int read(byte[] b) {
        //todo your code here
        if (currentPos >= fileNode.getTotalSize()) {
            return 0;
        }
        int readCount = 0;
        int targetLength = b.length;

        int blockInfoIndex;
        int blockInfoOffset;
        int blockInfoLength = fileNode.getBlockInfoLength();
        // initial by currentPos
        int leftBlockSize = fileNode.getTotalSize() - currentPos;
        blockInfoIndex = currentPos / DataNode.BLOCK_SIZE;
        blockInfoOffset = currentPos % DataNode.BLOCK_SIZE;

        while (readCount < targetLength && leftBlockSize > 0 && blockInfoIndex < blockInfoLength) {
            byte[] tempData;
            if (leftBlockSize >= DataNode.BLOCK_SIZE && (targetLength - readCount) >= DataNode.BLOCK_SIZE) {
                tempData = new byte[DataNode.BLOCK_SIZE];
                BlockInfo tempBlockInfo = fileNode.getBlockInfoByIndex(blockInfoIndex);
                int tempReadCount = tempBlockInfo.readFromDataNodeOnce(tempData, blockInfoOffset, DataNode.BLOCK_SIZE);
                System.arraycopy(tempData, 0, b, readCount, tempReadCount);
                readCount += tempReadCount;
                leftBlockSize -= tempReadCount;
            } else if (leftBlockSize < DataNode.BLOCK_SIZE) {
                if ((targetLength - readCount) >= leftBlockSize) {
                    // leftBlockSize <= (targetLength - readCount), leftBlockSize < DataNode.BLOCK_SIZE
                    tempData = new byte[leftBlockSize];
                    BlockInfo tempBlockInfo = fileNode.getBlockInfoByIndex(blockInfoIndex);
                    int tempReadCount = tempBlockInfo.readFromDataNodeOnce(tempData, blockInfoOffset, leftBlockSize);
                    System.arraycopy(tempData, 0, b, readCount, tempReadCount);
                    readCount += tempReadCount;
                    leftBlockSize -= tempReadCount;
                } else {
                    // (targetLength - readCount) < leftBlockSize < DataNode.BLOCK_SIZE
                    tempData = new byte[(targetLength - readCount)];
                    BlockInfo tempBlockInfo = fileNode.getBlockInfoByIndex(blockInfoIndex);
                    int tempReadCount = tempBlockInfo.readFromDataNodeOnce(tempData, blockInfoOffset, (targetLength - readCount));
                    System.arraycopy(tempData, 0, b, readCount, tempReadCount);
                    readCount += tempReadCount;
                    leftBlockSize -= tempReadCount;
                }
            } else {
                // leftBlockSize >= DataNode.BLOCK_SIZE > (targetLength - readCount)
                tempData = new byte[(targetLength - readCount)];
                BlockInfo tempBlockInfo = fileNode.getBlockInfoByIndex(blockInfoIndex);
                int tempReadCount = tempBlockInfo.readFromDataNodeOnce(tempData, blockInfoOffset, (targetLength - readCount));
                System.arraycopy(tempData, 0, b, readCount, tempReadCount);
                readCount += tempReadCount;
                leftBlockSize -= tempReadCount;
            }
            blockInfoOffset = 0;
            blockInfoIndex++;
        }
        currentPos += readCount;
        return readCount;
    }

    @Override
    public void close() throws IOException {
        //todo your code here
        currentPos = 0;
    }

    public void seek(int newPos) throws IndexOutOfBoundsException, IOException {
        //todo your code here
        if (newPos >= fileNode.getTotalSize()) {
            return;
        }
        currentPos = newPos;
    }
}
