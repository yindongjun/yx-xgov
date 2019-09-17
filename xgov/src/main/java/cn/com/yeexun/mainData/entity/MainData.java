package cn.com.yeexun.mainData.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "main_data")
public class MainData extends Unique{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据源名称
	 */
	private String dataName;
	
	/**
	 * 数据源id
	 */
	private Long dataSourceId;
	
	/**
	 * 创建数据源的时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 表数量
	 */
	@Transient
	private Long tableNumber;
	
	/**
	 * 数据库类型
	 */
	private String serverAddress;
	
	/**
	 * 创建的用户id
	 */
	private Long createUser;
	
	/**
	 * 修改主数据的用户id
	 */
	private Long updateUser;
	
	/**
	 * 删除标志位
	 */
	private Integer isdel;

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public Long getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(Long tableNumber) {
		this.tableNumber = tableNumber;
	}



	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Integer getIsdel() {
		return isdel;
	}

	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}


	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public MainData() {}

	public MainData(String dataName) {
		this.dataName = dataName;
	}

	/**
	 * @param dataName
	 * @param dataSourceId
	 * @param createTime
	 * @param updateTime
	 * @param tableNumber
	 * @param serverAddress
	 * @param createUser
	 * @param updateUser
	 * @param isdel
	 */
	public MainData(String dataName, Long dataSourceId, Date createTime, Date updateTime, Long tableNumber,
			String serverAddress, Long createUser, Long updateUser, Integer isdel) {
		super();
		this.dataName = dataName;
		this.dataSourceId = dataSourceId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.tableNumber = tableNumber;
		this.serverAddress = serverAddress;
		this.createUser = createUser;
		this.updateUser = updateUser;
		this.isdel = isdel;
	};
	
}
