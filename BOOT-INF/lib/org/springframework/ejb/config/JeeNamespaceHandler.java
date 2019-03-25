/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
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
/*    */ public class JeeNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init()
/*    */   {
/* 32 */     registerBeanDefinitionParser("jndi-lookup", new JndiLookupBeanDefinitionParser());
/* 33 */     registerBeanDefinitionParser("local-slsb", new LocalStatelessSessionBeanDefinitionParser());
/* 34 */     registerBeanDefinitionParser("remote-slsb", new RemoteStatelessSessionBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\config\JeeNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */