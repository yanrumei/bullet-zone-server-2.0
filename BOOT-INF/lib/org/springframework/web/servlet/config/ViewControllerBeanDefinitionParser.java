/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*     */ import org.springframework.web.servlet.view.RedirectView;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ViewControllerBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String HANDLER_MAPPING_BEAN_NAME = "org.springframework.web.servlet.config.viewControllerHandlerMapping";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*     */   {
/*  64 */     Object source = parserContext.extractSource(element);
/*     */     
/*     */ 
/*  67 */     BeanDefinition hm = registerHandlerMapping(parserContext, source);
/*     */     
/*     */ 
/*  70 */     MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
/*     */     
/*     */ 
/*  73 */     RootBeanDefinition controller = new RootBeanDefinition(ParameterizableViewController.class);
/*  74 */     controller.setSource(source);
/*     */     
/*  76 */     HttpStatus statusCode = null;
/*  77 */     if (element.hasAttribute("status-code")) {
/*  78 */       int statusValue = Integer.valueOf(element.getAttribute("status-code")).intValue();
/*  79 */       statusCode = HttpStatus.valueOf(statusValue);
/*     */     }
/*     */     
/*  82 */     String name = element.getLocalName();
/*  83 */     if (name.equals("view-controller")) {
/*  84 */       if (element.hasAttribute("view-name")) {
/*  85 */         controller.getPropertyValues().add("viewName", element.getAttribute("view-name"));
/*     */       }
/*  87 */       if (statusCode != null) {
/*  88 */         controller.getPropertyValues().add("statusCode", statusCode);
/*     */       }
/*     */     }
/*  91 */     else if (name.equals("redirect-view-controller")) {
/*  92 */       controller.getPropertyValues().add("view", getRedirectView(element, statusCode, source));
/*     */     }
/*  94 */     else if (name.equals("status-controller")) {
/*  95 */       controller.getPropertyValues().add("statusCode", statusCode);
/*  96 */       controller.getPropertyValues().add("statusOnly", Boolean.valueOf(true));
/*     */     }
/*     */     else
/*     */     {
/* 100 */       throw new IllegalStateException("Unexpected tag name: " + name);
/*     */     }
/*     */     Map<String, BeanDefinition> urlMap;
/*     */     Map<String, BeanDefinition> urlMap;
/* 104 */     if (hm.getPropertyValues().contains("urlMap")) {
/* 105 */       urlMap = (Map)hm.getPropertyValues().getPropertyValue("urlMap").getValue();
/*     */     }
/*     */     else {
/* 108 */       urlMap = new ManagedMap();
/* 109 */       hm.getPropertyValues().add("urlMap", urlMap);
/*     */     }
/* 111 */     urlMap.put(element.getAttribute("path"), controller);
/*     */     
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   private BeanDefinition registerHandlerMapping(ParserContext context, Object source) {
/* 117 */     if (context.getRegistry().containsBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping")) {
/* 118 */       return context.getRegistry().getBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping");
/*     */     }
/* 120 */     RootBeanDefinition beanDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 121 */     beanDef.setRole(2);
/* 122 */     context.getRegistry().registerBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping", beanDef);
/* 123 */     context.registerComponent(new BeanComponentDefinition(beanDef, "org.springframework.web.servlet.config.viewControllerHandlerMapping"));
/*     */     
/* 125 */     beanDef.setSource(source);
/* 126 */     beanDef.getPropertyValues().add("order", "1");
/* 127 */     beanDef.getPropertyValues().add("pathMatcher", MvcNamespaceUtils.registerPathMatcher(null, context, source));
/* 128 */     beanDef.getPropertyValues().add("urlPathHelper", MvcNamespaceUtils.registerUrlPathHelper(null, context, source));
/* 129 */     RuntimeBeanReference corsConfigurationsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
/* 130 */     beanDef.getPropertyValues().add("corsConfigurations", corsConfigurationsRef);
/*     */     
/* 132 */     return beanDef;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition getRedirectView(Element element, HttpStatus status, Object source) {
/* 136 */     RootBeanDefinition redirectView = new RootBeanDefinition(RedirectView.class);
/* 137 */     redirectView.setSource(source);
/* 138 */     redirectView.getConstructorArgumentValues().addIndexedArgumentValue(0, element.getAttribute("redirect-url"));
/*     */     
/* 140 */     if (status != null) {
/* 141 */       redirectView.getPropertyValues().add("statusCode", status);
/*     */     }
/*     */     
/* 144 */     if (element.hasAttribute("context-relative")) {
/* 145 */       redirectView.getPropertyValues().add("contextRelative", element.getAttribute("context-relative"));
/*     */     }
/*     */     else {
/* 148 */       redirectView.getPropertyValues().add("contextRelative", Boolean.valueOf(true));
/*     */     }
/*     */     
/* 151 */     if (element.hasAttribute("keep-query-params")) {
/* 152 */       redirectView.getPropertyValues().add("propagateQueryParams", element.getAttribute("keep-query-params"));
/*     */     }
/*     */     
/* 155 */     return redirectView;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\ViewControllerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */