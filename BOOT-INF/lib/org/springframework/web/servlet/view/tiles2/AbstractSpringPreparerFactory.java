/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.tiles.TilesApplicationContext;
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.context.TilesRequestContext;
/*    */ import org.apache.tiles.preparer.PreparerFactory;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.servlet.DispatcherServlet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public abstract class AbstractSpringPreparerFactory
/*    */   implements PreparerFactory
/*    */ {
/*    */   public ViewPreparer getPreparer(String name, TilesRequestContext context)
/*    */     throws TilesException
/*    */   {
/* 47 */     WebApplicationContext webApplicationContext = (WebApplicationContext)context.getRequestScope().get(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*    */     
/* 49 */     if (webApplicationContext == null) {
/* 50 */       webApplicationContext = (WebApplicationContext)context.getApplicationContext().getApplicationScope().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*    */       
/* 52 */       if (webApplicationContext == null) {
/* 53 */         throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*    */       }
/*    */     }
/* 56 */     return getPreparer(name, webApplicationContext);
/*    */   }
/*    */   
/*    */   protected abstract ViewPreparer getPreparer(String paramString, WebApplicationContext paramWebApplicationContext)
/*    */     throws TilesException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\AbstractSpringPreparerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */