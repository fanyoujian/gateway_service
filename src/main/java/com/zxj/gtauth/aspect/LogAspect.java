package com.zxj.gtauth.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 切面逻辑 demo
 * 樊佑剑
 */
//@Component
//@Aspect
public class LogAspect {

    private final static Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final String POINT_CUT = "execution(public * com.zxj.signauth.filter.*.*(..))";

    @Pointcut(POINT_CUT)
    public void pointCut() {
    }

//    /**
//     * 前置通知
//     * @param jp
//     */
//    @Before(value = "pointCut()")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//        System.out.println("【前置通知】the method 【" + methodName + "】 execution with " + Arrays.asList(jp.getArgs()));
//    }

////    @After(value = POINT_CUT)
//    /**
//     * 执行后置后
//     */
////    public void doAfterAdvice(JoinPoint joinPoint) {
////        System.out.println("后置通知执行了!");
////    }

    @AfterReturning(value = POINT_CUT, returning = "result")
    /**
     * 后置通知结果返回
     */
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("第一个后置返回通知的返回值result ：" + result);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(attributes!=null)
        {
            HttpServletRequest request = attributes.getRequest();

            //获取参数
            Map param = (Map)joinPoint.getArgs()[0];

            System.out.println("第一个后置返回通知的返回值 Map ：" + param);
        }

    }

    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     *
     * @param joinPoint
     * @param exception
     */
//    @AfterThrowing(value = POINT_CUT, throwing = "exception")
//    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
//        System.out.println(joinPoint.getSignature().getName());
//        if (exception instanceof NullPointerException) {
//            System.out.println("发生了空指针异常!!!!!");
//        }
//    }



    /**
     * 环绕通知：
     * 注意:Spring AOP的环绕通知会影响到AfterThrowing通知的运行,不要同时使用
     * <p>
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
//    @Around(value = "pointCut()")
//    public Object doAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
//
//        System.out.println("第一个环绕通知result ：" + pjp);
//
////        for (Object arg :  pjp.getArgs()) {
////
////            Map param = (Map)arg;
////            System.out.println("第一个环绕通知 param ：" + param);
////        }
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//
//        if(attributes!=null)
//        {
//
//            HttpServletRequest request = attributes.getRequest();
//
//            System.out.println("第一个环绕通知 request ：" + request);
//
//            System.out.println("URL : " + request.getRequestURL().toString());
//            System.out.println("HTTP_METHOD : " + request.getMethod());
//            System.out.println("IP : " + request.getRemoteAddr());
//            System.out.println("CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
//            System.out.println("REQUEST ARGS : " + JSON.toJSONString(pjp.getArgs()));
//
//        }
//
//
//
//        long startTime = System.currentTimeMillis();
//        try {
//            Object response = pjp.proceed();
//            if(response!=null)
//            {
//                // 3.出参打印
//                System.out.println("RESPONSE:{}"+JSON.toJSONString(response));
//            }
//
//            return response;
//        } catch (Throwable e) {
//            System.out.println(e.getMessage());
//            throw e;
//        } finally {
//        }
//
//    }
}