/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.Annotations;
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ResolvedConstructor
/*    */   extends ResolvedParameterizedMember<Constructor<?>>
/*    */ {
/*    */   public ResolvedConstructor(ResolvedType context, Annotations ann, Constructor<?> constructor, ResolvedType[] argumentTypes)
/*    */   {
/* 17 */     super(context, ann, constructor, null, argumentTypes);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\ResolvedConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */