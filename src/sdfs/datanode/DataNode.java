package sdfs.datanode;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

public class DataNode implements IDataNode {
    private static final String RELATIVE_PATH = "./data/block";

    static {
        File tempBlockDir = new File(RELATIVE_PATH);
        if (!tempBlockDir.isDirectory()) {
            // need create directory
            boolean createRes = tempBlockDir.mkdirs();
            System.out.println("create block directory: " + createRes);
        }
    }

    public DataNode() {
        System.out.println("initial data node success!");
    }

    @Override
    public int read(int blockNumber, int offset, int size, byte[] b) throws IndexOutOfBoundsException, FileNotFoundException, IOException {
        File tempFile = new File(RELATIVE_PATH + "/" + blockNumber + ".block");
        if (!tempFile.isFile()) {
            System.out.println("write to not exist: " + blockNumber + ".block" + " error");
            throw new FileNotFoundException();
        }
        if (offset < 0 || offset >= BLOCK_SIZE || b.length < size || offset + size > BLOCK_SIZE) {
            throw new IndexOutOfBoundsException();
        }

        BufferedInputStream tempIn = new BufferedInputStream(new FileInputStream(tempFile));
        for (int i = 0; i < offset; i++) {
            if (tempIn.read() == -1) {
                throw new IOException();
            }
        }

        int readCount = 0;
        int tempInt;
        for (int i = 0; i < size && i < b.length; i++) {
            if ((tempInt = tempIn.read()) == -1) {
                throw new IOException();
            }
            b[i] = (byte) tempInt;
            readCount++;
        }
        return readCount;
    }


    @Override
    public void write(int blockNumber, int offset, int size, byte[] b) throws IndexOutOfBoundsException, FileAlreadyExistsException, IOException {
        File tempFile = new File(RELATIVE_PATH + "/" + blockNumber + ".block");
        if (!tempFile.isFile()) {
            System.out.println("write to not exist: " + blockNumber + ".block" + " error");
            return;
        }
        if (offset < 0 || offset >= BLOCK_SIZE || b.length < size || offset + size > BLOCK_SIZE) {
            throw new IndexOutOfBoundsException();
        }

        BufferedOutputStream tempOut = new BufferedOutputStream(new FileOutputStream(tempFile));
        BufferedInputStream tempIn = new BufferedInputStream(new FileInputStream(tempFile));
        byte[] tempInArray = new byte[offset];
        int tempReadCount = tempIn.read(tempInArray, 0, offset);
        System.out.println("get previous count: " + tempReadCount);
        tempOut.write(tempInArray, 0, offset);
        tempOut.write(b, 0, size);
        tempOut.flush();
        tempOut.close();
    }

    private static int generateBlockIndex() {
        File tempDirectory = new File(RELATIVE_PATH);
        return tempDirectory.list().length + 1;
    }

    public static int createNewBlock() throws IOException {
        int tempBlockID = generateBlockIndex();
        File tempNewFile = new File(RELATIVE_PATH + "/" + tempBlockID + ".block");
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate block file: " + tempBlockID + ".block");
            return -1;
        }
        System.out.println("create block: " + tempBlockID + ".block");
        return tempBlockID;
    }
}
