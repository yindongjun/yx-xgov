package cn.com.yeexun.login;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;



public class User {

	public static final String ADMIN = "admin";

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6682859448649865369L;
	public static final int STATE_NORMAL = 1;
	public static final int STATE_LOCK = 2;
	
	private Long id;

	private String nickName;
	
	private String email;
	
	private String tel;
	
	@Column(name="login_name")
	private String loginName;

	private String loginPwd;

	private String salt;
	
	private String orgId;
	
	private	Date lastLoginTime;
	
	private long FristErrorTime;

	private Integer status;

	private String homePage;
	
	private String department;
	
	private String type;
	
	private String description;
	
	private int loginErrorNum;
	
	@Transient
	private String loginIp;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}


	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public  Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}


	public static String getAdmin() {
		return ADMIN;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getStateNormal() {
		return STATE_NORMAL;
	}

	public static int getStateLock() {
		return STATE_LOCK;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public int getLoginErrorNum() {
		return loginErrorNum;
	}

	public void setLoginErrorNum(int loginErrorNum) {
		this.loginErrorNum = loginErrorNum;
	}

	public long getFristErrorTime() {
		return FristErrorTime;
	}

	public void setFristErrorTime(long fristErrorTime) {
		FristErrorTime = fristErrorTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
