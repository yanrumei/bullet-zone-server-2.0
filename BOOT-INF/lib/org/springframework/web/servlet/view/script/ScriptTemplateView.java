/*     */ package org.springframework.web.servlet.view.script;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.scripting.support.StandardScriptEvalException;
/*     */ import org.springframework.scripting.support.StandardScriptUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptTemplateView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "text/html";
/*  72 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */   private static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:";
/*     */   
/*  77 */   private static final ThreadLocal<Map<Object, ScriptEngine>> enginesHolder = new NamedThreadLocal("ScriptTemplateView engines");
/*     */   
/*     */ 
/*     */   private ScriptEngine engine;
/*     */   
/*     */ 
/*     */   private String engineName;
/*     */   
/*     */ 
/*     */   private Boolean sharedEngine;
/*     */   
/*     */ 
/*     */   private String[] scripts;
/*     */   
/*     */ 
/*     */   private String renderObject;
/*     */   
/*     */   private String renderFunction;
/*     */   
/*     */   private Charset charset;
/*     */   
/*     */   private String[] resourceLoaderPaths;
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   private volatile ScriptEngineManager scriptEngineManager;
/*     */   
/*     */ 
/*     */   public ScriptTemplateView()
/*     */   {
/* 107 */     setContentType(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScriptTemplateView(String url)
/*     */   {
/* 115 */     super(url);
/* 116 */     setContentType(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEngine(ScriptEngine engine)
/*     */   {
/* 124 */     Assert.isInstanceOf(Invocable.class, engine, "ScriptEngine must implement Invocable");
/* 125 */     this.engine = engine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setEngineName(String engineName)
/*     */   {
/* 132 */     this.engineName = engineName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSharedEngine(Boolean sharedEngine)
/*     */   {
/* 139 */     this.sharedEngine = sharedEngine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setScripts(String... scripts)
/*     */   {
/* 146 */     this.scripts = scripts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRenderObject(String renderObject)
/*     */   {
/* 153 */     this.renderObject = renderObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRenderFunction(String functionName)
/*     */   {
/* 160 */     this.renderFunction = functionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/* 169 */     super.setContentType(contentType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 176 */     this.charset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setResourceLoaderPath(String resourceLoaderPath)
/*     */   {
/* 183 */     String[] paths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
/* 184 */     this.resourceLoaderPaths = new String[paths.length + 1];
/* 185 */     this.resourceLoaderPaths[0] = "";
/* 186 */     for (int i = 0; i < paths.length; i++) {
/* 187 */       String path = paths[i];
/* 188 */       if ((!path.endsWith("/")) && (!path.endsWith(":"))) {
/* 189 */         path = path + "/";
/*     */       }
/* 191 */       this.resourceLoaderPaths[(i + 1)] = path;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initApplicationContext(ApplicationContext context)
/*     */   {
/* 198 */     super.initApplicationContext(context);
/*     */     
/* 200 */     ScriptTemplateConfig viewConfig = autodetectViewConfig();
/* 201 */     if ((this.engine == null) && (viewConfig.getEngine() != null)) {
/* 202 */       setEngine(viewConfig.getEngine());
/*     */     }
/* 204 */     if ((this.engineName == null) && (viewConfig.getEngineName() != null)) {
/* 205 */       this.engineName = viewConfig.getEngineName();
/*     */     }
/* 207 */     if ((this.scripts == null) && (viewConfig.getScripts() != null)) {
/* 208 */       this.scripts = viewConfig.getScripts();
/*     */     }
/* 210 */     if ((this.renderObject == null) && (viewConfig.getRenderObject() != null)) {
/* 211 */       this.renderObject = viewConfig.getRenderObject();
/*     */     }
/* 213 */     if ((this.renderFunction == null) && (viewConfig.getRenderFunction() != null)) {
/* 214 */       this.renderFunction = viewConfig.getRenderFunction();
/*     */     }
/* 216 */     if (getContentType() == null) {
/* 217 */       setContentType(viewConfig.getContentType() != null ? viewConfig.getContentType() : "text/html");
/*     */     }
/* 219 */     if (this.charset == null) {
/* 220 */       this.charset = (viewConfig.getCharset() != null ? viewConfig.getCharset() : DEFAULT_CHARSET);
/*     */     }
/* 222 */     if (this.resourceLoaderPaths == null) {
/* 223 */       String resourceLoaderPath = viewConfig.getResourceLoaderPath();
/* 224 */       setResourceLoaderPath(resourceLoaderPath == null ? "classpath:" : resourceLoaderPath);
/*     */     }
/* 226 */     if (this.resourceLoader == null) {
/* 227 */       this.resourceLoader = getApplicationContext();
/*     */     }
/* 229 */     if ((this.sharedEngine == null) && (viewConfig.isSharedEngine() != null)) {
/* 230 */       this.sharedEngine = viewConfig.isSharedEngine();
/*     */     }
/*     */     
/* 233 */     Assert.isTrue((this.engine == null) || (this.engineName == null), "You should define either 'engine' or 'engineName', not both.");
/*     */     
/* 235 */     Assert.isTrue((this.engine != null) || (this.engineName != null), "No script engine found, please specify either 'engine' or 'engineName'.");
/*     */     
/*     */ 
/* 238 */     if (Boolean.FALSE.equals(this.sharedEngine)) {
/* 239 */       Assert.isTrue(this.engineName != null, "When 'sharedEngine' is set to false, you should specify the script engine using the 'engineName' property, not the 'engine' one.");
/*     */ 
/*     */ 
/*     */     }
/* 243 */     else if (this.engine != null) {
/* 244 */       loadScripts(this.engine);
/*     */     }
/*     */     else {
/* 247 */       setEngine(createEngineFromName());
/*     */     }
/*     */     
/* 250 */     Assert.isTrue(this.renderFunction != null, "The 'renderFunction' property must be defined.");
/*     */   }
/*     */   
/*     */   protected ScriptEngine getEngine() {
/* 254 */     if (Boolean.FALSE.equals(this.sharedEngine)) {
/* 255 */       Map<Object, ScriptEngine> engines = (Map)enginesHolder.get();
/* 256 */       if (engines == null) {
/* 257 */         engines = new HashMap(4);
/* 258 */         enginesHolder.set(engines);
/*     */       }
/* 260 */       Object engineKey = !ObjectUtils.isEmpty(this.scripts) ? new EngineKey(this.engineName, this.scripts) : this.engineName;
/*     */       
/* 262 */       ScriptEngine engine = (ScriptEngine)engines.get(engineKey);
/* 263 */       if (engine == null) {
/* 264 */         engine = createEngineFromName();
/* 265 */         engines.put(engineKey, engine);
/*     */       }
/* 267 */       return engine;
/*     */     }
/*     */     
/*     */ 
/* 271 */     return this.engine;
/*     */   }
/*     */   
/*     */   protected ScriptEngine createEngineFromName()
/*     */   {
/* 276 */     if (this.scriptEngineManager == null) {
/* 277 */       this.scriptEngineManager = new ScriptEngineManager(getApplicationContext().getClassLoader());
/*     */     }
/*     */     
/* 280 */     ScriptEngine engine = StandardScriptUtils.retrieveEngineByName(this.scriptEngineManager, this.engineName);
/* 281 */     loadScripts(engine);
/* 282 */     return engine;
/*     */   }
/*     */   
/*     */   protected void loadScripts(ScriptEngine engine) {
/* 286 */     if (!ObjectUtils.isEmpty(this.scripts)) {
/* 287 */       for (String script : this.scripts) {
/* 288 */         Resource resource = getResource(script);
/* 289 */         if (resource == null) {
/* 290 */           throw new IllegalStateException("Script resource [" + script + "] not found");
/*     */         }
/*     */         try {
/* 293 */           engine.eval(new InputStreamReader(resource.getInputStream()));
/*     */         }
/*     */         catch (Throwable ex) {
/* 296 */           throw new IllegalStateException("Failed to evaluate script [" + script + "]", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Resource getResource(String location) {
/* 303 */     for (String path : this.resourceLoaderPaths) {
/* 304 */       Resource resource = this.resourceLoader.getResource(path + location);
/* 305 */       if (resource.exists()) {
/* 306 */         return resource;
/*     */       }
/*     */     }
/* 309 */     return null;
/*     */   }
/*     */   
/*     */   protected ScriptTemplateConfig autodetectViewConfig() throws BeansException {
/*     */     try {
/* 314 */       return (ScriptTemplateConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(
/* 315 */         getApplicationContext(), ScriptTemplateConfig.class, true, false);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/* 318 */       throw new ApplicationContextException("Expected a single ScriptTemplateConfig bean in the current Servlet web application context or the parent root context: ScriptTemplateConfigurer is the usual implementation. This bean may have any name.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkResource(Locale locale)
/*     */     throws Exception
/*     */   {
/* 327 */     return getResource(getUrl()) != null;
/*     */   }
/*     */   
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 332 */     super.prepareResponse(request, response);
/*     */     
/* 334 */     setResponseContentType(request, response);
/* 335 */     response.setCharacterEncoding(this.charset.name());
/*     */   }
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 343 */       ScriptEngine engine = getEngine();
/* 344 */       Invocable invocable = (Invocable)engine;
/* 345 */       String url = getUrl();
/* 346 */       String template = getTemplate(url);
/*     */       Object html;
/*     */       Object html;
/* 349 */       if (this.renderObject != null) {
/* 350 */         Object thiz = engine.eval(this.renderObject);
/* 351 */         html = invocable.invokeMethod(thiz, this.renderFunction, new Object[] { template, model, url });
/*     */       }
/*     */       else {
/* 354 */         html = invocable.invokeFunction(this.renderFunction, new Object[] { template, model, url });
/*     */       }
/*     */       
/* 357 */       response.getWriter().write(String.valueOf(html));
/*     */     }
/*     */     catch (ScriptException ex) {
/* 360 */       throw new ServletException("Failed to render script template", new StandardScriptEvalException(ex));
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getTemplate(String path) throws IOException {
/* 365 */     Resource resource = getResource(path);
/* 366 */     if (resource == null) {
/* 367 */       throw new IllegalStateException("Template resource [" + path + "] not found");
/*     */     }
/* 369 */     InputStreamReader reader = new InputStreamReader(resource.getInputStream(), this.charset);
/* 370 */     return FileCopyUtils.copyToString(reader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class EngineKey
/*     */   {
/*     */     private final String engineName;
/*     */     
/*     */ 
/*     */     private final String[] scripts;
/*     */     
/*     */ 
/*     */ 
/*     */     public EngineKey(String engineName, String[] scripts)
/*     */     {
/* 386 */       this.engineName = engineName;
/* 387 */       this.scripts = scripts;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 392 */       if (this == other) {
/* 393 */         return true;
/*     */       }
/* 395 */       if (!(other instanceof EngineKey)) {
/* 396 */         return false;
/*     */       }
/* 398 */       EngineKey otherKey = (EngineKey)other;
/* 399 */       return (this.engineName.equals(otherKey.engineName)) && (Arrays.equals(this.scripts, otherKey.scripts));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 404 */       return this.engineName.hashCode() * 29 + Arrays.hashCode(this.scripts);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\script\ScriptTemplateView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */