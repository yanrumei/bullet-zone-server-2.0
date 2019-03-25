/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterRegistration.Dynamic;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.context.AbstractContextLoaderInitializer;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.FrameworkServlet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDispatcherServletInitializer
/*     */   extends AbstractContextLoaderInitializer
/*     */ {
/*     */   public static final String DEFAULT_SERVLET_NAME = "dispatcher";
/*     */   
/*     */   public void onStartup(ServletContext servletContext)
/*     */     throws ServletException
/*     */   {
/*  70 */     super.onStartup(servletContext);
/*  71 */     registerDispatcherServlet(servletContext);
/*     */   }
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
/*     */   protected void registerDispatcherServlet(ServletContext servletContext)
/*     */   {
/*  86 */     String servletName = getServletName();
/*  87 */     Assert.hasLength(servletName, "getServletName() must not return empty or null");
/*     */     
/*  89 */     WebApplicationContext servletAppContext = createServletApplicationContext();
/*  90 */     Assert.notNull(servletAppContext, "createServletApplicationContext() did not return an application context for servlet [" + servletName + "]");
/*     */     
/*     */ 
/*     */ 
/*  94 */     FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
/*  95 */     dispatcherServlet.setContextInitializers(getServletApplicationContextInitializers());
/*     */     
/*  97 */     ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
/*  98 */     Assert.notNull(registration, "Failed to register servlet with name '" + servletName + "'.Check if there is another servlet registered under the same name.");
/*     */     
/*     */ 
/*     */ 
/* 102 */     registration.setLoadOnStartup(1);
/* 103 */     registration.addMapping(getServletMappings());
/* 104 */     registration.setAsyncSupported(isAsyncSupported());
/*     */     
/* 106 */     Filter[] filters = getServletFilters();
/* 107 */     if (!ObjectUtils.isEmpty(filters)) {
/* 108 */       for (Filter filter : filters) {
/* 109 */         registerServletFilter(servletContext, filter);
/*     */       }
/*     */     }
/*     */     
/* 113 */     customizeRegistration(registration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getServletName()
/*     */   {
/* 122 */     return "dispatcher";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract WebApplicationContext createServletApplicationContext();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext)
/*     */   {
/* 142 */     return new DispatcherServlet(servletAppContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers()
/*     */   {
/* 154 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String[] getServletMappings();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Filter[] getServletFilters()
/*     */   {
/* 170 */     return null;
/*     */   }
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
/*     */   protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter)
/*     */   {
/* 191 */     String filterName = Conventions.getVariableName(filter);
/* 192 */     FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
/* 193 */     if (registration == null) {
/* 194 */       int counter = -1;
/* 195 */       while ((counter == -1) || (registration == null)) {
/* 196 */         counter++;
/* 197 */         registration = servletContext.addFilter(filterName + "#" + counter, filter);
/* 198 */         Assert.isTrue(counter < 100, "Failed to register filter '" + filter + "'.Could the same Filter instance have been registered already?");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 203 */     registration.setAsyncSupported(isAsyncSupported());
/* 204 */     registration.addMappingForServletNames(getDispatcherTypes(), false, new String[] { getServletName() });
/* 205 */     return registration;
/*     */   }
/*     */   
/*     */   private EnumSet<DispatcherType> getDispatcherTypes() {
/* 209 */     return isAsyncSupported() ? 
/* 210 */       EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC) : 
/* 211 */       EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isAsyncSupported()
/*     */   {
/* 220 */     return true;
/*     */   }
/*     */   
/*     */   protected void customizeRegistration(ServletRegistration.Dynamic registration) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\AbstractDispatcherServletInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */