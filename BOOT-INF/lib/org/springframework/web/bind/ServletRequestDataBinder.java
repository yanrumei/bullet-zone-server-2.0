/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import javax.servlet.ServletRequest;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.multipart.MultipartRequest;
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
/*     */ public class ServletRequestDataBinder
/*     */   extends WebDataBinder
/*     */ {
/*     */   public ServletRequestDataBinder(Object target)
/*     */   {
/*  68 */     super(target);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRequestDataBinder(Object target, String objectName)
/*     */   {
/*  78 */     super(target, objectName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bind(ServletRequest request)
/*     */   {
/* 100 */     MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
/* 101 */     MultipartRequest multipartRequest = (MultipartRequest)WebUtils.getNativeRequest(request, MultipartRequest.class);
/* 102 */     if (multipartRequest != null) {
/* 103 */       bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
/*     */     }
/* 105 */     addBindValues(mpvs, request);
/* 106 */     doBind(mpvs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeNoCatch()
/*     */     throws ServletRequestBindingException
/*     */   {
/* 126 */     if (getBindingResult().hasErrors())
/*     */     {
/*     */ 
/* 129 */       throw new ServletRequestBindingException("Errors binding onto object '" + getBindingResult().getObjectName() + "'", new BindException(getBindingResult()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\ServletRequestDataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */