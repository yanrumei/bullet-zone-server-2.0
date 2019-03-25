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
/*    */ class RemoteStatelessSessionBeanDefinitionParser
/*    */   extends AbstractJndiLocatingBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 36 */     return "org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\config\RemoteStatelessSessionBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */