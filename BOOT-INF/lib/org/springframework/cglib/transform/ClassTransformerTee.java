/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
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
/*    */ public class ClassTransformerTee
/*    */   extends ClassTransformer
/*    */ {
/*    */   private ClassVisitor branch;
/*    */   
/*    */   public ClassTransformerTee(ClassVisitor branch)
/*    */   {
/* 25 */     super(393216);
/* 26 */     this.branch = branch;
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 30 */     this.cv = new ClassVisitorTee(this.branch, target);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\ClassTransformerTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */