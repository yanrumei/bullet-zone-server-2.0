/*     */ package org.springframework.boot.autoconfigure.security;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.web.ErrorController;
/*     */ import org.springframework.boot.autoconfigure.web.ServerProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity.RequestMatcherConfigurer;
/*     */ import org.springframework.security.config.annotation.web.builders.WebSecurity;
/*     */ import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer.ChannelRequestMatcherRegistry;
/*     */ import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer.RequiresChannelUrl;
/*     */ import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.CacheControlConfig;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.ContentSecurityPolicyConfig;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.ContentTypeOptionsConfig;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.XXssConfig;
/*     */ import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
/*     */ import org.springframework.security.web.AuthenticationEntryPoint;
/*     */ import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
/*     */ import org.springframework.security.web.header.writers.HstsHeaderWriter;
/*     */ import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*     */ import org.springframework.security.web.util.matcher.AnyRequestMatcher;
/*     */ import org.springframework.security.web.util.matcher.OrRequestMatcher;
/*     */ import org.springframework.security.web.util.matcher.RequestMatcher;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ @EnableConfigurationProperties
/*     */ @ConditionalOnClass({EnableWebSecurity.class, AuthenticationEntryPoint.class})
/*     */ @ConditionalOnMissingBean({WebSecurityConfiguration.class})
/*     */ @ConditionalOnWebApplication
/*     */ @EnableWebSecurity
/*     */ public class SpringBootWebSecurityConfiguration
/*     */ {
/*  95 */   private static List<String> DEFAULT_IGNORED = Arrays.asList(new String[] { "/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon.ico" });
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({IgnoredPathsWebSecurityConfigurerAdapter.class})
/*     */   public IgnoredPathsWebSecurityConfigurerAdapter ignoredPathsWebSecurityConfigurerAdapter(List<IgnoredRequestCustomizer> customizers)
/*     */   {
/* 102 */     return new IgnoredPathsWebSecurityConfigurerAdapter(customizers);
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   public IgnoredRequestCustomizer defaultIgnoredRequestsCustomizer(ServerProperties server, SecurityProperties security, ObjectProvider<ErrorController> errorController)
/*     */   {
/* 109 */     return new DefaultIgnoredRequestCustomizer(server, security, 
/* 110 */       (ErrorController)errorController.getIfAvailable());
/*     */   }
/*     */   
/*     */   public static void configureHeaders(HeadersConfigurer<?> configurer, SecurityProperties.Headers headers) throws Exception
/*     */   {
/* 115 */     if (headers.getHsts() != SecurityProperties.Headers.HSTS.NONE) {
/* 116 */       boolean includeSubDomains = headers.getHsts() == SecurityProperties.Headers.HSTS.ALL;
/* 117 */       HstsHeaderWriter writer = new HstsHeaderWriter(includeSubDomains);
/* 118 */       writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
/* 119 */       configurer.addHeaderWriter(writer);
/*     */     }
/* 121 */     if (!headers.isContentType()) {
/* 122 */       configurer.contentTypeOptions().disable();
/*     */     }
/* 124 */     if (StringUtils.hasText(headers.getContentSecurityPolicy())) {
/* 125 */       String policyDirectives = headers.getContentSecurityPolicy();
/* 126 */       SecurityProperties.Headers.ContentSecurityPolicyMode mode = headers.getContentSecurityPolicyMode();
/* 127 */       if (mode == SecurityProperties.Headers.ContentSecurityPolicyMode.DEFAULT) {
/* 128 */         configurer.contentSecurityPolicy(policyDirectives);
/*     */       }
/*     */       else {
/* 131 */         configurer.contentSecurityPolicy(policyDirectives).reportOnly();
/*     */       }
/*     */     }
/* 134 */     if (!headers.isXss()) {
/* 135 */       configurer.xssProtection().disable();
/*     */     }
/* 137 */     if (!headers.isCache()) {
/* 138 */       configurer.cacheControl().disable();
/*     */     }
/* 140 */     if (!headers.isFrame()) {
/* 141 */       configurer.frameOptions().disable();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Order(Integer.MIN_VALUE)
/*     */   private static class IgnoredPathsWebSecurityConfigurerAdapter
/*     */     implements WebSecurityConfigurer<WebSecurity>
/*     */   {
/*     */     private final List<IgnoredRequestCustomizer> customizers;
/*     */     
/*     */     IgnoredPathsWebSecurityConfigurerAdapter(List<IgnoredRequestCustomizer> customizers)
/*     */     {
/* 154 */       this.customizers = customizers;
/*     */     }
/*     */     
/*     */     public void configure(WebSecurity builder)
/*     */       throws Exception
/*     */     {}
/*     */     
/*     */     public void init(WebSecurity builder) throws Exception
/*     */     {
/* 163 */       for (IgnoredRequestCustomizer customizer : this.customizers) {
/* 164 */         customizer.customize(builder.ignoring());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class DefaultIgnoredRequestCustomizer
/*     */     implements IgnoredRequestCustomizer
/*     */   {
/*     */     private final ServerProperties server;
/*     */     
/*     */     private final SecurityProperties security;
/*     */     private final ErrorController errorController;
/*     */     
/*     */     DefaultIgnoredRequestCustomizer(ServerProperties server, SecurityProperties security, ErrorController errorController)
/*     */     {
/* 180 */       this.server = server;
/* 181 */       this.security = security;
/* 182 */       this.errorController = errorController;
/*     */     }
/*     */     
/*     */     public void customize(WebSecurity.IgnoredRequestConfigurer configurer)
/*     */     {
/* 187 */       List<String> ignored = getIgnored(this.security);
/* 188 */       if (this.errorController != null) {
/* 189 */         ignored.add(normalizePath(this.errorController.getErrorPath()));
/*     */       }
/* 191 */       String[] paths = this.server.getPathsArray(ignored);
/* 192 */       List<RequestMatcher> matchers = new ArrayList();
/* 193 */       if (!ObjectUtils.isEmpty(paths)) {
/* 194 */         for (String pattern : paths) {
/* 195 */           matchers.add(new AntPathRequestMatcher(pattern, null));
/*     */         }
/*     */       }
/* 198 */       if (!matchers.isEmpty()) {
/* 199 */         configurer.requestMatchers(new RequestMatcher[] { new OrRequestMatcher(matchers) });
/*     */       }
/*     */     }
/*     */     
/*     */     private List<String> getIgnored(SecurityProperties security) {
/* 204 */       List<String> ignored = new ArrayList(security.getIgnored());
/* 205 */       if (ignored.isEmpty()) {
/* 206 */         ignored.addAll(SpringBootWebSecurityConfiguration.DEFAULT_IGNORED);
/*     */       }
/* 208 */       else if (ignored.contains("none")) {
/* 209 */         ignored.remove("none");
/*     */       }
/* 211 */       return ignored;
/*     */     }
/*     */     
/*     */     private String normalizePath(String errorPath) {
/* 215 */       String result = StringUtils.cleanPath(errorPath);
/* 216 */       if (!result.startsWith("/")) {
/* 217 */         result = "/" + result;
/*     */       }
/* 219 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnProperty(prefix="security.basic", name={"enabled"}, havingValue="false")
/*     */   @Order(2147483642)
/*     */   protected static class ApplicationNoWebSecurityConfigurerAdapter
/*     */     extends WebSecurityConfigurerAdapter
/*     */   {
/*     */     protected void configure(HttpSecurity http)
/*     */       throws Exception
/*     */     {
/* 232 */       http.requestMatcher(new RequestMatcher()
/*     */       {
/*     */         public boolean matches(HttpServletRequest request) {
/* 235 */           return false;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnProperty(prefix="security.basic", name={"enabled"}, matchIfMissing=true)
/*     */   @Order(2147483642)
/*     */   protected static class ApplicationWebSecurityConfigurerAdapter
/*     */     extends WebSecurityConfigurerAdapter
/*     */   {
/*     */     private SecurityProperties security;
/*     */     
/*     */     protected ApplicationWebSecurityConfigurerAdapter(SecurityProperties security)
/*     */     {
/* 251 */       this.security = security;
/*     */     }
/*     */     
/*     */     protected void configure(HttpSecurity http) throws Exception
/*     */     {
/* 256 */       if (this.security.isRequireSsl()) {
/* 257 */         ((ChannelSecurityConfigurer.RequiresChannelUrl)http.requiresChannel().anyRequest()).requiresSecure();
/*     */       }
/* 259 */       if (!this.security.isEnableCsrf()) {
/* 260 */         http.csrf().disable();
/*     */       }
/*     */       
/* 263 */       http.sessionManagement().sessionCreationPolicy(this.security.getSessions());
/* 264 */       SpringBootWebSecurityConfiguration.configureHeaders(http.headers(), this.security
/* 265 */         .getHeaders());
/* 266 */       String[] paths = getSecureApplicationPaths();
/* 267 */       if (paths.length > 0) {
/* 268 */         AuthenticationEntryPoint entryPoint = entryPoint();
/* 269 */         http.exceptionHandling().authenticationEntryPoint(entryPoint);
/* 270 */         http.httpBasic().authenticationEntryPoint(entryPoint);
/* 271 */         http.requestMatchers().antMatchers(paths);
/* 272 */         String[] roles = (String[])this.security.getUser().getRole().toArray(new String[0]);
/* 273 */         SecurityAuthorizeMode mode = this.security.getBasic().getAuthorizeMode();
/* 274 */         if ((mode == null) || (mode == SecurityAuthorizeMode.ROLE)) {
/* 275 */           ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).hasAnyRole(roles);
/*     */         }
/* 277 */         else if (mode == SecurityAuthorizeMode.AUTHENTICATED) {
/* 278 */           ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private String[] getSecureApplicationPaths() {
/* 284 */       List<String> list = new ArrayList();
/* 285 */       for (String path : this.security.getBasic().getPath()) {
/* 286 */         path = path == null ? "" : path.trim();
/* 287 */         if (path.equals("/**")) {
/* 288 */           return new String[] { path };
/*     */         }
/* 290 */         if (!path.equals("")) {
/* 291 */           list.add(path);
/*     */         }
/*     */       }
/* 294 */       return (String[])list.toArray(new String[list.size()]);
/*     */     }
/*     */     
/*     */     private AuthenticationEntryPoint entryPoint() {
/* 298 */       BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
/* 299 */       entryPoint.setRealmName(this.security.getBasic().getRealm());
/* 300 */       return entryPoint;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\SpringBootWebSecurityConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */