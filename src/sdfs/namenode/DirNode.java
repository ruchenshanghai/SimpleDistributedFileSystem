/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.namenode;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DirNode extends Entity {
    boolean initialRes = false;
    private List<Entity> contents = new LinkedList<>();

    public static String getRelativePath() {
        return RELATIVE_PATH;
    }

    // initial the node directory
    private static final String RELATIVE_PATH = "./data/node";
    static {
        File tempNodeDir = new File(RELATIVE_PATH);
        File tempRootNode = new File(RELATIVE_PATH + "/0.node");
        if (!tempNodeDir.isDirectory() || !tempRootNode.isFile()) {
            // need create directory
            boolean createDirRes = tempNodeDir.mkdirs();
            System.out.println("create node directory: " + createDirRes);
            try {
                boolean createRootRes = tempRootNode.createNewFile();
                System.out.println("create root directory node: " + createRootRes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public DirNode(int id) throws IOException {
        this.id = id;
        this.type = TYPE.DIR;
    }

    public DirNode(int id, String directoryName) throws IOException {
        super(id, TYPE.DIR, directoryName);
    }


    public void initialContents() throws IOException {
        // get info from disk
        System.out.println("initial directory node name: " + this.name);
        try {
            File dirFile = new File(RELATIVE_PATH + "/" + this.id + ".node");
            BufferedInputStream dirInputStream = new BufferedInputStream(new FileInputStream(dirFile));

            byte tempByte;
            while ((tempByte = (byte) dirInputStream.read()) != -1) {
                int nodeType = tempByte;
                int nodeNameLength = 0;
                byte[] nameByteArray;
                String nodeName = null;
                int nodeID = 0;
                Entity nodeEntity = null;

                tempByte = (byte) dirInputStream.read();
                nodeNameLength = ((tempByte << 16) & 0xff0000) | nodeNameLength;
                tempByte = (byte) dirInputStream.read();
                nodeNameLength = ((tempByte << 8) & 0xff00) | nodeNameLength;
                tempByte = (byte) dirInputStream.read();
                nodeNameLength = ((tempByte) & 0xff) | nodeNameLength;
                nameByteArray = new byte[nodeNameLength];

                int readRes = dirInputStream.read(nameByteArray, 0, nodeNameLength);
                if (readRes == -1) {
                    return;
                }
                nodeName = new String(nameByteArray);
                System.out.println("read node name: " + nodeName);

                tempByte = (byte) dirInputStream.read();
                nodeID = ((tempByte << 24) & 0xff000000) | nodeID;
                tempByte = (byte) dirInputStream.read();
                nodeID = ((tempByte << 16) & 0xff0000) | nodeID;
                tempByte = (byte) dirInputStream.read();
                nodeID = ((tempByte << 8) & 0xff00) | nodeID;
                tempByte = (byte) dirInputStream.read();
                nodeID = ((tempByte) & 0xff) | nodeID;

                switch (nodeType) {
                    case 0: {
                        // file node
                        nodeEntity = new FileNode(nodeID, nodeName);
                    } break;
                    case 1: {
                        // directory node
                        nodeEntity = new DirNode(nodeID, nodeName);
                    } break;
                }
                contents.add(nodeEntity);
            }
            initialRes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean containsDir(String directoryName) {
        Iterator<Entity> contentIter = contents.iterator();
        while (contentIter.hasNext()) {
            Entity tempEntity = contentIter.next();
            if (tempEntity.name.equals(directoryName)) {
                return true;
            }
        }
        return false;
    }

    public static int currentNodeIndex() {
        File tempDirectory = new File(RELATIVE_PATH);
        return tempDirectory.list().length;
    }

    public DirNode checkOutDir(String directoryName) throws IOException {
        Iterator<Entity> contentIter = contents.iterator();
        while (contentIter.hasNext()) {
            Entity tempEntry = contentIter.next();
            if (tempEntry.type == TYPE.DIR && tempEntry.name.equals(directoryName)) {
                return new DirNode(tempEntry.id, tempEntry.name);
            }
        }
        return null;
    }

    public FileNode checkOutFile(String fileName) {
        Iterator<Entity> contentIter = contents.iterator();
        while (contentIter.hasNext()) {
            Entity tempEntry = contentIter.next();
            if (tempEntry.type == TYPE.FILE && tempEntry.name.equals(fileName)) {
                File tempFile = new File(RELATIVE_PATH + "/" + tempEntry.id + ".node");
                if (!tempFile.isFile()) {
                    return null;
                }
                try {
                    FileInputStream tempIn = new FileInputStream(tempFile);
                    ObjectInputStream objIn = new ObjectInputStream(tempIn);
                    FileNode tempFileNode = (FileNode) objIn.readObject();
                    tempFileNode.setId(tempEntry.id);
                    tempFileNode.setName(tempEntry.name);
                    tempFileNode.setType(TYPE.FILE);
                    return tempFileNode;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

//    public Entity createNewEntity(String newEntityName, Entity.TYPE type) throws IOException {
//        int tempNodeID = currentNodeIndex();
//        File tempNewFile = new File(RELATIVE_PATH + "/" + tempNodeID + ".node");
//        byte[] newEntityNameByteArray = newEntityName.getBytes();
//        int newEntityNameByteLength = newEntityNameByteArray.length;
//        Entity newEntity = null;
//        File tempDirFile = new File(RELATIVE_PATH + "/" + this.id + ".node");
//        BufferedOutputStream tempNodeOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
//
//        if (this.containsDir(newEntityName)) {
//            System.out.println("duplicate entity name: " + newEntityName);
//            return newEntity;
//        }
//        if (!tempNewFile.createNewFile()) {
//            System.out.println("duplicate entity node: " + tempNodeID + ".node");
//            tempNewFile.delete();
//            return newEntity;
//        }
//
//        switch (type) {
//            case FILE: {
//                tempNodeOut.write(0);
//                newEntity = new FileNode(tempNodeID, newEntityName);
//            } break;
//            case DIR: {
//                tempNodeOut.write(1);
//                newEntity = new DirNode(tempNodeID, newEntityName);
//            } break;
//        }
//        tempNodeOut.write((newEntityNameByteLength & 0xff0000) >> 16);
//        tempNodeOut.write((newEntityNameByteLength & 0xff00) >> 8);
//        tempNodeOut.write((newEntityNameByteLength & 0xff));
//        tempNodeOut.write(newEntityNameByteArray, 0, newEntityNameByteLength);
//        tempNodeOut.write((tempNodeID & 0xff000000) >> 24);
//        tempNodeOut.write((tempNodeID & 0xff0000) >> 16);
//        tempNodeOut.write((tempNodeID & 0xff00) >> 8);
//        tempNodeOut.write((tempNodeID & 0xff));
//        tempNodeOut.flush();
//        tempNodeOut.close();
//        return newEntity;
//    }

}
