package cn.com.yeexun.qualityTask.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.common.ssm.engine.mapper.BaseDao;
import cn.com.yeexun.qualityTask.entity.DesignTableInfo;
import cn.com.yeexun.qualityTask.entity.QualityTableCountVo;
import cn.com.yeexun.qualityTask.entity.QualityTaskDetail;

public interface IDesignTableInfoDao extends BaseDao<DesignTableInfo>{

	List<DesignTableInfo> getTableDesignList(@Param("sourceId") long sourceId)throws Exception;

//	DesignTableInfo getByRuleId(@Param("ruleId")Long ruleId)throws Exception;

//	List<DesignTableInfo> getChangeTableInfo(@Param("designSourceId")Long designSourceId)throws Exception;
	
//	List<DesignTableInfo> getByDesignSourceId(@Param("designSourceId")Long designSourceId)throws Exception;
	
//	void deleteTableInfo(@Param("sourceInfoId") long id)throws Exception;
	
	DesignTableInfo getDesignTableInfo(@Param("id") Long id) throws Exception;

//	DesignTableInfo findBySourceId(Long sourceId);

	void deleteQualityTasks(String tableDesignIds);

	DesignTableInfo getBySourceAndTable(@Param("datasourceId") Long datasourceId
			, @Param("name") String name);


	List<QualityTableCountVo> countQualityTable();

	List<DesignTableInfo> findByIds(@Param("ids") String ids);

	List<DesignTableInfo> listReportTables();

	List<QualityTaskDetail> getQualityTaskIdBySourceId(Long sourceId);

//	void deleteQualityTasksBySourceId(Long sourceId);

//	DesignTableInfo getBySourceAndTableName(@Param("designSourceId")long id, @Param("tableName")String tableName);

}
