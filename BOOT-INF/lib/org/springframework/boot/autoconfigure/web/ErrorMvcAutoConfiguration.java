/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.ErrorPage;
/*     */ import org.springframework.boot.web.servlet.ErrorPageRegistrar;
/*     */ import org.springframework.boot.web.servlet.ErrorPageRegistry;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.expression.MapAccessor;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.util.HtmlUtils;
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
/*     */ @ConditionalOnWebApplication
/*     */ @ConditionalOnClass({Servlet.class, DispatcherServlet.class})
/*     */ @AutoConfigureBefore({WebMvcAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({ResourceProperties.class})
/*     */ public class ErrorMvcAutoConfiguration
/*     */ {
/*     */   private final ServerProperties serverProperties;
/*     */   private final List<ErrorViewResolver> errorViewResolvers;
/*     */   
/*     */   public ErrorMvcAutoConfiguration(ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider)
/*     */   {
/*  91 */     this.serverProperties = serverProperties;
/*  92 */     this.errorViewResolvers = ((List)errorViewResolversProvider.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(value={ErrorAttributes.class}, search=SearchStrategy.CURRENT)
/*     */   public DefaultErrorAttributes errorAttributes() {
/*  98 */     return new DefaultErrorAttributes();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(value={ErrorController.class}, search=SearchStrategy.CURRENT)
/*     */   public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
/* 104 */     return new BasicErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public ErrorPageCustomizer errorPageCustomizer()
/*     */   {
/* 110 */     return new ErrorPageCustomizer(this.serverProperties);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public static PreserveErrorControllerTargetClassPostProcessor preserveErrorControllerTargetClassPostProcessor() {
/* 115 */     return new PreserveErrorControllerTargetClassPostProcessor();
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   static class DefaultErrorViewResolverConfiguration
/*     */   {
/*     */     private final ApplicationContext applicationContext;
/*     */     private final ResourceProperties resourceProperties;
/*     */     
/*     */     DefaultErrorViewResolverConfiguration(ApplicationContext applicationContext, ResourceProperties resourceProperties)
/*     */     {
/* 127 */       this.applicationContext = applicationContext;
/* 128 */       this.resourceProperties = resourceProperties;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnBean({DispatcherServlet.class})
/*     */     @ConditionalOnMissingBean
/*     */     public DefaultErrorViewResolver conventionErrorViewResolver() {
/* 135 */       return new DefaultErrorViewResolver(this.applicationContext, this.resourceProperties);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnProperty(prefix="server.error.whitelabel", name={"enabled"}, matchIfMissing=true)
/*     */   @Conditional({ErrorMvcAutoConfiguration.ErrorTemplateMissingCondition.class})
/*     */   protected static class WhitelabelErrorViewConfiguration
/*     */   {
/* 146 */     private final ErrorMvcAutoConfiguration.SpelView defaultErrorView = new ErrorMvcAutoConfiguration.SpelView("<html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>${timestamp}</div><div>There was an unexpected error (type=${error}, status=${status}).</div><div>${message}</div></body></html>");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Bean(name={"error"})
/*     */     @ConditionalOnMissingBean(name={"error"})
/*     */     public View defaultErrorView()
/*     */     {
/* 156 */       return this.defaultErrorView;
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({BeanNameViewResolver.class})
/*     */     public BeanNameViewResolver beanNameViewResolver()
/*     */     {
/* 164 */       BeanNameViewResolver resolver = new BeanNameViewResolver();
/* 165 */       resolver.setOrder(2147483637);
/* 166 */       return resolver;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ErrorTemplateMissingCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 180 */       ConditionMessage.Builder message = ConditionMessage.forCondition("ErrorTemplate Missing", new Object[0]);
/*     */       
/* 182 */       TemplateAvailabilityProviders providers = new TemplateAvailabilityProviders(context.getClassLoader());
/* 183 */       TemplateAvailabilityProvider provider = providers.getProvider("error", context
/* 184 */         .getEnvironment(), context.getClassLoader(), context
/* 185 */         .getResourceLoader());
/* 186 */       if (provider != null) {
/* 187 */         return 
/* 188 */           ConditionOutcome.noMatch(message.foundExactly("template from " + provider));
/*     */       }
/* 190 */       return 
/* 191 */         ConditionOutcome.match(message.didNotFind("error template view").atAll());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SpelView
/*     */     implements View
/*     */   {
/*     */     private final NonRecursivePropertyPlaceholderHelper helper;
/*     */     
/*     */     private final String template;
/*     */     
/*     */     private volatile Map<String, Expression> expressions;
/*     */     
/*     */ 
/*     */     SpelView(String template)
/*     */     {
/* 208 */       this.helper = new NonRecursivePropertyPlaceholderHelper("${", "}");
/* 209 */       this.template = template;
/*     */     }
/*     */     
/*     */     public String getContentType()
/*     */     {
/* 214 */       return "text/html";
/*     */     }
/*     */     
/*     */     public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/*     */       throws Exception
/*     */     {
/* 220 */       if (response.getContentType() == null) {
/* 221 */         response.setContentType(getContentType());
/*     */       }
/* 223 */       Map<String, Object> map = new HashMap(model);
/* 224 */       map.put("path", request.getContextPath());
/* 225 */       PropertyPlaceholderHelper.PlaceholderResolver resolver = new ErrorMvcAutoConfiguration.ExpressionResolver(getExpressions(), map);
/* 226 */       String result = this.helper.replacePlaceholders(this.template, resolver);
/* 227 */       response.getWriter().append(result);
/*     */     }
/*     */     
/*     */     private Map<String, Expression> getExpressions() {
/* 231 */       if (this.expressions == null) {
/* 232 */         synchronized (this) {
/* 233 */           ErrorMvcAutoConfiguration.ExpressionCollector expressionCollector = new ErrorMvcAutoConfiguration.ExpressionCollector(null);
/* 234 */           this.helper.replacePlaceholders(this.template, expressionCollector);
/* 235 */           this.expressions = expressionCollector.getExpressions();
/*     */         }
/*     */       }
/* 238 */       return this.expressions;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ExpressionCollector
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/* 248 */     private final SpelExpressionParser parser = new SpelExpressionParser();
/*     */     
/* 250 */     private final Map<String, Expression> expressions = new HashMap();
/*     */     
/*     */     public String resolvePlaceholder(String name)
/*     */     {
/* 254 */       this.expressions.put(name, this.parser.parseExpression(name));
/* 255 */       return null;
/*     */     }
/*     */     
/*     */     public Map<String, Expression> getExpressions() {
/* 259 */       return Collections.unmodifiableMap(this.expressions);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ExpressionResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final Map<String, Expression> expressions;
/*     */     
/*     */     private final EvaluationContext context;
/*     */     
/*     */ 
/*     */     ExpressionResolver(Map<String, Expression> expressions, Map<String, ?> map)
/*     */     {
/* 274 */       this.expressions = expressions;
/* 275 */       this.context = getContext(map);
/*     */     }
/*     */     
/*     */     private EvaluationContext getContext(Map<String, ?> map) {
/* 279 */       StandardEvaluationContext context = new StandardEvaluationContext();
/* 280 */       context.addPropertyAccessor(new MapAccessor());
/* 281 */       context.setRootObject(map);
/* 282 */       return context;
/*     */     }
/*     */     
/*     */     public String resolvePlaceholder(String placeholderName)
/*     */     {
/* 287 */       Expression expression = (Expression)this.expressions.get(placeholderName);
/* 288 */       return escape(expression == null ? null : expression.getValue(this.context));
/*     */     }
/*     */     
/*     */     private String escape(Object value) {
/* 292 */       return HtmlUtils.htmlEscape(value == null ? null : value.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ErrorPageCustomizer
/*     */     implements ErrorPageRegistrar, Ordered
/*     */   {
/*     */     private final ServerProperties properties;
/*     */     
/*     */ 
/*     */     protected ErrorPageCustomizer(ServerProperties properties)
/*     */     {
/* 306 */       this.properties = properties;
/*     */     }
/*     */     
/*     */ 
/*     */     public void registerErrorPages(ErrorPageRegistry errorPageRegistry)
/*     */     {
/* 312 */       ErrorPage errorPage = new ErrorPage(this.properties.getServletPrefix() + this.properties.getError().getPath());
/* 313 */       errorPageRegistry.addErrorPages(new ErrorPage[] { errorPage });
/*     */     }
/*     */     
/*     */     public int getOrder()
/*     */     {
/* 318 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class PreserveErrorControllerTargetClassPostProcessor
/*     */     implements BeanFactoryPostProcessor
/*     */   {
/*     */     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {
/* 334 */       String[] errorControllerBeans = beanFactory.getBeanNamesForType(ErrorController.class, false, false);
/* 335 */       for (String errorControllerBean : errorControllerBeans) {
/*     */         try {
/* 337 */           beanFactory.getBeanDefinition(errorControllerBean).setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
/*     */         }
/*     */         catch (Throwable localThrowable) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ErrorMvcAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */