/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.NamingException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JndiLookupFailureException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public JndiLookupFailureException(String msg, NamingException cause)
/*    */   {
/* 42 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiLookupFailureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */