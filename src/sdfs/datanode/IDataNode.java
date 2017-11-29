/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.datanode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public interface IDataNode {

    //number of bytes in one block file
    final int blockSize = 128 * 1024;
    static final int BLOCK_SIZE = 128 * 1024;

    /**
     * Read data from a block.
     * It should be redirect to [blockNumber].block file
     *
     * @param blockNumber the block number to be read
     * @param offset      the offset on the block file
     * @param size        the total size to be read
     * @param b           the buffer to store the data
     * @return the total number of bytes read into the buffer
     * @throws IndexOutOfBoundsException if offset less than zero, or offset+size larger than block size, or buffer size is less that size given
     * @throws FileNotFoundException     if the block is free (block file not exist)
     */
    int read(int blockNumber, int offset, int size, byte b[]) throws IndexOutOfBoundsException, FileNotFoundException, IOException;

    /**
     * Write data to a block.
     * It should be redirect to [blockNumber].block file
     *
     * @param blockNumber the block number to be written
     * @param offset      the offset on the block file
     * @param size        the total size to be written
     * @param b           the buffer to store the data
     * @throws IndexOutOfBoundsException  if offset less than zero, or offset+size larger than block size, or buffer size is less that size given
     * @throws FileAlreadyExistsException if the block is not free (block file exist)
     */
    void write(int blockNumber, int offset, int size, byte b[]) throws IndexOutOfBoundsException, FileAlreadyExistsException, IOException;
}