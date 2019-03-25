/*    */ package org.springframework.boot.context.embedded.tomcat;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.catalina.Context;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class TomcatErrorPage
/*    */ {
/*    */   private static final String ERROR_PAGE_CLASS = "org.apache.tomcat.util.descriptor.web.ErrorPage";
/*    */   private static final String LEGACY_ERROR_PAGE_CLASS = "org.apache.catalina.deploy.ErrorPage";
/*    */   private final String location;
/*    */   private final String exceptionType;
/*    */   private final int errorCode;
/*    */   private final Object nativePage;
/*    */   
/*    */   TomcatErrorPage(org.springframework.boot.web.servlet.ErrorPage errorPage)
/*    */   {
/* 50 */     this.location = errorPage.getPath();
/* 51 */     this.exceptionType = errorPage.getExceptionName();
/* 52 */     this.errorCode = errorPage.getStatusCode();
/* 53 */     this.nativePage = createNativePage(errorPage);
/*    */   }
/*    */   
/*    */   private Object createNativePage(org.springframework.boot.web.servlet.ErrorPage errorPage) {
/*    */     try {
/* 58 */       if (ClassUtils.isPresent("org.apache.tomcat.util.descriptor.web.ErrorPage", null)) {
/* 59 */         return BeanUtils.instantiate(ClassUtils.forName("org.apache.tomcat.util.descriptor.web.ErrorPage", null));
/*    */       }
/* 61 */       if (ClassUtils.isPresent("org.apache.catalina.deploy.ErrorPage", null)) {
/* 62 */         return 
/* 63 */           BeanUtils.instantiate(ClassUtils.forName("org.apache.catalina.deploy.ErrorPage", null));
/*    */       }
/*    */     }
/*    */     catch (ClassNotFoundException localClassNotFoundException) {}catch (LinkageError localLinkageError) {}
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 72 */     return null;
/*    */   }
/*    */   
/*    */   public void addToContext(Context context) {
/* 76 */     Assert.state(this.nativePage != null, "Neither Tomcat 7 nor 8 detected so no native error page exists");
/*    */     
/* 78 */     if (ClassUtils.isPresent("org.apache.tomcat.util.descriptor.web.ErrorPage", null)) {
/* 79 */       org.apache.tomcat.util.descriptor.web.ErrorPage errorPage = (org.apache.tomcat.util.descriptor.web.ErrorPage)this.nativePage;
/* 80 */       errorPage.setLocation(this.location);
/* 81 */       errorPage.setErrorCode(this.errorCode);
/* 82 */       errorPage.setExceptionType(this.exceptionType);
/* 83 */       context.addErrorPage(errorPage);
/*    */     }
/*    */     else {
/* 86 */       callMethod(this.nativePage, "setLocation", this.location, String.class);
/* 87 */       callMethod(this.nativePage, "setErrorCode", Integer.valueOf(this.errorCode), Integer.TYPE);
/* 88 */       callMethod(this.nativePage, "setExceptionType", this.exceptionType, String.class);
/*    */       
/* 90 */       callMethod(context, "addErrorPage", this.nativePage, this.nativePage
/* 91 */         .getClass());
/*    */     }
/*    */   }
/*    */   
/*    */   private void callMethod(Object target, String name, Object value, Class<?> type) {
/* 96 */     Method method = ReflectionUtils.findMethod(target.getClass(), name, new Class[] { type });
/* 97 */     ReflectionUtils.invokeMethod(method, target, new Object[] { value });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatErrorPage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */