package com.tng.portal.sms.service.impl;


import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.sms.constant.SystemMsg;
import com.tng.portal.sms.entity.QuestionAnswer;
import com.tng.portal.sms.entity.QuestionAnswerPk;
import com.tng.portal.sms.entity.QuestionTemplate;
import com.tng.portal.sms.entity.QuestionType;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.repository.QuestionAnswerRepository;
import com.tng.portal.sms.repository.QuestionRepository;
import com.tng.portal.sms.repository.QuestionTypeRepository;
import com.tng.portal.sms.service.QuestionService;
import com.tng.portal.sms.vo.QuestionTemplateDto;
import com.tng.portal.sms.vo.QuestionTemplateVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{
    @Autowired
	private QuestionRepository questionRepository;
    
    @Autowired
	private QuestionTypeRepository questionTypeRepository;
    
    @Autowired
	private QuestionAnswerRepository questionAnswerRepository;
    
    @Autowired
    private AnaAccountRepository anaAccountRepository;
    
	@Qualifier("anaUserService")
	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;
    
    /**
	 * Query question template list 
	 * 
	 * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public PageDatas<QuestionTemplateDto> getQuestionsByPage(Integer pageNo, Integer pageSize, String questionTypeIdSearch, String questionSearch, String language, String creatorIdSearch, String sortBy, boolean isAscending) {
		PageDatas<QuestionTemplateDto> pageDatas = new PageDatas<>(pageNo, pageSize);
		Sort sort = new Sort(Direction.ASC, "id");
		if(sortBy != null){
			switch(sortBy){
			case "creatorName":
				sort=new Sort(!isAscending ? Direction.DESC : Direction.ASC, "anaAccount.name");
				break;
			default:
				sort=new Sort(!isAscending ? Direction.DESC : Direction.ASC, sortBy);
				break;
			}
		}
		AnaAccount account = accountService.getAccount(userService.getCurrentAccountInfo().getAccountId());
		String mid = account.getExternalGroupId();

		Specification<QuestionTemplate> querySpecifi = new Specification<QuestionTemplate>() {  
            @Override  
            public Predicate toPredicate(Root<QuestionTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(questionSearch)){
                	predicates.add(cb.like(root.get("question").as(String.class), "%" + questionSearch + "%"));
                }
                if (StringUtils.isNotBlank(mid)){
                	predicates.add(cb.equal(root.get("mid").as(String.class), mid));
                }else {
					predicates.add(cb.isNull(root.get("mid")));
				}
                if (StringUtils.isNotBlank(questionTypeIdSearch)){
                	Join<QuestionTemplate, QuestionType> join = root.join("questionTypes", JoinType.LEFT);
                	predicates.add(cb.equal(join.get("id").as(String.class), questionTypeIdSearch));
                }
                if (StringUtils.isNotBlank(creatorIdSearch)){
                	Join<QuestionTemplate, AnaAccount> join = root.join("anaAccount", JoinType.LEFT);
                	predicates.add(cb.equal(join.get("id").as(String.class), creatorIdSearch));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));  
            }  
        };  
        
        Page<QuestionTemplate> page = this.questionRepository.findAll(querySpecifi, pageDatas.pageRequest(sort));  
        List<QuestionTemplate> questionemplates = page.getContent();
		List<QuestionTemplateDto> dtos = questionemplates.stream().map(item -> {
			QuestionTemplateDto dto = new QuestionTemplateDto();
			dto.setId(item.getId());
			dto.setQuestion(item.getQuestion());
			dto.setCreatorName(item.getAnaAccount().getFullName());
			dto.setQuestionAnswers(item.getQuestionAnswers());
			dto.setQuestionTypes(item.getQuestionTypes());
			return dto;
		}).collect(Collectors.toList());
    	pageDatas.initDatas(dtos, page.getTotalElements(), page.getTotalPages());
        return pageDatas;
	}

	/**
	 * Save question template
	 * @param vo question template info
	 * @return
	 */
	@Override
	@Transactional
	public Long addQuestionTemplate(QuestionTemplateVo vo){
		QuestionTemplate qt = this.questionRepository.findByQuestion(vo.getQuestion());
		if(qt != null)
			throw new BusinessException(SystemMsg.ServerErrorMsg.QUESTION_TEMPLATE_EXISTS_ERROR.getErrorCode());
		qt = new QuestionTemplate();
		qt.setQuestion(vo.getQuestion());
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		qt.setAnaAccount(anaAccount);
		qt.setMid(anaAccount.getExternalGroupId());

		List<QuestionType> typeList = vo.getTypeIds().stream().map(item -> {
			QuestionType type = this.questionTypeRepository.findOne(item);
			return type;
		}).collect(Collectors.toList());
		
		qt.setQuestionTypes(typeList);
		Long questionId = this.questionRepository.save(qt).getId();
		
		Map<Long, String> answerMap = vo.getAnswerMap();
		if (answerMap != null) {
			Iterator<Entry<Long, String>> iterator = answerMap.entrySet().iterator();
            while (iterator.hasNext()) {
            	Entry<Long, String> entry = iterator.next();
                QuestionAnswer questionAnswer = new QuestionAnswer();
                QuestionAnswerPk pk = new QuestionAnswerPk();
                pk.setQuestionId(questionId);
                pk.setLanguageId(entry.getKey());
                questionAnswer.setQuestionAnswerPk(pk);
                questionAnswer.setAnswer(entry.getValue());
                questionAnswerRepository.save(questionAnswer);
            }
        }
		return questionId;
	}

	/**
	 * update question template
	 * @param vo
	 * @return
	 */
	@Override
	@Transactional
	public Long updateQuestion(QuestionTemplateVo vo){
		String content = vo.getQuestion();
		QuestionTemplate record = questionRepository.findTopByQuestionAndIdNot(content, vo.getId());
		if(record != null) {
			throw new BusinessException(SystemMsg.ServerErrorMsg.QUESTION_TEMPLATE_EXISTS_ERROR.getErrorCode());
		}
		
		QuestionTemplate question = questionRepository.findOne(vo.getId());
		question.setQuestion(vo.getQuestion());
		
		List<QuestionType> types = vo.getTypeIds().stream().map(tid -> {
			QuestionType type = questionTypeRepository.findOne(tid);
			return type;
		}).collect(Collectors.toList());
		question.setQuestionTypes(types);
		
		List<QuestionAnswer> ansList = question.getQuestionAnswers();
		ansList.clear();
		Map<Long, String> ansMap = vo.getAnswerMap();
		if (ansMap != null) {
			Iterator<Entry<Long, String>> iter = ansMap.entrySet().iterator();
			while (iter.hasNext()) {
            	Entry<Long, String> entry = iter.next();
            	if (entry.getKey() == null 
            			|| StringUtils.isEmpty(entry.getValue())) {
            		continue;
            	}
            	
            	QuestionAnswerPk pk = new QuestionAnswerPk();
            	pk.setLanguageId(entry.getKey());
            	pk.setQuestionId(question.getId());
            	QuestionAnswer ans = new QuestionAnswer();
            	ans.setQuestionAnswerPk(pk);
            	ans.setAnswer(entry.getValue());
            	ansList.add(ans);
			}
		}
		
		questionRepository.save(question);
		return question.getId();
	}
	
	/**
     * Delete question template by question ids
     * 
     * @param ids 
     * 			question template ids
     * 
     * @return
     */
	@Override
	@Transactional
	public String deleteQuestionTemplates(String ids) {
		if(StringUtils.isNotBlank(ids)){
			String [] arr = ids.split(",");
			if(arr != null && arr.length > 0){
				for(String id : arr){
					this.questionRepository.delete(Long.parseLong(id));
				}
			}
		}
		return ids;
	}

	/**
     * Query question template by question id
     * 
     * @param id 
     * 			question template id
     * 
     * @return
     */
	@Transactional
	public QuestionTemplateVo getQuestionTemplate(Long id) {
		QuestionTemplate question = this.questionRepository.findOne(id);

		QuestionTemplateVo questionVo = new QuestionTemplateVo();
		questionVo.setId(question.getId());
		questionVo.setQuestion(question.getQuestion());
		List<QuestionType> typeList = question.getQuestionTypes();
		List<Long> typeIdList = typeList.stream().map(item -> {
			return item.getId();
		}).collect(Collectors.toList());
		questionVo.setTypeIds(typeIdList);

		List<QuestionAnswer> answers = this.questionAnswerRepository.findByQuestionId(question.getId());
		Map<Long, String> answerMap = new HashMap<>();
		for (QuestionAnswer qa : answers) {
			answerMap.put(qa.getQuestionAnswerPk().getLanguageId(), qa.getAnswer());
		}
		questionVo.setAnswerMap(answerMap);

		return questionVo;
	}

	@Override
	@Transactional
	public List<QuestionTemplate> getQuestionByTypeId(Long typeId){
		QuestionType type = questionTypeRepository.findOne(typeId);
		if(type == null)		
			throw new BusinessException(SystemMsg.ServerErrorMsg.NOT_EXISTS_QUESTION_TYPE.getErrorCode());
		
		List<QuestionTemplate> questions = questionRepository.findByQuestionTypes(type);
		return questions.stream().map(item -> {
			QuestionTemplate resetQuestion = new QuestionTemplate();
			resetQuestion.setId(item.getId());
			resetQuestion.setQuestion(item.getQuestion());
			return resetQuestion;
		}).collect(Collectors.toList());
	}

}