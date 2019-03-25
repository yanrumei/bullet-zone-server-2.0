/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanNameUrlHandlerMapping
/*    */   extends AbstractDetectingUrlHandlerMapping
/*    */ {
/*    */   protected String[] determineUrlsForHandler(String beanName)
/*    */   {
/* 58 */     List<String> urls = new ArrayList();
/* 59 */     if (beanName.startsWith("/")) {
/* 60 */       urls.add(beanName);
/*    */     }
/* 62 */     String[] aliases = getApplicationContext().getAliases(beanName);
/* 63 */     for (String alias : aliases) {
/* 64 */       if (alias.startsWith("/")) {
/* 65 */         urls.add(alias);
/*    */       }
/*    */     }
/* 68 */     return StringUtils.toStringArray(urls);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\BeanNameUrlHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */