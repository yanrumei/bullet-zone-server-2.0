/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import org.springframework.cglib.core.ClassGenerator;
/*    */ import org.springframework.cglib.core.DefaultGeneratorStrategy;
/*    */ import org.springframework.cglib.core.TypeUtils;
/*    */ import org.springframework.cglib.transform.ClassTransformer;
/*    */ import org.springframework.cglib.transform.MethodFilter;
/*    */ import org.springframework.cglib.transform.MethodFilterTransformer;
/*    */ import org.springframework.cglib.transform.TransformingClassGenerator;
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
/*    */ public class UndeclaredThrowableStrategy
/*    */   extends DefaultGeneratorStrategy
/*    */ {
/*    */   private Class wrapper;
/*    */   
/*    */   public UndeclaredThrowableStrategy(Class wrapper)
/*    */   {
/* 46 */     this.wrapper = wrapper;
/*    */   }
/*    */   
/* 49 */   private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() {
/*    */     public boolean accept(int access, String name, String desc, String signature, String[] exceptions) {
/* 51 */       return (!TypeUtils.isPrivate(access)) && (name.indexOf('$') < 0);
/*    */     }
/*    */   };
/*    */   
/*    */   protected ClassGenerator transform(ClassGenerator cg) throws Exception {
/* 56 */     ClassTransformer tr = new UndeclaredThrowableTransformer(this.wrapper);
/* 57 */     tr = new MethodFilterTransformer(TRANSFORM_FILTER, tr);
/* 58 */     return new TransformingClassGenerator(cg, tr);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\impl\UndeclaredThrowableStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */