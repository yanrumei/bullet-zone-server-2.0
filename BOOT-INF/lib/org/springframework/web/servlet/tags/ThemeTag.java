/*    */ package org.springframework.web.servlet.tags;
/*    */ 
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.context.NoSuchMessageException;
/*    */ import org.springframework.ui.context.Theme;
/*    */ import org.springframework.web.servlet.support.RequestContext;
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
/*    */ public class ThemeTag
/*    */   extends MessageTag
/*    */ {
/*    */   protected MessageSource getMessageSource()
/*    */   {
/* 55 */     return getRequestContext().getTheme().getMessageSource();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex)
/*    */   {
/* 63 */     return "Theme '" + getRequestContext().getTheme().getName() + "': " + ex.getMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\ThemeTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */