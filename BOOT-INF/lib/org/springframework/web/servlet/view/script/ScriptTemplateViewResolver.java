/*    */ package org.springframework.web.servlet.view.script;
/*    */ 
/*    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
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
/*    */ public class ScriptTemplateViewResolver
/*    */   extends UrlBasedViewResolver
/*    */ {
/*    */   public ScriptTemplateViewResolver()
/*    */   {
/* 43 */     setViewClass(requiredViewClass());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ScriptTemplateViewResolver(String prefix, String suffix)
/*    */   {
/* 54 */     this();
/* 55 */     setPrefix(prefix);
/* 56 */     setSuffix(suffix);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Class<?> requiredViewClass()
/*    */   {
/* 62 */     return ScriptTemplateView.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\script\ScriptTemplateViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */