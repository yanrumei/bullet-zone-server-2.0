/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*     */ import org.springframework.web.context.support.StandardServletEnvironment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpServletBean
/*     */   extends HttpServlet
/*     */   implements EnvironmentCapable, EnvironmentAware
/*     */ {
/*  85 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private ConfigurableEnvironment environment;
/*     */   
/*  89 */   private final Set<String> requiredProperties = new HashSet(4);
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
/*     */   protected final void addRequiredProperty(String property)
/*     */   {
/* 102 */     this.requiredProperties.add(property);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 114 */     Assert.isInstanceOf(ConfigurableEnvironment.class, environment, "ConfigurableEnvironment required");
/* 115 */     this.environment = ((ConfigurableEnvironment)environment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurableEnvironment getEnvironment()
/*     */   {
/* 125 */     if (this.environment == null) {
/* 126 */       this.environment = createEnvironment();
/*     */     }
/* 128 */     return this.environment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ConfigurableEnvironment createEnvironment()
/*     */   {
/* 137 */     return new StandardServletEnvironment();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void init()
/*     */     throws ServletException
/*     */   {
/* 148 */     if (this.logger.isDebugEnabled()) {
/* 149 */       this.logger.debug("Initializing servlet '" + getServletName() + "'");
/*     */     }
/*     */     
/*     */ 
/* 153 */     PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
/* 154 */     if (!pvs.isEmpty()) {
/*     */       try {
/* 156 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/* 157 */         ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
/* 158 */         bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
/* 159 */         initBeanWrapper(bw);
/* 160 */         bw.setPropertyValues(pvs, true);
/*     */       }
/*     */       catch (BeansException ex) {
/* 163 */         if (this.logger.isErrorEnabled()) {
/* 164 */           this.logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
/*     */         }
/* 166 */         throw ex;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 171 */     initServletBean();
/*     */     
/* 173 */     if (this.logger.isDebugEnabled()) {
/* 174 */       this.logger.debug("Servlet '" + getServletName() + "' configured successfully");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initBeanWrapper(BeanWrapper bw)
/*     */     throws BeansException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initServletBean()
/*     */     throws ServletException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getServletName()
/*     */   {
/* 206 */     return getServletConfig() != null ? getServletConfig().getServletName() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ServletContext getServletContext()
/*     */   {
/* 216 */     return getServletConfig() != null ? getServletConfig().getServletContext() : null;
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
/*     */   private static class ServletConfigPropertyValues
/*     */     extends MutablePropertyValues
/*     */   {
/*     */     public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
/*     */       throws ServletException
/*     */     {
/* 235 */       Set<String> missingProps = !CollectionUtils.isEmpty(requiredProperties) ? new HashSet(requiredProperties) : null;
/*     */       
/*     */ 
/* 238 */       Enumeration<String> paramNames = config.getInitParameterNames();
/* 239 */       while (paramNames.hasMoreElements()) {
/* 240 */         String property = (String)paramNames.nextElement();
/* 241 */         Object value = config.getInitParameter(property);
/* 242 */         addPropertyValue(new PropertyValue(property, value));
/* 243 */         if (missingProps != null) {
/* 244 */           missingProps.remove(property);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 249 */       if (!CollectionUtils.isEmpty(missingProps))
/*     */       {
/*     */ 
/*     */ 
/* 253 */         throw new ServletException("Initialization from ServletConfig for servlet '" + config.getServletName() + "' failed; the following required properties were missing: " + StringUtils.collectionToDelimitedString(missingProps, ", "));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\HttpServletBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */