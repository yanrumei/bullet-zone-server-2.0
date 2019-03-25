/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FieldEntity
/*    */ {
/*    */   protected FieldHolder parent;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract int getIntValue();
/*    */   
/*    */ 
/*    */ 
/*    */   public FieldHolder getParent()
/*    */   {
/* 19 */     return this.parent;
/*    */   }
/*    */   
/*    */   public void setParent(FieldHolder parent) {
/* 23 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public abstract FieldEntity copy();
/*    */   
/*    */   public void hit(int damage) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\FieldEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */