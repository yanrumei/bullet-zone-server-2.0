/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URIEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean encode;
/*     */   
/*     */   public URIEditor()
/*     */   {
/*  63 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URIEditor(boolean encode)
/*     */   {
/*  72 */     this.classLoader = null;
/*  73 */     this.encode = encode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URIEditor(ClassLoader classLoader)
/*     */   {
/*  83 */     this(classLoader, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URIEditor(ClassLoader classLoader, boolean encode)
/*     */   {
/*  94 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  95 */     this.encode = encode;
/*     */   }
/*     */   
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/* 101 */     if (StringUtils.hasText(text)) {
/* 102 */       String uri = text.trim();
/* 103 */       if ((this.classLoader != null) && (uri.startsWith("classpath:")))
/*     */       {
/* 105 */         ClassPathResource resource = new ClassPathResource(uri.substring("classpath:".length()), this.classLoader);
/*     */         try {
/* 107 */           String url = resource.getURL().toString();
/* 108 */           setValue(createURI(url));
/*     */         }
/*     */         catch (IOException ex) {
/* 111 */           throw new IllegalArgumentException("Could not retrieve URI for " + resource + ": " + ex.getMessage());
/*     */         }
/*     */         catch (URISyntaxException ex) {
/* 114 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 119 */           setValue(createURI(uri));
/*     */         }
/*     */         catch (URISyntaxException ex) {
/* 122 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 127 */       setValue(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected URI createURI(String value)
/*     */     throws URISyntaxException
/*     */   {
/* 140 */     int colonIndex = value.indexOf(':');
/* 141 */     if ((this.encode) && (colonIndex != -1)) {
/* 142 */       int fragmentIndex = value.indexOf('#', colonIndex + 1);
/* 143 */       String scheme = value.substring(0, colonIndex);
/* 144 */       String ssp = value.substring(colonIndex + 1, fragmentIndex > 0 ? fragmentIndex : value.length());
/* 145 */       String fragment = fragmentIndex > 0 ? value.substring(fragmentIndex + 1) : null;
/* 146 */       return new URI(scheme, ssp, fragment);
/*     */     }
/*     */     
/*     */ 
/* 150 */     return new URI(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 157 */     URI value = (URI)getValue();
/* 158 */     return value != null ? value.toString() : "";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\URIEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */