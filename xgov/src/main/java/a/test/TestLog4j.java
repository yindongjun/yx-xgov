package a.test;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog4j {
	
	static Logger logger = LoggerFactory.getLogger(TestLog4j.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("D:\\git_repository\\xgov-new\\xgov\\"
				+ "DataTools\\tools.manage\\src\\main\\java\\cn\\com\\yeexun\\"
				+ "dispatch\\quzrtz\\test\\log4j.properties");
		System.setProperty("logFileName", "log1");
		logger.debug("hello log4j");
		logger.info("info");
		
	}

}
