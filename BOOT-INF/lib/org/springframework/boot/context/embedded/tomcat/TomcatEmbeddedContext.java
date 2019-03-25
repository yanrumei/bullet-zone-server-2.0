/*    */ package org.springframework.boot.context.embedded.tomcat;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.catalina.Container;
/*    */ import org.apache.catalina.Loader;
/*    */ import org.apache.catalina.Manager;
/*    */ import org.apache.catalina.core.StandardContext;
/*    */ import org.apache.catalina.session.ManagerBase;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class TomcatEmbeddedContext
/*    */   extends StandardContext
/*    */ {
/*    */   private TomcatStarter starter;
/*    */   private final boolean overrideLoadOnStart;
/*    */   
/*    */   TomcatEmbeddedContext()
/*    */   {
/* 43 */     this.overrideLoadOnStart = (ReflectionUtils.findMethod(StandardContext.class, "loadOnStartup", new Class[] { Container[].class }).getReturnType() == Boolean.TYPE);
/*    */   }
/*    */   
/*    */   public boolean loadOnStartup(Container[] children)
/*    */   {
/* 48 */     if (this.overrideLoadOnStart) {
/* 49 */       return true;
/*    */     }
/* 51 */     return super.loadOnStartup(children);
/*    */   }
/*    */   
/*    */   public void setManager(Manager manager)
/*    */   {
/* 56 */     if ((manager instanceof ManagerBase)) {
/* 57 */       ((ManagerBase)manager).setSessionIdGenerator(new LazySessionIdGenerator());
/*    */     }
/* 59 */     super.setManager(manager);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void deferredLoadOnStartup()
/*    */   {
/* 68 */     ClassLoader classLoader = getLoader().getClassLoader();
/* 69 */     ClassLoader existingLoader = null;
/* 70 */     if (classLoader != null) {
/* 71 */       existingLoader = ClassUtils.overrideThreadContextClassLoader(classLoader);
/*    */     }
/*    */     
/* 74 */     if (this.overrideLoadOnStart)
/*    */     {
/*    */ 
/*    */ 
/* 78 */       super.loadOnStartup(findChildren());
/*    */     }
/* 80 */     if (existingLoader != null) {
/* 81 */       ClassUtils.overrideThreadContextClassLoader(existingLoader);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setStarter(TomcatStarter starter) {
/* 86 */     this.starter = starter;
/*    */   }
/*    */   
/*    */   public TomcatStarter getStarter() {
/* 90 */     return this.starter;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatEmbeddedContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */