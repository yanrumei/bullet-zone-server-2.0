/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRegistrationBean
/*     */   extends RegistrationBean
/*     */ {
/*  56 */   private static final Log logger = LogFactory.getLog(ServletRegistrationBean.class);
/*     */   
/*  58 */   private static final String[] DEFAULT_MAPPINGS = { "/*" };
/*     */   
/*     */   private Servlet servlet;
/*     */   
/*  62 */   private Set<String> urlMappings = new LinkedHashSet();
/*     */   
/*  64 */   private boolean alwaysMapUrl = true;
/*     */   
/*  66 */   private int loadOnStartup = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MultipartConfigElement multipartConfig;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistrationBean() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistrationBean(Servlet servlet, String... urlMappings)
/*     */   {
/*  83 */     this(servlet, true, urlMappings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistrationBean(Servlet servlet, boolean alwaysMapUrl, String... urlMappings)
/*     */   {
/*  95 */     Assert.notNull(servlet, "Servlet must not be null");
/*  96 */     Assert.notNull(urlMappings, "UrlMappings must not be null");
/*  97 */     this.servlet = servlet;
/*  98 */     this.alwaysMapUrl = alwaysMapUrl;
/*  99 */     this.urlMappings.addAll(Arrays.asList(urlMappings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Servlet getServlet()
/*     */   {
/* 107 */     return this.servlet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServlet(Servlet servlet)
/*     */   {
/* 115 */     Assert.notNull(servlet, "Servlet must not be null");
/* 116 */     this.servlet = servlet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlMappings(Collection<String> urlMappings)
/*     */   {
/* 126 */     Assert.notNull(urlMappings, "UrlMappings must not be null");
/* 127 */     this.urlMappings = new LinkedHashSet(urlMappings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> getUrlMappings()
/*     */   {
/* 135 */     return this.urlMappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addUrlMappings(String... urlMappings)
/*     */   {
/* 144 */     Assert.notNull(urlMappings, "UrlMappings must not be null");
/* 145 */     this.urlMappings.addAll(Arrays.asList(urlMappings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLoadOnStartup(int loadOnStartup)
/*     */   {
/* 154 */     this.loadOnStartup = loadOnStartup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMultipartConfig(MultipartConfigElement multipartConfig)
/*     */   {
/* 162 */     this.multipartConfig = multipartConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipartConfigElement getMultipartConfig()
/*     */   {
/* 171 */     return this.multipartConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServletName()
/*     */   {
/* 179 */     return getOrDeduceName(this.servlet);
/*     */   }
/*     */   
/*     */   public void onStartup(ServletContext servletContext) throws ServletException
/*     */   {
/* 184 */     Assert.notNull(this.servlet, "Servlet must not be null");
/* 185 */     String name = getServletName();
/* 186 */     if (!isEnabled()) {
/* 187 */       logger.info("Servlet " + name + " was not registered (disabled)");
/* 188 */       return;
/*     */     }
/* 190 */     logger.info("Mapping servlet: '" + name + "' to " + this.urlMappings);
/* 191 */     ServletRegistration.Dynamic added = servletContext.addServlet(name, this.servlet);
/* 192 */     if (added == null) {
/* 193 */       logger.info("Servlet " + name + " was not registered (possibly already registered?)");
/*     */       
/* 195 */       return;
/*     */     }
/* 197 */     configure(added);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configure(ServletRegistration.Dynamic registration)
/*     */   {
/* 206 */     super.configure(registration);
/*     */     
/* 208 */     String[] urlMapping = (String[])this.urlMappings.toArray(new String[this.urlMappings.size()]);
/* 209 */     if ((urlMapping.length == 0) && (this.alwaysMapUrl)) {
/* 210 */       urlMapping = DEFAULT_MAPPINGS;
/*     */     }
/* 212 */     if (!ObjectUtils.isEmpty(urlMapping)) {
/* 213 */       registration.addMapping(urlMapping);
/*     */     }
/* 215 */     registration.setLoadOnStartup(this.loadOnStartup);
/* 216 */     if (this.multipartConfig != null) {
/* 217 */       registration.setMultipartConfig(this.multipartConfig);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletRegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */