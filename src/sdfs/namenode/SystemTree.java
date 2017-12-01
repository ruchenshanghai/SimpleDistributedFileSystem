package sdfs.namenode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SystemTree {
    private SystemNode rootDirNode = null;
    private static SystemTree systemTree = new SystemTree();

    private SystemTree() {
        try {
            rootDirNode = new SystemNode(0, "", SystemNode.TYPE.DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SystemTree getSingleInstanceSystemTree() {
        return systemTree;
    }

    public String parseFileUriOut(String fileUri) {
        String[] pathArray = fileUri.split("/");
        SystemNode tempNode = rootDirNode;
        for (int i = 0; i < pathArray.length - 1; i++) {
            tempNode = tempNode.checkOutChild(pathArray[i]);
            if (tempNode == null) {
                System.out.println("open file error");
                return null;
            }
        }
        return pathArray[pathArray.length - 1];
    }

    public SystemNode createFileUri(String fileUri) throws IOException {
        String[] pathArray = fileUri.split("/");
        SystemNode tempDirNode = rootDirNode;
        for (int i = 0; i < pathArray.length - 1; i++) {
            tempDirNode = tempDirNode.checkOutChild(pathArray[i]);
            if (tempDirNode == null) {
                System.out.println("open file error");
                return null;
            }
        }
        if (tempDirNode.getType() == SystemNode.TYPE.FILE) {
            return null;
        }
        String tempNewFileName = pathArray[pathArray.length - 1];
        SystemNode tempFileNode = new SystemNode(FileNode.getNotExistId(), tempNewFileName, SystemNode.TYPE.FILE);
        tempDirNode.addChildNode(tempFileNode);
        return tempFileNode;
    }

    public SystemNode fetchFileOut(String fileUri) {
        String[] pathArray = fileUri.split("/");
        SystemNode tempNode = rootDirNode;
        for (int i = 0; i < pathArray.length; i++) {
            tempNode = tempNode.checkOutChild(pathArray[i]);
            if (tempNode == null) {
                System.out.println("open file error");
                return null;
            }
        }
        return tempNode;
    }

    public static void main(String[] args) {
        try {
            SystemNode systemNode = new SystemNode(0, "", SystemNode.TYPE.DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SystemNode {
    // initial the node directory
    private static final String RELATIVE_PATH = "./data/node";
    private static final String ROOT_FILE = "./data/node/0.node";

    private SystemNode parentNode = null;

    static enum TYPE {
        DIR, FILE
    }

    private int ID = 0;
    private String name = null;
    private TYPE type = null;
    private Map<String, SystemNode> childContents = null;

    public SystemNode(int ID, String name, TYPE type) throws IOException {
        this.ID = ID;
        this.name = name;
        this.type = type;
        if (type == TYPE.DIR) {
            childContents = new HashMap<>();
            boolean initialRes = initialDirNode();
            if (!initialRes) {
                throw new IOException();
            }
        }
    }


    private boolean initialDirNode() {
        if (this.type == TYPE.FILE) {
            // not directory
            return false;
        }
        try {
            File tempFile = new File(RELATIVE_PATH + "/" + this.ID + ".node");
            BufferedInputStream rootInputStream = new BufferedInputStream(new FileInputStream(tempFile));
            int tempInt;
            byte tempByte;
            while ((tempInt = rootInputStream.read()) != -1) {
                tempByte = (byte) tempInt;
                int nodeType = tempByte;
                int tempNameLength = 0;
                byte[] tempNameByteArray;
                String tempNodeName = null;
                int tempNodeID = 0;
                SystemNode tempNode = null;

                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNameLength = ((tempByte << 16) & 0xff0000) | tempNameLength;
                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNameLength = ((tempByte << 8) & 0xff00) | tempNameLength;
                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNameLength = ((tempByte) & 0xff) | tempNameLength;
                tempNameByteArray = new byte[tempNameLength];

                int readRes = rootInputStream.read(tempNameByteArray, 0, tempNameLength);
                if (readRes == -1) {
                    return false;
                }
                tempNodeName = new String(tempNameByteArray);
                System.out.println("read node name: " + tempNodeName);

                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNodeID = ((tempByte << 24) & 0xff000000) | tempNodeID;
                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNodeID = ((tempByte << 16) & 0xff0000) | tempNodeID;
                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNodeID = ((tempByte << 8) & 0xff00) | tempNodeID;
                if ((tempInt = rootInputStream.read()) == -1) {
                    return false;
                }
                tempByte = (byte) tempInt;
                tempNodeID = ((tempByte) & 0xff) | tempNodeID;

                switch (nodeType) {
                    case 0: {
                        // file node
                        tempNode = new SystemNode(tempNodeID, tempNodeName, SystemNode.TYPE.FILE);
                    }
                    break;
                    case 1: {
                        // directory node
                        tempNode = new SystemNode(tempNodeID, tempNodeName, SystemNode.TYPE.DIR);
                    }
                    break;
                }
                if (!this.addChildNode(tempNode)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean addChildNode(SystemNode childNode) {
        if (this.type == TYPE.FILE) {
            // not directory
            return false;
        }
        if (childNode.getName().equals("") || this.childContents.containsKey(childNode.getName())) {
            // name error
            return false;
        }
        this.childContents.put(childNode.getName(), childNode);
        childNode.setParentNode(this);
        return true;
    }

    public SystemNode checkOutChild(String childName) {
        if (this.type == TYPE.FILE || !this.childContents.containsKey(childName)) {
            return null;
        }
        return this.childContents.get(childName);
    }

    public SystemNode getParentNode() {
        return parentNode;
    }

    public TYPE getType() {
        return type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setParentNode(SystemNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getName() {
        return name;
    }
}
