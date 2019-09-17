package cn.com.yeexun.standardAudit.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.IBaseService;
import cn.com.yeexun.dataSource.entity.DataSource;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.user.entity.UserDto;

public interface IStandardAuditService extends IBaseService<StandardAudit> {
	
	//修改标准审核表中记录状态(0:待审核,1:通过,2:退回)
	public void updataAuditStatus(List<Long> ids, String status, String type, UserDto currentUser) throws Exception;
	

	public List<StandardAudit> listStandardAuditPage(String status,String auditresult,
			String type, String taskName, Page<StandardAudit> page) throws Exception;
	
	/**
	 * 根据任务id及类型查找审核对象
	 * @param type 任务类型0：数据元 1：数据集 2：代码
	 * @param flowId 任务id
	 * @return
	 * @throws Exception
	 */
	public StandardAudit getByFlowId(String type, Long flowId) throws Exception;


	StandardAudit changeDetail(Long id, String type);



}
