package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.*;
import java.net.URISyntaxException;

public class NameNode implements INameNode {
    

    // constructor
    public NameNode() {

        System.out.println("initial name node success!");
    }

    private Entity createNewEntity(String fileUri, Entity.TYPE type) throws IOException {
        // get directory and name
        String[] pathArray = fileUri.split("/");
        String tempDirectoryName = "";
        DirNode dirNode = new DirNode(0, tempDirectoryName);
        dirNode.initialContents();
        if (!dirNode.initialRes) {
            return null;
        }
        for (int i = 0; i < pathArray.length - 1; i++) {
            dirNode = dirNode.checkOutDir(pathArray[i]);
            if (dirNode == null) {
                System.out.println("create entity error");
                return null;
            }
            dirNode.initialContents();
        }
        String newEntityName = pathArray[pathArray.length - 1];
        System.out.println("new entity name: " + newEntityName);
        if (newEntityName.equals("") || dirNode.containsDir(newEntityName)) {
            System.out.println("entity name error");
            return null;
        }
        // create node
        int tempNodeID = DirNode.currentNodeIndex();
        File tempNewFile = new File(DirNode.getRelativePath() + "/" + tempNodeID + ".node");
        byte[] newEntityNameByteArray = newEntityName.getBytes();
        int newEntityNameByteLength = newEntityNameByteArray.length;
        Entity newEntity = null;
        File tempDirFile = new File(DirNode.getRelativePath() + "/" + dirNode.getId() + ".node");
        BufferedOutputStream tempNodeOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate entity node: " + tempNodeID + ".node");
            tempNewFile.delete();
            return null;
        }
        // output to node
        switch (type) {
            case FILE: {
                tempNodeOut.write(0);
                newEntity = new FileNode(tempNodeID, newEntityName);
            } break;
            case DIR: {
                tempNodeOut.write(1);
                newEntity = new DirNode(tempNodeID, newEntityName);
            } break;
        }
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
        return newEntity;
    }

    public void closeFileNode(FileNode fileNode) throws IOException, URISyntaxException {
        // no use
        File tempFile = new File(DirNode.getRelativePath() + "/" + fileNode.getId() + ".node");
        if (!tempFile.isFile()) {
            throw new IOException();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(tempFile);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(fileNode);
            objOut.flush();
            objOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @Override
    public FileNode open(String fileUri) throws IOException, URISyntaxException {
        String[] pathArray = fileUri.split("/");
        String tempDirectoryName = "";
        DirNode dirNode = new DirNode(0, tempDirectoryName);
        dirNode.initialContents();
        if (!dirNode.initialRes) {
            return null;
        }
        for (int i = 0; i < pathArray.length - 1; i++) {
            dirNode = dirNode.checkOutDir(pathArray[i]);
            if (dirNode == null) {
                System.out.println("open file error");
                return null;
            }
            dirNode.initialContents();
        }
        String targetFileName = pathArray[pathArray.length - 1];
        if (targetFileName.equals("")) {
            System.out.println("blank file name");
            return null;
        }
        System.out.println("target file name: " + targetFileName);
        FileNode targetFileNode = dirNode.checkOutFile(targetFileName);
        return targetFileNode;
    }

    @Override
    public FileNode create(String fileUri) throws IOException, URISyntaxException {
        FileNode fileNode = (FileNode) createNewEntity(fileUri, Entity.TYPE.FILE);
        if (fileNode == null) {
            System.out.println("create file error: " + fileUri);
        }
        return fileNode;
    }


    @Override
    public void close(String fileUri) throws IOException, URISyntaxException {
        // no use

    }

    @Override
    public void mkdir(String fileUri) throws IOException, URISyntaxException {
        DirNode dirNode = (DirNode) createNewEntity(fileUri, Entity.TYPE.DIR);
        if (dirNode == null) {
            System.out.println("create directory error: " + fileUri);
        }
    }

    @Override
    public LocatedBlock addBlock(String fileUri) {


        return null;
    }
}
