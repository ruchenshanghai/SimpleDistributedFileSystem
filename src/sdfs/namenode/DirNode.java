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

    // initial the node directory
    private static final String relativePath = "./data/node";

    static {
        File tempNodeDir = new File(relativePath);
        File tempRootNode = new File(relativePath + "/0.node");
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
        this.id = id;
        this.type = TYPE.DIR;
        this.name = directoryName;
    }


    public void initialContents() throws IOException {
        try {
            File dirFile = new File(relativePath + "/" + this.id + ".node");
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
                System.out.println("read node name length: " + readRes);
                if (readRes == -1) {
                    return;
                }
                nodeName = new String(nameByteArray);

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

    private static int currentNodeIndex() {
        File tempDirectory = new File(relativePath);
        return tempDirectory.list().length;
    }

    public static boolean createNewEntity(DirNode dirNode, String newEntityName, Entity.TYPE type) throws IOException {
        int tempNodeID = currentNodeIndex();
        File tempNewFile = new File(relativePath + "/" + tempNodeID + ".node");
        if (!tempNewFile.createNewFile()) {
            tempNewFile.delete();
            return false;
        }

        File tempDirFile = new File(relativePath + "/" + dirNode.id + ".node");
        BufferedOutputStream tempNodeOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
        switch (type) {
            case FILE: {
                tempNodeOut.write(0);
            } break;
            case DIR: {
                tempNodeOut.write(1);
            } break;
        }
        byte[] newEntityNameByteArray = newEntityName.getBytes();
        int newEntityNameByteLength = newEntityNameByteArray.length;
        tempNodeOut.write((newEntityNameByteLength & 0xff0000) >> 16);
        tempNodeOut.write((newEntityNameByteLength & 0xff00) >> 8);
        tempNodeOut.write((newEntityNameByteLength & 0xff));
        tempNodeOut.write(newEntityNameByteArray, 0, newEntityNameByteLength);
        tempNodeOut.write((tempNodeID & 0xff000000) >> 24);
        tempNodeOut.write((tempNodeID & 0xff0000) >> 16);
        tempNodeOut.write((tempNodeID & 0xff00) >> 8);
        tempNodeOut.write((tempNodeID & 0xff));
        tempNodeOut.flush();
        tempNodeOut.close();
        return true;
    }

}
