/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JsonStreamContext
/*     */ {
/*     */   protected static final int TYPE_ROOT = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final int TYPE_ARRAY = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final int TYPE_OBJECT = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _type;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _index;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonStreamContext getParent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean inArray()
/*     */   {
/*  61 */     return this._type == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean inRoot()
/*     */   {
/*  68 */     return this._type == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean inObject()
/*     */   {
/*  74 */     return this._type == 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final String getTypeDesc()
/*     */   {
/*  85 */     switch (this._type) {
/*  86 */     case 0:  return "ROOT";
/*  87 */     case 1:  return "ARRAY";
/*  88 */     case 2:  return "OBJECT";
/*     */     }
/*  90 */     return "?";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String typeDesc()
/*     */   {
/*  97 */     switch (this._type) {
/*  98 */     case 0:  return "root";
/*  99 */     case 1:  return "Array";
/* 100 */     case 2:  return "Object";
/*     */     }
/* 102 */     return "?";
/*     */   }
/*     */   
/*     */ 
/*     */   public final int getEntryCount()
/*     */   {
/* 108 */     return this._index + 1;
/*     */   }
/*     */   
/*     */   public final int getCurrentIndex()
/*     */   {
/* 113 */     return this._index < 0 ? 0 : this._index;
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
/*     */   public abstract String getCurrentName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getCurrentValue()
/*     */   {
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonStreamContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */