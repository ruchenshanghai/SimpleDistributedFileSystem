/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileNode extends Entity implements Serializable {
    private static final long serialVersionUID = -5007570814999866661L;
    private final List<BlockInfo> blockInfos = new ArrayList<>();
    //todo your code here

    public FileNode(int id, String fileName) {
        this.id = id;
        this.type = TYPE.FILE;
        this.name = fileName;
    }

}

class BlockInfo implements Serializable {
    private static final long serialVersionUID = 8712105981933359634L;
    private final List<LocatedBlock> locatedBlocks = new ArrayList<>();
    //todo your code here
}