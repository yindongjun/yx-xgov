package cn.com.yeexun.user.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class UserDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	 
	
	

}
