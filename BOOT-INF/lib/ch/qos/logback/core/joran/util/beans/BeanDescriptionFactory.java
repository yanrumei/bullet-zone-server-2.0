/*    */ package ch.qos.logback.core.joran.util.beans;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class BeanDescriptionFactory
/*    */   extends ContextAwareBase
/*    */ {
/*    */   BeanDescriptionFactory(Context context)
/*    */   {
/* 23 */     setContext(context);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BeanDescription create(Class<?> clazz)
/*    */   {
/* 32 */     Map<String, Method> propertyNameToGetter = new HashMap();
/* 33 */     Map<String, Method> propertyNameToSetter = new HashMap();
/* 34 */     Map<String, Method> propertyNameToAdder = new HashMap();
/* 35 */     Method[] methods = clazz.getMethods();
/* 36 */     for (Method method : methods) {
/* 37 */       if (!method.isBridge())
/*    */       {
/*    */ 
/*    */ 
/* 41 */         if (BeanUtil.isGetter(method)) {
/* 42 */           String propertyName = BeanUtil.getPropertyName(method);
/* 43 */           Method oldGetter = (Method)propertyNameToGetter.put(propertyName, method);
/* 44 */           if (oldGetter != null) {
/* 45 */             if (oldGetter.getName().startsWith("is")) {
/* 46 */               propertyNameToGetter.put(propertyName, oldGetter);
/*    */             }
/* 48 */             String message = String.format("Class '%s' contains multiple getters for the same property '%s'.", new Object[] { clazz.getCanonicalName(), propertyName });
/* 49 */             addWarn(message);
/*    */           }
/* 51 */         } else if (BeanUtil.isSetter(method)) {
/* 52 */           String propertyName = BeanUtil.getPropertyName(method);
/* 53 */           Method oldSetter = (Method)propertyNameToSetter.put(propertyName, method);
/* 54 */           if (oldSetter != null) {
/* 55 */             String message = String.format("Class '%s' contains multiple setters for the same property '%s'.", new Object[] { clazz.getCanonicalName(), propertyName });
/* 56 */             addWarn(message);
/*    */           }
/* 58 */         } else if (BeanUtil.isAdder(method)) {
/* 59 */           String propertyName = BeanUtil.getPropertyName(method);
/* 60 */           Method oldAdder = (Method)propertyNameToAdder.put(propertyName, method);
/* 61 */           if (oldAdder != null) {
/* 62 */             String message = String.format("Class '%s' contains multiple adders for the same property '%s'.", new Object[] { clazz.getCanonicalName(), propertyName });
/* 63 */             addWarn(message);
/*    */           }
/*    */         } }
/*    */     }
/* 67 */     return new BeanDescription(clazz, propertyNameToGetter, propertyNameToSetter, propertyNameToAdder);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\jora\\util\beans\BeanDescriptionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */