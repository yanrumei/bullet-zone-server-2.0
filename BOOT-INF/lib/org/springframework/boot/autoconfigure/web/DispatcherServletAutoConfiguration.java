/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.ServletRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
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
/*     */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*     */ @Configuration
/*     */ @ConditionalOnWebApplication
/*     */ @ConditionalOnClass({DispatcherServlet.class})
/*     */ @AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
/*     */ public class DispatcherServletAutoConfiguration
/*     */ {
/*     */   public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "dispatcherServlet";
/*     */   public static final String DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME = "dispatcherServletRegistration";
/*     */   
/*     */   @Configuration
/*     */   @Conditional({DispatcherServletAutoConfiguration.DefaultDispatcherServletCondition.class})
/*     */   @ConditionalOnClass({ServletRegistration.class})
/*     */   @EnableConfigurationProperties({WebMvcProperties.class})
/*     */   protected static class DispatcherServletConfiguration
/*     */   {
/*     */     private final WebMvcProperties webMvcProperties;
/*     */     
/*     */     public DispatcherServletConfiguration(WebMvcProperties webMvcProperties)
/*     */     {
/*  89 */       this.webMvcProperties = webMvcProperties;
/*     */     }
/*     */     
/*     */     @Bean(name={"dispatcherServlet"})
/*     */     public DispatcherServlet dispatcherServlet() {
/*  94 */       DispatcherServlet dispatcherServlet = new DispatcherServlet();
/*  95 */       dispatcherServlet.setDispatchOptionsRequest(this.webMvcProperties
/*  96 */         .isDispatchOptionsRequest());
/*  97 */       dispatcherServlet.setDispatchTraceRequest(this.webMvcProperties
/*  98 */         .isDispatchTraceRequest());
/*  99 */       dispatcherServlet.setThrowExceptionIfNoHandlerFound(this.webMvcProperties
/* 100 */         .isThrowExceptionIfNoHandlerFound());
/* 101 */       return dispatcherServlet;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnBean({MultipartResolver.class})
/*     */     @ConditionalOnMissingBean(name={"multipartResolver"})
/*     */     public MultipartResolver multipartResolver(MultipartResolver resolver)
/*     */     {
/* 109 */       return resolver;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @Conditional({DispatcherServletAutoConfiguration.DispatcherServletRegistrationCondition.class})
/*     */   @ConditionalOnClass({ServletRegistration.class})
/*     */   @EnableConfigurationProperties({WebMvcProperties.class})
/*     */   @Import({DispatcherServletAutoConfiguration.DispatcherServletConfiguration.class})
/*     */   protected static class DispatcherServletRegistrationConfiguration
/*     */   {
/*     */     private final ServerProperties serverProperties;
/*     */     
/*     */     private final WebMvcProperties webMvcProperties;
/*     */     
/*     */     private final MultipartConfigElement multipartConfig;
/*     */     
/*     */ 
/*     */     public DispatcherServletRegistrationConfiguration(ServerProperties serverProperties, WebMvcProperties webMvcProperties, ObjectProvider<MultipartConfigElement> multipartConfigProvider)
/*     */     {
/* 130 */       this.serverProperties = serverProperties;
/* 131 */       this.webMvcProperties = webMvcProperties;
/* 132 */       this.multipartConfig = ((MultipartConfigElement)multipartConfigProvider.getIfAvailable());
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean(name={"dispatcherServletRegistration"})
/*     */     @ConditionalOnBean(value={DispatcherServlet.class}, name={"dispatcherServlet"})
/*     */     public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet)
/*     */     {
/* 140 */       ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet, new String[] { this.serverProperties.getServletMapping() });
/* 141 */       registration.setName("dispatcherServlet");
/* 142 */       registration.setLoadOnStartup(this.webMvcProperties
/* 143 */         .getServlet().getLoadOnStartup());
/* 144 */       if (this.multipartConfig != null) {
/* 145 */         registration.setMultipartConfig(this.multipartConfig);
/*     */       }
/* 147 */       return registration;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Order(2147483637)
/*     */   private static class DefaultDispatcherServletCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 159 */       ConditionMessage.Builder message = ConditionMessage.forCondition("Default DispatcherServlet", new Object[0]);
/* 160 */       ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
/* 161 */       List<String> dispatchServletBeans = Arrays.asList(beanFactory
/* 162 */         .getBeanNamesForType(DispatcherServlet.class, false, false));
/* 163 */       if (dispatchServletBeans.contains("dispatcherServlet")) {
/* 164 */         return ConditionOutcome.noMatch(message.found("dispatcher servlet bean")
/* 165 */           .items(new Object[] { "dispatcherServlet" }));
/*     */       }
/* 167 */       if (beanFactory.containsBean("dispatcherServlet")) {
/* 168 */         return 
/* 169 */           ConditionOutcome.noMatch(message.found("non dispatcher servlet bean")
/* 170 */           .items(new Object[] { "dispatcherServlet" }));
/*     */       }
/* 172 */       if (dispatchServletBeans.isEmpty()) {
/* 173 */         return 
/* 174 */           ConditionOutcome.match(message.didNotFind("dispatcher servlet beans").atAll());
/*     */       }
/* 176 */       return ConditionOutcome.match(message
/* 177 */         .found("dispatcher servlet bean", "dispatcher servlet beans")
/* 178 */         .items(ConditionMessage.Style.QUOTE, dispatchServletBeans)
/* 179 */         .append("and none is named dispatcherServlet"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Order(2147483637)
/*     */   private static class DispatcherServletRegistrationCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 191 */       ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
/* 192 */       ConditionOutcome outcome = checkDefaultDispatcherName(beanFactory);
/* 193 */       if (!outcome.isMatch()) {
/* 194 */         return outcome;
/*     */       }
/* 196 */       return checkServletRegistration(beanFactory);
/*     */     }
/*     */     
/*     */     private ConditionOutcome checkDefaultDispatcherName(ConfigurableListableBeanFactory beanFactory)
/*     */     {
/* 201 */       List<String> servlets = Arrays.asList(beanFactory
/* 202 */         .getBeanNamesForType(DispatcherServlet.class, false, false));
/*     */       
/* 204 */       boolean containsDispatcherBean = beanFactory.containsBean("dispatcherServlet");
/* 205 */       if ((containsDispatcherBean) && 
/* 206 */         (!servlets.contains("dispatcherServlet"))) {
/* 207 */         return 
/* 208 */           ConditionOutcome.noMatch(startMessage().found("non dispatcher servlet")
/* 209 */           .items(new Object[] { "dispatcherServlet" }));
/*     */       }
/* 211 */       return ConditionOutcome.match();
/*     */     }
/*     */     
/*     */     private ConditionOutcome checkServletRegistration(ConfigurableListableBeanFactory beanFactory)
/*     */     {
/* 216 */       ConditionMessage.Builder message = startMessage();
/* 217 */       List<String> registrations = Arrays.asList(beanFactory
/* 218 */         .getBeanNamesForType(ServletRegistrationBean.class, false, false));
/*     */       
/* 220 */       boolean containsDispatcherRegistrationBean = beanFactory.containsBean("dispatcherServletRegistration");
/* 221 */       if (registrations.isEmpty()) {
/* 222 */         if (containsDispatcherRegistrationBean) {
/* 223 */           return 
/* 224 */             ConditionOutcome.noMatch(message.found("non servlet registration bean").items(new Object[] { "dispatcherServletRegistration" }));
/*     */         }
/*     */         
/* 227 */         return 
/* 228 */           ConditionOutcome.match(message.didNotFind("servlet registration bean").atAll());
/*     */       }
/*     */       
/* 231 */       if (registrations.contains("dispatcherServletRegistration")) {
/* 232 */         return ConditionOutcome.noMatch(message.found("servlet registration bean")
/* 233 */           .items(new Object[] { "dispatcherServletRegistration" }));
/*     */       }
/* 235 */       if (containsDispatcherRegistrationBean) {
/* 236 */         return 
/* 237 */           ConditionOutcome.noMatch(message.found("non servlet registration bean").items(new Object[] { "dispatcherServletRegistration" }));
/*     */       }
/*     */       
/* 240 */       return ConditionOutcome.match(message.found("servlet registration beans")
/* 241 */         .items(ConditionMessage.Style.QUOTE, registrations).append("and none is named dispatcherServletRegistration"));
/*     */     }
/*     */     
/*     */     private ConditionMessage.Builder startMessage()
/*     */     {
/* 246 */       return ConditionMessage.forCondition("DispatcherServlet Registration", new Object[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\DispatcherServletAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */