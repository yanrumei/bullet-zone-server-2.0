/*     */ package com.fasterxml.jackson.databind.exc;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
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
/*     */ public class InvalidFormatException
/*     */   extends JsonMappingException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Object _value;
/*     */   protected final Class<?> _targetType;
/*     */   
/*     */   @Deprecated
/*     */   public InvalidFormatException(String msg, Object value, Class<?> targetType)
/*     */   {
/*  43 */     super(null, msg);
/*  44 */     this._value = value;
/*  45 */     this._targetType = targetType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public InvalidFormatException(String msg, JsonLocation loc, Object value, Class<?> targetType)
/*     */   {
/*  55 */     super(null, msg, loc);
/*  56 */     this._value = value;
/*  57 */     this._targetType = targetType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InvalidFormatException(JsonParser p, String msg, Object value, Class<?> targetType)
/*     */   {
/*  66 */     super(p, msg);
/*  67 */     this._value = value;
/*  68 */     this._targetType = targetType;
/*     */   }
/*     */   
/*     */ 
/*     */   public static InvalidFormatException from(JsonParser p, String msg, Object value, Class<?> targetType)
/*     */   {
/*  74 */     return new InvalidFormatException(p, msg, value, targetType);
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
/*     */   public Object getValue()
/*     */   {
/*  90 */     return this._value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getTargetType()
/*     */   {
/* 100 */     return this._targetType;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\exc\InvalidFormatException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */