package cn.com.yeexun.standardAudit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.common.ssm.engine.mapper.Page;
import cn.com.common.ssm.engine.service.BaseService;
import cn.com.yeexun.businessTerms.service.ICodeService;
import cn.com.yeexun.dataElement.service.IDataElementService;
import cn.com.yeexun.dataSet.service.IDataSetService;
import cn.com.yeexun.standardAudit.dao.IStandardAuditDao;
import cn.com.yeexun.standardAudit.entity.AuditDetail;
import cn.com.yeexun.standardAudit.entity.StandardAudit;
import cn.com.yeexun.standardAudit.service.IAuditDetailService;
import cn.com.yeexun.standardAudit.service.IStandardAuditService;
import cn.com.yeexun.user.entity.UserDto;

@Service
public class StandardAuditService extends BaseService<StandardAudit> implements IStandardAuditService {

	@Autowired
	private IStandardAuditDao standardAuditDao;
	@Autowired
	private IDataElementService dataElementService;
	@Autowired
	private IDataSetService dataSetService;
	@Autowired
	private IAuditDetailService auditDetailService;
	@Autowired
	private ICodeService codeService;
	
	@Override
	public List<StandardAudit> listStandardAuditPage(String status,String auditresult,
			String type, String taskName, Page<StandardAudit> page) {
		List<StandardAudit> list = standardAuditDao.listStandardAuditPage(status,auditresult, type, taskName, page);
		return list;
	}



	@Override
	public void updataAuditStatus(List<Long> ids, String auditresult, String comment, UserDto currentUser)
			throws Exception {
		List<StandardAudit> list = this.listByIds(ids);
		List<Long> idElementList = new ArrayList<>();
		List<Long> idSetList = new ArrayList<>();
		List<Long> idCodeList = new ArrayList<>();
		if(list != null && list.size() > 0 ){
			for(StandardAudit saEntity:list){
				if(saEntity.getType().equals(StandardAudit.TYPE_DATA_ELEMENT)){
					idElementList.add(saEntity.getFlowId());
				} else if(saEntity.getType().equals(StandardAudit.TYPE_DATASET)) {
					idSetList.add(saEntity.getFlowId());
				} else if(saEntity.getType().equals(StandardAudit.TYPE_CODE)) {
					idCodeList.add(saEntity.getFlowId());
				}
				saEntity.setStatus(StandardAudit.STATUS_AUDITED);
				saEntity.setAuditResult(auditresult);
				saEntity.setAuditor(currentUser.getName());
				saEntity.setAuditTime(new Date());
				saEntity.setComment(comment);
				save(saEntity);
				
				//插入审核记录详情
				AuditDetail adEntity = new AuditDetail();
				adEntity.setAuditor(currentUser.getName());
				adEntity.setAuditTime(new Date());
				adEntity.setAuditResult(saEntity.getAuditResult());
				adEntity.setAuditDesc(saEntity.getComment());
				adEntity.setStandardAuditId(saEntity.getId());
				auditDetailService.save(adEntity);
			}
		}else {
			//new ValidationException("");
		}
		if(idElementList != null && idElementList.size() > 0){
			dataElementService.submit(idElementList, auditresult);
		}
		
		if(idSetList != null && idSetList.size() > 0){
			dataSetService.submit(idSetList, auditresult);
		}
		if(idCodeList != null && idCodeList.size() > 0){
			codeService.submit(idCodeList, auditresult);
		}
		
		
		
	}



	@Override
	public StandardAudit getByFlowId(String type, Long flowId) throws Exception {
		return standardAuditDao.getByFlowId(type, flowId);
	}



	@Override
	public StandardAudit changeDetail(Long id, String type) {
		return standardAuditDao.changeDetail(id, type);
	}

}
