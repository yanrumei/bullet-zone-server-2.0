/*     */ package org.apache.tomcat.util.descriptor.tld;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.tagext.FunctionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaglibXml
/*     */ {
/*     */   private String tlibVersion;
/*     */   private String jspVersion;
/*     */   private String shortName;
/*     */   private String uri;
/*     */   private String info;
/*     */   private ValidatorXml validator;
/*  40 */   private final List<TagXml> tags = new ArrayList();
/*  41 */   private final List<TagFileXml> tagFiles = new ArrayList();
/*  42 */   private final List<String> listeners = new ArrayList();
/*  43 */   private final List<FunctionInfo> functions = new ArrayList();
/*     */   
/*     */   public String getTlibVersion() {
/*  46 */     return this.tlibVersion;
/*     */   }
/*     */   
/*     */   public void setTlibVersion(String tlibVersion) {
/*  50 */     this.tlibVersion = tlibVersion;
/*     */   }
/*     */   
/*     */   public String getJspVersion() {
/*  54 */     return this.jspVersion;
/*     */   }
/*     */   
/*     */   public void setJspVersion(String jspVersion) {
/*  58 */     this.jspVersion = jspVersion;
/*     */   }
/*     */   
/*     */   public String getShortName() {
/*  62 */     return this.shortName;
/*     */   }
/*     */   
/*     */   public void setShortName(String shortName) {
/*  66 */     this.shortName = shortName;
/*     */   }
/*     */   
/*     */   public String getUri() {
/*  70 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setUri(String uri) {
/*  74 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public String getInfo() {
/*  78 */     return this.info;
/*     */   }
/*     */   
/*     */   public void setInfo(String info) {
/*  82 */     this.info = info;
/*     */   }
/*     */   
/*     */   public ValidatorXml getValidator() {
/*  86 */     return this.validator;
/*     */   }
/*     */   
/*     */   public void setValidator(ValidatorXml validator) {
/*  90 */     this.validator = validator;
/*     */   }
/*     */   
/*     */   public void addTag(TagXml tag) {
/*  94 */     this.tags.add(tag);
/*     */   }
/*     */   
/*     */   public List<TagXml> getTags() {
/*  98 */     return this.tags;
/*     */   }
/*     */   
/*     */   public void addTagFile(TagFileXml tag) {
/* 102 */     this.tagFiles.add(tag);
/*     */   }
/*     */   
/*     */   public List<TagFileXml> getTagFiles() {
/* 106 */     return this.tagFiles;
/*     */   }
/*     */   
/*     */   public void addListener(String listener) {
/* 110 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public List<String> getListeners() {
/* 114 */     return this.listeners;
/*     */   }
/*     */   
/*     */   public void addFunction(String name, String klass, String signature) {
/* 118 */     this.functions.add(new FunctionInfo(name, klass, signature));
/*     */   }
/*     */   
/*     */   public List<FunctionInfo> getFunctions() {
/* 122 */     return this.functions;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\TaglibXml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */