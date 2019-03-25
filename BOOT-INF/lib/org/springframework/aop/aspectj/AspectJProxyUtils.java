/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.aop.Advisor;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AspectJProxyUtils
/*    */ {
/*    */   public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors)
/*    */   {
/* 44 */     if (!advisors.isEmpty()) {
/* 45 */       boolean foundAspectJAdvice = false;
/* 46 */       for (Advisor advisor : advisors)
/*    */       {
/*    */ 
/* 49 */         if (isAspectJAdvice(advisor)) {
/* 50 */           foundAspectJAdvice = true;
/*    */         }
/*    */       }
/* 53 */       if ((foundAspectJAdvice) && (!advisors.contains(ExposeInvocationInterceptor.ADVISOR))) {
/* 54 */         advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
/* 55 */         return true;
/*    */       }
/*    */     }
/* 58 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static boolean isAspectJAdvice(Advisor advisor)
/*    */   {
/* 66 */     if ((!(advisor instanceof InstantiationModelAwarePointcutAdvisor)) && 
/* 67 */       (!(advisor.getAdvice() instanceof AbstractAspectJAdvice))) if (!(advisor instanceof PointcutAdvisor)) {
/*    */         break label45;
/*    */       }
/*    */     label45:
/* 66 */     return 
/*    */     
/*    */ 
/* 69 */       (((PointcutAdvisor)advisor).getPointcut() instanceof AspectJExpressionPointcut);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\AspectJProxyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */