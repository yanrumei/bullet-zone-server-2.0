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
/*    */ public abstract class ClassTransformer
/*    */   extends ClassVisitor
/*    */ {
/*    */   public ClassTransformer()
/*    */   {
/* 23 */     super(393216);
/*    */   }
/*    */   
/* 26 */   public ClassTransformer(int opcode) { super(opcode); }
/*    */   
/*    */   public abstract void setTarget(ClassVisitor paramClassVisitor);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\ClassTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */