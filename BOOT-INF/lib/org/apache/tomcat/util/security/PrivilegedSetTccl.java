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
/*    */ public class PrivilegedSetTccl
/*    */   implements PrivilegedAction<Void>
/*    */ {
/*    */   private final ClassLoader cl;
/*    */   private final Thread t;
/*    */   
/*    */   public PrivilegedSetTccl(ClassLoader cl)
/*    */   {
/* 27 */     this(Thread.currentThread(), cl);
/*    */   }
/*    */   
/*    */   public PrivilegedSetTccl(Thread t, ClassLoader cl) {
/* 31 */     this.t = t;
/* 32 */     this.cl = cl;
/*    */   }
/*    */   
/*    */ 
/*    */   public Void run()
/*    */   {
/* 38 */     this.t.setContextClassLoader(this.cl);
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\security\PrivilegedSetTccl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */