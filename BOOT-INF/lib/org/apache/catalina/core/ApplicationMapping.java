/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.mapper.MappingData;
/*     */ import org.apache.catalina.servlet4preview.http.MappingMatch;
/*     */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApplicationMapping
/*     */ {
/*     */   private final MappingData mappingData;
/*  27 */   private volatile ServletMapping mapping = null;
/*     */   
/*     */   public ApplicationMapping(MappingData mappingData) {
/*  30 */     this.mappingData = mappingData;
/*     */   }
/*     */   
/*     */   public ServletMapping getServletMapping() {
/*  34 */     if (this.mapping == null) {
/*  35 */       if (this.mappingData == null) {
/*  36 */         this.mapping = new MappingImpl("", "", MappingMatch.UNKNOWN, "");
/*     */       } else { String servletName;
/*     */         String servletName;
/*  39 */         if (this.mappingData.wrapper == null) {
/*  40 */           servletName = "";
/*     */         } else {
/*  42 */           servletName = this.mappingData.wrapper.getName();
/*     */         }
/*  44 */         switch (this.mappingData.matchType) {
/*     */         case CONTEXT_ROOT: 
/*  46 */           this.mapping = new MappingImpl("", "", this.mappingData.matchType, servletName);
/*  47 */           break;
/*     */         case DEFAULT: 
/*  49 */           this.mapping = new MappingImpl("", "/", this.mappingData.matchType, servletName);
/*  50 */           break;
/*     */         
/*     */         case EXACT: 
/*  53 */           this.mapping = new MappingImpl(this.mappingData.wrapperPath.toString().substring(1), this.mappingData.wrapperPath.toString(), this.mappingData.matchType, servletName);
/*  54 */           break;
/*     */         case EXTENSION: 
/*  56 */           String path = this.mappingData.wrapperPath.toString();
/*  57 */           int extIndex = path.lastIndexOf('.');
/*     */           
/*  59 */           this.mapping = new MappingImpl(path.substring(1, extIndex), "*" + path.substring(extIndex), this.mappingData.matchType, servletName);
/*  60 */           break;
/*     */         case PATH:  String matchValue;
/*     */           String matchValue;
/*  63 */           if (this.mappingData.pathInfo.isNull()) {
/*  64 */             matchValue = null;
/*     */           } else {
/*  66 */             matchValue = this.mappingData.pathInfo.toString().substring(1);
/*     */           }
/*     */           
/*  69 */           this.mapping = new MappingImpl(matchValue, this.mappingData.wrapperPath.toString() + "/*", this.mappingData.matchType, servletName);
/*     */           
/*  71 */           break;
/*     */         case UNKNOWN: 
/*  73 */           this.mapping = new MappingImpl("", "", this.mappingData.matchType, servletName);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*  79 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public void recycle() {
/*  83 */     this.mapping = null;
/*     */   }
/*     */   
/*     */   private static class MappingImpl implements ServletMapping
/*     */   {
/*     */     private final String matchValue;
/*     */     private final String pattern;
/*     */     private final MappingMatch mappingType;
/*     */     private final String servletName;
/*     */     
/*     */     public MappingImpl(String matchValue, String pattern, MappingMatch mappingType, String servletName)
/*     */     {
/*  95 */       this.matchValue = matchValue;
/*  96 */       this.pattern = pattern;
/*  97 */       this.mappingType = mappingType;
/*  98 */       this.servletName = servletName;
/*     */     }
/*     */     
/*     */     public String getMatchValue()
/*     */     {
/* 103 */       return this.matchValue;
/*     */     }
/*     */     
/*     */     public String getPattern()
/*     */     {
/* 108 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public MappingMatch getMappingMatch()
/*     */     {
/* 113 */       return this.mappingType;
/*     */     }
/*     */     
/*     */     public String getServletName()
/*     */     {
/* 118 */       return this.servletName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */