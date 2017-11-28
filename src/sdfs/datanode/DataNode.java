package sdfs.datanode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class DataNode implements IDataNode {
    private static final String relativePath = "./data/block";
    static {
        File tempBlockDir = new File(relativePath);
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

    }
}
