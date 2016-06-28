package mybatis;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * mybatis反向工具，可以使用此生成所需代码。
 * 
 */
public class MyBatisGeneratorTool {

	public static void main(String[] args) {
		String path=MyBatisGeneratorTool.class.getResource("").getPath()+"MybatisGenerator.xml";
        System.out.println("path -- "+path);  
        
		try {
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			
			File configFile = new File(path);
			Configuration config = cp.parseConfiguration(configFile);
			
			boolean overwrite = true;
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLParserException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}