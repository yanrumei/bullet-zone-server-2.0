/*     */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityProperties;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Condition;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Conditional({ResourceServerCondition.class})
/*     */ @ConditionalOnClass({EnableResourceServer.class, SecurityProperties.class})
/*     */ @ConditionalOnWebApplication
/*     */ @ConditionalOnBean({ResourceServerConfiguration.class})
/*     */ @Import({ResourceServerTokenServicesConfiguration.class})
/*     */ public class OAuth2ResourceServerConfiguration
/*     */ {
/*     */   private final ResourceServerProperties resource;
/*     */   
/*     */   public OAuth2ResourceServerConfiguration(ResourceServerProperties resource)
/*     */   {
/*  74 */     this.resource = resource;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({ResourceServerConfigurer.class})
/*     */   public ResourceServerConfigurer resourceServer() {
/*  80 */     return new ResourceSecurityConfigurer(this.resource);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public static ResourceServerFilterChainOrderProcessor resourceServerFilterChainOrderProcessor(ResourceServerProperties properties)
/*     */   {
/*  86 */     return new ResourceServerFilterChainOrderProcessor(properties, null);
/*     */   }
/*     */   
/*     */   protected static class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter
/*     */   {
/*     */     private ResourceServerProperties resource;
/*     */     
/*     */     public ResourceSecurityConfigurer(ResourceServerProperties resource)
/*     */     {
/*  95 */       this.resource = resource;
/*     */     }
/*     */     
/*     */     public void configure(ResourceServerSecurityConfigurer resources)
/*     */       throws Exception
/*     */     {
/* 101 */       resources.resourceId(this.resource.getResourceId());
/*     */     }
/*     */     
/*     */     public void configure(HttpSecurity http) throws Exception
/*     */     {
/* 106 */       ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ResourceServerFilterChainOrderProcessor
/*     */     implements BeanPostProcessor, ApplicationContextAware
/*     */   {
/*     */     private final ResourceServerProperties properties;
/*     */     
/*     */     private ApplicationContext context;
/*     */     
/*     */     private ResourceServerFilterChainOrderProcessor(ResourceServerProperties properties)
/*     */     {
/* 120 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     public void setApplicationContext(ApplicationContext context)
/*     */       throws BeansException
/*     */     {
/* 126 */       this.context = context;
/*     */     }
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/* 132 */       return bean;
/*     */     }
/*     */     
/*     */     public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/* 138 */       if (((bean instanceof ResourceServerConfiguration)) && 
/* 139 */         (this.context.getBeanNamesForType(ResourceServerConfiguration.class, false, false).length == 1))
/*     */       {
/* 141 */         ResourceServerConfiguration config = (ResourceServerConfiguration)bean;
/* 142 */         config.setOrder(this.properties.getFilterOrder());
/*     */       }
/*     */       
/* 145 */       return bean;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class ResourceServerCondition
/*     */     extends SpringBootCondition
/*     */     implements ConfigurationCondition
/*     */   {
/*     */     private static final String AUTHORIZATION_ANNOTATION = "org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration";
/*     */     
/*     */ 
/*     */     public ConfigurationCondition.ConfigurationPhase getConfigurationPhase()
/*     */     {
/* 159 */       return ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 166 */       ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth ResourceServer Condition", new Object[0]);
/* 167 */       Environment environment = context.getEnvironment();
/* 168 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "security.oauth2.resource.");
/*     */       
/* 170 */       if (hasOAuthClientId(environment)) {
/* 171 */         return ConditionOutcome.match(message.foundExactly("client-id property"));
/*     */       }
/* 173 */       if (!resolver.getSubProperties("jwt").isEmpty()) {
/* 174 */         return 
/* 175 */           ConditionOutcome.match(message.foundExactly("JWT resource configuration"));
/*     */       }
/* 177 */       if (!resolver.getSubProperties("jwk").isEmpty()) {
/* 178 */         return 
/* 179 */           ConditionOutcome.match(message.foundExactly("JWK resource configuration"));
/*     */       }
/* 181 */       if (StringUtils.hasText(resolver.getProperty("user-info-uri"))) {
/* 182 */         return 
/* 183 */           ConditionOutcome.match(message.foundExactly("user-info-uri property"));
/*     */       }
/* 185 */       if (StringUtils.hasText(resolver.getProperty("token-info-uri"))) {
/* 186 */         return 
/* 187 */           ConditionOutcome.match(message.foundExactly("token-info-uri property"));
/*     */       }
/* 189 */       if (ClassUtils.isPresent("org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration", null))
/*     */       {
/* 191 */         if (OAuth2ResourceServerConfiguration.AuthorizationServerEndpointsConfigurationBeanCondition.matches(context)) {
/* 192 */           return ConditionOutcome.match(message
/* 193 */             .found("class").items(new Object[] { "org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration" }));
/*     */         }
/*     */       }
/* 196 */       return ConditionOutcome.noMatch(message
/* 197 */         .didNotFind("client id, JWT resource or authorization server")
/* 198 */         .atAll());
/*     */     }
/*     */     
/*     */     private boolean hasOAuthClientId(Environment environment) {
/* 202 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "security.oauth2.client.");
/*     */       
/* 204 */       return StringUtils.hasLength(resolver.getProperty("client-id", ""));
/*     */     }
/*     */   }
/*     */   
/*     */   @ConditionalOnBean({AuthorizationServerEndpointsConfiguration.class})
/*     */   private static class AuthorizationServerEndpointsConfigurationBeanCondition
/*     */   {
/*     */     public static boolean matches(ConditionContext context)
/*     */     {
/* 213 */       Class<AuthorizationServerEndpointsConfigurationBeanCondition> type = AuthorizationServerEndpointsConfigurationBeanCondition.class;
/* 214 */       Conditional conditional = (Conditional)AnnotationUtils.findAnnotation(type, Conditional.class);
/*     */       
/* 216 */       StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(type);
/* 217 */       for (Class<? extends Condition> conditionType : conditional.value()) {
/* 218 */         Condition condition = (Condition)BeanUtils.instantiateClass(conditionType);
/* 219 */         if (condition.matches(context, metadata)) {
/* 220 */           return true;
/*     */         }
/*     */       }
/* 223 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\OAuth2ResourceServerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */