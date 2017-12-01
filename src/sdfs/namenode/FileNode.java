/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.namenode;

import sdfs.datanode.DataNode;
import sdfs.utils.LocatedBlock;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class FileNode extends Entity implements Serializable {
    private static final long serialVersionUID = -5007570814999866661L;
    private final List<BlockInfo> blockInfos = new ArrayList<>();
    private int totalSize = 0;

    public FileNode(int id, String fileName) {
        super(id, TYPE.FILE, fileName);
        System.out.println("initial file node name: " + name);
    }

    public void addBlockInfo(BlockInfo newBlockInfo) {
        blockInfos.add(newBlockInfo);
    }

    public void addTotalSize(int addSize) {
        if (addSize > 0) {
            totalSize += addSize;
        }
    }

    public int getBlockInfoLength() {
        return blockInfos.size();
    }

    public BlockInfo getBlockInfoByIndex(int index) {
        if (index >= blockInfos.size() || index < 0) {
            return null;
        }
        return blockInfos.get(index);
    }

//    // fill block write
//    public boolean writeToBlockInfosOnce(byte[] data, int endPos) {
//        if (endPos > DataNode.BLOCK_SIZE) {
//            return false;
//        }
//        BlockInfo tempBlockInfo = null;
//        try {
//            tempBlockInfo = new BlockInfo();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("add block info error");
//            return false;
//        }
//        blockInfos.add(tempBlockInfo);
//        boolean writeRes = tempBlockInfo.writeToDataNodeOnce(data, endPos);
//        if (!writeRes) {
//            System.out.println("write to block info error");
//            return false;
//        }
//        totalSize += endPos;
//        return true;
//    }

    public int getTotalSize() {
        return totalSize;
    }

//// read from block
//    public int readFromBlockInfos(byte[] target, int startPos) {
//        int blockInfoIndex = startPos / DataNode.BLOCK_SIZE;
//        int blockInfoPos = startPos % DataNode.BLOCK_SIZE;
//        int readCount = 0;
////        while (readCount < target.length && blockInfoIndex < blockInfos.size()) {
////            while (readCount < target.length && blockInfoPos < DataNode.BLOCK_SIZE) {
////                target[readCount]
////            }
////        }
//
//
//        return 0;
//    }

}
//class BlockInfo implements Serializable {
//    private static final long serialVersionUID = 8712105981933359634L;
//    private final List<LocatedBlock> locatedBlocks = new ArrayList<>();
//    private static final int DUPLICATE_NUM = 2;
//    private static final String LOCALHOST_NAME = "localhost";
//
//    public BlockInfo() throws IOException {
//        for (int i = 0; i < DUPLICATE_NUM; i++) {
//            int tempBlockID = DataNode.createNewBlock();
//            if (tempBlockID == 0) {
//                return;
//            }
//            InetAddress tempInetAddress = InetAddress.getByName(LOCALHOST_NAME);
//            LocatedBlock tempBlock = new LocatedBlock(tempInetAddress, tempBlockID);
//            locatedBlocks.add(tempBlock);
//        }
//    }
//
//    public boolean writeToDataNodeOnce(byte[] data, int endPos) {
//        if (endPos > DataNode.BLOCK_SIZE) {
//            return false;
//        }
//        DataNode dataNode = new DataNode();
//        int tempDataLength = endPos;
//        for (int i = 0; i < locatedBlocks.size(); i++) {
//            int tempBlockID = locatedBlocks.get(i).blockNumber;
//            try {
//                dataNode.write(tempBlockID, 0, tempDataLength, data);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public int readFromDataNodeOnce(byte[] target, int targetPos, int blockPos) {
//        if (targetPos >= target.length) {
//            return 0;
//        }
//
//
//        return 0;
//    }
//}