/*     */ package org.springframework.web.servlet.mvc.annotation;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*     */ @Deprecated
/*     */ abstract class ServletAnnotationMappingUtils
/*     */ {
/*     */   public static boolean checkRequestMethod(RequestMethod[] methods, HttpServletRequest request)
/*     */   {
/*  47 */     String inputMethod = request.getMethod();
/*  48 */     if ((ObjectUtils.isEmpty(methods)) && (!RequestMethod.OPTIONS.name().equals(inputMethod))) {
/*  49 */       return true;
/*     */     }
/*  51 */     for (RequestMethod method : methods) {
/*  52 */       if (method.name().equals(inputMethod)) {
/*  53 */         return true;
/*     */       }
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkParameters(String[] params, HttpServletRequest request)
/*     */   {
/*  66 */     if (!ObjectUtils.isEmpty(params)) {
/*  67 */       for (String param : params) {
/*  68 */         int separator = param.indexOf('=');
/*  69 */         if (separator == -1) {
/*  70 */           if (param.startsWith("!")) {
/*  71 */             if (WebUtils.hasSubmitParameter(request, param.substring(1))) {
/*  72 */               return false;
/*     */             }
/*     */           }
/*  75 */           else if (!WebUtils.hasSubmitParameter(request, param)) {
/*  76 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/*  80 */           boolean negated = (separator > 0) && (param.charAt(separator - 1) == '!');
/*  81 */           String key = !negated ? param.substring(0, separator) : param.substring(0, separator - 1);
/*  82 */           String value = param.substring(separator + 1);
/*  83 */           boolean match = value.equals(request.getParameter(key));
/*  84 */           if (negated) {
/*  85 */             match = !match;
/*     */           }
/*  87 */           if (!match) {
/*  88 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  93 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkHeaders(String[] headers, HttpServletRequest request)
/*     */   {
/* 103 */     if (!ObjectUtils.isEmpty(headers)) {
/* 104 */       for (String header : headers) {
/* 105 */         int separator = header.indexOf('=');
/* 106 */         if (separator == -1) {
/* 107 */           if (header.startsWith("!")) {
/* 108 */             if (request.getHeader(header.substring(1)) != null) {
/* 109 */               return false;
/*     */             }
/*     */           }
/* 112 */           else if (request.getHeader(header) == null) {
/* 113 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 117 */           boolean negated = (separator > 0) && (header.charAt(separator - 1) == '!');
/* 118 */           String key = !negated ? header.substring(0, separator) : header.substring(0, separator - 1);
/* 119 */           String value = header.substring(separator + 1);
/* 120 */           if (isMediaTypeHeader(key)) {
/* 121 */             List<MediaType> requestMediaTypes = MediaType.parseMediaTypes(request.getHeader(key));
/* 122 */             List<MediaType> valueMediaTypes = MediaType.parseMediaTypes(value);
/* 123 */             boolean found = false;
/* 124 */             for (Iterator<MediaType> valIter = valueMediaTypes.iterator(); (valIter.hasNext()) && (!found);) {
/* 125 */               MediaType valueMediaType = (MediaType)valIter.next();
/* 126 */               Iterator<MediaType> reqIter = requestMediaTypes.iterator();
/* 127 */               while ((reqIter.hasNext()) && (!found)) {
/* 128 */                 MediaType requestMediaType = (MediaType)reqIter.next();
/* 129 */                 if (valueMediaType.includes(requestMediaType)) {
/* 130 */                   found = true;
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 135 */             if (negated) {
/* 136 */               found = !found;
/*     */             }
/* 138 */             if (!found) {
/* 139 */               return false;
/*     */             }
/*     */           }
/*     */           else {
/* 143 */             boolean match = value.equals(request.getHeader(key));
/* 144 */             if (negated) {
/* 145 */               match = !match;
/*     */             }
/* 147 */             if (!match) {
/* 148 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isMediaTypeHeader(String headerName) {
/* 158 */     return ("Accept".equalsIgnoreCase(headerName)) || ("Content-Type".equalsIgnoreCase(headerName));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\ServletAnnotationMappingUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */