/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ 
/*    */ public class NumberField
/*    */   extends FieldEntity
/*    */ {
/*    */   private static final String TAG = "NumberField";
/*    */   
/*    */   private final int value;
/*    */   
/*    */   public NumberField(int value)
/*    */   {
/* 13 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getIntValue()
/*    */   {
/* 18 */     return 0;
/*    */   }
/*    */   
/*    */   public FieldEntity copy()
/*    */   {
/* 23 */     return null;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 28 */     return Integer.toString(this.value == 1000 ? 1 : 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\NumberField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */