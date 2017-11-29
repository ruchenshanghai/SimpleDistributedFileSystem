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
    private int ID;
    private String name;

    public FileNode(int id, String fileName) {
        super(id, TYPE.FILE, fileName);
        this.ID = id;
        this.name = fileName;
        System.out.println("initial file node name: " + this.name);
    }

    public List<BlockInfo> getBlockInfos() {
        return blockInfos;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}

class BlockInfo implements Serializable {
    private static final long serialVersionUID = 8712105981933359634L;
    private final List<LocatedBlock> locatedBlocks = new ArrayList<>();
    //todo your code here

}