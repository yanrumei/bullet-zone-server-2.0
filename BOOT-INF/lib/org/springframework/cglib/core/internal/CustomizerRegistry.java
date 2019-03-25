/*    */ package org.springframework.cglib.core.internal;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.cglib.core.Customizer;
/*    */ import org.springframework.cglib.core.KeyFactoryCustomizer;
/*    */ 
/*    */ public class CustomizerRegistry
/*    */ {
/*    */   private final Class[] customizerTypes;
/* 11 */   private Map<Class, List<KeyFactoryCustomizer>> customizers = new java.util.HashMap();
/*    */   
/*    */   public CustomizerRegistry(Class[] customizerTypes) {
/* 14 */     this.customizerTypes = customizerTypes;
/*    */   }
/*    */   
/*    */   public void add(KeyFactoryCustomizer customizer) {
/* 18 */     Class<? extends KeyFactoryCustomizer> klass = customizer.getClass();
/* 19 */     for (Class type : this.customizerTypes) {
/* 20 */       if (type.isAssignableFrom(klass)) {
/* 21 */         List<KeyFactoryCustomizer> list = (List)this.customizers.get(type);
/* 22 */         if (list == null) {
/* 23 */           this.customizers.put(type, list = new java.util.ArrayList());
/*    */         }
/* 25 */         list.add(customizer);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public <T> List<T> get(Class<T> klass) {
/* 31 */     List<KeyFactoryCustomizer> list = (List)this.customizers.get(klass);
/* 32 */     if (list == null) {
/* 33 */       return java.util.Collections.emptyList();
/*    */     }
/* 35 */     return list;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static CustomizerRegistry singleton(Customizer customizer)
/*    */   {
/* 44 */     CustomizerRegistry registry = new CustomizerRegistry(new Class[] { Customizer.class });
/* 45 */     registry.add(customizer);
/* 46 */     return registry;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\internal\CustomizerRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */