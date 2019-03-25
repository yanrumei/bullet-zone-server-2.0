/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.jsp.PageContext;
/*    */ import org.apache.tiles.context.TilesRequestContext;
/*    */ import org.apache.tiles.jsp.context.JspTilesRequestContext;
/*    */ import org.apache.tiles.locale.impl.DefaultLocaleResolver;
/*    */ import org.apache.tiles.servlet.context.ServletTilesRequestContext;
/*    */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*    */ @Deprecated
/*    */ public class SpringLocaleResolver
/*    */   extends DefaultLocaleResolver
/*    */ {
/*    */   public Locale resolveLocale(TilesRequestContext context)
/*    */   {
/* 52 */     if ((context instanceof JspTilesRequestContext)) {
/* 53 */       PageContext pc = ((JspTilesRequestContext)context).getPageContext();
/* 54 */       return RequestContextUtils.getLocale((HttpServletRequest)pc.getRequest());
/*    */     }
/* 56 */     if ((context instanceof ServletTilesRequestContext)) {
/* 57 */       HttpServletRequest request = ((ServletTilesRequestContext)context).getRequest();
/* 58 */       if (request != null) {
/* 59 */         return RequestContextUtils.getLocale(request);
/*    */       }
/*    */     }
/* 62 */     return super.resolveLocale(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\SpringLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */