/*    */ package org.springframework.boot.web.filter;
/*    */ 
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.filter.HttpPutFormContentFilter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrderedHttpPutFormContentFilter
/*    */   extends HttpPutFormContentFilter
/*    */   implements Ordered
/*    */ {
/*    */   public static final int DEFAULT_ORDER = -9900;
/* 38 */   private int order = 55636;
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 42 */     return this.order;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setOrder(int order)
/*    */   {
/* 50 */     this.order = order;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\filter\OrderedHttpPutFormContentFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */