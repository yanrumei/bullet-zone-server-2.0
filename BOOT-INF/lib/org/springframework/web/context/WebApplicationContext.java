/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ApplicationContext;
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
/*    */ public abstract interface WebApplicationContext
/*    */   extends ApplicationContext
/*    */ {
/* 54 */   public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
/*    */   public static final String SCOPE_REQUEST = "request";
/*    */   public static final String SCOPE_SESSION = "session";
/*    */   public static final String SCOPE_GLOBAL_SESSION = "globalSession";
/*    */   public static final String SCOPE_APPLICATION = "application";
/*    */   public static final String SERVLET_CONTEXT_BEAN_NAME = "servletContext";
/*    */   public static final String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";
/*    */   public static final String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";
/*    */   
/*    */   public abstract ServletContext getServletContext();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\WebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */