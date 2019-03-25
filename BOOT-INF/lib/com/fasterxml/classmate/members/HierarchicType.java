/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HierarchicType
/*    */ {
/*    */   protected final boolean _isMixin;
/*    */   protected final ResolvedType _type;
/*    */   protected final int _priority;
/*    */   
/*    */   public HierarchicType(ResolvedType type, boolean mixin, int priority)
/*    */   {
/* 35 */     this._type = type;
/* 36 */     this._isMixin = mixin;
/* 37 */     this._priority = priority;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */   public ResolvedType getType() { return this._type; }
/* 47 */   public Class<?> getErasedType() { return this._type.getErasedType(); }
/* 48 */   public boolean isMixin() { return this._isMixin; }
/* 49 */   public int getPriority() { return this._priority; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 57 */   public String toString() { return this._type.toString(); }
/* 58 */   public int hashCode() { return this._type.hashCode(); }
/*    */   
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 63 */     if (o == this) return true;
/* 64 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 65 */     HierarchicType other = (HierarchicType)o;
/* 66 */     return this._type.equals(other._type);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\HierarchicType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */