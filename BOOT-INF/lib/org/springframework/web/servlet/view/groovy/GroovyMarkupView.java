/*     */ package org.springframework.web.servlet.view.groovy;
/*     */ 
/*     */ import groovy.lang.Writable;
/*     */ import groovy.text.Template;
/*     */ import groovy.text.markup.MarkupTemplateEngine;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*     */ import org.springframework.web.util.NestedServletException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyMarkupView
/*     */   extends AbstractTemplateView
/*     */ {
/*     */   private MarkupTemplateEngine engine;
/*     */   
/*     */   public void setTemplateEngine(MarkupTemplateEngine engine)
/*     */   {
/*  63 */     this.engine = engine;
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
/*     */   protected void initApplicationContext(ApplicationContext context)
/*     */   {
/*  76 */     super.initApplicationContext();
/*  77 */     if (this.engine == null) {
/*  78 */       setTemplateEngine(autodetectMarkupTemplateEngine());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected MarkupTemplateEngine autodetectMarkupTemplateEngine()
/*     */     throws BeansException
/*     */   {
/*     */     try
/*     */     {
/*  88 */       return 
/*  89 */         ((GroovyMarkupConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), GroovyMarkupConfig.class, true, false)).getTemplateEngine();
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/*  92 */       throw new ApplicationContextException("Expected a single GroovyMarkupConfig bean in the current Servlet web application context or the parent root context: GroovyMarkupConfigurer is the usual implementation. This bean may have any name.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean checkResource(Locale locale)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 102 */       this.engine.resolveTemplate(getUrl());
/*     */     }
/*     */     catch (IOException ex) {
/* 105 */       return false;
/*     */     }
/* 107 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 114 */     Template template = getTemplate(getUrl());
/* 115 */     template.make(model).writeTo(new BufferedWriter(response.getWriter()));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Template getTemplate(String viewUrl)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 124 */       return this.engine.createTemplateByPath(viewUrl);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 127 */       Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
/*     */       
/*     */ 
/* 130 */       throw new NestedServletException("Could not find class while rendering Groovy Markup view with name '" + getUrl() + "': " + ex.getMessage() + "'", cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\groovy\GroovyMarkupView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */