package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class NameNode implements INameNode {

    // constructor
    public NameNode() throws IOException, URISyntaxException {

        System.out.println("initial name node success!");
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
        System.out.println("target file name: " + targetFileName);
        FileNode targetFileNode = dirNode.checkOutFile(targetFileName);
        return targetFileNode;
    }

    @Override
    public FileNode create(String fileUri) throws IOException, URISyntaxException {
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
                System.out.println("create file error");
                return null;
            }
            dirNode.initialContents();
        }
        String newFileName = pathArray[pathArray.length - 1];
        System.out.println("new file name: " + newFileName);
        Entity newEntity = dirNode.createNewEntity(newFileName, Entity.TYPE.FILE);
        if (newEntity == null) {
            System.out.println("create file " + newFileName + " fail");
            return null;
        } else {
            System.out.println("create file " + newFileName + " success");
            return (FileNode) newEntity;
        }
    }

    @Override
    public void close(String fileUri) throws IOException, URISyntaxException {

    }

    @Override
    public void mkdir(String fileUri) throws IOException, URISyntaxException {
        String[] pathArray = fileUri.split("/");
        String tempDirectoryName = "";
        DirNode dirNode = new DirNode(0, tempDirectoryName);
        dirNode.initialContents();
        if (!dirNode.initialRes) {
            return;
        }
        for (int i = 0; i < pathArray.length - 1; i++) {
            dirNode = dirNode.checkOutDir(pathArray[i]);
            if (dirNode == null) {
                System.out.println("mkdir error");
                return;
            }
            dirNode.initialContents();
        }
        String newDirName = pathArray[pathArray.length - 1];
        System.out.println("new directory name: " + newDirName);
        Entity newEntity = dirNode.createNewEntity(newDirName, Entity.TYPE.DIR);
        if (newEntity == null) {
            System.out.println("create directory " + newDirName + " fail");
        } else {
            System.out.println("create directory " + newDirName + " success");
        }
    }

    @Override
    public LocatedBlock addBlock(String fileUri) {

        return null;
    }
}
