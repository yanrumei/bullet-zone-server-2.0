/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassFilterTransformer
/*    */   extends AbstractClassFilterTransformer
/*    */ {
/*    */   private ClassFilter filter;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass)
/*    */   {
/* 24 */     super(pass);
/* 25 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   protected boolean accept(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 29 */     return this.filter.accept(name.replace('/', '.'));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\ClassFilterTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */