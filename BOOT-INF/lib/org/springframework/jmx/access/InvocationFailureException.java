/*    */ package org.springframework.jmx.access;
/*    */ 
/*    */ import org.springframework.jmx.JmxException;
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
/*    */ public class InvocationFailureException
/*    */   extends JmxException
/*    */ {
/*    */   public InvocationFailureException(String msg)
/*    */   {
/* 38 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InvocationFailureException(String msg, Throwable cause)
/*    */   {
/* 48 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\access\InvocationFailureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */