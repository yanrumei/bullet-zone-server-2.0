/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import javax.servlet.descriptor.JspPropertyGroupDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspPropertyGroupDescriptorImpl
/*     */   implements JspPropertyGroupDescriptor
/*     */ {
/*     */   private final JspPropertyGroup jspPropertyGroup;
/*     */   
/*     */   public JspPropertyGroupDescriptorImpl(JspPropertyGroup jspPropertyGroup)
/*     */   {
/*  34 */     this.jspPropertyGroup = jspPropertyGroup;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getBuffer()
/*     */   {
/*  40 */     return this.jspPropertyGroup.getBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDefaultContentType()
/*     */   {
/*  46 */     return this.jspPropertyGroup.getDefaultContentType();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeferredSyntaxAllowedAsLiteral()
/*     */   {
/*  52 */     String result = null;
/*     */     
/*  54 */     if (this.jspPropertyGroup.getDeferredSyntax() != null) {
/*  55 */       result = this.jspPropertyGroup.getDeferredSyntax().toString();
/*     */     }
/*     */     
/*  58 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getElIgnored()
/*     */   {
/*  64 */     String result = null;
/*     */     
/*  66 */     if (this.jspPropertyGroup.getElIgnored() != null) {
/*  67 */       result = this.jspPropertyGroup.getElIgnored().toString();
/*     */     }
/*     */     
/*  70 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getErrorOnUndeclaredNamespace()
/*     */   {
/*  76 */     String result = null;
/*     */     
/*  78 */     if (this.jspPropertyGroup.getErrorOnUndeclaredNamespace() != null)
/*     */     {
/*  80 */       result = this.jspPropertyGroup.getErrorOnUndeclaredNamespace().toString();
/*     */     }
/*     */     
/*  83 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getIncludeCodas()
/*     */   {
/*  89 */     return new ArrayList(this.jspPropertyGroup.getIncludeCodas());
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getIncludePreludes()
/*     */   {
/*  95 */     return new ArrayList(this.jspPropertyGroup.getIncludePreludes());
/*     */   }
/*     */   
/*     */ 
/*     */   public String getIsXml()
/*     */   {
/* 101 */     String result = null;
/*     */     
/* 103 */     if (this.jspPropertyGroup.getIsXml() != null) {
/* 104 */       result = this.jspPropertyGroup.getIsXml().toString();
/*     */     }
/*     */     
/* 107 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPageEncoding()
/*     */   {
/* 113 */     return this.jspPropertyGroup.getPageEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getScriptingInvalid()
/*     */   {
/* 119 */     String result = null;
/*     */     
/* 121 */     if (this.jspPropertyGroup.getScriptingInvalid() != null) {
/* 122 */       result = this.jspPropertyGroup.getScriptingInvalid().toString();
/*     */     }
/*     */     
/* 125 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getTrimDirectiveWhitespaces()
/*     */   {
/* 131 */     String result = null;
/*     */     
/* 133 */     if (this.jspPropertyGroup.getTrimWhitespace() != null) {
/* 134 */       result = this.jspPropertyGroup.getTrimWhitespace().toString();
/*     */     }
/*     */     
/* 137 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getUrlPatterns()
/*     */   {
/* 143 */     return new ArrayList(this.jspPropertyGroup.getUrlPatterns());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\JspPropertyGroupDescriptorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */