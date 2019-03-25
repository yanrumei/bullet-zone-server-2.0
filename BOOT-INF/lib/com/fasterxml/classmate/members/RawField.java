/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ 
/*    */ public final class RawField
/*    */   extends RawMember
/*    */ {
/*    */   protected final Field _field;
/*    */   private final int _hashCode;
/*    */   
/*    */   public RawField(ResolvedType context, Field field)
/*    */   {
/* 16 */     super(context);
/* 17 */     this._field = field;
/* 18 */     this._hashCode = (this._field == null ? 0 : this._field.hashCode());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Field getRawMember()
/*    */   {
/* 29 */     return this._field;
/*    */   }
/*    */   
/*    */   public boolean isTransient() {
/* 33 */     return Modifier.isTransient(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isVolatile() {
/* 37 */     return Modifier.isVolatile(getModifiers());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 48 */     if (o == this) return true;
/* 49 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 50 */     RawField other = (RawField)o;
/* 51 */     return other._field == this._field;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 56 */     return this._hashCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\RawField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */