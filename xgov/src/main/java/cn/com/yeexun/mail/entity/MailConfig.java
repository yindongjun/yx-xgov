package cn.com.yeexun.mail.entity;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "mail_config")
public class MailConfig extends Unique {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String host;
	
	private Integer port;
	
	private String username;
	
	private String password;
	
	private String protocol;
	
	private String enableSsl;
	
	public MailConfig() {
		super();
	}

	public MailConfig(String host, Integer port, String username, String password, String protocol, String enableSsl) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.protocol = protocol;
		this.enableSsl = enableSsl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getEnableSsl() {
		return enableSsl;
	}

	public void setEnableSsl(String enableSsl) {
		this.enableSsl = enableSsl;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
