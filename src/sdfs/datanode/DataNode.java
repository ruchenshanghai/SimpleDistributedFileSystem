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
        return 0;

    }

    @Override
    public void write(int blockNumber, int offset, int size, byte[] b) throws IndexOutOfBoundsException, FileAlreadyExistsException, IOException {
        File tempFile = new File(RELATIVE_PATH + "/" + blockNumber + ".block");
        BufferedOutputStream tempOut = new BufferedOutputStream(new FileOutputStream(tempFile));
        if (!tempFile.isFile()) {
            System.out.println("write to not exist: " + blockNumber + ".block" + " error");
            return;
        }
        if (offset > 0) {
            // get previous data
            BufferedInputStream tempIn = new BufferedInputStream(new FileInputStream(tempFile));
            byte[] tempInArray = new byte[offset];
            int tempReadCount = tempIn.read(tempInArray, 0, offset);
            System.out.println("get previous count: " + tempReadCount);
            tempOut.write(tempInArray, 0, offset);
        }
        if (offset + size <= BLOCK_SIZE) {
            tempOut.write(b, 0, size);
        }
        tempOut.flush();
        tempOut.close();
    }

    private static int currentBlockIndex() {
        File tempDirectory = new File(RELATIVE_PATH);
        return tempDirectory.list().length + 1;
    }

    public static int createNewBlock() throws IOException {
        int tempBlockID = currentBlockIndex();
        File tempNewFile = new File(RELATIVE_PATH + "/" + tempBlockID + ".block");
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate block file: " + tempBlockID + ".block");
            tempNewFile.delete();
            return 0;
        }
        System.out.println("create block: " + tempBlockID + ".block");
        return tempBlockID;
    }
}
