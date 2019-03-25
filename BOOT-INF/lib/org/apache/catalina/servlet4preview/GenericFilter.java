/*    */ package org.apache.catalina.servlet4preview;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Enumeration;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
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
/*    */ @Deprecated
/*    */ public abstract class GenericFilter
/*    */   implements Filter, FilterConfig, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private volatile FilterConfig filterConfig;
/*    */   
/*    */   public String getInitParameter(String name)
/*    */   {
/* 45 */     return getFilterConfig().getInitParameter(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public Enumeration<String> getInitParameterNames()
/*    */   {
/* 51 */     return getFilterConfig().getInitParameterNames();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FilterConfig getFilterConfig()
/*    */   {
/* 62 */     return this.filterConfig;
/*    */   }
/*    */   
/*    */ 
/*    */   public ServletContext getServletContext()
/*    */   {
/* 68 */     return getFilterConfig().getServletContext();
/*    */   }
/*    */   
/*    */   public void init(FilterConfig filterConfig)
/*    */     throws ServletException
/*    */   {
/* 74 */     this.filterConfig = filterConfig;
/* 75 */     init();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void init()
/*    */     throws ServletException
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getFilterName()
/*    */   {
/* 93 */     return getFilterConfig().getFilterName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\GenericFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */