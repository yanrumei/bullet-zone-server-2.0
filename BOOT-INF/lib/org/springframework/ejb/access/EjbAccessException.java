/*    */ package org.springframework.ejb.access;
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
/*    */ public class EjbAccessException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public EjbAccessException(String msg)
/*    */   {
/* 35 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EjbAccessException(String msg, Throwable cause)
/*    */   {
/* 44 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\access\EjbAccessException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */