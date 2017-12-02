package sdfs.namenode;

import sdfs.datanode.DataNode;
import sdfs.utils.LocatedBlock;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class BlockInfo implements Serializable {
    private static final long serialVersionUID = 8712105981933359634L;
    private final List<LocatedBlock> locatedBlocks = new ArrayList<>();
    private static final int DUPLICATE_NUM = 1;
    private static final String LOCALHOST_NAME = "localhost";

    public BlockInfo() throws IOException {
        for (int i = 0; i < DUPLICATE_NUM; i++) {
            int tempBlockID = DataNode.createNewBlock();
            if (tempBlockID == -1) {
                throw new IOException();
            }
            InetAddress tempInetAddress = InetAddress.getByName(LOCALHOST_NAME);
            LocatedBlock tempBlock = new LocatedBlock(tempInetAddress, tempBlockID);
            locatedBlocks.add(tempBlock);
        }
    }

    public boolean writeToDataNodeOnce(byte[] data, int endPos) {
        if (endPos > data.length || endPos > DataNode.BLOCK_SIZE) {
            return false;
        }
        DataNode dataNode = new DataNode();
        for (int i = 0; i < locatedBlocks.size(); i++) {
            int tempBlockID = locatedBlocks.get(i).blockNumber;
            try {
                dataNode.write(tempBlockID, 0, endPos, data);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public int readFromDataNodeOnce(byte[] target, int startPos, int endPos) {
        if (endPos > target.length || endPos > DataNode.BLOCK_SIZE) {
            return 0;
        }
        DataNode dataNode = new DataNode();
        int randomIndex = (int) (Math.random() * DUPLICATE_NUM);
        int tempBlockID = locatedBlocks.get(randomIndex).blockNumber;
        try {
            dataNode.read(tempBlockID, startPos, endPos, target);
            return endPos;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
