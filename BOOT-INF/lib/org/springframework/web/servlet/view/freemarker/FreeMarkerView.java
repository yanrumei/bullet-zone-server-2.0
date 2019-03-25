/*     */ package org.springframework.web.servlet.view.freemarker;
/*     */ 
/*     */ import freemarker.core.ParseException;
/*     */ import freemarker.ext.jsp.TaglibFactory;
/*     */ import freemarker.ext.servlet.AllHttpScopesHashModel;
/*     */ import freemarker.ext.servlet.HttpRequestHashModel;
/*     */ import freemarker.ext.servlet.HttpRequestParametersHashModel;
/*     */ import freemarker.ext.servlet.HttpSessionHashModel;
/*     */ import freemarker.ext.servlet.ServletContextHashModel;
/*     */ import freemarker.template.Configuration;
/*     */ import freemarker.template.DefaultObjectWrapperBuilder;
/*     */ import freemarker.template.ObjectWrapper;
/*     */ import freemarker.template.SimpleHash;
/*     */ import freemarker.template.Template;
/*     */ import freemarker.template.TemplateException;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.GenericServlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FreeMarkerView
/*     */   extends AbstractTemplateView
/*     */ {
/*     */   private String encoding;
/*     */   private Configuration configuration;
/*     */   private TaglibFactory taglibFactory;
/*     */   private ServletContextHashModel servletContextHashModel;
/*     */   
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 104 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getEncoding()
/*     */   {
/* 111 */     return this.encoding;
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
/*     */   public void setConfiguration(Configuration configuration)
/*     */   {
/* 124 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Configuration getConfiguration()
/*     */   {
/* 131 */     return this.configuration;
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
/*     */   protected void initServletContext(ServletContext servletContext)
/*     */     throws BeansException
/*     */   {
/* 145 */     if (getConfiguration() != null) {
/* 146 */       this.taglibFactory = new TaglibFactory(servletContext);
/*     */     }
/*     */     else {
/* 149 */       FreeMarkerConfig config = autodetectConfiguration();
/* 150 */       setConfiguration(config.getConfiguration());
/* 151 */       this.taglibFactory = config.getTaglibFactory();
/*     */     }
/*     */     
/* 154 */     GenericServlet servlet = new GenericServletAdapter(null);
/*     */     try {
/* 156 */       servlet.init(new DelegatingServletConfig(null));
/*     */     }
/*     */     catch (ServletException ex) {
/* 159 */       throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
/*     */     }
/* 161 */     this.servletContextHashModel = new ServletContextHashModel(servlet, getObjectWrapper());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FreeMarkerConfig autodetectConfiguration()
/*     */     throws BeansException
/*     */   {
/*     */     try
/*     */     {
/* 173 */       return (FreeMarkerConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(
/* 174 */         getApplicationContext(), FreeMarkerConfig.class, true, false);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/* 177 */       throw new ApplicationContextException("Must define a single FreeMarkerConfig bean in this web application context (may be inherited): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectWrapper getObjectWrapper()
/*     */   {
/* 190 */     ObjectWrapper ow = getConfiguration().getObjectWrapper();
/* 191 */     return ow != null ? ow : new DefaultObjectWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
/* 192 */       .build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean checkResource(Locale locale)
/*     */     throws Exception
/*     */   {
/* 202 */     String url = getUrl();
/*     */     try
/*     */     {
/* 205 */       getTemplate(url, locale);
/* 206 */       return true;
/*     */     }
/*     */     catch (FileNotFoundException ex) {
/* 209 */       if (this.logger.isDebugEnabled()) {
/* 210 */         this.logger.debug("No FreeMarker view found for URL: " + url);
/*     */       }
/* 212 */       return false;
/*     */     }
/*     */     catch (ParseException ex) {
/* 215 */       throw new ApplicationContextException("Failed to parse FreeMarker template for URL [" + url + "]", ex);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 219 */       throw new ApplicationContextException("Could not load FreeMarker template for URL [" + url + "]", ex);
/*     */     }
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
/*     */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 234 */     exposeHelpers(model, request);
/* 235 */     doRender(model, request, response);
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
/*     */   protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 276 */     exposeModelAsRequestAttributes(model, request);
/*     */     
/* 278 */     SimpleHash fmModel = buildTemplateModel(model, request, response);
/*     */     
/* 280 */     if (this.logger.isDebugEnabled()) {
/* 281 */       this.logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
/*     */     }
/*     */     
/* 284 */     Locale locale = RequestContextUtils.getLocale(request);
/* 285 */     processTemplate(getTemplate(locale), fmModel, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 297 */     AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
/* 298 */     fmModel.put("JspTaglibs", this.taglibFactory);
/* 299 */     fmModel.put("Application", this.servletContextHashModel);
/* 300 */     fmModel.put("Session", buildSessionModel(request, response));
/* 301 */     fmModel.put("Request", new HttpRequestHashModel(request, response, getObjectWrapper()));
/* 302 */     fmModel.put("RequestParameters", new HttpRequestParametersHashModel(request));
/* 303 */     fmModel.putAll(model);
/* 304 */     return fmModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HttpSessionHashModel buildSessionModel(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 315 */     HttpSession session = request.getSession(false);
/* 316 */     if (session != null) {
/* 317 */       return new HttpSessionHashModel(session, getObjectWrapper());
/*     */     }
/*     */     
/* 320 */     return new HttpSessionHashModel(null, request, response, getObjectWrapper());
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
/*     */   protected Template getTemplate(Locale locale)
/*     */     throws IOException
/*     */   {
/* 336 */     return getTemplate(getUrl(), locale);
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
/*     */   protected Template getTemplate(String name, Locale locale)
/*     */     throws IOException
/*     */   {
/* 350 */     return getEncoding() != null ? 
/* 351 */       getConfiguration().getTemplate(name, locale, getEncoding()) : 
/* 352 */       getConfiguration().getTemplate(name, locale);
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
/*     */   protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
/*     */     throws IOException, TemplateException
/*     */   {
/* 368 */     template.process(model, response.getWriter());
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
/*     */   private class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private DelegatingServletConfig() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getServletName()
/*     */     {
/* 394 */       return FreeMarkerView.this.getBeanName();
/*     */     }
/*     */     
/*     */     public ServletContext getServletContext()
/*     */     {
/* 399 */       return FreeMarkerView.this.getServletContext();
/*     */     }
/*     */     
/*     */     public String getInitParameter(String paramName)
/*     */     {
/* 404 */       return null;
/*     */     }
/*     */     
/*     */     public Enumeration<String> getInitParameterNames()
/*     */     {
/* 409 */       return Collections.enumeration(Collections.emptySet());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GenericServletAdapter
/*     */     extends GenericServlet
/*     */   {
/*     */     public void service(ServletRequest servletRequest, ServletResponse servletResponse) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\freemarker\FreeMarkerView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */