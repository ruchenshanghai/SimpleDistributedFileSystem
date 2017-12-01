/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.namenode;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DirNode extends Entity {
    boolean initialRes = false;
    private List<Entity> contents = new LinkedList<>();

    private static final int ROOT_ID = 0;
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

    public DirNode(int id, String directoryName) throws IOException {
        super(id, TYPE.DIR, directoryName);
    }


    public void initialContents() throws IOException {
        // get info from disk
        System.out.println("initial directory node name: " + this.name);
        try {
            File dirFile = new File(RELATIVE_PATH + "/" + this.id + ".node");
            BufferedInputStream dirInputStream = new BufferedInputStream(new FileInputStream(dirFile));

            int tempInt;
            byte tempByte;
            while ((tempInt = dirInputStream.read()) != -1) {
                tempByte = (byte) tempInt;
                int nodeType = tempByte;
                int nodeNameLength = 0;
                byte[] nameByteArray;
                String nodeName = null;
                int nodeID = 0;
                Entity nodeEntity = null;

                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeNameLength = ((tempByte << 16) & 0xff0000) | nodeNameLength;
                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeNameLength = ((tempByte << 8) & 0xff00) | nodeNameLength;
                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeNameLength = ((tempByte) & 0xff) | nodeNameLength;
                nameByteArray = new byte[nodeNameLength];

                int readRes = dirInputStream.read(nameByteArray, 0, nodeNameLength);
                if (readRes == -1) {
                    return;
                }
                nodeName = new String(nameByteArray);
                System.out.println("read node name: " + nodeName);

                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeID = ((tempByte << 24) & 0xff000000) | nodeID;
                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeID = ((tempByte << 16) & 0xff0000) | nodeID;
                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
                nodeID = ((tempByte << 8) & 0xff00) | nodeID;
                if ((tempInt = dirInputStream.read()) == -1) {
                    throw new IOException();
                }
                tempByte = (byte) tempInt;
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
            if (tempEntity.type == TYPE.DIR && tempEntity.name.equals(directoryName)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFile(String fileName) {
        Iterator<Entity> contentIter = contents.iterator();
        while (contentIter.hasNext()) {
            Entity tempEntity = contentIter.next();
            if (tempEntity.type == TYPE.FILE && tempEntity.name.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static String getRelativePath() {
        return RELATIVE_PATH;
    }

    public static int generateNodeIndex() {
        File tempDirectory = new File(RELATIVE_PATH);
        return tempDirectory.list().length;
    }

    // array: 0-name, 1-parent id
    public static String[] createNewDirectory(String fileUri) throws IOException, URISyntaxException {
        // get directory and name
        String[] pathArray = fileUri.split("/");
        String tempDirectoryName = "";
        DirNode dirNode = new DirNode(ROOT_ID, tempDirectoryName);
        dirNode.initialContents();
        if (!dirNode.initialRes) {
            throw new URISyntaxException(fileUri, "file uri error");
        }
        for (int i = 0; i < pathArray.length - 1; i++) {
            dirNode = dirNode.checkOutDir(pathArray[i]);
            if (dirNode == null) {
                System.out.println("create directory error");
                throw new IOException();
            }
            dirNode.initialContents();
        }
        String newDirectoryName = pathArray[pathArray.length - 1];
        System.out.println("new directory name: " + newDirectoryName);
        if (newDirectoryName.equals("") || dirNode.containsDir(newDirectoryName)) {
            System.out.println("directory name error");
            throw new URISyntaxException(fileUri, "file uri error");
        }
        return new String[]{newDirectoryName, String.valueOf(dirNode.getId())};
    }


    private DirNode checkOutDir(String directoryName) throws IOException {
        Iterator<Entity> contentIter = contents.iterator();
        while (contentIter.hasNext()) {
            Entity tempEntry = contentIter.next();
            if (tempEntry.type == TYPE.DIR && tempEntry.name.equals(directoryName)) {
                return new DirNode(tempEntry.id, tempEntry.name);
            }
        }
        return null;
    }

}
