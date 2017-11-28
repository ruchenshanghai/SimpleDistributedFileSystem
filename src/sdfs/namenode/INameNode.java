/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.namenode;

import sdfs.utils.LocatedBlock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;

public interface INameNode {
    /**
     * Open a file that is already exist.
     *
     * @param fileUri The file uri to be open
     * @return The file node represent the file
     * @throws FileNotFoundException if the file is not exist
     */
    FileNode open(String fileUri) throws IOException, URISyntaxException;

    /**
     * Create a empty file.
     *
     * @param fileUri The file uri to be create
     * @return The file node represent the file. It should occupy none blocks.
     * @throws FileAlreadyExistsException if the file is already exist
     */
    FileNode create(String fileUri) throws IOException, URISyntaxException;

    /**
     * Close a file. If file metadata is changed, store them on the disk.
     *
     * @param fileUri file to be closed
     */
    void close(String fileUri) throws IOException, URISyntaxException;

    /**
     * Make a directory on given file uri.
     *
     * @param fileUri the directory path
     * @throws FileAlreadyExistsException if directory or file is already exist
     */
    void mkdir(String fileUri) throws IOException, URISyntaxException;

    /**
     * Request a new free block for a file
     * No metadata should be written to disk until it is correctly close
     *
     * @param fileUri the file that request the new block
     * @return a block that is free and could be used by client
     */
    LocatedBlock addBlock(String fileUri);

}
