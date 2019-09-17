package cn.com.yeexun.mainData.entity;

import java.util.Date;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "main_data_detail")
public class MainDataDetail extends Unique{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainDataDetail() {};
//	private Long id;
	
	/**
	 * 数据源id
	 */
	private Long mainDataId;
	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 删除标志位，1:表示删除    0:未删除
	 */
	private Integer isdel;
	
	/**
	 * 创建用户
	 */
	private Long createUser;

	/**
	 * 更改用户
	 */
	private Long updateUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

//	public long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	
	public Long getMainDataId() {
		return mainDataId;
	}

	public void setMainDataId(Long mainDataId) {
		this.mainDataId = mainDataId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getIsdel() {
		return isdel;
	}

	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
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

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param id
	 * @param dataSourceId
	 * @param tableName
	 * @param isdel
	 * @param createUser
	 * @param updateUser
	 * @param createTime
	 * @param updataTime
	 */
	public MainDataDetail(Long id, Long mainDataId, String tableName, Integer isdel, Long createUser,
			Long updateUser, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.mainDataId = mainDataId;
		this.tableName = tableName;
		this.isdel = isdel;
		this.createUser = createUser;
		this.updateUser = updateUser;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

}
