/*    */ package edu.unh.cs.cs619.bulletzone.util;
/*    */ 
/*    */ 
/*    */ public class GridWrapper
/*    */ {
/*    */   private int[][] grid;
/*    */   private long timeStamp;
/*    */   
/*    */   public GridWrapper(int[][] grid)
/*    */   {
/* 11 */     this.grid = grid;
/* 12 */     this.timeStamp = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public int[][] getGrid() {
/* 16 */     return this.grid;
/*    */   }
/*    */   
/*    */   public void setGrid(int[][] grid) {
/* 20 */     this.grid = grid;
/*    */   }
/*    */   
/*    */   public long getTimeStamp() {
/* 24 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public void setTimeStamp(long timeStamp) {
/* 28 */     this.timeStamp = timeStamp;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzon\\util\GridWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */