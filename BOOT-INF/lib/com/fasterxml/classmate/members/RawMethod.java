/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import com.fasterxml.classmate.util.MethodKey;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ 
/*    */ public final class RawMethod
/*    */   extends RawMember
/*    */ {
/*    */   protected final Method _method;
/*    */   protected final int _hashCode;
/*    */   
/*    */   public RawMethod(ResolvedType context, Method method)
/*    */   {
/* 17 */     super(context);
/* 18 */     this._method = method;
/* 19 */     this._hashCode = (this._method == null ? 0 : this._method.hashCode());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Method getRawMember()
/*    */   {
/* 30 */     return this._method;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 34 */     return Modifier.isAbstract(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isStrict() {
/* 38 */     return Modifier.isStrict(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isNative() {
/* 42 */     return Modifier.isNative(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isSynchronized() {
/* 46 */     return Modifier.isSynchronized(getModifiers());
/*    */   }
/*    */   
/*    */   public MethodKey createKey()
/*    */   {
/* 51 */     String name = this._method.getName();
/* 52 */     Class<?>[] argTypes = this._method.getParameterTypes();
/* 53 */     return new MethodKey(name, argTypes);
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
/* 64 */     return this._hashCode;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 69 */     if (o == this) return true;
/* 70 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 71 */     RawMethod other = (RawMethod)o;
/* 72 */     return other._method == this._method;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\RawMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */