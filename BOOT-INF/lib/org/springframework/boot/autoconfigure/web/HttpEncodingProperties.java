/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.http.encoding")
/*     */ public class HttpEncodingProperties
/*     */ {
/*  35 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private Charset charset = DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Boolean force;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean forceRequest;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean forceResponse;
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<Locale, Charset> mapping;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/*  65 */     return this.charset;
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset) {
/*  69 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public boolean isForce() {
/*  73 */     return Boolean.TRUE.equals(this.force);
/*     */   }
/*     */   
/*     */   public void setForce(boolean force) {
/*  77 */     this.force = Boolean.valueOf(force);
/*     */   }
/*     */   
/*     */   public boolean isForceRequest() {
/*  81 */     return Boolean.TRUE.equals(this.forceRequest);
/*     */   }
/*     */   
/*     */   public void setForceRequest(boolean forceRequest) {
/*  85 */     this.forceRequest = Boolean.valueOf(forceRequest);
/*     */   }
/*     */   
/*     */   public boolean isForceResponse() {
/*  89 */     return Boolean.TRUE.equals(this.forceResponse);
/*     */   }
/*     */   
/*     */   public void setForceResponse(boolean forceResponse) {
/*  93 */     this.forceResponse = Boolean.valueOf(forceResponse);
/*     */   }
/*     */   
/*     */   public Map<Locale, Charset> getMapping() {
/*  97 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public void setMapping(Map<Locale, Charset> mapping) {
/* 101 */     this.mapping = mapping;
/*     */   }
/*     */   
/*     */   boolean shouldForce(Type type) {
/* 105 */     Boolean force = type == Type.REQUEST ? this.forceRequest : this.forceResponse;
/* 106 */     if (force == null) {
/* 107 */       force = this.force;
/*     */     }
/* 109 */     if (force == null) {
/* 110 */       force = Boolean.valueOf(type == Type.REQUEST);
/*     */     }
/* 112 */     return force.booleanValue();
/*     */   }
/*     */   
/*     */   static enum Type
/*     */   {
/* 117 */     REQUEST,  RESPONSE;
/*     */     
/*     */     private Type() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\HttpEncodingProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */