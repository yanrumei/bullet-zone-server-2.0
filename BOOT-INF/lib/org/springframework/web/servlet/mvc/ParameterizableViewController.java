/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*     */ public class ParameterizableViewController
/*     */   extends AbstractController
/*     */ {
/*     */   private Object view;
/*     */   private HttpStatus statusCode;
/*     */   private boolean statusOnly;
/*     */   
/*     */   public ParameterizableViewController()
/*     */   {
/*  48 */     super(false);
/*  49 */     setSupportedMethods(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewName(String viewName)
/*     */   {
/*  58 */     this.view = viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getViewName()
/*     */   {
/*  66 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setView(View view)
/*     */   {
/*  75 */     this.view = view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public View getView()
/*     */   {
/*  84 */     return (this.view instanceof View) ? (View)this.view : null;
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
/*     */   public void setStatusCode(HttpStatus statusCode)
/*     */   {
/*  99 */     this.statusCode = statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpStatus getStatusCode()
/*     */   {
/* 107 */     return this.statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatusOnly(boolean statusOnly)
/*     */   {
/* 119 */     this.statusOnly = statusOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isStatusOnly()
/*     */   {
/* 126 */     return this.statusOnly;
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
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 140 */     String viewName = getViewName();
/*     */     
/* 142 */     if (getStatusCode() != null) {
/* 143 */       if (getStatusCode().is3xxRedirection()) {
/* 144 */         request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, getStatusCode());
/* 145 */         viewName = (viewName != null) && (!viewName.startsWith("redirect:")) ? "redirect:" + viewName : viewName;
/*     */       }
/*     */       else {
/* 148 */         response.setStatus(getStatusCode().value());
/* 149 */         if ((isStatusOnly()) || ((getStatusCode().equals(HttpStatus.NO_CONTENT)) && (getViewName() == null))) {
/* 150 */           return null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 155 */     ModelAndView modelAndView = new ModelAndView();
/* 156 */     modelAndView.addAllObjects(RequestContextUtils.getInputFlashMap(request));
/*     */     
/* 158 */     if (getViewName() != null) {
/* 159 */       modelAndView.setViewName(viewName);
/*     */     }
/*     */     else {
/* 162 */       modelAndView.setView(getView());
/*     */     }
/*     */     
/* 165 */     return isStatusOnly() ? null : modelAndView;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\ParameterizableViewController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */