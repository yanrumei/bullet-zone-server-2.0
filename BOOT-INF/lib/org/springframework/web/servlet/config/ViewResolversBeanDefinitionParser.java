/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.ViewResolverComposite;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
/*     */ import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
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
/*     */ 
/*     */ 
/*     */ public class ViewResolversBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   public static final String VIEW_RESOLVER_BEAN_NAME = "mvcViewResolver";
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext context)
/*     */   {
/*  72 */     Object source = context.extractSource(element);
/*  73 */     context.pushContainingComponent(new CompositeComponentDefinition(element.getTagName(), source));
/*     */     
/*  75 */     ManagedList<Object> resolvers = new ManagedList(4);
/*  76 */     resolvers.setSource(context.extractSource(element));
/*  77 */     String[] names = { "jsp", "tiles", "bean-name", "freemarker", "velocity", "groovy", "script-template", "bean", "ref" };
/*     */     
/*  79 */     for (Element resolverElement : DomUtils.getChildElementsByTagName(element, names)) {
/*  80 */       String name = resolverElement.getLocalName();
/*  81 */       if (("bean".equals(name)) || ("ref".equals(name))) {
/*  82 */         resolvers.add(context.getDelegate().parsePropertySubElement(resolverElement, null));
/*     */       }
/*     */       else {
/*  85 */         RootBeanDefinition resolverBeanDef = null;
/*  86 */         if ("jsp".equals(name)) {
/*  87 */           resolverBeanDef = new RootBeanDefinition(InternalResourceViewResolver.class);
/*  88 */           resolverBeanDef.getPropertyValues().add("prefix", "/WEB-INF/");
/*  89 */           resolverBeanDef.getPropertyValues().add("suffix", ".jsp");
/*  90 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/*  92 */         else if ("tiles".equals(name)) {
/*  93 */           resolverBeanDef = new RootBeanDefinition(TilesViewResolver.class);
/*  94 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/*  96 */         else if ("freemarker".equals(name)) {
/*  97 */           resolverBeanDef = new RootBeanDefinition(FreeMarkerViewResolver.class);
/*  98 */           resolverBeanDef.getPropertyValues().add("suffix", ".ftl");
/*  99 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/* 101 */         else if ("velocity".equals(name)) {
/* 102 */           resolverBeanDef = new RootBeanDefinition(VelocityViewResolver.class);
/* 103 */           resolverBeanDef.getPropertyValues().add("suffix", ".vm");
/* 104 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/* 106 */         else if ("groovy".equals(name)) {
/* 107 */           resolverBeanDef = new RootBeanDefinition(GroovyMarkupViewResolver.class);
/* 108 */           resolverBeanDef.getPropertyValues().add("suffix", ".tpl");
/* 109 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/* 111 */         else if ("script-template".equals(name)) {
/* 112 */           resolverBeanDef = new RootBeanDefinition(ScriptTemplateViewResolver.class);
/* 113 */           addUrlBasedViewResolverProperties(resolverElement, resolverBeanDef);
/*     */         }
/* 115 */         else if ("bean-name".equals(name)) {
/* 116 */           resolverBeanDef = new RootBeanDefinition(BeanNameViewResolver.class);
/*     */         }
/*     */         else
/*     */         {
/* 120 */           throw new IllegalStateException("Unexpected element name: " + name);
/*     */         }
/* 122 */         resolverBeanDef.setSource(source);
/* 123 */         resolverBeanDef.setRole(2);
/* 124 */         resolvers.add(resolverBeanDef);
/*     */       }
/*     */     }
/* 127 */     String beanName = "mvcViewResolver";
/* 128 */     RootBeanDefinition compositeResolverBeanDef = new RootBeanDefinition(ViewResolverComposite.class);
/* 129 */     compositeResolverBeanDef.setSource(source);
/* 130 */     compositeResolverBeanDef.setRole(2);
/*     */     
/* 132 */     names = new String[] { "content-negotiation" };
/* 133 */     List<Element> contentNegotiationElements = DomUtils.getChildElementsByTagName(element, names);
/* 134 */     if (contentNegotiationElements.isEmpty()) {
/* 135 */       compositeResolverBeanDef.getPropertyValues().add("viewResolvers", resolvers);
/*     */     }
/* 137 */     else if (contentNegotiationElements.size() == 1) {
/* 138 */       BeanDefinition beanDef = createContentNegotiatingViewResolver((Element)contentNegotiationElements.get(0), context);
/* 139 */       beanDef.getPropertyValues().add("viewResolvers", resolvers);
/* 140 */       ManagedList<Object> list = new ManagedList(1);
/* 141 */       list.add(beanDef);
/* 142 */       compositeResolverBeanDef.getPropertyValues().add("order", Integer.valueOf(Integer.MIN_VALUE));
/* 143 */       compositeResolverBeanDef.getPropertyValues().add("viewResolvers", list);
/*     */     }
/*     */     else {
/* 146 */       throw new IllegalArgumentException("Only one <content-negotiation> element is allowed.");
/*     */     }
/*     */     
/* 149 */     if (element.hasAttribute("order")) {
/* 150 */       compositeResolverBeanDef.getPropertyValues().add("order", element.getAttribute("order"));
/*     */     }
/*     */     
/* 153 */     context.getReaderContext().getRegistry().registerBeanDefinition(beanName, compositeResolverBeanDef);
/* 154 */     context.registerComponent(new BeanComponentDefinition(compositeResolverBeanDef, beanName));
/* 155 */     context.popAndRegisterContainingComponent();
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   private void addUrlBasedViewResolverProperties(Element element, RootBeanDefinition beanDefinition) {
/* 160 */     if (element.hasAttribute("prefix")) {
/* 161 */       beanDefinition.getPropertyValues().add("prefix", element.getAttribute("prefix"));
/*     */     }
/* 163 */     if (element.hasAttribute("suffix")) {
/* 164 */       beanDefinition.getPropertyValues().add("suffix", element.getAttribute("suffix"));
/*     */     }
/* 166 */     if (element.hasAttribute("cache-views")) {
/* 167 */       beanDefinition.getPropertyValues().add("cache", element.getAttribute("cache-views"));
/*     */     }
/* 169 */     if (element.hasAttribute("view-class")) {
/* 170 */       beanDefinition.getPropertyValues().add("viewClass", element.getAttribute("view-class"));
/*     */     }
/* 172 */     if (element.hasAttribute("view-names")) {
/* 173 */       beanDefinition.getPropertyValues().add("viewNames", element.getAttribute("view-names"));
/*     */     }
/*     */   }
/*     */   
/*     */   private BeanDefinition createContentNegotiatingViewResolver(Element resolverElement, ParserContext context) {
/* 178 */     RootBeanDefinition beanDef = new RootBeanDefinition(ContentNegotiatingViewResolver.class);
/* 179 */     beanDef.setSource(context.extractSource(resolverElement));
/* 180 */     beanDef.setRole(2);
/* 181 */     MutablePropertyValues values = beanDef.getPropertyValues();
/*     */     
/* 183 */     List<Element> elements = DomUtils.getChildElementsByTagName(resolverElement, new String[] { "default-views" });
/* 184 */     if (!elements.isEmpty()) {
/* 185 */       ManagedList<Object> list = new ManagedList();
/* 186 */       for (Element element : DomUtils.getChildElementsByTagName((Element)elements.get(0), new String[] { "bean", "ref" })) {
/* 187 */         list.add(context.getDelegate().parsePropertySubElement(element, null));
/*     */       }
/* 189 */       values.add("defaultViews", list);
/*     */     }
/* 191 */     if (resolverElement.hasAttribute("use-not-acceptable")) {
/* 192 */       values.add("useNotAcceptableStatusCode", resolverElement.getAttribute("use-not-acceptable"));
/*     */     }
/* 194 */     Object manager = MvcNamespaceUtils.getContentNegotiationManager(context);
/* 195 */     if (manager != null) {
/* 196 */       values.add("contentNegotiationManager", manager);
/*     */     }
/* 198 */     return beanDef;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\ViewResolversBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */