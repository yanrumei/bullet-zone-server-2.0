/*     */ package org.springframework.web.servlet.view.xslt;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XsltViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*     */   private String sourceKey;
/*     */   private URIResolver uriResolver;
/*     */   private ErrorListener errorListener;
/*  43 */   private boolean indent = true;
/*     */   
/*     */   private Properties outputProperties;
/*     */   
/*  47 */   private boolean cacheTemplates = true;
/*     */   
/*     */   public XsltViewResolver()
/*     */   {
/*  51 */     setViewClass(requiredViewClass());
/*     */   }
/*     */   
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  57 */     return XsltView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSourceKey(String sourceKey)
/*     */   {
/*  69 */     this.sourceKey = sourceKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUriResolver(URIResolver uriResolver)
/*     */   {
/*  77 */     this.uriResolver = uriResolver;
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
/*     */   public void setErrorListener(ErrorListener errorListener)
/*     */   {
/*  90 */     this.errorListener = errorListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIndent(boolean indent)
/*     */   {
/* 101 */     this.indent = indent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOutputProperties(Properties outputProperties)
/*     */   {
/* 111 */     this.outputProperties = outputProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheTemplates(boolean cacheTemplates)
/*     */   {
/* 120 */     this.cacheTemplates = cacheTemplates;
/*     */   }
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 126 */     XsltView view = (XsltView)super.buildView(viewName);
/* 127 */     view.setSourceKey(this.sourceKey);
/* 128 */     if (this.uriResolver != null) {
/* 129 */       view.setUriResolver(this.uriResolver);
/*     */     }
/* 131 */     if (this.errorListener != null) {
/* 132 */       view.setErrorListener(this.errorListener);
/*     */     }
/* 134 */     view.setIndent(this.indent);
/* 135 */     view.setOutputProperties(this.outputProperties);
/* 136 */     view.setCacheTemplates(this.cacheTemplates);
/* 137 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\xslt\XsltViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */