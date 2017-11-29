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
        SDFSInputStream sdfsIn = new SDFSInputStream(fileUri);
        return sdfsIn;
    }

    @Override
    public SDFSOutputStream create(String fileUri) throws FileAlreadyExistsException, IOException {
        SDFSOutputStream sdfsOut = new SDFSOutputStream(fileUri);
        return sdfsOut;
    }

    @Override
    public void mkdir(String fileUri) throws FileAlreadyExistsException, IOException {
        try {
            NameNode nameNode = new NameNode();
            nameNode.mkdir(fileUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("mkdir error");
        }
    }
}
