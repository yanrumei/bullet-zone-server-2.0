/*     */ package org.springframework.boot.autoconfigure.mustache.web;
/*     */ 
/*     */ import com.samskivert.mustache.Mustache;
/*     */ import com.samskivert.mustache.Mustache.Compiler;
/*     */ import com.samskivert.mustache.Template;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.propertyeditors.LocaleEditor;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MustacheViewResolver
/*     */   extends AbstractTemplateViewResolver
/*     */ {
/*  44 */   private Mustache.Compiler compiler = Mustache.compiler();
/*     */   private String charset;
/*     */   
/*     */   public MustacheViewResolver()
/*     */   {
/*  49 */     setViewClass(requiredViewClass());
/*     */   }
/*     */   
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  54 */     return MustacheView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCompiler(Mustache.Compiler compiler)
/*     */   {
/*  62 */     this.compiler = compiler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(String charset)
/*     */   {
/*  70 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   protected View loadView(String viewName, Locale locale) throws Exception
/*     */   {
/*  75 */     Resource resource = resolveResource(viewName, locale);
/*  76 */     if (resource == null) {
/*  77 */       return null;
/*     */     }
/*  79 */     MustacheView mustacheView = (MustacheView)super.loadView(viewName, locale);
/*  80 */     mustacheView.setTemplate(createTemplate(resource));
/*  81 */     return mustacheView;
/*     */   }
/*     */   
/*     */   private Resource resolveResource(String viewName, Locale locale) {
/*  85 */     return resolveFromLocale(viewName, getLocale(locale));
/*     */   }
/*     */   
/*     */   private Resource resolveFromLocale(String viewName, String locale)
/*     */   {
/*  90 */     Resource resource = getApplicationContext().getResource(getPrefix() + viewName + locale + getSuffix());
/*  91 */     if ((resource == null) || (!resource.exists())) {
/*  92 */       if (locale.isEmpty()) {
/*  93 */         return null;
/*     */       }
/*  95 */       int index = locale.lastIndexOf("_");
/*  96 */       return resolveFromLocale(viewName, locale.substring(0, index));
/*     */     }
/*  98 */     return resource;
/*     */   }
/*     */   
/*     */   private String getLocale(Locale locale) {
/* 102 */     if (locale == null) {
/* 103 */       return "";
/*     */     }
/* 105 */     LocaleEditor localeEditor = new LocaleEditor();
/* 106 */     localeEditor.setValue(locale);
/* 107 */     return "_" + localeEditor.getAsText();
/*     */   }
/*     */   
/*     */   private Template createTemplate(Resource resource) throws IOException {
/* 111 */     Reader reader = getReader(resource);
/*     */     try {
/* 113 */       return this.compiler.compile(reader);
/*     */     }
/*     */     finally {
/* 116 */       reader.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private Reader getReader(Resource resource) throws IOException {
/* 121 */     if (this.charset != null) {
/* 122 */       return new InputStreamReader(resource.getInputStream(), this.charset);
/*     */     }
/* 124 */     return new InputStreamReader(resource.getInputStream());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\web\MustacheViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */