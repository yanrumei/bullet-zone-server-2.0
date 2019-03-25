/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
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
/*    */ class PropertyOverrideBeanDefinitionParser
/*    */   extends AbstractPropertyLoadingBeanDefinitionParser
/*    */ {
/*    */   protected Class<?> getBeanClass(Element element)
/*    */   {
/* 36 */     return PropertyOverrideConfigurer.class;
/*    */   }
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*    */   {
/* 41 */     super.doParse(element, parserContext, builder);
/*    */     
/* 43 */     builder.addPropertyValue("ignoreInvalidKeys", 
/* 44 */       Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\config\PropertyOverrideBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */