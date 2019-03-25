/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.CollectionUtils;
/*    */ import org.springframework.cglib.core.ReflectUtils;
/*    */ import org.springframework.cglib.core.RejectModifierPredicate;
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
/*    */ class MixinEverythingEmitter
/*    */   extends MixinEmitter
/*    */ {
/*    */   public MixinEverythingEmitter(ClassVisitor v, String className, Class[] classes)
/*    */   {
/* 33 */     super(v, className, classes, null);
/*    */   }
/*    */   
/*    */   protected Class[] getInterfaces(Class[] classes) {
/* 37 */     List list = new ArrayList();
/* 38 */     for (int i = 0; i < classes.length; i++) {
/* 39 */       ReflectUtils.addAllInterfaces(classes[i], list);
/*    */     }
/* 41 */     return (Class[])list.toArray(new Class[list.size()]);
/*    */   }
/*    */   
/*    */   protected Method[] getMethods(Class type) {
/* 45 */     List methods = new ArrayList(Arrays.asList(type.getMethods()));
/* 46 */     CollectionUtils.filter(methods, new RejectModifierPredicate(24));
/* 47 */     return (Method[])methods.toArray(new Method[methods.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\MixinEverythingEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */