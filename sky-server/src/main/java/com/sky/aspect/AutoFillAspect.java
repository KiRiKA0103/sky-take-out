package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;


// 自定义切面,实现公共字段自动填充处理逻辑
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    // 切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut(){}

    // 前置通知,进行公共字段赋值
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) throws Exception {

        // 获取到当前被拦截方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获取方法上的注解对象
        OperationType operationType = autoFill.value();
        // 获取到当前被拦截方法的参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        // 准备赋值的数据
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        // 为对应属性通过反射赋值
        if(operationType == OperationType.INSERT){
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            // 通过反射为对象属性赋值
            setCreateTime.invoke(entity,time);
            setCreateUser.invoke(entity,id);
            setUpdateTime.invoke(entity,time);
            setUpdateUser.invoke(entity,id);
        }else if(operationType == OperationType.UPDATE){
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            // 通过反射为对象属性赋值
            setUpdateTime.invoke(entity,time);
            setUpdateUser.invoke(entity,id);
        }

    }
}
