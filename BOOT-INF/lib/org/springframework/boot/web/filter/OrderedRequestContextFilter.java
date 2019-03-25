/*    */ package org.springframework.boot.web.filter;
/*    */ 
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.filter.RequestContextFilter;
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
/*    */ 
/*    */ public class OrderedRequestContextFilter
/*    */   extends RequestContextFilter
/*    */   implements Ordered
/*    */ {
/* 32 */   private int order = -105;
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 36 */     return this.order;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setOrder(int order)
/*    */   {
/* 44 */     this.order = order;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\filter\OrderedRequestContextFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */