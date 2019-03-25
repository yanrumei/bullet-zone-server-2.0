/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletWrappingController
/*     */   extends AbstractController
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*     */   private Class<? extends Servlet> servletClass;
/*     */   private String servletName;
/*  89 */   private Properties initParameters = new Properties();
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private Servlet servletInstance;
/*     */   
/*     */   public ServletWrappingController()
/*     */   {
/*  97 */     super(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletClass(Class<? extends Servlet> servletClass)
/*     */   {
/* 107 */     this.servletClass = servletClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletName(String servletName)
/*     */   {
/* 115 */     this.servletName = servletName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInitParameters(Properties initParameters)
/*     */   {
/* 123 */     this.initParameters = initParameters;
/*     */   }
/*     */   
/*     */   public void setBeanName(String name)
/*     */   {
/* 128 */     this.beanName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 138 */     if (this.servletClass == null) {
/* 139 */       throw new IllegalArgumentException("'servletClass' is required");
/*     */     }
/* 141 */     if (this.servletName == null) {
/* 142 */       this.servletName = this.beanName;
/*     */     }
/* 144 */     this.servletInstance = ((Servlet)this.servletClass.newInstance());
/* 145 */     this.servletInstance.init(new DelegatingServletConfig(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 157 */     this.servletInstance.service(request, response);
/* 158 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 168 */     this.servletInstance.destroy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private DelegatingServletConfig() {}
/*     */     
/*     */ 
/*     */     public String getServletName()
/*     */     {
/* 181 */       return ServletWrappingController.this.servletName;
/*     */     }
/*     */     
/*     */     public ServletContext getServletContext()
/*     */     {
/* 186 */       return ServletWrappingController.this.getServletContext();
/*     */     }
/*     */     
/*     */     public String getInitParameter(String paramName)
/*     */     {
/* 191 */       return ServletWrappingController.this.initParameters.getProperty(paramName);
/*     */     }
/*     */     
/*     */ 
/*     */     public Enumeration<String> getInitParameterNames()
/*     */     {
/* 197 */       return ServletWrappingController.this.initParameters.keys();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\ServletWrappingController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */