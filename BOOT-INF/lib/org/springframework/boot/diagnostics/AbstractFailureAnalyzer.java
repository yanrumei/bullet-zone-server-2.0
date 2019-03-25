/*    */ package org.springframework.boot.diagnostics;
/*    */ 
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ public abstract class AbstractFailureAnalyzer<T extends Throwable>
/*    */   implements FailureAnalyzer
/*    */ {
/*    */   public FailureAnalysis analyze(Throwable failure)
/*    */   {
/* 34 */     T cause = findCause(failure, getCauseType());
/* 35 */     if (cause != null) {
/* 36 */       return analyze(failure, cause);
/*    */     }
/* 38 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract FailureAnalysis analyze(Throwable paramThrowable, T paramT);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Class<? extends T> getCauseType()
/*    */   {
/* 57 */     return 
/* 58 */       ResolvableType.forClass(AbstractFailureAnalyzer.class, getClass()).resolveGeneric(new int[0]);
/*    */   }
/*    */   
/*    */   protected final <E extends Throwable> T findCause(Throwable failure, Class<E> type)
/*    */   {
/* 63 */     while (failure != null) {
/* 64 */       if (type.isInstance(failure)) {
/* 65 */         return failure;
/*    */       }
/* 67 */       failure = failure.getCause();
/*    */     }
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\AbstractFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */