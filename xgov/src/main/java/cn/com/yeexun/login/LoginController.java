package cn.com.yeexun.login;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.yeexun.XgovApplication;
import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.utils.CodeUtil;
import cn.com.yeexun.utils.FileUtil;

@Controller
public class LoginController {
	
	@ResponseBody
	@RequestMapping(value = "loginV1", method = RequestMethod.POST)
	public String login(String loginName, String loginPwd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String driver = XgovApplication.env.getProperty("spring.datasource.driver-class-name");
		String url = XgovApplication.env.getProperty("spring.datasource.url");
		String username = XgovApplication.env.getProperty("spring.datasource.username");
		String pwd = XgovApplication.env.getProperty("spring.datasource.password");
		Connection connection = JdbcHelper.getConnection(driver, url, username, pwd);
		String sql = "select login_name, login_pwd from security_user where login_name = '" + loginName + "'";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		if (rs.next()) {
//			String name = rs.getString("login_name");
			String userpwd = rs.getString("login_pwd");
			if (loginPwd.equals(userpwd)) {
				return FileUtils.readFileToString(new File(FileUtil.getJarRootDir() 
						+ "/import-template/login.json"), "UTF-8");
			} else {
				map = CodeUtil.getSystemErrorMap();
				map.put(CodeUtil.MESSAGE_TEXT, "密码错误！");
				return JSON.toJSONString(map);
			}
		} else {
			map = CodeUtil.getSystemErrorMap();
			map.put(CodeUtil.MESSAGE_TEXT, "用户名不存在！");
			return JSON.toJSONString(map);
		}
	}

}
 