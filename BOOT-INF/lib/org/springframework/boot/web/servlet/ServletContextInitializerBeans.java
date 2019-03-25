/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import javax.servlet.Servlet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.scope.ScopedProxyUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class ServletContextInitializerBeans
/*     */   extends AbstractCollection<ServletContextInitializer>
/*     */ {
/*     */   private static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
/*  66 */   private static final Log logger = LogFactory.getLog(ServletContextInitializerBeans.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private final Set<Object> seen = new HashSet();
/*     */   
/*     */   private final MultiValueMap<Class<?>, ServletContextInitializer> initializers;
/*     */   private List<ServletContextInitializer> sortedList;
/*     */   
/*     */   public ServletContextInitializerBeans(ListableBeanFactory beanFactory)
/*     */   {
/*  78 */     this.initializers = new LinkedMultiValueMap();
/*  79 */     addServletContextInitializerBeans(beanFactory);
/*  80 */     addAdaptableBeans(beanFactory);
/*  81 */     List<ServletContextInitializer> sortedInitializers = new ArrayList();
/*  82 */     for (Map.Entry<?, List<ServletContextInitializer>> entry : this.initializers
/*  83 */       .entrySet()) {
/*  84 */       AnnotationAwareOrderComparator.sort((List)entry.getValue());
/*  85 */       sortedInitializers.addAll((Collection)entry.getValue());
/*     */     }
/*  87 */     this.sortedList = Collections.unmodifiableList(sortedInitializers);
/*     */   }
/*     */   
/*     */   private void addServletContextInitializerBeans(ListableBeanFactory beanFactory) {
/*  91 */     for (Map.Entry<String, ServletContextInitializer> initializerBean : getOrderedBeansOfType(beanFactory, ServletContextInitializer.class))
/*     */     {
/*  93 */       addServletContextInitializerBean((String)initializerBean.getKey(), 
/*  94 */         (ServletContextInitializer)initializerBean.getValue(), beanFactory);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addServletContextInitializerBean(String beanName, ServletContextInitializer initializer, ListableBeanFactory beanFactory)
/*     */   {
/* 100 */     if ((initializer instanceof ServletRegistrationBean)) {
/* 101 */       Servlet source = ((ServletRegistrationBean)initializer).getServlet();
/* 102 */       addServletContextInitializerBean(Servlet.class, beanName, initializer, beanFactory, source);
/*     */ 
/*     */     }
/* 105 */     else if ((initializer instanceof FilterRegistrationBean)) {
/* 106 */       Filter source = ((FilterRegistrationBean)initializer).getFilter();
/* 107 */       addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source);
/*     */ 
/*     */     }
/* 110 */     else if ((initializer instanceof DelegatingFilterProxyRegistrationBean))
/*     */     {
/* 112 */       String source = ((DelegatingFilterProxyRegistrationBean)initializer).getTargetBeanName();
/* 113 */       addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source);
/*     */ 
/*     */     }
/* 116 */     else if ((initializer instanceof ServletListenerRegistrationBean))
/*     */     {
/* 118 */       EventListener source = ((ServletListenerRegistrationBean)initializer).getListener();
/* 119 */       addServletContextInitializerBean(EventListener.class, beanName, initializer, beanFactory, source);
/*     */     }
/*     */     else
/*     */     {
/* 123 */       addServletContextInitializerBean(ServletContextInitializer.class, beanName, initializer, beanFactory, initializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void addServletContextInitializerBean(Class<?> type, String beanName, ServletContextInitializer initializer, ListableBeanFactory beanFactory, Object source)
/*     */   {
/* 131 */     this.initializers.add(type, initializer);
/* 132 */     if (source != null)
/*     */     {
/* 134 */       this.seen.add(source);
/*     */     }
/* 136 */     if (logger.isDebugEnabled()) {
/* 137 */       String resourceDescription = getResourceDescription(beanName, beanFactory);
/* 138 */       int order = getOrder(initializer);
/* 139 */       logger.debug("Added existing " + type
/* 140 */         .getSimpleName() + " initializer bean '" + beanName + "'; order=" + order + ", resource=" + resourceDescription);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String getResourceDescription(String beanName, ListableBeanFactory beanFactory)
/*     */   {
/* 147 */     if ((beanFactory instanceof BeanDefinitionRegistry)) {
/* 148 */       BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
/* 149 */       return registry.getBeanDefinition(beanName).getResourceDescription();
/*     */     }
/* 151 */     return "unknown";
/*     */   }
/*     */   
/*     */   private void addAdaptableBeans(ListableBeanFactory beanFactory)
/*     */   {
/* 156 */     MultipartConfigElement multipartConfig = getMultipartConfig(beanFactory);
/* 157 */     addAsRegistrationBean(beanFactory, Servlet.class, new ServletRegistrationBeanAdapter(multipartConfig));
/*     */     
/* 159 */     addAsRegistrationBean(beanFactory, Filter.class, new FilterRegistrationBeanAdapter(null));
/*     */     
/* 161 */     for (Class<?> listenerType : ServletListenerRegistrationBean.getSupportedTypes()) {
/* 163 */       addAsRegistrationBean(beanFactory, EventListener.class, listenerType, new ServletListenerRegistrationBeanAdapter(null));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private MultipartConfigElement getMultipartConfig(ListableBeanFactory beanFactory)
/*     */   {
/* 170 */     List<Map.Entry<String, MultipartConfigElement>> beans = getOrderedBeansOfType(beanFactory, MultipartConfigElement.class);
/*     */     
/* 172 */     return beans.isEmpty() ? null : (MultipartConfigElement)((Map.Entry)beans.get(0)).getValue();
/*     */   }
/*     */   
/*     */   private <T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type, RegistrationBeanAdapter<T> adapter)
/*     */   {
/* 177 */     addAsRegistrationBean(beanFactory, type, type, adapter);
/*     */   }
/*     */   
/*     */   private <T, B extends T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type, Class<B> beanType, RegistrationBeanAdapter<T> adapter)
/*     */   {
/* 182 */     List<Map.Entry<String, B>> beans = getOrderedBeansOfType(beanFactory, beanType, this.seen);
/*     */     
/* 184 */     for (Map.Entry<String, B> bean : beans) {
/* 185 */       if (this.seen.add(bean.getValue())) {
/* 186 */         int order = getOrder(bean.getValue());
/* 187 */         String beanName = (String)bean.getKey();
/*     */         
/* 189 */         RegistrationBean registration = adapter.createRegistrationBean(beanName, bean
/* 190 */           .getValue(), beans.size());
/* 191 */         registration.setName(beanName);
/* 192 */         registration.setOrder(order);
/* 193 */         this.initializers.add(type, registration);
/* 194 */         if (logger.isDebugEnabled()) {
/* 195 */           logger.debug("Created " + type
/* 196 */             .getSimpleName() + " initializer for bean '" + beanName + "'; order=" + order + ", resource=" + 
/*     */             
/* 198 */             getResourceDescription(beanName, beanFactory));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int getOrder(Object value) {
/* 205 */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 210 */       new AnnotationAwareOrderComparator()
/*     */       {
/* 208 */         public int getOrder(Object obj) { return super.getOrder(obj); } }
/*     */       
/* 210 */       .getOrder(value);
/*     */   }
/*     */   
/*     */   private <T> List<Map.Entry<String, T>> getOrderedBeansOfType(ListableBeanFactory beanFactory, Class<T> type)
/*     */   {
/* 215 */     return getOrderedBeansOfType(beanFactory, type, Collections.emptySet());
/*     */   }
/*     */   
/*     */   private <T> List<Map.Entry<String, T>> getOrderedBeansOfType(ListableBeanFactory beanFactory, Class<T> type, Set<?> excludes)
/*     */   {
/* 220 */     List<Map.Entry<String, T>> beans = new ArrayList();
/* 221 */     Comparator<Map.Entry<String, T>> comparator = new Comparator()
/*     */     {
/*     */       public int compare(Map.Entry<String, T> o1, Map.Entry<String, T> o2)
/*     */       {
/* 225 */         return AnnotationAwareOrderComparator.INSTANCE.compare(o1.getValue(), o2
/* 226 */           .getValue());
/*     */       }
/*     */       
/* 229 */     };
/* 230 */     String[] names = beanFactory.getBeanNamesForType(type, true, false);
/* 231 */     Map<String, T> map = new LinkedHashMap();
/* 232 */     for (String name : names) {
/* 233 */       if ((!excludes.contains(name)) && (!ScopedProxyUtils.isScopedTarget(name))) {
/* 234 */         T bean = beanFactory.getBean(name, type);
/* 235 */         if (!excludes.contains(bean)) {
/* 236 */           map.put(name, bean);
/*     */         }
/*     */       }
/*     */     }
/* 240 */     beans.addAll(map.entrySet());
/* 241 */     Collections.sort(beans, comparator);
/* 242 */     return beans;
/*     */   }
/*     */   
/*     */   public Iterator<ServletContextInitializer> iterator()
/*     */   {
/* 247 */     return this.sortedList.iterator();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 252 */     return this.sortedList.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface RegistrationBeanAdapter<T>
/*     */   {
/*     */     public abstract RegistrationBean createRegistrationBean(String paramString, T paramT, int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ServletRegistrationBeanAdapter
/*     */     implements ServletContextInitializerBeans.RegistrationBeanAdapter<Servlet>
/*     */   {
/*     */     private final MultipartConfigElement multipartConfig;
/*     */     
/*     */ 
/*     */ 
/*     */     ServletRegistrationBeanAdapter(MultipartConfigElement multipartConfig)
/*     */     {
/* 275 */       this.multipartConfig = multipartConfig;
/*     */     }
/*     */     
/*     */ 
/*     */     public RegistrationBean createRegistrationBean(String name, Servlet source, int totalNumberOfSourceBeans)
/*     */     {
/* 281 */       String url = "/" + name + "/";
/* 282 */       if (name.equals("dispatcherServlet")) {
/* 283 */         url = "/";
/*     */       }
/* 285 */       ServletRegistrationBean bean = new ServletRegistrationBean(source, new String[] { url });
/* 286 */       bean.setMultipartConfig(this.multipartConfig);
/* 287 */       return bean;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class FilterRegistrationBeanAdapter
/*     */     implements ServletContextInitializerBeans.RegistrationBeanAdapter<Filter>
/*     */   {
/*     */     public RegistrationBean createRegistrationBean(String name, Filter source, int totalNumberOfSourceBeans)
/*     */     {
/* 301 */       return new FilterRegistrationBean(source, new ServletRegistrationBean[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ServletListenerRegistrationBeanAdapter
/*     */     implements ServletContextInitializerBeans.RegistrationBeanAdapter<EventListener>
/*     */   {
/*     */     public RegistrationBean createRegistrationBean(String name, EventListener source, int totalNumberOfSourceBeans)
/*     */     {
/* 315 */       return new ServletListenerRegistrationBean(source);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletContextInitializerBeans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */