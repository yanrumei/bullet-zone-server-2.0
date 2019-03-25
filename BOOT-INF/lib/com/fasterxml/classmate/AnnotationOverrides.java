/*    */ package com.fasterxml.classmate;
/*    */ 
/*    */ import com.fasterxml.classmate.util.ClassKey;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
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
/*    */ public abstract class AnnotationOverrides
/*    */   implements Serializable
/*    */ {
/*    */   public List<Class<?>> mixInsFor(Class<?> beanClass)
/*    */   {
/* 28 */     return mixInsFor(new ClassKey(beanClass));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract List<Class<?>> mixInsFor(ClassKey paramClassKey);
/*    */   
/*    */ 
/*    */   public static StdBuilder builder()
/*    */   {
/* 38 */     return new StdBuilder();
/*    */   }
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
/*    */   public static class StdBuilder
/*    */   {
/* 54 */     protected final HashMap<ClassKey, List<Class<?>>> _targetsToOverrides = new HashMap();
/*    */     
/*    */ 
/*    */     public StdBuilder add(Class<?> target, Class<?> mixin)
/*    */     {
/* 59 */       return add(new ClassKey(target), mixin);
/*    */     }
/*    */     
/*    */     public StdBuilder add(ClassKey target, Class<?> mixin)
/*    */     {
/* 64 */       List<Class<?>> mixins = (List)this._targetsToOverrides.get(target);
/* 65 */       if (mixins == null) {
/* 66 */         mixins = new ArrayList();
/* 67 */         this._targetsToOverrides.put(target, mixins);
/*    */       }
/* 69 */       mixins.add(mixin);
/* 70 */       return this;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public AnnotationOverrides build()
/*    */     {
/* 78 */       return new AnnotationOverrides.StdImpl(this._targetsToOverrides);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static class StdImpl
/*    */     extends AnnotationOverrides
/*    */   {
/*    */     protected final HashMap<ClassKey, List<Class<?>>> _targetsToOverrides;
/*    */     
/*    */ 
/*    */     public StdImpl(HashMap<ClassKey, List<Class<?>>> overrides)
/*    */     {
/* 92 */       this._targetsToOverrides = new HashMap(overrides);
/*    */     }
/*    */     
/*    */     public List<Class<?>> mixInsFor(ClassKey target)
/*    */     {
/* 97 */       return (List)this._targetsToOverrides.get(target);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\AnnotationOverrides.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */