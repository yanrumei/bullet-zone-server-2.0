/*     */ package org.springframework.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotationVisitor
/*     */ {
/*     */   protected final int api;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotationVisitor av;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationVisitor(int api)
/*     */   {
/*  62 */     this(api, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationVisitor(int api, AnnotationVisitor av)
/*     */   {
/*  76 */     if ((api < 262144) || (api > 393216)) {
/*  77 */       throw new IllegalArgumentException();
/*     */     }
/*  79 */     this.api = api;
/*  80 */     this.av = av;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void visit(String name, Object value)
/*     */   {
/*  99 */     if (this.av != null) {
/* 100 */       this.av.visit(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void visitEnum(String name, String desc, String value)
/*     */   {
/* 115 */     if (this.av != null) {
/* 116 */       this.av.visitEnum(name, desc, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationVisitor visitAnnotation(String name, String desc)
/*     */   {
/* 134 */     if (this.av != null) {
/* 135 */       return this.av.visitAnnotation(name, desc);
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationVisitor visitArray(String name)
/*     */   {
/* 155 */     if (this.av != null) {
/* 156 */       return this.av.visitArray(name);
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void visitEnd()
/*     */   {
/* 165 */     if (this.av != null) {
/* 166 */       this.av.visitEnd();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\asm\AnnotationVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */