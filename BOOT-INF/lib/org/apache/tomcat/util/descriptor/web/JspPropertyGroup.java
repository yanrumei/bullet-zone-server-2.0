/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspPropertyGroup
/*     */   extends XmlEncodingBase
/*     */ {
/*  31 */   private Boolean deferredSyntax = null;
/*     */   
/*  33 */   public void setDeferredSyntax(String deferredSyntax) { this.deferredSyntax = Boolean.valueOf(deferredSyntax); }
/*     */   
/*  35 */   public Boolean getDeferredSyntax() { return this.deferredSyntax; }
/*     */   
/*  37 */   private Boolean elIgnored = null;
/*     */   
/*  39 */   public void setElIgnored(String elIgnored) { this.elIgnored = Boolean.valueOf(elIgnored); }
/*     */   
/*  41 */   public Boolean getElIgnored() { return this.elIgnored; }
/*     */   
/*  43 */   private final Collection<String> includeCodas = new ArrayList();
/*     */   
/*  45 */   public void addIncludeCoda(String includeCoda) { this.includeCodas.add(includeCoda); }
/*     */   
/*  47 */   public Collection<String> getIncludeCodas() { return this.includeCodas; }
/*     */   
/*  49 */   private final Collection<String> includePreludes = new ArrayList();
/*     */   
/*  51 */   public void addIncludePrelude(String includePrelude) { this.includePreludes.add(includePrelude); }
/*     */   
/*  53 */   public Collection<String> getIncludePreludes() { return this.includePreludes; }
/*     */   
/*  55 */   private Boolean isXml = null;
/*     */   
/*  57 */   public void setIsXml(String isXml) { this.isXml = Boolean.valueOf(isXml); }
/*     */   
/*  59 */   public Boolean getIsXml() { return this.isXml; }
/*     */   
/*  61 */   private String pageEncoding = null;
/*     */   
/*  63 */   public void setPageEncoding(String pageEncoding) { this.pageEncoding = pageEncoding; }
/*     */   
/*  65 */   public String getPageEncoding() { return this.pageEncoding; }
/*     */   
/*  67 */   private Boolean scriptingInvalid = null;
/*     */   
/*  69 */   public void setScriptingInvalid(String scriptingInvalid) { this.scriptingInvalid = Boolean.valueOf(scriptingInvalid); }
/*     */   
/*  71 */   public Boolean getScriptingInvalid() { return this.scriptingInvalid; }
/*     */   
/*  73 */   private Boolean trimWhitespace = null;
/*     */   
/*  75 */   public void setTrimWhitespace(String trimWhitespace) { this.trimWhitespace = Boolean.valueOf(trimWhitespace); }
/*     */   
/*  77 */   public Boolean getTrimWhitespace() { return this.trimWhitespace; }
/*     */   
/*  79 */   private LinkedHashSet<String> urlPattern = new LinkedHashSet();
/*     */   
/*  81 */   public void addUrlPattern(String urlPattern) { addUrlPatternDecoded(UDecoder.URLDecode(urlPattern, getCharset())); }
/*     */   
/*     */ 
/*  84 */   public void addUrlPatternDecoded(String urlPattern) { this.urlPattern.add(urlPattern); }
/*     */   
/*  86 */   public Set<String> getUrlPatterns() { return this.urlPattern; }
/*     */   
/*  88 */   private String defaultContentType = null;
/*     */   
/*  90 */   public void setDefaultContentType(String defaultContentType) { this.defaultContentType = defaultContentType; }
/*     */   
/*  92 */   public String getDefaultContentType() { return this.defaultContentType; }
/*     */   
/*  94 */   private String buffer = null;
/*     */   
/*  96 */   public void setBuffer(String buffer) { this.buffer = buffer; }
/*     */   
/*  98 */   public String getBuffer() { return this.buffer; }
/*     */   
/* 100 */   private Boolean errorOnUndeclaredNamespace = null;
/*     */   
/*     */   public void setErrorOnUndeclaredNamespace(String errorOnUndeclaredNamespace)
/*     */   {
/* 104 */     this.errorOnUndeclaredNamespace = Boolean.valueOf(errorOnUndeclaredNamespace);
/*     */   }
/*     */   
/* 107 */   public Boolean getErrorOnUndeclaredNamespace() { return this.errorOnUndeclaredNamespace; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\JspPropertyGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */