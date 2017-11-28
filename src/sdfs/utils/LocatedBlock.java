/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.utils;

import java.io.Serializable;
import java.net.InetAddress;

public class LocatedBlock implements Serializable {
    private static final long serialVersionUID = -6509598325324530684L;
    public final InetAddress inetAddress;
    public final int blockNumber;

    public LocatedBlock(InetAddress inetAddress, int blockNumber) {
        this.inetAddress = inetAddress;
        this.blockNumber = blockNumber;
    }
}