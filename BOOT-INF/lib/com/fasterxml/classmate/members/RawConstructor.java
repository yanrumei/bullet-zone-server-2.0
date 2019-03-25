/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import com.fasterxml.classmate.util.MethodKey;
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ 
/*    */ public final class RawConstructor
/*    */   extends RawMember
/*    */ {
/*    */   protected final Constructor<?> _constructor;
/*    */   protected final int _hashCode;
/*    */   
/*    */   public RawConstructor(ResolvedType context, Constructor<?> constructor)
/*    */   {
/* 16 */     super(context);
/* 17 */     this._constructor = constructor;
/* 18 */     this._hashCode = (this._constructor == null ? 0 : this._constructor.hashCode());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MethodKey createKey()
/*    */   {
/* 27 */     String name = "<init>";
/* 28 */     Class<?>[] argTypes = this._constructor.getParameterTypes();
/* 29 */     return new MethodKey(name, argTypes);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Constructor<?> getRawMember()
/*    */   {
/* 40 */     return this._constructor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 51 */     return this._hashCode;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 56 */     if (o == this) return true;
/* 57 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 58 */     RawConstructor other = (RawConstructor)o;
/* 59 */     return other._constructor == this._constructor;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\RawConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */