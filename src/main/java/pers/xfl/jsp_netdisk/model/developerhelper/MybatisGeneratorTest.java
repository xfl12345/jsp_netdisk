package pers.xfl.jsp_netdisk.model.developerhelper;

import org.apache.ibatis.io.Resources;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorTest {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        String path = "mybatis/generator_config.xml";
        File configFile = Resources.getResourceAsFile(path);
        System.out.println(configFile.getAbsolutePath());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        //小心,这里是覆盖以前生成的,如果你有修改以前的,注意换路径生成
        boolean overwrite = true;
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("finished");
    }
}
