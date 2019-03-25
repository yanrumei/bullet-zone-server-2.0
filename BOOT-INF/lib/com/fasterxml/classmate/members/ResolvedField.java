/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.Annotations;
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ 
/*    */ public final class ResolvedField
/*    */   extends ResolvedMember<Field>
/*    */   implements Comparable<ResolvedField>
/*    */ {
/*    */   public ResolvedField(ResolvedType context, Annotations ann, Field field, ResolvedType type)
/*    */   {
/* 15 */     super(context, ann, field, type);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isTransient()
/*    */   {
/* 25 */     return Modifier.isTransient(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isVolatile() {
/* 29 */     return Modifier.isVolatile(getModifiers());
/*    */   }
/*    */   
/*    */   public int compareTo(ResolvedField other)
/*    */   {
/* 34 */     return getName().compareTo(other.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\ResolvedField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */