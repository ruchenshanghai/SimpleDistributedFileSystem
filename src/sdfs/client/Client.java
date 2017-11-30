package sdfs.client;

import sdfs.namenode.FileNode;
import sdfs.namenode.NameNode;
import sdfs.utils.SDFSInputStream;
import sdfs.utils.SDFSOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;

public class Client implements ISimpleDistributedFileSystem {
    @Override
    public SDFSInputStream open(String fileUri) throws FileNotFoundException, IOException {
        SDFSInputStream sdfsIn = null;
        try {
            sdfsIn = new SDFSInputStream(fileUri);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
        return sdfsIn;
    }

    @Override
    public SDFSOutputStream create(String fileUri) throws FileAlreadyExistsException, IOException {
        SDFSOutputStream sdfsOut = null;
        try {
            sdfsOut = new SDFSOutputStream(fileUri);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileAlreadyExistsException("create file error");
        }
        return sdfsOut;
    }

    @Override
    public void mkdir(String fileUri) throws FileAlreadyExistsException, IOException {
        try {
            NameNode nameNode = NameNode.getSingleNameNodeInstance();
            nameNode.mkdir(fileUri);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mkdir error");
            throw new FileAlreadyExistsException("mkdir error");
        }
    }
}
