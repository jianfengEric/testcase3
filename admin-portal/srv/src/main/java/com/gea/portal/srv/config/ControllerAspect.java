package com.gea.portal.srv.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Around("execution(* com.gea.portal.srv.controller.*.*(..))")
	public Object handleLog(ProceedingJoinPoint thisJoinPoint) throws Throwable {

		StringBuilder sb = new StringBuilder();
		
		sb.append("[").append(thisJoinPoint.getSignature().toShortString()).append("]");

		Object[] args = thisJoinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getStaticPart().getSignature();
		String[] parameterNames = methodSignature.getParameterNames();
		assert args.length == parameterNames.length;
		boolean isFirst = true;
		
		StringBuilder parametersLog = new StringBuilder(); 
		parametersLog.append("{");
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			if(isFirst){
				isFirst = !isFirst;
			}else{
				parametersLog.append(", ");
			}
			parametersLog.append(parameterNames[argIndex]).append(":").append(args[argIndex]==null?"null":args[argIndex].toString());
		}
		
		parametersLog.append("}");
		String logStr = parametersLog.toString();

		logger.info("{} Parameter Data: {}", sb, logStr);
		return thisJoinPoint.proceed();
	}
}