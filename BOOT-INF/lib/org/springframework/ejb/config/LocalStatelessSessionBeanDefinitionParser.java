/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LocalStatelessSessionBeanDefinitionParser
/*    */   extends AbstractJndiLocatingBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 36 */     return "org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\config\LocalStatelessSessionBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */