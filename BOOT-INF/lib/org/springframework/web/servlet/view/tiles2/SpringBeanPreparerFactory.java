/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class SpringBeanPreparerFactory
/*    */   extends AbstractSpringPreparerFactory
/*    */ {
/*    */   protected ViewPreparer getPreparer(String name, WebApplicationContext context)
/*    */     throws TilesException
/*    */   {
/* 44 */     return (ViewPreparer)context.getBean(name, ViewPreparer.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\SpringBeanPreparerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */