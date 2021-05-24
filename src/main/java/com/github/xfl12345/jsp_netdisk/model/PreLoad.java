package com.github.xfl12345.jsp_netdisk.model;

import com.fasterxml.uuid.Generators;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class PreLoad {

    public PreLoad() throws IOException {
        Logger logger = LoggerFactory.getLogger(PreLoad.class);
        Generators.timeBasedGenerator();
        logger.info("PreLoad loaded.");
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
            logger("fileBasedTimestampSynchronizer initialize failed.Feature disabled.");
        }
    }
     */

}
