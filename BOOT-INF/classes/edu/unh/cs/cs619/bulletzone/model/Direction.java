/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ public enum Direction {
/*  4 */   Up,  Down,  Left,  Right;
/*    */   
/*    */   private Direction() {}
/*  7 */   public static Direction fromByte(byte directionByte) { Direction direction = null;
/*    */     
/*  9 */     switch (directionByte) {
/*    */     case 0: 
/* 11 */       direction = Up;
/* 12 */       break;
/*    */     case 2: 
/* 14 */       direction = Right;
/*    */       
/* 16 */       break;
/*    */     case 4: 
/* 18 */       direction = Down;
/*    */       
/* 20 */       break;
/*    */     case 6: 
/* 22 */       direction = Left;
/*    */       
/* 24 */       break;
/*    */     }
/*    */     
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 31 */     return direction;
/*    */   }
/*    */   
/*    */   public static byte toByte(Direction direction)
/*    */   {
/* 36 */     switch (Direction.1.$SwitchMap$edu$unh$cs$cs619$bulletzone$model$Direction[direction.ordinal()]) {
/*    */     case 1: 
/* 38 */       return 4;
/*    */     case 2: 
/* 40 */       return 6;
/*    */     case 3: 
/* 42 */       return 2;
/*    */     case 4: 
/* 44 */       return 0;
/*    */     }
/* 46 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Direction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */