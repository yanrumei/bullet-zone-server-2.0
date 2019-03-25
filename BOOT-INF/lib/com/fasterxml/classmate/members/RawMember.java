/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Modifier;
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
/*    */ public abstract class RawMember
/*    */ {
/*    */   protected final ResolvedType _declaringType;
/*    */   
/*    */   protected RawMember(ResolvedType context)
/*    */   {
/* 33 */     this._declaringType = context;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final ResolvedType getDeclaringType()
/*    */   {
/* 43 */     return this._declaringType;
/*    */   }
/*    */   
/*    */   public abstract Member getRawMember();
/*    */   
/*    */   public String getName() {
/* 49 */     return getRawMember().getName();
/*    */   }
/*    */   
/*    */   public boolean isStatic() {
/* 53 */     return Modifier.isStatic(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isFinal() {
/* 57 */     return Modifier.isFinal(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isPrivate() {
/* 61 */     return Modifier.isPrivate(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isProtected() {
/* 65 */     return Modifier.isProtected(getModifiers());
/*    */   }
/*    */   
/*    */   public boolean isPublic() {
/* 69 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */   
/*    */   public Annotation[] getAnnotations() {
/* 73 */     return ((AnnotatedElement)getRawMember()).getAnnotations();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract int hashCode();
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 88 */     return getName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final int getModifiers()
/*    */   {
/* 97 */     return getRawMember().getModifiers();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\RawMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */