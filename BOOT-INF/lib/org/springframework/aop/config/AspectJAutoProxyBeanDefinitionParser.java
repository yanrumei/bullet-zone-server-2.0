/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.TypedStringValue;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.ManagedList;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
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
/*    */ class AspectJAutoProxyBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*    */   {
/* 42 */     AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
/* 43 */     extendBeanDefinition(element, parserContext);
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   private void extendBeanDefinition(Element element, ParserContext parserContext)
/*    */   {
/* 49 */     BeanDefinition beanDef = parserContext.getRegistry().getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 50 */     if (element.hasChildNodes()) {
/* 51 */       addIncludePatterns(element, parserContext, beanDef);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addIncludePatterns(Element element, ParserContext parserContext, BeanDefinition beanDef) {
/* 56 */     ManagedList<TypedStringValue> includePatterns = new ManagedList();
/* 57 */     NodeList childNodes = element.getChildNodes();
/* 58 */     for (int i = 0; i < childNodes.getLength(); i++) {
/* 59 */       Node node = childNodes.item(i);
/* 60 */       if ((node instanceof Element)) {
/* 61 */         Element includeElement = (Element)node;
/* 62 */         TypedStringValue valueHolder = new TypedStringValue(includeElement.getAttribute("name"));
/* 63 */         valueHolder.setSource(parserContext.extractSource(includeElement));
/* 64 */         includePatterns.add(valueHolder);
/*    */       }
/*    */     }
/* 67 */     if (!includePatterns.isEmpty()) {
/* 68 */       includePatterns.setSource(parserContext.extractSource(element));
/* 69 */       beanDef.getPropertyValues().add("includePatterns", includePatterns);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\AspectJAutoProxyBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */