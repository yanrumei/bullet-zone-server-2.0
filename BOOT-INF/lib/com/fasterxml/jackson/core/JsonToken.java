/*     */ package com.fasterxml.jackson.core;
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
/*     */ public enum JsonToken
/*     */ {
/*  31 */   NOT_AVAILABLE(null, -1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   START_OBJECT("{", 1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   END_OBJECT("}", 2), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   START_ARRAY("[", 3), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   END_ARRAY("]", 4), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   FIELD_NAME(null, 5), 
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
/*  73 */   VALUE_EMBEDDED_OBJECT(null, 12), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   VALUE_STRING(null, 6), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   VALUE_NUMBER_INT(null, 7), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   VALUE_NUMBER_FLOAT(null, 8), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   VALUE_TRUE("true", 9), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   VALUE_FALSE("false", 10), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   VALUE_NULL("null", 11);
/*     */   
/*     */ 
/*     */ 
/*     */   final String _serialized;
/*     */   
/*     */   final char[] _serializedChars;
/*     */   
/*     */   final byte[] _serializedBytes;
/*     */   
/*     */   final int _id;
/*     */   
/*     */   final boolean _isStructStart;
/*     */   
/*     */   final boolean _isStructEnd;
/*     */   
/*     */   final boolean _isNumber;
/*     */   
/*     */   final boolean _isBoolean;
/*     */   
/*     */   final boolean _isScalar;
/*     */   
/*     */ 
/*     */   private JsonToken(String token, int id)
/*     */   {
/* 139 */     if (token == null) {
/* 140 */       this._serialized = null;
/* 141 */       this._serializedChars = null;
/* 142 */       this._serializedBytes = null;
/*     */     } else {
/* 144 */       this._serialized = token;
/* 145 */       this._serializedChars = token.toCharArray();
/*     */       
/* 147 */       int len = this._serializedChars.length;
/* 148 */       this._serializedBytes = new byte[len];
/* 149 */       for (int i = 0; i < len; i++) {
/* 150 */         this._serializedBytes[i] = ((byte)this._serializedChars[i]);
/*     */       }
/*     */     }
/* 153 */     this._id = id;
/*     */     
/* 155 */     this._isBoolean = ((id == 10) || (id == 9));
/* 156 */     this._isNumber = ((id == 7) || (id == 8));
/*     */     
/* 158 */     this._isStructStart = ((id == 1) || (id == 3));
/* 159 */     this._isStructEnd = ((id == 2) || (id == 4));
/*     */     
/* 161 */     this._isScalar = ((!this._isStructStart) && (!this._isStructEnd) && (id != 5) && (id != -1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 166 */   public final int id() { return this._id; }
/*     */   
/* 168 */   public final String asString() { return this._serialized; }
/* 169 */   public final char[] asCharArray() { return this._serializedChars; }
/* 170 */   public final byte[] asByteArray() { return this._serializedBytes; }
/*     */   
/* 172 */   public final boolean isNumeric() { return this._isNumber; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isStructStart()
/*     */   {
/* 182 */     return this._isStructStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isStructEnd()
/*     */   {
/* 192 */     return this._isStructEnd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */   public final boolean isScalarValue() { return this._isScalar; }
/* 200 */   public final boolean isBoolean() { return this._isBoolean; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */