/*    */ package edu.unh.cs.cs619.bulletzone.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class ResultWrapper<T>
/*    */   implements Serializable
/*    */ {
/*    */   private T result;
/*    */   
/*    */   public ResultWrapper(T result)
/*    */   {
/* 12 */     this.result = result;
/*    */   }
/*    */   
/*    */   public T getResult() {
/* 16 */     return (T)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzon\\util\ResultWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */