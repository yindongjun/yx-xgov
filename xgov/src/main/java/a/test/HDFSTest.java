package a.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import java.io.*;

public class HDFSTest {
    /**
     * 读取文件内容
     */
    public static void cat(Configuration conf, String remoteFilePath) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        Path remotePath = new Path(remoteFilePath);
        FSDataInputStream in = fs.open(remotePath);
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        String line = null;
        int ln = 0;
        while ((line = d.readLine()) != null) {
            String[] strarray = line.split(" ");
            for (int i = 0; i < strarray.length; i++) {
                System.out.print(strarray[i]);
                System.out.print(" ");

            }

            System.out.println(++ln + "---" + " ");
        }
        d.close();
        in.close();
        fs.close();
    }

    /**
     * 主函数
     */
    public static void main(String[] args) {
        Configuration conf = new Configuration();
//        conf.set("fs.default.name", "hdfs://10.221.121.2:8020/");
//        conf.set("fs.defaultFS", "hdfs://NameNodeCluster");
        conf.set("fs.defaultFS", "hdfs://10.221.121.3:8020/");
        String remoteFilePath = "/user/hive/warehouse/hive_auto.db/card_8/000000_0"; // HDFS路径

        try {
        	long start = System.currentTimeMillis();
            System.out.println("读取文件: " + remoteFilePath);
            HDFSTest.cat(conf, remoteFilePath);
            System.out.println("\n读取完成，时间：" + (System.currentTimeMillis() - start) / 1000 + "s.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}