/*     */ package com.fasterxml.jackson.databind.jsonFormatVisitors;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface JsonFormatVisitorWrapper
/*     */   extends JsonFormatVisitorWithSerializerProvider
/*     */ {
/*     */   public abstract JsonObjectFormatVisitor expectObjectFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonArrayFormatVisitor expectArrayFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonStringFormatVisitor expectStringFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonNumberFormatVisitor expectNumberFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonIntegerFormatVisitor expectIntegerFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonBooleanFormatVisitor expectBooleanFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonNullFormatVisitor expectNullFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonAnyFormatVisitor expectAnyFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public abstract JsonMapFormatVisitor expectMapFormat(JavaType paramJavaType)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public static class Base
/*     */     implements JsonFormatVisitorWrapper
/*     */   {
/*     */     protected SerializerProvider _provider;
/*     */     
/*     */     public Base() {}
/*     */     
/*     */     public Base(SerializerProvider p)
/*     */     {
/*  78 */       this._provider = p;
/*     */     }
/*     */     
/*     */     public SerializerProvider getProvider()
/*     */     {
/*  83 */       return this._provider;
/*     */     }
/*     */     
/*     */     public void setProvider(SerializerProvider p)
/*     */     {
/*  88 */       this._provider = p;
/*     */     }
/*     */     
/*     */     public JsonObjectFormatVisitor expectObjectFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/*  94 */       return null;
/*     */     }
/*     */     
/*     */     public JsonArrayFormatVisitor expectArrayFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 100 */       return null;
/*     */     }
/*     */     
/*     */     public JsonStringFormatVisitor expectStringFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 106 */       return null;
/*     */     }
/*     */     
/*     */     public JsonNumberFormatVisitor expectNumberFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 112 */       return null;
/*     */     }
/*     */     
/*     */     public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 118 */       return null;
/*     */     }
/*     */     
/*     */     public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 124 */       return null;
/*     */     }
/*     */     
/*     */     public JsonNullFormatVisitor expectNullFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 130 */       return null;
/*     */     }
/*     */     
/*     */     public JsonAnyFormatVisitor expectAnyFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 136 */       return null;
/*     */     }
/*     */     
/*     */     public JsonMapFormatVisitor expectMapFormat(JavaType type)
/*     */       throws JsonMappingException
/*     */     {
/* 142 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonFormatVisitorWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */