/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import javax.servlet.Filter;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class FilterRegistrationBean
/*    */   extends AbstractFilterRegistrationBean
/*    */ {
/*    */   public static final int REQUEST_WRAPPER_FILTER_MAX_ORDER = 0;
/*    */   private Filter filter;
/*    */   
/*    */   public FilterRegistrationBean()
/*    */   {
/* 54 */     super(new ServletRegistrationBean[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FilterRegistrationBean(Filter filter, ServletRegistrationBean... servletRegistrationBeans)
/*    */   {
/* 65 */     super(servletRegistrationBeans);
/* 66 */     Assert.notNull(filter, "Filter must not be null");
/* 67 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public Filter getFilter()
/*    */   {
/* 72 */     return this.filter;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFilter(Filter filter)
/*    */   {
/* 80 */     Assert.notNull(filter, "Filter must not be null");
/* 81 */     this.filter = filter;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\FilterRegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */