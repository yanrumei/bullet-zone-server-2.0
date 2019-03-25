/*    */ package com.fasterxml.jackson.databind.jsonFormatVisitors;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface JsonMapFormatVisitor
/*    */   extends JsonFormatVisitorWithSerializerProvider
/*    */ {
/*    */   public abstract void keyFormat(JsonFormatVisitable paramJsonFormatVisitable, JavaType paramJavaType)
/*    */     throws JsonMappingException;
/*    */   
/*    */   public abstract void valueFormat(JsonFormatVisitable paramJsonFormatVisitable, JavaType paramJavaType)
/*    */     throws JsonMappingException;
/*    */   
/*    */   public static class Base
/*    */     implements JsonMapFormatVisitor
/*    */   {
/*    */     protected SerializerProvider _provider;
/*    */     
/*    */     public Base() {}
/*    */     
/*    */     public Base(SerializerProvider p)
/*    */     {
/* 32 */       this._provider = p;
/*    */     }
/*    */     
/* 35 */     public SerializerProvider getProvider() { return this._provider; }
/*    */     
/*    */     public void setProvider(SerializerProvider p) {
/* 38 */       this._provider = p;
/*    */     }
/*    */     
/*    */     public void keyFormat(JsonFormatVisitable handler, JavaType keyType)
/*    */       throws JsonMappingException
/*    */     {}
/*    */     
/*    */     public void valueFormat(JsonFormatVisitable handler, JavaType valueType)
/*    */       throws JsonMappingException
/*    */     {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonMapFormatVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */