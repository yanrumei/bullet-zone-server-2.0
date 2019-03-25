/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.Annotations;
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ 
/*    */ public final class ResolvedMethod
/*    */   extends ResolvedParameterizedMember<Method>
/*    */   implements Comparable<ResolvedMethod>
/*    */ {
/*    */   public ResolvedMethod(ResolvedType context, Annotations ann, Method method, ResolvedType returnType, ResolvedType[] argumentTypes)
/*    */   {
/* 15 */     super(context, ann, method, returnType, argumentTypes);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isAbstract()
/*    */   {
/* 25 */     return Modifier.isAbstract(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isStrict() {
/* 29 */     return Modifier.isStrict(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isNative() {
/* 33 */     return Modifier.isNative(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isSynchronized() {
/* 37 */     return Modifier.isSynchronized(getModifiers());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ResolvedType getReturnType()
/*    */   {
/* 46 */     return getType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compareTo(ResolvedMethod other)
/*    */   {
/* 58 */     int diff = getName().compareTo(other.getName());
/* 59 */     if (diff == 0)
/*    */     {
/* 61 */       diff = getArgumentCount() - other.getArgumentCount();
/*    */     }
/* 63 */     return diff;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\ResolvedMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */