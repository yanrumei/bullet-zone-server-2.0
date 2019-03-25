/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.asm.Attribute;
/*    */ import org.springframework.asm.FieldVisitor;
/*    */ import org.springframework.asm.TypePath;
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
/*    */ public class FieldVisitorTee
/*    */   extends FieldVisitor
/*    */ {
/*    */   private FieldVisitor fv1;
/*    */   private FieldVisitor fv2;
/*    */   
/*    */   public FieldVisitorTee(FieldVisitor fv1, FieldVisitor fv2)
/*    */   {
/* 28 */     super(393216);
/* 29 */     this.fv1 = fv1;
/* 30 */     this.fv2 = fv2;
/*    */   }
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 34 */     return AnnotationVisitorTee.getInstance(this.fv1.visitAnnotation(desc, visible), this.fv2
/* 35 */       .visitAnnotation(desc, visible));
/*    */   }
/*    */   
/*    */   public void visitAttribute(Attribute attr) {
/* 39 */     this.fv1.visitAttribute(attr);
/* 40 */     this.fv2.visitAttribute(attr);
/*    */   }
/*    */   
/*    */   public void visitEnd() {
/* 44 */     this.fv1.visitEnd();
/* 45 */     this.fv2.visitEnd();
/*    */   }
/*    */   
/*    */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 49 */     return AnnotationVisitorTee.getInstance(this.fv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.fv2
/* 50 */       .visitTypeAnnotation(typeRef, typePath, desc, visible));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\FieldVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */