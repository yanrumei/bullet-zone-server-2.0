/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import com.fasterxml.jackson.core.util.RequestPayload;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonParseException
/*     */   extends JsonProcessingException
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   protected transient JsonParser _processor;
/*     */   protected RequestPayload _requestPayload;
/*     */   
/*     */   @Deprecated
/*     */   public JsonParseException(String msg, JsonLocation loc)
/*     */   {
/*  33 */     super(msg, loc);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonParseException(String msg, JsonLocation loc, Throwable root) {
/*  38 */     super(msg, loc, root);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParseException(JsonParser p, String msg)
/*     */   {
/*  49 */     super(msg, p == null ? null : p.getCurrentLocation());
/*  50 */     this._processor = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonParseException(JsonParser p, String msg, Throwable root)
/*     */   {
/*  57 */     super(msg, p == null ? null : p.getCurrentLocation(), root);
/*  58 */     this._processor = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonParseException(JsonParser p, String msg, JsonLocation loc)
/*     */   {
/*  65 */     super(msg, loc);
/*  66 */     this._processor = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonParseException(JsonParser p, String msg, JsonLocation loc, Throwable root)
/*     */   {
/*  73 */     super(msg, loc, root);
/*  74 */     this._processor = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParseException withParser(JsonParser p)
/*     */   {
/*  86 */     this._processor = p;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParseException withRequestPayload(RequestPayload p)
/*     */   {
/*  99 */     this._requestPayload = p;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public JsonParser getProcessor()
/*     */   {
/* 105 */     return this._processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestPayload getRequestPayload()
/*     */   {
/* 117 */     return this._requestPayload;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRequestPayloadAsString()
/*     */   {
/* 129 */     return this._requestPayload != null ? this._requestPayload.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 137 */     String msg = super.getMessage();
/* 138 */     if (this._requestPayload != null) {
/* 139 */       msg = msg + "\nRequest payload : " + this._requestPayload.toString();
/*     */     }
/* 141 */     return msg;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */