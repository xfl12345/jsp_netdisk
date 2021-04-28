package pers.xfl.jsp_netdisk.model;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.ext.FileBasedTimestampSynchronizer;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.io.IOException;

public class PreLoad {
    static {
        Generators.timeBasedGenerator();
        System.out.println("PreLoad loaded.");
    }

    /*
    public static EthernetAddress ethernetAddress = EthernetAddress.fromInterface();
    public static FileBasedTimestampSynchronizer fileBasedTimestampSynchronizer;
    public static TimeBasedGenerator timeBasedGenerator;

    static {
        try {
            fileBasedTimestampSynchronizer = new FileBasedTimestampSynchronizer();
            timeBasedGenerator = Generators.timeBasedGenerator(ethernetAddress, fileBasedTimestampSynchronizer);
        } catch (IOException e) {
            e.printStackTrace();
            timeBasedGenerator = Generators.timeBasedGenerator(ethernetAddress);
            System.out.println("fileBasedTimestampSynchronizer initialize failed.Feature disabled.");
        }
    }
     */

}
