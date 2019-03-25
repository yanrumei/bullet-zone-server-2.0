/*    */ package org.springframework.web.servlet.view.freemarker;
/*    */ 
/*    */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
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
/*    */ public class FreeMarkerViewResolver
/*    */   extends AbstractTemplateViewResolver
/*    */ {
/*    */   public FreeMarkerViewResolver()
/*    */   {
/* 48 */     setViewClass(requiredViewClass());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FreeMarkerViewResolver(String prefix, String suffix)
/*    */   {
/* 59 */     this();
/* 60 */     setPrefix(prefix);
/* 61 */     setSuffix(suffix);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Class<?> requiredViewClass()
/*    */   {
/* 70 */     return FreeMarkerView.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\freemarker\FreeMarkerViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */