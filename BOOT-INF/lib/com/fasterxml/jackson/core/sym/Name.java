/*    */ package com.fasterxml.jackson.core.sym;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Name
/*    */ {
/*    */   protected final String _name;
/*    */   
/*    */ 
/*    */   protected final int _hashCode;
/*    */   
/*    */ 
/*    */ 
/*    */   protected Name(String name, int hashCode)
/*    */   {
/* 17 */     this._name = name;
/* 18 */     this._hashCode = hashCode;
/*    */   }
/*    */   
/* 21 */   public String getName() { return this._name; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean equals(int paramInt);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean equals(int paramInt1, int paramInt2);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean equals(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract boolean equals(int[] paramArrayOfInt, int paramInt);
/*    */   
/*    */ 
/*    */ 
/* 46 */   public String toString() { return this._name; }
/*    */   
/* 48 */   public final int hashCode() { return this._hashCode; }
/*    */   
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 53 */     return o == this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\sym\Name.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */