/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/*     */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public abstract class MvcNamespaceUtils
/*     */ {
/*  45 */   private static final String BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME = BeanNameUrlHandlerMapping.class
/*  46 */     .getName();
/*     */   
/*  48 */   private static final String SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME = SimpleControllerHandlerAdapter.class
/*  49 */     .getName();
/*     */   
/*  51 */   private static final String HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME = HttpRequestHandlerAdapter.class
/*  52 */     .getName();
/*     */   
/*     */   private static final String URL_PATH_HELPER_BEAN_NAME = "mvcUrlPathHelper";
/*     */   
/*     */   private static final String PATH_MATCHER_BEAN_NAME = "mvcPathMatcher";
/*     */   
/*     */   private static final String CORS_CONFIGURATION_BEAN_NAME = "mvcCorsConfigurations";
/*     */   
/*     */   private static final String HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME = "mvcHandlerMappingIntrospector";
/*     */   
/*     */   public static void registerDefaultComponents(ParserContext parserContext, Object source)
/*     */   {
/*  64 */     registerBeanNameUrlHandlerMapping(parserContext, source);
/*  65 */     registerHttpRequestHandlerAdapter(parserContext, source);
/*  66 */     registerSimpleControllerHandlerAdapter(parserContext, source);
/*  67 */     registerHandlerMappingIntrospector(parserContext, source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RuntimeBeanReference registerUrlPathHelper(RuntimeBeanReference urlPathHelperRef, ParserContext parserContext, Object source)
/*     */   {
/*  78 */     if (urlPathHelperRef != null) {
/*  79 */       if (parserContext.getRegistry().isAlias("mvcUrlPathHelper")) {
/*  80 */         parserContext.getRegistry().removeAlias("mvcUrlPathHelper");
/*     */       }
/*  82 */       parserContext.getRegistry().registerAlias(urlPathHelperRef.getBeanName(), "mvcUrlPathHelper");
/*     */     }
/*  84 */     else if ((!parserContext.getRegistry().isAlias("mvcUrlPathHelper")) && 
/*  85 */       (!parserContext.getRegistry().containsBeanDefinition("mvcUrlPathHelper"))) {
/*  86 */       RootBeanDefinition urlPathHelperDef = new RootBeanDefinition(UrlPathHelper.class);
/*  87 */       urlPathHelperDef.setSource(source);
/*  88 */       urlPathHelperDef.setRole(2);
/*  89 */       parserContext.getRegistry().registerBeanDefinition("mvcUrlPathHelper", urlPathHelperDef);
/*  90 */       parserContext.registerComponent(new BeanComponentDefinition(urlPathHelperDef, "mvcUrlPathHelper"));
/*     */     }
/*  92 */     return new RuntimeBeanReference("mvcUrlPathHelper");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RuntimeBeanReference registerPathMatcher(RuntimeBeanReference pathMatcherRef, ParserContext parserContext, Object source)
/*     */   {
/* 103 */     if (pathMatcherRef != null) {
/* 104 */       if (parserContext.getRegistry().isAlias("mvcPathMatcher")) {
/* 105 */         parserContext.getRegistry().removeAlias("mvcPathMatcher");
/*     */       }
/* 107 */       parserContext.getRegistry().registerAlias(pathMatcherRef.getBeanName(), "mvcPathMatcher");
/*     */     }
/* 109 */     else if ((!parserContext.getRegistry().isAlias("mvcPathMatcher")) && 
/* 110 */       (!parserContext.getRegistry().containsBeanDefinition("mvcPathMatcher"))) {
/* 111 */       RootBeanDefinition pathMatcherDef = new RootBeanDefinition(AntPathMatcher.class);
/* 112 */       pathMatcherDef.setSource(source);
/* 113 */       pathMatcherDef.setRole(2);
/* 114 */       parserContext.getRegistry().registerBeanDefinition("mvcPathMatcher", pathMatcherDef);
/* 115 */       parserContext.registerComponent(new BeanComponentDefinition(pathMatcherDef, "mvcPathMatcher"));
/*     */     }
/* 117 */     return new RuntimeBeanReference("mvcPathMatcher");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void registerBeanNameUrlHandlerMapping(ParserContext context, Object source)
/*     */   {
/* 125 */     if (!context.getRegistry().containsBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME)) {
/* 126 */       RootBeanDefinition mappingDef = new RootBeanDefinition(BeanNameUrlHandlerMapping.class);
/* 127 */       mappingDef.setSource(source);
/* 128 */       mappingDef.setRole(2);
/* 129 */       mappingDef.getPropertyValues().add("order", Integer.valueOf(2));
/* 130 */       RuntimeBeanReference corsRef = registerCorsConfigurations(null, context, source);
/* 131 */       mappingDef.getPropertyValues().add("corsConfigurations", corsRef);
/* 132 */       context.getRegistry().registerBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME, mappingDef);
/* 133 */       context.registerComponent(new BeanComponentDefinition(mappingDef, BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void registerHttpRequestHandlerAdapter(ParserContext context, Object source)
/*     */   {
/* 142 */     if (!context.getRegistry().containsBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME)) {
/* 143 */       RootBeanDefinition adapterDef = new RootBeanDefinition(HttpRequestHandlerAdapter.class);
/* 144 */       adapterDef.setSource(source);
/* 145 */       adapterDef.setRole(2);
/* 146 */       context.getRegistry().registerBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME, adapterDef);
/* 147 */       context.registerComponent(new BeanComponentDefinition(adapterDef, HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void registerSimpleControllerHandlerAdapter(ParserContext context, Object source)
/*     */   {
/* 156 */     if (!context.getRegistry().containsBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME)) {
/* 157 */       RootBeanDefinition beanDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
/* 158 */       beanDef.setSource(source);
/* 159 */       beanDef.setRole(2);
/* 160 */       context.getRegistry().registerBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME, beanDef);
/* 161 */       context.registerComponent(new BeanComponentDefinition(beanDef, SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RuntimeBeanReference registerCorsConfigurations(Map<String, CorsConfiguration> corsConfigurations, ParserContext context, Object source)
/*     */   {
/* 174 */     if (!context.getRegistry().containsBeanDefinition("mvcCorsConfigurations")) {
/* 175 */       RootBeanDefinition corsDef = new RootBeanDefinition(LinkedHashMap.class);
/* 176 */       corsDef.setSource(source);
/* 177 */       corsDef.setRole(2);
/* 178 */       if (corsConfigurations != null) {
/* 179 */         corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
/*     */       }
/* 181 */       context.getReaderContext().getRegistry().registerBeanDefinition("mvcCorsConfigurations", corsDef);
/* 182 */       context.registerComponent(new BeanComponentDefinition(corsDef, "mvcCorsConfigurations"));
/*     */     }
/* 184 */     else if (corsConfigurations != null) {
/* 185 */       BeanDefinition corsDef = context.getRegistry().getBeanDefinition("mvcCorsConfigurations");
/* 186 */       corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
/*     */     }
/* 188 */     return new RuntimeBeanReference("mvcCorsConfigurations");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void registerHandlerMappingIntrospector(ParserContext parserContext, Object source)
/*     */   {
/* 196 */     if (!parserContext.getRegistry().containsBeanDefinition("mvcHandlerMappingIntrospector")) {
/* 197 */       RootBeanDefinition beanDef = new RootBeanDefinition(HandlerMappingIntrospector.class);
/* 198 */       beanDef.setSource(source);
/* 199 */       beanDef.setRole(2);
/* 200 */       beanDef.setLazyInit(true);
/* 201 */       parserContext.getRegistry().registerBeanDefinition("mvcHandlerMappingIntrospector", beanDef);
/* 202 */       parserContext.registerComponent(new BeanComponentDefinition(beanDef, "mvcHandlerMappingIntrospector"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getContentNegotiationManager(ParserContext context)
/*     */   {
/* 212 */     String name = AnnotationDrivenBeanDefinitionParser.HANDLER_MAPPING_BEAN_NAME;
/* 213 */     if (context.getRegistry().containsBeanDefinition(name)) {
/* 214 */       BeanDefinition handlerMappingBeanDef = context.getRegistry().getBeanDefinition(name);
/* 215 */       return handlerMappingBeanDef.getPropertyValues().get("contentNegotiationManager");
/*     */     }
/* 217 */     name = "mvcContentNegotiationManager";
/* 218 */     if (context.getRegistry().containsBeanDefinition(name)) {
/* 219 */       return new RuntimeBeanReference(name);
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\MvcNamespaceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */