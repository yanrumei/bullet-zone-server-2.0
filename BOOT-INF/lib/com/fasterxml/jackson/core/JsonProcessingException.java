/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonProcessingException
/*     */   extends IOException
/*     */ {
/*     */   static final long serialVersionUID = 123L;
/*     */   protected JsonLocation _location;
/*     */   
/*     */   protected JsonProcessingException(String msg, JsonLocation loc, Throwable rootCause)
/*     */   {
/*  25 */     super(msg);
/*  26 */     if (rootCause != null) {
/*  27 */       initCause(rootCause);
/*     */     }
/*  29 */     this._location = loc;
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg) {
/*  33 */     super(msg);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg, JsonLocation loc) {
/*  37 */     this(msg, loc, null);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg, Throwable rootCause) {
/*  41 */     this(msg, null, rootCause);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(Throwable rootCause) {
/*  45 */     this(null, null, rootCause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonLocation getLocation()
/*     */   {
/*  54 */     return this._location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginalMessage()
/*     */   {
/*  63 */     return super.getMessage();
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
/*     */   public Object getProcessor()
/*     */   {
/*  79 */     return null;
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
/*     */   protected String getMessageSuffix()
/*     */   {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 104 */     String msg = super.getMessage();
/* 105 */     if (msg == null) {
/* 106 */       msg = "N/A";
/*     */     }
/* 108 */     JsonLocation loc = getLocation();
/* 109 */     String suffix = getMessageSuffix();
/*     */     
/* 111 */     if ((loc != null) || (suffix != null)) {
/* 112 */       StringBuilder sb = new StringBuilder(100);
/* 113 */       sb.append(msg);
/* 114 */       if (suffix != null) {
/* 115 */         sb.append(suffix);
/*     */       }
/* 117 */       if (loc != null) {
/* 118 */         sb.append('\n');
/* 119 */         sb.append(" at ");
/* 120 */         sb.append(loc.toString());
/*     */       }
/* 122 */       msg = sb.toString();
/*     */     }
/* 124 */     return msg;
/*     */   }
/*     */   
/* 127 */   public String toString() { return getClass().getName() + ": " + getMessage(); }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonProcessingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */