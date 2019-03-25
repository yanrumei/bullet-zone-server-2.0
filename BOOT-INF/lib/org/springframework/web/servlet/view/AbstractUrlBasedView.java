/*    */ package org.springframework.web.servlet.view;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public abstract class AbstractUrlBasedView
/*    */   extends AbstractView
/*    */   implements InitializingBean
/*    */ {
/*    */   private String url;
/*    */   
/*    */   protected AbstractUrlBasedView() {}
/*    */   
/*    */   protected AbstractUrlBasedView(String url)
/*    */   {
/* 46 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUrl(String url)
/*    */   {
/* 55 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getUrl()
/*    */   {
/* 62 */     return this.url;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws Exception
/*    */   {
/* 67 */     if ((isUrlRequired()) && (getUrl() == null)) {
/* 68 */       throw new IllegalArgumentException("Property 'url' is required");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isUrlRequired()
/*    */   {
/* 78 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean checkResource(Locale locale)
/*    */     throws Exception
/*    */   {
/* 90 */     return true;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 95 */     return super.toString() + "; URL [" + getUrl() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\AbstractUrlBasedView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */