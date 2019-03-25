/*    */ package com.fasterxml.classmate.util;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import com.fasterxml.classmate.types.ResolvedRecursiveType;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassStack
/*    */ {
/*    */   protected final ClassStack _parent;
/*    */   protected final Class<?> _current;
/*    */   private ArrayList<ResolvedRecursiveType> _selfRefs;
/*    */   
/*    */   public ClassStack(Class<?> rootType)
/*    */   {
/* 20 */     this(null, rootType);
/*    */   }
/*    */   
/*    */   private ClassStack(ClassStack parent, Class<?> curr) {
/* 24 */     this._parent = parent;
/* 25 */     this._current = curr;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClassStack child(Class<?> cls)
/*    */   {
/* 33 */     return new ClassStack(this, cls);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addSelfReference(ResolvedRecursiveType ref)
/*    */   {
/* 42 */     if (this._selfRefs == null) {
/* 43 */       this._selfRefs = new ArrayList();
/*    */     }
/* 45 */     this._selfRefs.add(ref);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resolveSelfReferences(ResolvedType resolved)
/*    */   {
/* 55 */     if (this._selfRefs != null) {
/* 56 */       for (ResolvedRecursiveType ref : this._selfRefs) {
/* 57 */         ref.setReference(resolved);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public ClassStack find(Class<?> cls)
/*    */   {
/* 64 */     if (this._current == cls) return this;
/* 65 */     for (ClassStack curr = this._parent; curr != null; curr = curr._parent) {
/* 66 */       if (curr._current == cls) {
/* 67 */         return curr;
/*    */       }
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmat\\util\ClassStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */