/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*    */ import org.springframework.beans.factory.support.ManagedList;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*    */ import org.springframework.util.xml.DomUtils;
/*    */ import org.springframework.web.servlet.handler.MappedInterceptor;
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
/*    */ class InterceptorsBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*    */   {
/* 45 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
/* 46 */     parserContext.pushContainingComponent(compDefinition);
/*    */     
/* 48 */     RuntimeBeanReference pathMatcherRef = null;
/* 49 */     if (element.hasAttribute("path-matcher")) {
/* 50 */       pathMatcherRef = new RuntimeBeanReference(element.getAttribute("path-matcher"));
/*    */     }
/*    */     
/* 53 */     List<Element> interceptors = DomUtils.getChildElementsByTagName(element, new String[] { "bean", "ref", "interceptor" });
/* 54 */     for (Element interceptor : interceptors) {
/* 55 */       RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/* 56 */       mappedInterceptorDef.setSource(parserContext.extractSource(interceptor));
/* 57 */       mappedInterceptorDef.setRole(2);
/*    */       
/* 59 */       ManagedList<String> includePatterns = null;
/* 60 */       ManagedList<String> excludePatterns = null;
/*    */       Object interceptorBean;
/* 62 */       Object interceptorBean; if ("interceptor".equals(interceptor.getLocalName())) {
/* 63 */         includePatterns = getIncludePatterns(interceptor, "mapping");
/* 64 */         excludePatterns = getIncludePatterns(interceptor, "exclude-mapping");
/* 65 */         Element beanElem = (Element)DomUtils.getChildElementsByTagName(interceptor, new String[] { "bean", "ref" }).get(0);
/* 66 */         interceptorBean = parserContext.getDelegate().parsePropertySubElement(beanElem, null);
/*    */       }
/*    */       else {
/* 69 */         interceptorBean = parserContext.getDelegate().parsePropertySubElement(interceptor, null);
/*    */       }
/* 71 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, includePatterns);
/* 72 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, excludePatterns);
/* 73 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(2, interceptorBean);
/*    */       
/* 75 */       if (pathMatcherRef != null) {
/* 76 */         mappedInterceptorDef.getPropertyValues().add("pathMatcher", pathMatcherRef);
/*    */       }
/*    */       
/* 79 */       String beanName = parserContext.getReaderContext().registerWithGeneratedName(mappedInterceptorDef);
/* 80 */       parserContext.registerComponent(new BeanComponentDefinition(mappedInterceptorDef, beanName));
/*    */     }
/*    */     
/* 83 */     parserContext.popAndRegisterContainingComponent();
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   private ManagedList<String> getIncludePatterns(Element interceptor, String elementName) {
/* 88 */     List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, elementName);
/* 89 */     ManagedList<String> patterns = new ManagedList(paths.size());
/* 90 */     for (Element path : paths) {
/* 91 */       patterns.add(path.getAttribute("path"));
/*    */     }
/* 93 */     return patterns;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\InterceptorsBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */