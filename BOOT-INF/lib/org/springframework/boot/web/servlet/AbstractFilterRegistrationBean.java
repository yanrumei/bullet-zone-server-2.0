/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterRegistration.Dynamic;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ abstract class AbstractFilterRegistrationBean
/*     */   extends RegistrationBean
/*     */ {
/*     */   protected static final int REQUEST_WRAPPER_FILTER_MAX_ORDER = 0;
/*  50 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  52 */   private static final EnumSet<DispatcherType> ASYNC_DISPATCHER_TYPES = EnumSet.of(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST, DispatcherType.ASYNC);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final EnumSet<DispatcherType> NON_ASYNC_DISPATCHER_TYPES = EnumSet.of(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
/*     */   
/*  59 */   private static final String[] DEFAULT_URL_MAPPINGS = { "/*" };
/*     */   
/*  61 */   private Set<ServletRegistrationBean> servletRegistrationBeans = new LinkedHashSet();
/*     */   
/*  63 */   private Set<String> servletNames = new LinkedHashSet();
/*     */   
/*  65 */   private Set<String> urlPatterns = new LinkedHashSet();
/*     */   
/*     */   private EnumSet<DispatcherType> dispatcherTypes;
/*     */   
/*  69 */   private boolean matchAfter = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AbstractFilterRegistrationBean(ServletRegistrationBean... servletRegistrationBeans)
/*     */   {
/*  77 */     Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
/*     */     
/*  79 */     Collections.addAll(this.servletRegistrationBeans, servletRegistrationBeans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletRegistrationBeans(Collection<? extends ServletRegistrationBean> servletRegistrationBeans)
/*     */   {
/*  88 */     Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
/*     */     
/*  90 */     this.servletRegistrationBeans = new LinkedHashSet(servletRegistrationBeans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<ServletRegistrationBean> getServletRegistrationBeans()
/*     */   {
/* 102 */     return this.servletRegistrationBeans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addServletRegistrationBeans(ServletRegistrationBean... servletRegistrationBeans)
/*     */   {
/* 112 */     Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
/*     */     
/* 114 */     Collections.addAll(this.servletRegistrationBeans, servletRegistrationBeans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletNames(Collection<String> servletNames)
/*     */   {
/* 125 */     Assert.notNull(servletNames, "ServletNames must not be null");
/* 126 */     this.servletNames = new LinkedHashSet(servletNames);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> getServletNames()
/*     */   {
/* 135 */     return this.servletNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addServletNames(String... servletNames)
/*     */   {
/* 143 */     Assert.notNull(servletNames, "ServletNames must not be null");
/* 144 */     this.servletNames.addAll(Arrays.asList(servletNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPatterns(Collection<String> urlPatterns)
/*     */   {
/* 155 */     Assert.notNull(urlPatterns, "UrlPatterns must not be null");
/* 156 */     this.urlPatterns = new LinkedHashSet(urlPatterns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> getUrlPatterns()
/*     */   {
/* 165 */     return this.urlPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addUrlPatterns(String... urlPatterns)
/*     */   {
/* 173 */     Assert.notNull(urlPatterns, "UrlPatterns must not be null");
/* 174 */     Collections.addAll(this.urlPatterns, urlPatterns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDispatcherTypes(DispatcherType first, DispatcherType... rest)
/*     */   {
/* 184 */     this.dispatcherTypes = EnumSet.of(first, rest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDispatcherTypes(EnumSet<DispatcherType> dispatcherTypes)
/*     */   {
/* 194 */     this.dispatcherTypes = dispatcherTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMatchAfter(boolean matchAfter)
/*     */   {
/* 204 */     this.matchAfter = matchAfter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMatchAfter()
/*     */   {
/* 213 */     return this.matchAfter;
/*     */   }
/*     */   
/*     */   public void onStartup(ServletContext servletContext) throws ServletException
/*     */   {
/* 218 */     Filter filter = getFilter();
/* 219 */     Assert.notNull(filter, "Filter must not be null");
/* 220 */     String name = getOrDeduceName(filter);
/* 221 */     if (!isEnabled()) {
/* 222 */       this.logger.info("Filter " + name + " was not registered (disabled)");
/* 223 */       return;
/*     */     }
/* 225 */     FilterRegistration.Dynamic added = servletContext.addFilter(name, filter);
/* 226 */     if (added == null) {
/* 227 */       this.logger.info("Filter " + name + " was not registered (possibly already registered?)");
/*     */       
/* 229 */       return;
/*     */     }
/* 231 */     configure(added);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Filter getFilter();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configure(FilterRegistration.Dynamic registration)
/*     */   {
/* 246 */     super.configure(registration);
/* 247 */     EnumSet<DispatcherType> dispatcherTypes = this.dispatcherTypes;
/* 248 */     if (dispatcherTypes == null) {
/* 249 */       dispatcherTypes = isAsyncSupported() ? ASYNC_DISPATCHER_TYPES : NON_ASYNC_DISPATCHER_TYPES;
/*     */     }
/*     */     
/* 252 */     Set<String> servletNames = new LinkedHashSet();
/* 253 */     for (ServletRegistrationBean servletRegistrationBean : this.servletRegistrationBeans) {
/* 254 */       servletNames.add(servletRegistrationBean.getServletName());
/*     */     }
/* 256 */     servletNames.addAll(this.servletNames);
/* 257 */     if ((servletNames.isEmpty()) && (this.urlPatterns.isEmpty())) {
/* 258 */       this.logger.info("Mapping filter: '" + registration.getName() + "' to: " + 
/* 259 */         Arrays.asList(DEFAULT_URL_MAPPINGS));
/* 260 */       registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter, DEFAULT_URL_MAPPINGS);
/*     */     }
/*     */     else
/*     */     {
/* 264 */       if (!servletNames.isEmpty()) {
/* 265 */         this.logger.info("Mapping filter: '" + registration.getName() + "' to servlets: " + servletNames);
/*     */         
/* 267 */         registration.addMappingForServletNames(dispatcherTypes, this.matchAfter, 
/* 268 */           (String[])servletNames.toArray(new String[servletNames.size()]));
/*     */       }
/* 270 */       if (!this.urlPatterns.isEmpty()) {
/* 271 */         this.logger.info("Mapping filter: '" + registration.getName() + "' to urls: " + this.urlPatterns);
/*     */         
/* 273 */         registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter, 
/* 274 */           (String[])this.urlPatterns.toArray(new String[this.urlPatterns.size()]));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\AbstractFilterRegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */