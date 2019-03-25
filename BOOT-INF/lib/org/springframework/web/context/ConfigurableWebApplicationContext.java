/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
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
/*    */ public abstract interface ConfigurableWebApplicationContext
/*    */   extends WebApplicationContext, ConfigurableApplicationContext
/*    */ {
/* 45 */   public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
/*    */   public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";
/*    */   
/*    */   public abstract void setServletContext(ServletContext paramServletContext);
/*    */   
/*    */   public abstract void setServletConfig(ServletConfig paramServletConfig);
/*    */   
/*    */   public abstract ServletConfig getServletConfig();
/*    */   
/*    */   public abstract void setNamespace(String paramString);
/*    */   
/*    */   public abstract String getNamespace();
/*    */   
/*    */   public abstract void setConfigLocation(String paramString);
/*    */   
/*    */   public abstract void setConfigLocations(String... paramVarArgs);
/*    */   
/*    */   public abstract String[] getConfigLocations();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\ConfigurableWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */