package cn.com.yeexun.log.dao;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.log.entity.ExecErrLog;

public interface IErrLogDao extends BaseDao<ExecErrLog>{
	
	public ExecErrLog getByEtlFlowId(@Param("etlFlowId")Long etlFlowId, @Param("type")String type) throws Exception;
}
