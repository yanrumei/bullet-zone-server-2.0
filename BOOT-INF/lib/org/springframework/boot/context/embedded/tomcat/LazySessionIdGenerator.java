/*    */ package org.springframework.boot.context.embedded.tomcat;
/*    */ 
/*    */ import org.apache.catalina.LifecycleException;
/*    */ import org.apache.catalina.LifecycleState;
/*    */ import org.apache.catalina.util.StandardSessionIdGenerator;
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
/*    */ class LazySessionIdGenerator
/*    */   extends StandardSessionIdGenerator
/*    */ {
/*    */   protected void startInternal()
/*    */     throws LifecycleException
/*    */   {
/* 33 */     setState(LifecycleState.STARTING);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\LazySessionIdGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */