package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class NameNode implements INameNode {

    private static NameNode singleNameNode = new NameNode();
    private static SystemTree singleSystemTree = SystemTree.getSingleInstanceSystemTree();
    private static Map<String, FileNode> changedFileMap = new HashMap<>();

    // constructor
    private NameNode() {
        System.out.println("initial name node success!");
    }

    public static NameNode getSingleNameNodeInstance() {
        return singleNameNode;
    }

    @Override
    public FileNode open(String fileUri) throws IOException, URISyntaxException {
        SystemNode targetNode = singleSystemTree.fetchFileOut(fileUri);
        if (targetNode == null) {
            throw new URISyntaxException(fileUri, "file uri error");
        }
        FileInputStream fileIn = new FileInputStream(new File(DirNode.getRelativePath() + "/" + targetNode.getID() + ".node"));
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        try {
            FileNode targetFile = (FileNode) objIn.readObject();
            return targetFile;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @Override
    public FileNode create(String fileUri) throws IOException, URISyntaxException {
        SystemNode newNode = singleSystemTree.createFileUri(fileUri);
        if (newNode == null) {
            throw new URISyntaxException(fileUri, "file uri error");
        }
        FileNode newFileNode = new FileNode(newNode.getID(), newNode.getName());
        if (!changedFileMap.containsKey(fileUri)) {
            changedFileMap.put(fileUri, newFileNode);
        }
        return newFileNode;
    }

    @Override
    public void mkdir(String fileUri) throws IOException, URISyntaxException {
        // get directory and name
        String[] tempRes = DirNode.createNewDirectory(fileUri);
        String newDirectoryName = tempRes[0];
        int parentDirID = Integer.parseInt(tempRes[1]);
        // create node
        int tempNodeID = DirNode.generateNodeIndex();
        File tempNewFile = new File(DirNode.getRelativePath() + "/" + tempNodeID + ".node");
        byte[] newEntityNameByteArray = newDirectoryName.getBytes();
        int newEntityNameByteLength = newEntityNameByteArray.length;
        File tempDirFile = new File(DirNode.getRelativePath() + "/" + parentDirID + ".node");
        BufferedOutputStream tempDirOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate directory node: " + tempNodeID + ".node");
            tempDirOut.close();
            throw new IOException();
        }

        tempDirOut.write(Entity.getDirectoryInt());
        tempDirOut.write((newEntityNameByteLength & 0xff0000) >> 16);
        tempDirOut.write((newEntityNameByteLength & 0xff00) >> 8);
        tempDirOut.write((newEntityNameByteLength & 0xff));
        tempDirOut.write(newEntityNameByteArray, 0, newEntityNameByteLength);
        tempDirOut.write((tempNodeID & 0xff000000) >> 24);
        tempDirOut.write((tempNodeID & 0xff0000) >> 16);
        tempDirOut.write((tempNodeID & 0xff00) >> 8);
        tempDirOut.write((tempNodeID & 0xff));
        tempDirOut.flush();
        tempDirOut.close();

        boolean mkdirRes = singleSystemTree.mkdir(fileUri, tempNodeID);
        if (!mkdirRes) {
            return;
        }
    }

    @Override
    public void close(String fileUri) throws IOException, URISyntaxException {
        // check changed fileUri
        SystemNode targetLeafNode = singleSystemTree.fetchFileOut(fileUri);
        if (targetLeafNode != null && targetLeafNode.getType() == SystemNode.TYPE.FILE && changedFileMap.containsKey(fileUri)) {
            FileNode tempNewNode = changedFileMap.get(fileUri);

            if (targetLeafNode.getID() == FileNode.getNotExistId()) {
                // .node not exist
                int tempNewFileNodeID = DirNode.generateNodeIndex();
                String tempNewFileName = targetLeafNode.getName();
                byte[] tempNewFileBytes = tempNewFileName.getBytes();

                File tempDirFile = new File(DirNode.getRelativePath() + "/" + targetLeafNode.getParentNode().getID() + ".node");
                File tempNewFile = new File(DirNode.getRelativePath() + "/" + tempNewFileNodeID + ".node");
                DirNode tempDirNode = new DirNode(targetLeafNode.getParentNode().getID(), targetLeafNode.getParentNode().getName());
                tempDirNode.initialContents();

                if ((!tempDirFile.isFile()) || tempDirNode.containsFile(tempNewFileName) || tempNewFile.isFile()) {
                    // file error
                    throw new IOException();
                }

                targetLeafNode.setID(tempNewFileNodeID);
                tempNewNode.setId(tempNewFileNodeID);
                tempNewFile.createNewFile();

                BufferedOutputStream tempDirOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
                tempDirOut.write(Entity.getFileInt());
                tempDirOut.write((tempNewFileBytes.length & 0xff0000) >> 16);
                tempDirOut.write((tempNewFileBytes.length & 0xff00) >> 8);
                tempDirOut.write((tempNewFileBytes.length & 0xff));
                tempDirOut.write(tempNewFileBytes, 0, tempNewFileBytes.length);
                tempDirOut.write((tempNewFileNodeID & 0xff000000) >> 24);
                tempDirOut.write((tempNewFileNodeID & 0xff0000) >> 16);
                tempDirOut.write((tempNewFileNodeID & 0xff00) >> 8);
                tempDirOut.write((tempNewFileNodeID & 0xff));
                tempDirOut.flush();
                tempDirOut.close();
            }
            // save file node
            File tempNewFile = new File(DirNode.getRelativePath() + "/" + tempNewNode.getId() + ".node");
            if (!tempNewFile.isFile()) {
                // file error
                throw new IOException();
            }
            FileOutputStream tempFileOut = new FileOutputStream(tempNewFile, false);
            ObjectOutputStream objOut = new ObjectOutputStream(tempFileOut);
            objOut.writeObject(tempNewNode);
            objOut.flush();
            objOut.close();
            tempFileOut.close();
            changedFileMap.remove(fileUri);
        } else {
            throw new IOException();
        }
    }

    public BlockInfo addBlockInfo(String fileUri) throws IOException {
        if (!changedFileMap.containsKey(fileUri)) {
            // file not open
            return null;
        }
        FileNode tempFileNode = changedFileMap.get(fileUri);
        BlockInfo tempBlockInfo = new BlockInfo();
        tempFileNode.addBlockInfo(tempBlockInfo);
        return tempBlockInfo;
    }

    @Override
    public LocatedBlock addBlock(String fileUri) {
//        if (!changedFileMap.containsKey(fileUri)) {
//            // file not open
//            return null;
//        }
//        FileNode tempFileNode = changedFileMap.get(fileUri);

        return null;
    }
}
