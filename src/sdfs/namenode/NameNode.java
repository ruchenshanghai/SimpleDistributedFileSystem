package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
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
        SystemNode targetNode = singleSystemTree.fetchFileOut(fileUri);
        FileNode targetFile = new FileNode(targetNode.getID(), targetNode.getName());
        if (targetNode == null) {
            throw new IOException();
        }
        return targetFile;
    }

    @Override
    public FileNode create(String fileUri) throws IOException, URISyntaxException {
        SystemNode newNode = singleSystemTree.createFileUri(fileUri);
        if (newNode == null) {
            throw new URISyntaxException(fileUri, "file uri error");
        }
        FileNode newFileNode = new FileNode(newNode.getID(), newNode.getName());
        changedFileMap.put(fileUri, newFileNode);
        return newFileNode;
    }

    @Override
    public void mkdir(String fileUri) throws IOException, URISyntaxException {
        // get directory and name
        String[] tempRes = DirNode.createNewDirectory(fileUri);
        String newDirectoryName = tempRes[0];
        int parentDirID = Integer.parseInt(tempRes[1]);
        // create node
        int tempNodeID = DirNode.currentNodeIndex();
        File tempNewFile = new File(DirNode.getRelativePath() + "/" + tempNodeID + ".node");
        byte[] newEntityNameByteArray = newDirectoryName.getBytes();
        int newEntityNameByteLength = newEntityNameByteArray.length;
        File tempDirFile = new File(DirNode.getRelativePath() + "/" + parentDirID + ".node");
        BufferedOutputStream tempNodeOut = new BufferedOutputStream(new FileOutputStream(tempDirFile, true));
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate directory node: " + tempNodeID + ".node");
            tempNodeOut.close();
            throw new IOException();
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
    }

    @Override
    public void close(String fileUri) throws IOException, URISyntaxException {
        // check changed fileUri
        SystemNode targetLeafNode = singleSystemTree.fetchFileOut(fileUri);
        if (targetLeafNode != null && targetLeafNode.getType() == SystemNode.TYPE.FILE && changedFileMap.containsKey(fileUri)) {
            FileNode tempNewNode = changedFileMap.get(fileUri);
            if (targetLeafNode.getID() == FileNode.getNotExistId()) {
                int tempNodeID = DirNode.currentNodeIndex();
                File tempDirFile = new File(DirNode.getRelativePath() + "/" + targetLeafNode.getParentNode().getID() + ".node");
                File tempFile = new File(DirNode.getRelativePath() + "/" + tempNodeID + ".node");
                DirNode tempDirNode = new DirNode(targetLeafNode.getParentNode().getID());
                tempDirNode.initialContents();

                if ((!tempDirFile.isFile()) || tempFile.isFile()) {
                    throw new IOException();
                }
                
                boolean createRes = tempFile.createNewFile();
                if (!createRes) {
                    throw new IOException();
                }
                targetLeafNode.setID(tempNodeID);
                tempNewNode.setId(tempNodeID);

//                if (tempDirNode.)


                FileOutputStream fileOut = new FileOutputStream(tempFile);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
//                objOut.writeObject();
                objOut.flush();
                objOut.close();
                fileOut.close();



            }


//            try {
//                FileOutputStream fileOut = new FileOutputStream(tempDirFile);
//                ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
//                objOut.writeObject(fileNode);
//                objOut.flush();
//                objOut.close();
//                fileOut.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new IOException();
//            }

        } else {
            throw new IOException();
        }

    }

    public BlockInfo addBlockInfo(String fileUri) {

        return null;
    }

    @Override
    public LocatedBlock addBlock(String fileUri) {


        return null;
    }
}
