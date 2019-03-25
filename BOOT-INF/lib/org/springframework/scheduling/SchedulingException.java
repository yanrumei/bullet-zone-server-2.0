/*    */ package org.springframework.scheduling;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class SchedulingException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public SchedulingException(String msg)
/*    */   {
/* 37 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SchedulingException(String msg, Throwable cause)
/*    */   {
/* 47 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\SchedulingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */