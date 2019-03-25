/*    */ package org.apache.catalina.core;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
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
/*    */ public final class StandardWrapperFacade
/*    */   implements ServletConfig
/*    */ {
/*    */   private final ServletConfig config;
/*    */   
/*    */   public StandardWrapperFacade(StandardWrapper config)
/*    */   {
/* 47 */     this.config = config;
/*    */   }
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
/* 64 */   private ServletContext context = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getServletName()
/*    */   {
/* 72 */     return this.config.getServletName();
/*    */   }
/*    */   
/*    */ 
/*    */   public ServletContext getServletContext()
/*    */   {
/* 78 */     if (this.context == null) {
/* 79 */       this.context = this.config.getServletContext();
/* 80 */       if ((this.context instanceof ApplicationContext)) {
/* 81 */         this.context = ((ApplicationContext)this.context).getFacade();
/*    */       }
/*    */     }
/* 84 */     return this.context;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getInitParameter(String name)
/*    */   {
/* 90 */     return this.config.getInitParameter(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public Enumeration<String> getInitParameterNames()
/*    */   {
/* 96 */     return this.config.getInitParameterNames();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardWrapperFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */