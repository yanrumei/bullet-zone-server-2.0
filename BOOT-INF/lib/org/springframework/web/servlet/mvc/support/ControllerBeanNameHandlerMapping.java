/*    */ package org.springframework.web.servlet.mvc.support;
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
/*    */ @Deprecated
/*    */ public class ControllerBeanNameHandlerMapping
/*    */   extends AbstractControllerUrlHandlerMapping
/*    */ {
/* 45 */   private String urlPrefix = "";
/*    */   
/* 47 */   private String urlSuffix = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUrlPrefix(String urlPrefix)
/*    */   {
/* 56 */     this.urlPrefix = (urlPrefix != null ? urlPrefix : "");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUrlSuffix(String urlSuffix)
/*    */   {
/* 65 */     this.urlSuffix = (urlSuffix != null ? urlSuffix : "");
/*    */   }
/*    */   
/*    */ 
/*    */   protected String[] buildUrlsForHandler(String beanName, Class<?> beanClass)
/*    */   {
/* 71 */     List<String> urls = new ArrayList();
/* 72 */     urls.add(generatePathMapping(beanName));
/* 73 */     String[] aliases = getApplicationContext().getAliases(beanName);
/* 74 */     for (String alias : aliases) {
/* 75 */       urls.add(generatePathMapping(alias));
/*    */     }
/* 77 */     return StringUtils.toStringArray(urls);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected String generatePathMapping(String beanName)
/*    */   {
/* 84 */     String name = "/" + beanName;
/* 85 */     StringBuilder path = new StringBuilder();
/* 86 */     if (!name.startsWith(this.urlPrefix)) {
/* 87 */       path.append(this.urlPrefix);
/*    */     }
/* 89 */     path.append(name);
/* 90 */     if (!name.endsWith(this.urlSuffix)) {
/* 91 */       path.append(this.urlSuffix);
/*    */     }
/* 93 */     return path.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\ControllerBeanNameHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */