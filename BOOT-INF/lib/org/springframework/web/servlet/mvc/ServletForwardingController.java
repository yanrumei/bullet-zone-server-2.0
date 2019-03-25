/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletForwardingController
/*     */   extends AbstractController
/*     */   implements BeanNameAware
/*     */ {
/*     */   private String servletName;
/*     */   private String beanName;
/*     */   
/*     */   public ServletForwardingController()
/*     */   {
/*  94 */     super(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletName(String servletName)
/*     */   {
/* 104 */     this.servletName = servletName;
/*     */   }
/*     */   
/*     */   public void setBeanName(String name)
/*     */   {
/* 109 */     this.beanName = name;
/* 110 */     if (this.servletName == null) {
/* 111 */       this.servletName = name;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 120 */     RequestDispatcher rd = getServletContext().getNamedDispatcher(this.servletName);
/* 121 */     if (rd == null) {
/* 122 */       throw new ServletException("No servlet with name '" + this.servletName + "' defined in web.xml");
/*     */     }
/*     */     
/* 125 */     if (useInclude(request, response)) {
/* 126 */       rd.include(request, response);
/* 127 */       if (this.logger.isDebugEnabled()) {
/* 128 */         this.logger.debug("Included servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 133 */       rd.forward(request, response);
/* 134 */       if (this.logger.isDebugEnabled()) {
/* 135 */         this.logger.debug("Forwarded to servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
/*     */       }
/*     */     }
/*     */     
/* 139 */     return null;
/*     */   }
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
/*     */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 157 */     return (WebUtils.isIncludeRequest(request)) || (response.isCommitted());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\ServletForwardingController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */