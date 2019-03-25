/*    */ package org.springframework.boot.web.filter;
/*    */ 
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.filter.CharacterEncodingFilter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrderedCharacterEncodingFilter
/*    */   extends CharacterEncodingFilter
/*    */   implements Ordered
/*    */ {
/* 31 */   private int order = Integer.MIN_VALUE;
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 35 */     return this.order;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setOrder(int order)
/*    */   {
/* 43 */     this.order = order;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\filter\OrderedCharacterEncodingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */