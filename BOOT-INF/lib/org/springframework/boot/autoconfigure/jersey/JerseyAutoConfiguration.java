/*     */ package org.springframework.boot.autoconfigure.jersey;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import javax.ws.rs.ApplicationPath;
/*     */ import javax.ws.rs.ext.ContextResolver;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.glassfish.jersey.jackson.JacksonFeature;
/*     */ import org.glassfish.jersey.server.ResourceConfig;
/*     */ import org.glassfish.jersey.servlet.ServletContainer;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.FilterRegistrationBean;
/*     */ import org.springframework.boot.web.servlet.RegistrationBean;
/*     */ import org.springframework.boot.web.servlet.ServletRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.WebApplicationInitializer;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.filter.RequestContextFilter;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass(name={"org.glassfish.jersey.server.spring.SpringComponentProvider", "javax.servlet.ServletRegistration"})
/*     */ @ConditionalOnBean(type={"org.glassfish.jersey.server.ResourceConfig"})
/*     */ @ConditionalOnWebApplication
/*     */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*     */ @AutoConfigureBefore({DispatcherServletAutoConfiguration.class})
/*     */ @AutoConfigureAfter({JacksonAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({JerseyProperties.class})
/*     */ public class JerseyAutoConfiguration
/*     */   implements ServletContextAware
/*     */ {
/*  92 */   private static final Log logger = LogFactory.getLog(JerseyAutoConfiguration.class);
/*     */   
/*     */   private final JerseyProperties jersey;
/*     */   
/*     */   private final ResourceConfig config;
/*     */   
/*     */   private final List<ResourceConfigCustomizer> customizers;
/*     */   
/*     */   private String path;
/*     */   
/*     */   public JerseyAutoConfiguration(JerseyProperties jersey, ResourceConfig config, ObjectProvider<List<ResourceConfigCustomizer>> customizers)
/*     */   {
/* 104 */     this.jersey = jersey;
/* 105 */     this.config = config;
/* 106 */     this.customizers = ((List)customizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void path() {
/* 111 */     resolveApplicationPath();
/* 112 */     customize();
/*     */   }
/*     */   
/*     */   private void resolveApplicationPath() {
/* 116 */     if (StringUtils.hasLength(this.jersey.getApplicationPath())) {
/* 117 */       this.path = parseApplicationPath(this.jersey.getApplicationPath());
/*     */     }
/*     */     else {
/* 120 */       this.path = findApplicationPath(
/* 121 */         (ApplicationPath)AnnotationUtils.findAnnotation(this.config.getClass(), ApplicationPath.class));
/*     */     }
/*     */   }
/*     */   
/*     */   private void customize() {
/* 126 */     if (this.customizers != null) {
/* 127 */       AnnotationAwareOrderComparator.sort(this.customizers);
/* 128 */       for (ResourceConfigCustomizer customizer : this.customizers) {
/* 129 */         customizer.customize(this.config);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public FilterRegistrationBean requestContextFilter() {
/* 137 */     FilterRegistrationBean registration = new FilterRegistrationBean();
/* 138 */     registration.setFilter(new RequestContextFilter());
/* 139 */     registration.setOrder(this.jersey.getFilter().getOrder() - 1);
/* 140 */     registration.setName("requestContextFilter");
/* 141 */     return registration;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(name={"jerseyFilterRegistration"})
/*     */   @ConditionalOnProperty(prefix="spring.jersey", name={"type"}, havingValue="filter")
/*     */   public FilterRegistrationBean jerseyFilterRegistration() {
/* 148 */     FilterRegistrationBean registration = new FilterRegistrationBean();
/* 149 */     registration.setFilter(new ServletContainer(this.config));
/* 150 */     registration.setUrlPatterns(Arrays.asList(new String[] { this.path }));
/* 151 */     registration.setOrder(this.jersey.getFilter().getOrder());
/* 152 */     registration.addInitParameter("jersey.config.servlet.filter.contextPath", 
/* 153 */       stripPattern(this.path));
/* 154 */     addInitParameters(registration);
/* 155 */     registration.setName("jerseyFilter");
/* 156 */     registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
/* 157 */     return registration;
/*     */   }
/*     */   
/*     */   private String stripPattern(String path) {
/* 161 */     if (path.endsWith("/*")) {
/* 162 */       path = path.substring(0, path.lastIndexOf("/*"));
/*     */     }
/* 164 */     return path;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(name={"jerseyServletRegistration"})
/*     */   @ConditionalOnProperty(prefix="spring.jersey", name={"type"}, havingValue="servlet", matchIfMissing=true)
/*     */   public ServletRegistrationBean jerseyServletRegistration() {
/* 171 */     ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(this.config), new String[] { this.path });
/*     */     
/* 173 */     addInitParameters(registration);
/* 174 */     registration.setName(getServletRegistrationName());
/* 175 */     registration.setLoadOnStartup(this.jersey.getServlet().getLoadOnStartup());
/* 176 */     return registration;
/*     */   }
/*     */   
/*     */   private String getServletRegistrationName() {
/* 180 */     return ClassUtils.getUserClass(this.config.getClass()).getName();
/*     */   }
/*     */   
/*     */   private void addInitParameters(RegistrationBean registration) {
/* 184 */     for (Map.Entry<String, String> entry : this.jersey.getInit().entrySet()) {
/* 185 */       registration.addInitParameter((String)entry.getKey(), (String)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static String findApplicationPath(ApplicationPath annotation)
/*     */   {
/* 191 */     if (annotation == null) {
/* 192 */       return "/*";
/*     */     }
/* 194 */     return parseApplicationPath(annotation.value());
/*     */   }
/*     */   
/*     */   private static String parseApplicationPath(String applicationPath) {
/* 198 */     if (!applicationPath.startsWith("/")) {
/* 199 */       applicationPath = "/" + applicationPath;
/*     */     }
/* 201 */     return applicationPath + "/*";
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 206 */     String servletRegistrationName = getServletRegistrationName();
/*     */     
/* 208 */     ServletRegistration registration = servletContext.getServletRegistration(servletRegistrationName);
/* 209 */     if (registration != null) {
/* 210 */       if (logger.isInfoEnabled()) {
/* 211 */         logger.info("Configuring existing registration for Jersey servlet '" + servletRegistrationName + "'");
/*     */       }
/*     */       
/* 214 */       registration.setInitParameters(this.jersey.getInit());
/* 215 */       registration.setInitParameter("jersey.config.disableMetainfServicesLookup", Boolean.TRUE
/*     */       
/* 217 */         .toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Order(Integer.MIN_VALUE)
/*     */   public static final class JerseyWebApplicationInitializer
/*     */     implements WebApplicationInitializer
/*     */   {
/*     */     public void onStartup(ServletContext servletContext)
/*     */       throws ServletException
/*     */     {
/* 229 */       servletContext.setInitParameter("contextConfigLocation", "<NONE>");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @ConditionalOnClass({JacksonFeature.class})
/*     */   @ConditionalOnSingleCandidate(ObjectMapper.class)
/*     */   @Configuration
/*     */   static class JacksonResourceConfigCustomizer
/*     */   {
/*     */     private static final String JAXB_ANNOTATION_INTROSPECTOR_CLASS_NAME = "com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector";
/*     */     
/*     */     @Bean
/*     */     public ResourceConfigCustomizer resourceConfigCustomizer(final ObjectMapper objectMapper)
/*     */     {
/* 244 */       addJaxbAnnotationIntrospectorIfPresent(objectMapper);
/* 245 */       new ResourceConfigCustomizer()
/*     */       {
/*     */         public void customize(ResourceConfig config) {
/* 248 */           config.register(JacksonFeature.class);
/* 249 */           config.register(new JerseyAutoConfiguration.JacksonResourceConfigCustomizer.ObjectMapperContextResolver(objectMapper, null), new Class[] { ContextResolver.class });
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     private void addJaxbAnnotationIntrospectorIfPresent(ObjectMapper objectMapper)
/*     */     {
/* 256 */       if (ClassUtils.isPresent("com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector", 
/* 257 */         getClass().getClassLoader())) {
/* 258 */         new ObjectMapperCustomizer(null).addJaxbAnnotationIntrospector(objectMapper);
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class ObjectMapperCustomizer
/*     */     {
/*     */       private void addJaxbAnnotationIntrospector(ObjectMapper objectMapper)
/*     */       {
/* 266 */         JaxbAnnotationIntrospector jaxbAnnotationIntrospector = new JaxbAnnotationIntrospector(objectMapper.getTypeFactory());
/* 267 */         objectMapper.setAnnotationIntrospectors(
/* 268 */           createPair(objectMapper.getSerializationConfig(), jaxbAnnotationIntrospector), 
/*     */           
/* 270 */           createPair(objectMapper.getDeserializationConfig(), jaxbAnnotationIntrospector));
/*     */       }
/*     */       
/*     */ 
/*     */       private AnnotationIntrospector createPair(MapperConfig<?> config, JaxbAnnotationIntrospector jaxbAnnotationIntrospector)
/*     */       {
/* 276 */         return AnnotationIntrospector.pair(config.getAnnotationIntrospector(), jaxbAnnotationIntrospector);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private static final class ObjectMapperContextResolver
/*     */       implements ContextResolver<ObjectMapper>
/*     */     {
/*     */       private final ObjectMapper objectMapper;
/*     */       
/*     */       private ObjectMapperContextResolver(ObjectMapper objectMapper)
/*     */       {
/* 288 */         this.objectMapper = objectMapper;
/*     */       }
/*     */       
/*     */       public ObjectMapper getContext(Class<?> type)
/*     */       {
/* 293 */         return this.objectMapper;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jersey\JerseyAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */