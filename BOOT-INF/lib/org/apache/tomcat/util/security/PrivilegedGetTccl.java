/*    */ package org.apache.tomcat.util.security;
/*    */ 
/*    */ import java.security.PrivilegedAction;
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
/*    */ public class PrivilegedGetTccl
/*    */   implements PrivilegedAction<ClassLoader>
/*    */ {
/*    */   public ClassLoader run()
/*    */   {
/* 24 */     return Thread.currentThread().getContextClassLoader();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\security\PrivilegedGetTccl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */