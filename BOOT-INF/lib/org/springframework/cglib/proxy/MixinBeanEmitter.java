/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.ReflectUtils;
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
/*    */ class MixinBeanEmitter
/*    */   extends MixinEmitter
/*    */ {
/*    */   public MixinBeanEmitter(ClassVisitor v, String className, Class[] classes)
/*    */   {
/* 28 */     super(v, className, classes, null);
/*    */   }
/*    */   
/*    */   protected Class[] getInterfaces(Class[] classes) {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   protected Method[] getMethods(Class type) {
/* 36 */     return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\MixinBeanEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */