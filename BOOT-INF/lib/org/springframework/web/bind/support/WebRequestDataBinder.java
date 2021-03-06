/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartRequest;
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
/*     */ public class WebRequestDataBinder
/*     */   extends WebDataBinder
/*     */ {
/*  73 */   private static final boolean servlet3Parts = ClassUtils.hasMethod(HttpServletRequest.class, "getParts", new Class[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebRequestDataBinder(Object target)
/*     */   {
/*  83 */     super(target);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebRequestDataBinder(Object target, String objectName)
/*     */   {
/*  93 */     super(target, objectName);
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
/*     */ 
/*     */   public void bind(WebRequest request)
/*     */   {
/* 116 */     MutablePropertyValues mpvs = new MutablePropertyValues(request.getParameterMap());
/* 117 */     if ((isMultipartRequest(request)) && ((request instanceof NativeWebRequest))) {
/* 118 */       MultipartRequest multipartRequest = (MultipartRequest)((NativeWebRequest)request).getNativeRequest(MultipartRequest.class);
/* 119 */       if (multipartRequest != null) {
/* 120 */         bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
/*     */       }
/* 122 */       else if (servlet3Parts) {
/* 123 */         HttpServletRequest serlvetRequest = (HttpServletRequest)((NativeWebRequest)request).getNativeRequest(HttpServletRequest.class);
/* 124 */         new Servlet3MultipartHelper(isBindEmptyMultipartFiles()).bindParts(serlvetRequest, mpvs);
/*     */       }
/*     */     }
/* 127 */     doBind(mpvs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isMultipartRequest(WebRequest request)
/*     */   {
/* 135 */     String contentType = request.getHeader("Content-Type");
/* 136 */     return (contentType != null) && (StringUtils.startsWithIgnoreCase(contentType, "multipart"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeNoCatch()
/*     */     throws BindException
/*     */   {
/* 146 */     if (getBindingResult().hasErrors()) {
/* 147 */       throw new BindException(getBindingResult());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Servlet3MultipartHelper
/*     */   {
/*     */     private final boolean bindEmptyMultipartFiles;
/*     */     
/*     */ 
/*     */ 
/*     */     public Servlet3MultipartHelper(boolean bindEmptyMultipartFiles)
/*     */     {
/* 161 */       this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
/*     */     }
/*     */     
/*     */     public void bindParts(HttpServletRequest request, MutablePropertyValues mpvs) {
/*     */       try {
/* 166 */         MultiValueMap<String, Part> map = new LinkedMultiValueMap();
/* 167 */         for (Part part : request.getParts()) {
/* 168 */           map.add(part.getName(), part);
/*     */         }
/* 170 */         for (Map.Entry<String, List<Part>> entry : map.entrySet()) {
/* 171 */           if (((List)entry.getValue()).size() == 1) {
/* 172 */             Part part = (Part)((List)entry.getValue()).get(0);
/* 173 */             if ((this.bindEmptyMultipartFiles) || (part.getSize() > 0L)) {
/* 174 */               mpvs.add((String)entry.getKey(), part);
/*     */             }
/*     */           }
/*     */           else {
/* 178 */             mpvs.add((String)entry.getKey(), entry.getValue());
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 183 */         throw new MultipartException("Failed to get request parts", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\WebRequestDataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */