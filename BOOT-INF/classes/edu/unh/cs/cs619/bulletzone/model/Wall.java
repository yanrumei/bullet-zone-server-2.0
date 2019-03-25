/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ public class Wall extends FieldEntity {
/*    */   int destructValue;
/*    */   int pos;
/*    */   
/*  7 */   public Wall() { this.destructValue = 1000; }
/*    */   
/*    */   public Wall(int destructValue, int pos)
/*    */   {
/* 11 */     this.destructValue = destructValue;
/* 12 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public FieldEntity copy()
/*    */   {
/* 17 */     return new Wall();
/*    */   }
/*    */   
/*    */   public int getIntValue()
/*    */   {
/* 22 */     return this.destructValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 27 */     return "W";
/*    */   }
/*    */   
/*    */   public int getPos() {
/* 31 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Wall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */