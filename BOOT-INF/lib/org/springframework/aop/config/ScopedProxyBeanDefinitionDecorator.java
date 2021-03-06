/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.aop.scope.ScopedProxyUtils;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ class ScopedProxyBeanDefinitionDecorator
/*    */   implements BeanDefinitionDecorator
/*    */ {
/*    */   private static final String PROXY_TARGET_CLASS = "proxy-target-class";
/*    */   
/*    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
/*    */   {
/* 44 */     boolean proxyTargetClass = true;
/* 45 */     if ((node instanceof Element)) {
/* 46 */       Element ele = (Element)node;
/* 47 */       if (ele.hasAttribute("proxy-target-class")) {
/* 48 */         proxyTargetClass = Boolean.valueOf(ele.getAttribute("proxy-target-class")).booleanValue();
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 55 */     BeanDefinitionHolder holder = ScopedProxyUtils.createScopedProxy(definition, parserContext.getRegistry(), proxyTargetClass);
/* 56 */     String targetBeanName = ScopedProxyUtils.getTargetBeanName(definition.getBeanName());
/* 57 */     parserContext.getReaderContext().fireComponentRegistered(new BeanComponentDefinition(definition
/* 58 */       .getBeanDefinition(), targetBeanName));
/* 59 */     return holder;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\ScopedProxyBeanDefinitionDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */