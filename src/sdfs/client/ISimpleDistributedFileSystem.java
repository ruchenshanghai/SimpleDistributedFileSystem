/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.client;

import sdfs.utils.SDFSInputStream;
import sdfs.utils.SDFSOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * In the future, when create a SimpleDistributedFileSystem, it should accept a string likes sdfs://[ip]:[port]/
 * For now, ip is always 127.0.0.1 and port is omitted.
 * You could decide to support it now or in the future lab.
 */
public interface ISimpleDistributedFileSystem {
    /**
     * Open a file that is already exist.
     *
     * @param fileUri The file uri to be open. The fileUri should look like /foo/bar.data which is a request to sdfs://[ip]:[port]/foo/bar.data
     * @return The input stream to this file
     * @throws FileNotFoundException if the file is not exist
     */
    SDFSInputStream open(String fileUri) throws FileNotFoundException, IOException;

    /**
     * Create a empty file and return the output stream to this file.
     *
     * @param fileUri The file uri to be create. The fileUri should look like /foo/bar.data which is a request to sdfs://[ip]:[port]/foo/bar.data
     * @return output stream to this file
     * @throws FileAlreadyExistsException if the file is already exist
     */
    SDFSOutputStream create(String fileUri) throws FileAlreadyExistsException, IOException;

    /**
     * Make a directory on given file uri.
     *
     * @param fileUri the directory path
     * @throws FileAlreadyExistsException if directory or file is already exist
     */
    void mkdir(String fileUri) throws FileAlreadyExistsException, IOException;
}
