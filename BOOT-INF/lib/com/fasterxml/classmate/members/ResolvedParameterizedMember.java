/*    */ package com.fasterxml.classmate.members;
/*    */ 
/*    */ import com.fasterxml.classmate.Annotations;
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Member;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ResolvedParameterizedMember<T extends Member>
/*    */   extends ResolvedMember<T>
/*    */ {
/*    */   protected final ResolvedType[] _paramTypes;
/*    */   protected final Annotations[] _paramAnnotations;
/*    */   
/*    */   protected ResolvedParameterizedMember(ResolvedType context, Annotations ann, T member, ResolvedType type, ResolvedType[] argumentTypes)
/*    */   {
/* 20 */     super(context, ann, member, type);
/* 21 */     this._paramTypes = (argumentTypes == null ? ResolvedType.NO_TYPES : argumentTypes);
/* 22 */     this._paramAnnotations = new Annotations[this._paramTypes.length];
/*    */   }
/*    */   
/*    */   public Annotations getParameterAnnotations(int index) {
/* 26 */     if (index >= this._paramTypes.length) {
/* 27 */       throw new IndexOutOfBoundsException("No parameter at index " + index + ", this is greater than the total number of parameters");
/*    */     }
/* 29 */     if (this._paramAnnotations[index] == null) {
/* 30 */       this._paramAnnotations[index] = new Annotations();
/*    */     }
/* 32 */     return this._paramAnnotations[index];
/*    */   }
/*    */   
/*    */   public void applyParamOverride(int index, Annotation override)
/*    */   {
/* 37 */     if (index >= this._paramAnnotations.length) {
/* 38 */       return;
/*    */     }
/* 40 */     getParameterAnnotations(index).add(override);
/*    */   }
/*    */   
/*    */   public void applyParamOverrides(int index, Annotations overrides)
/*    */   {
/* 45 */     if (index >= this._paramAnnotations.length) {
/* 46 */       return;
/*    */     }
/* 48 */     getParameterAnnotations(index).addAll(overrides);
/*    */   }
/*    */   
/*    */   public void applyParamDefault(int index, Annotation defaultValue)
/*    */   {
/* 53 */     if (index >= this._paramAnnotations.length) {
/* 54 */       return;
/*    */     }
/* 56 */     getParameterAnnotations(index).addAsDefault(defaultValue);
/*    */   }
/*    */   
/*    */   public <A extends Annotation> A getParam(int index, Class<A> cls)
/*    */   {
/* 61 */     if (index >= this._paramAnnotations.length) {
/* 62 */       return null;
/*    */     }
/* 64 */     return this._paramAnnotations[index].get(cls);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getArgumentCount()
/*    */   {
/* 71 */     return this._paramTypes.length;
/*    */   }
/*    */   
/*    */   public ResolvedType getArgumentType(int index)
/*    */   {
/* 76 */     if ((index < 0) || (index >= this._paramTypes.length)) {
/* 77 */       return null;
/*    */     }
/* 79 */     return this._paramTypes[index];
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\ResolvedParameterizedMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */