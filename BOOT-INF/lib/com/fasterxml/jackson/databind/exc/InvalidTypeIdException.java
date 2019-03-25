/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidTypeIdException
/*    */   extends JsonMappingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final JavaType _baseType;
/*    */   protected final String _typeId;
/*    */   
/*    */   public InvalidTypeIdException(JsonParser p, String msg, JavaType baseType, String typeId)
/*    */   {
/* 35 */     super(p, msg);
/* 36 */     this._baseType = baseType;
/* 37 */     this._typeId = typeId;
/*    */   }
/*    */   
/*    */   public static InvalidTypeIdException from(JsonParser p, String msg, JavaType baseType, String typeId)
/*    */   {
/* 42 */     return new InvalidTypeIdException(p, msg, baseType, typeId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 51 */   public JavaType getBaseType() { return this._baseType; }
/* 52 */   public String getTypeId() { return this._typeId; }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\exc\InvalidTypeIdException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */