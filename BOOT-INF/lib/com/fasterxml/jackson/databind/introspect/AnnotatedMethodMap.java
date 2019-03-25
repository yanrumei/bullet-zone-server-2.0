/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AnnotatedMethodMap
/*    */   implements Iterable<AnnotatedMethod>
/*    */ {
/*    */   protected LinkedHashMap<MemberKey, AnnotatedMethod> _methods;
/*    */   
/*    */   public void add(AnnotatedMethod am)
/*    */   {
/* 23 */     if (this._methods == null) {
/* 24 */       this._methods = new LinkedHashMap();
/*    */     }
/* 26 */     this._methods.put(new MemberKey(am.getAnnotated()), am);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AnnotatedMethod remove(AnnotatedMethod am)
/*    */   {
/* 35 */     return remove(am.getAnnotated());
/*    */   }
/*    */   
/*    */   public AnnotatedMethod remove(Method m)
/*    */   {
/* 40 */     if (this._methods != null) {
/* 41 */       return (AnnotatedMethod)this._methods.remove(new MemberKey(m));
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 47 */     return (this._methods == null) || (this._methods.size() == 0);
/*    */   }
/*    */   
/*    */   public int size() {
/* 51 */     return this._methods == null ? 0 : this._methods.size();
/*    */   }
/*    */   
/*    */   public AnnotatedMethod find(String name, Class<?>[] paramTypes)
/*    */   {
/* 56 */     if (this._methods == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     return (AnnotatedMethod)this._methods.get(new MemberKey(name, paramTypes));
/*    */   }
/*    */   
/*    */   public AnnotatedMethod find(Method m)
/*    */   {
/* 64 */     if (this._methods == null) {
/* 65 */       return null;
/*    */     }
/* 67 */     return (AnnotatedMethod)this._methods.get(new MemberKey(m));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Iterator<AnnotatedMethod> iterator()
/*    */   {
/* 79 */     if (this._methods != null) {
/* 80 */       return this._methods.values().iterator();
/*    */     }
/* 82 */     List<AnnotatedMethod> empty = Collections.emptyList();
/* 83 */     return empty.iterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMethodMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */