/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import org.springframework.asm.Label;
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
/*    */ public class Block
/*    */ {
/*    */   private CodeEmitter e;
/*    */   private Label start;
/*    */   private Label end;
/*    */   
/*    */   public Block(CodeEmitter e)
/*    */   {
/* 27 */     this.e = e;
/* 28 */     this.start = e.mark();
/*    */   }
/*    */   
/*    */   public CodeEmitter getCodeEmitter() {
/* 32 */     return this.e;
/*    */   }
/*    */   
/*    */   public void end() {
/* 36 */     if (this.end != null) {
/* 37 */       throw new IllegalStateException("end of label already set");
/*    */     }
/* 39 */     this.end = this.e.mark();
/*    */   }
/*    */   
/*    */   public Label getStart() {
/* 43 */     return this.start;
/*    */   }
/*    */   
/*    */   public Label getEnd() {
/* 47 */     return this.end;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\Block.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */