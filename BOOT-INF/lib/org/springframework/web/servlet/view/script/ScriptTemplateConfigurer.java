/*     */ package org.springframework.web.servlet.view.script;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import javax.script.ScriptEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptTemplateConfigurer
/*     */   implements ScriptTemplateConfig
/*     */ {
/*     */   private ScriptEngine engine;
/*     */   private String engineName;
/*     */   private Boolean sharedEngine;
/*     */   private String[] scripts;
/*     */   private String renderObject;
/*     */   private String renderFunction;
/*     */   private String contentType;
/*     */   private Charset charset;
/*     */   private String resourceLoaderPath;
/*     */   
/*     */   public void setEngine(ScriptEngine engine)
/*     */   {
/*  79 */     this.engine = engine;
/*     */   }
/*     */   
/*     */   public ScriptEngine getEngine()
/*     */   {
/*  84 */     return this.engine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEngineName(String engineName)
/*     */   {
/*  94 */     this.engineName = engineName;
/*     */   }
/*     */   
/*     */   public String getEngineName()
/*     */   {
/*  99 */     return this.engineName;
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
/*     */   public void setSharedEngine(Boolean sharedEngine)
/*     */   {
/* 116 */     this.sharedEngine = sharedEngine;
/*     */   }
/*     */   
/*     */   public Boolean isSharedEngine()
/*     */   {
/* 121 */     return this.sharedEngine;
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
/*     */   public void setScripts(String... scriptNames)
/*     */   {
/* 136 */     this.scripts = scriptNames;
/*     */   }
/*     */   
/*     */   public String[] getScripts()
/*     */   {
/* 141 */     return this.scripts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRenderObject(String renderObject)
/*     */   {
/* 150 */     this.renderObject = renderObject;
/*     */   }
/*     */   
/*     */   public String getRenderObject()
/*     */   {
/* 155 */     return this.renderObject;
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
/*     */   public void setRenderFunction(String renderFunction)
/*     */   {
/* 169 */     this.renderFunction = renderFunction;
/*     */   }
/*     */   
/*     */   public String getRenderFunction()
/*     */   {
/* 174 */     return this.renderFunction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/* 183 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 192 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 200 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/* 205 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceLoaderPath(String resourceLoaderPath)
/*     */   {
/* 217 */     this.resourceLoaderPath = resourceLoaderPath;
/*     */   }
/*     */   
/*     */   public String getResourceLoaderPath()
/*     */   {
/* 222 */     return this.resourceLoaderPath;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\script\ScriptTemplateConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */