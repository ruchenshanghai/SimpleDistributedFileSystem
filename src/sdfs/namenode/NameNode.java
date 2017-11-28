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
        return null;
    }

    @Override
    public FileNode create(String fileUri) throws IOException, URISyntaxException {
        return null;
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
        System.out.println("new directory: " + newDirName);
        boolean createRes = DirNode.createNewEntity(dirNode, newDirName, Entity.TYPE.DIR);
        System.out.println("mkdir result: " + createRes);
    }

    @Override
    public LocatedBlock addBlock(String fileUri) {
        return null;
    }
}
