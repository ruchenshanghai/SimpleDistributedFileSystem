package sdfs.datanode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private static final int BLOCK_SIZE = 128*1024;

    public DataNode() {
        System.out.println("initial data node success!");
    }

    @Override
    public int read(int blockNumber, int offset, int size, byte[] b) throws IndexOutOfBoundsException, FileNotFoundException, IOException {
        return 0;
    }

    @Override
    public void write(int blockNumber, int offset, int size, byte[] b) throws IndexOutOfBoundsException, FileAlreadyExistsException, IOException {

    }

    public static int currentBlockIndex() {
        File tempDirectory = new File(RELATIVE_PATH);
        return tempDirectory.list().length + 1;
    }

    public boolean createNewBlock() throws IOException {
        int tempBlockID = currentBlockIndex();
        File tempNewFile = new File(RELATIVE_PATH + "/" + tempBlockID + ".block");
        if (!tempNewFile.createNewFile()) {
            System.out.println("duplicate block file: " + tempBlockID + ".block");
            tempNewFile.delete();
            return false;
        }
        return true;
    }
}
