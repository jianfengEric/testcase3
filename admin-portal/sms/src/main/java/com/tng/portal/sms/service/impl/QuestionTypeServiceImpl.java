package com.tng.portal.sms.service.impl;


import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.sms.constant.SystemMsg;
import com.tng.portal.sms.entity.QuestionType;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.repository.QuestionRepository;
import com.tng.portal.sms.repository.QuestionTypeRepository;
import com.tng.portal.sms.service.QuestionTypeService;
import com.tng.portal.sms.vo.QuestionTypeVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionTypeServiceImpl implements QuestionTypeService{
    @Autowired
	private QuestionTypeRepository questionTypeRepository;
    
    @Autowired
	private QuestionRepository questionRepository;
    
    @Autowired
    private AnaAccountRepository anaAccountRepository;
    
    @Qualifier("anaUserService")
	@Autowired
	private UserService userService;
    
	@Override
	@Transactional
	public PageDatas<QuestionTypeVo> getQuestionTypesByPage(Integer pageNo, Integer pageSize, String nameSearch, String creatorIdSearch) {
		AnaAccount account = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		String mid = account.getExternalGroupId();
		List<QuestionType> questionTypes = null;
		Page<QuestionType> page = null;
		PageDatas<QuestionTypeVo> pageDatas = null;
		if(pageNo==null || pageSize == null){
			questionTypes = questionTypeRepository.findAll();
			pageDatas = new PageDatas<>();
		}else{
			pageDatas = new PageDatas<>(pageNo, pageSize);
			Specification<QuestionType> querySpecifi = new Specification<QuestionType>()
			{
				@Override
				public Predicate toPredicate(Root<QuestionType> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb)
				{
					List<Predicate> predicates = new ArrayList<>();
					if (StringUtils.isNotBlank(nameSearch))
					{
						predicates.add(cb.like(root.get("name").as(String.class), "%" + nameSearch + "%"));
					}
					if (StringUtils.isNotBlank(mid))
					{
						predicates.add(cb.equal(root.get("mid").as(String.class), mid));
					}else
					{
						predicates.add(cb.isNull(root.get("mid")));
					}
					if (StringUtils.isNotBlank(creatorIdSearch))
					{
						Join<QuestionType, AnaAccount> join = root.join("anaAccount", JoinType.LEFT);
						predicates.equals(cb.like(join.get("id").as(String.class), creatorIdSearch));
					}

					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			page = questionTypeRepository.findAll(querySpecifi, pageDatas.pageRequest());
			questionTypes = page.getContent();

		}

		List<QuestionTypeVo> questionTypeVos = questionTypes.stream().map(item -> {
			QuestionTypeVo vo = new QuestionTypeVo();
			vo.setId(item.getId());
			vo.setName(item.getName());
			vo.setCreatorName(item.getAnaAccount().getFullName());
			vo.setQuestionCount(item.getQuestionTemplates().size());
			return vo;
		}).collect(Collectors.toList());
		if(pageNo==null || pageSize == null){
			pageDatas.setList(questionTypeVos);
		}else{
			pageDatas.initDatas(questionTypeVos, page.getTotalElements(), page.getTotalPages());
		}




        return pageDatas;
	}

	/**
     * Delete question type by id
     * 
     * @param id
     * 			question type id
     * 
     * @return
     */
	@Override
	@Transactional
	public Long deleteQuestionType(Long id) {
		QuestionType type = this.questionTypeRepository.findOne(id);
		if(type == null)		
			throw new BusinessException(SystemMsg.ServerErrorMsg.NOT_EXISTS_QUESTION_TYPE.getErrorCode());
		
		this.questionTypeRepository.delete(type);
		return id;
	}

	/**
	 * update question type
	 * 
	 * @param type 
	 * 			question type info
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public Long updateQuestionType(QuestionType type){
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		String mid = anaAccount.getExternalGroupId();
		QuestionType conflictQt = questionTypeRepository.findByNameAndMidAndIdNot(type.getName(), mid, type.getId());
		if(conflictQt != null)
			throw new BusinessException(SystemMsg.ServerErrorMsg.QUESTION_TYPE_EXISTS_ERROR.getErrorCode());
		
		QuestionType qt = questionTypeRepository.findOne(type.getId());
		qt.setName(type.getName());
		return questionTypeRepository.save(qt).getId();
	}
	
	/**
	 * add question type
	 * 
	 * @param type 
	 * 			question type info
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public Long addQuestionType(QuestionType type){
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		String mid = anaAccount.getExternalGroupId();
		QuestionType qt = questionTypeRepository.findByNameAndMid(type.getName(), mid);
		if(qt != null)
			throw new BusinessException(SystemMsg.ServerErrorMsg.QUESTION_TYPE_EXISTS_ERROR.getErrorCode());

		type.setAnaAccount(anaAccount);
		type.setMid(anaAccount.getExternalGroupId());
		return this.questionTypeRepository.save(type).getId();
	}
	
	@Override
	@Transactional
	public List<QuestionType> getQuestionTypes() {
		List<QuestionType> qTypes = questionTypeRepository.findAll();
		
		if(qTypes != null && !qTypes.isEmpty()){
			//remove question type without following
			qTypes = qTypes.stream().filter(item -> item.getQuestionTemplates() !=null && item.getQuestionTemplates().size() != 0).collect(Collectors.toList());
		}
		
		return qTypes;
	}

}