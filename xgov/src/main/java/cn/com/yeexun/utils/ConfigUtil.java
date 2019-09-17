package cn.com.yeexun.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigUtil {

//	private static final String CongifName = "config.properties";//配置文件的名字
//	private Properties props;
	
	// 单例
	private static ConfigUtil instance;

	private ConfigUtil() {
//		this.loadProFile();
	}

	public static synchronized ConfigUtil getIntance() {
		if (null == instance) {
			instance = new ConfigUtil();
		}
		return instance;
	}

//	/**
//	 * 加载配置文件 
//	 */
//	private void loadProFile() {
//		this.props = new Properties();
//		try {
//			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CongifName);
//			props.load(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public String getValue(String key){
		if (PropertyPlaceholder.isContainsKey(key)) {
			return PropertyPlaceholder.getContextProperty(key);
		}
		return null;
	}
	
	
	public List<String> getListFromArray(String key){
		List<String> list = new ArrayList<String>();
		String line = getValue(key);
		if (null == line || "".equals(line)) {
			return list;
		}
		String[] array = null;
		if(line.contains(",")){
			array = line.split(",");
		}else {
			array = new String[]{line};
		}
		for(int i=0; i<array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public Set<String> getSetFromArray(String key){
		Set<String> set = new HashSet<String>();
		String line = getValue(key);
		if (null == line || "".equals(line)) {
			return set;
		}
		String[] array = null;
		if(line.contains(",")){
			array = line.split(",");
		}else {
			array = new String[]{line};
		}
		for(int i=0; i<array.length; i++) {
			set.add(array[i]);
		}
		return set;
	}
	
}
