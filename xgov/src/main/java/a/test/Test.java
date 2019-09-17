package a.test;

import java.io.File;

public class Test {
	
	/**
	 * 目录重命名
	 * @param args
	 */
	public static void main(String[] args) {
		
		File originFile = new File("E:\\aa");
		originFile.renameTo(new File("E:\\aabb"));
	}

}
