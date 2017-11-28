package sdfs.client;

import sdfs.utils.SDFSInputStream;
import sdfs.utils.SDFSOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class Client implements ISimpleDistributedFileSystem {
    @Override
    public SDFSInputStream open(String fileUri) throws FileNotFoundException, IOException {
        return null;
    }

    @Override
    public SDFSOutputStream create(String fileUri) throws FileAlreadyExistsException, IOException {
        return null;
    }

    @Override
    public void mkdir(String fileUri) throws FileAlreadyExistsException, IOException {

    }
}
