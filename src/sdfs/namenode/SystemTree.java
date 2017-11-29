package sdfs.namenode;

import java.util.HashSet;
import java.util.Set;

public class SystemTree {
    private SystemNode root = null;

    public SystemTree() {
    }
}

class SystemNode {
    private SystemNode parentNode = null;
    static enum TYPE {
        LEAF, NO_LEAF
    }
    private int ID = 0;
    private String name = null;
    private TYPE type;
    private Set<SystemNode> childNodeSet = null;


    public SystemNode() {

    }

    public SystemNode(SystemNode parentNode, int ID, String name, TYPE type) {
        this.parentNode = parentNode;
        this.ID = ID;
        this.name = name;
        this.type = type;
        if (type == TYPE.NO_LEAF) {
            childNodeSet = new HashSet<>();
        }
    }
}
