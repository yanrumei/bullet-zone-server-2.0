/*     */ package org.apache.tomcat.util.descriptor.tld;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.tagext.TagAttributeInfo;
/*     */ import javax.servlet.jsp.tagext.TagVariableInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TagXml
/*     */ {
/*     */   private String name;
/*     */   private String tagClass;
/*     */   private String teiClass;
/*  36 */   private String bodyContent = "JSP";
/*     */   private String displayName;
/*     */   private String smallIcon;
/*     */   private String largeIcon;
/*     */   private String info;
/*     */   private boolean dynamicAttributes;
/*  42 */   private final List<TagAttributeInfo> attributes = new ArrayList();
/*  43 */   private final List<TagVariableInfo> variables = new ArrayList();
/*     */   
/*     */   public String getName() {
/*  46 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  50 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getTagClass() {
/*  54 */     return this.tagClass;
/*     */   }
/*     */   
/*     */   public void setTagClass(String tagClass) {
/*  58 */     this.tagClass = tagClass;
/*     */   }
/*     */   
/*     */   public String getTeiClass() {
/*  62 */     return this.teiClass;
/*     */   }
/*     */   
/*     */   public void setTeiClass(String teiClass) {
/*  66 */     this.teiClass = teiClass;
/*     */   }
/*     */   
/*     */   public String getBodyContent() {
/*  70 */     return this.bodyContent;
/*     */   }
/*     */   
/*     */   public void setBodyContent(String bodyContent) {
/*  74 */     this.bodyContent = bodyContent;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/*  78 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  82 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */   public String getSmallIcon() {
/*  86 */     return this.smallIcon;
/*     */   }
/*     */   
/*     */   public void setSmallIcon(String smallIcon) {
/*  90 */     this.smallIcon = smallIcon;
/*     */   }
/*     */   
/*     */   public String getLargeIcon() {
/*  94 */     return this.largeIcon;
/*     */   }
/*     */   
/*     */   public void setLargeIcon(String largeIcon) {
/*  98 */     this.largeIcon = largeIcon;
/*     */   }
/*     */   
/*     */   public String getInfo() {
/* 102 */     return this.info;
/*     */   }
/*     */   
/*     */   public void setInfo(String info) {
/* 106 */     this.info = info;
/*     */   }
/*     */   
/*     */   public boolean hasDynamicAttributes() {
/* 110 */     return this.dynamicAttributes;
/*     */   }
/*     */   
/*     */   public void setDynamicAttributes(boolean dynamicAttributes) {
/* 114 */     this.dynamicAttributes = dynamicAttributes;
/*     */   }
/*     */   
/*     */   public List<TagAttributeInfo> getAttributes() {
/* 118 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public List<TagVariableInfo> getVariables() {
/* 122 */     return this.variables;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\TagXml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */