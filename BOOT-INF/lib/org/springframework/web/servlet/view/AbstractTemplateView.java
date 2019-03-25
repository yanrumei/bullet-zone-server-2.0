/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.web.servlet.support.RequestContext;
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
/*     */ public abstract class AbstractTemplateView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE = "springMacroRequestContext";
/*  55 */   private boolean exposeRequestAttributes = false;
/*     */   
/*  57 */   private boolean allowRequestOverride = false;
/*     */   
/*  59 */   private boolean exposeSessionAttributes = false;
/*     */   
/*  61 */   private boolean allowSessionOverride = false;
/*     */   
/*  63 */   private boolean exposeSpringMacroHelpers = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeRequestAttributes(boolean exposeRequestAttributes)
/*     */   {
/*  71 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowRequestOverride(boolean allowRequestOverride)
/*     */   {
/*  81 */     this.allowRequestOverride = allowRequestOverride;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeSessionAttributes(boolean exposeSessionAttributes)
/*     */   {
/*  89 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowSessionOverride(boolean allowSessionOverride)
/*     */   {
/*  99 */     this.allowSessionOverride = allowSessionOverride;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers)
/*     */   {
/* 111 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     Enumeration<String> en;
/* 119 */     if (this.exposeRequestAttributes)
/* 120 */       for (en = request.getAttributeNames(); en.hasMoreElements();) {
/* 121 */         String attribute = (String)en.nextElement();
/* 122 */         if ((model.containsKey(attribute)) && (!this.allowRequestOverride)) {
/* 123 */           throw new ServletException("Cannot expose request attribute '" + attribute + "' because of an existing model object of the same name");
/*     */         }
/*     */         
/* 126 */         Object attributeValue = request.getAttribute(attribute);
/* 127 */         if (this.logger.isDebugEnabled()) {
/* 128 */           this.logger.debug("Exposing request attribute '" + attribute + "' with value [" + attributeValue + "] to model");
/*     */         }
/*     */         
/* 131 */         model.put(attribute, attributeValue);
/*     */       }
/*     */     HttpSession session;
/*     */     Enumeration<String> en;
/* 135 */     if (this.exposeSessionAttributes) {
/* 136 */       session = request.getSession(false);
/* 137 */       if (session != null) {
/* 138 */         for (en = session.getAttributeNames(); en.hasMoreElements();) {
/* 139 */           String attribute = (String)en.nextElement();
/* 140 */           if ((model.containsKey(attribute)) && (!this.allowSessionOverride)) {
/* 141 */             throw new ServletException("Cannot expose session attribute '" + attribute + "' because of an existing model object of the same name");
/*     */           }
/*     */           
/* 144 */           Object attributeValue = session.getAttribute(attribute);
/* 145 */           if (this.logger.isDebugEnabled()) {
/* 146 */             this.logger.debug("Exposing session attribute '" + attribute + "' with value [" + attributeValue + "] to model");
/*     */           }
/*     */           
/* 149 */           model.put(attribute, attributeValue);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 154 */     if (this.exposeSpringMacroHelpers) {
/* 155 */       if (model.containsKey("springMacroRequestContext")) {
/* 156 */         throw new ServletException("Cannot expose bind macro helper 'springMacroRequestContext' because of an existing model object of the same name");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 161 */       model.put("springMacroRequestContext", new RequestContext(request, response, 
/* 162 */         getServletContext(), model));
/*     */     }
/*     */     
/* 165 */     applyContentType(response);
/*     */     
/* 167 */     renderMergedTemplateModel(model, request, response);
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
/*     */   protected void applyContentType(HttpServletResponse response)
/*     */   {
/* 180 */     if (response.getContentType() == null) {
/* 181 */       response.setContentType(getContentType());
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void renderMergedTemplateModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\AbstractTemplateView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */