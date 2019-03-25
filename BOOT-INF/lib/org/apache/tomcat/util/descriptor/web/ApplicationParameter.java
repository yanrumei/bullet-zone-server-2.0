/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class ApplicationParameter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  43 */   private String description = null;
/*     */   
/*     */   public String getDescription() {
/*  46 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  50 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private String name = null;
/*     */   
/*     */   public String getName() {
/*  60 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  64 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private boolean override = true;
/*     */   
/*     */   public boolean getOverride() {
/*  75 */     return this.override;
/*     */   }
/*     */   
/*     */   public void setOverride(boolean override) {
/*  79 */     this.override = override;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private String value = null;
/*     */   
/*     */   public String getValue() {
/*  89 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  93 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     StringBuilder sb = new StringBuilder("ApplicationParameter[");
/* 106 */     sb.append("name=");
/* 107 */     sb.append(this.name);
/* 108 */     if (this.description != null) {
/* 109 */       sb.append(", description=");
/* 110 */       sb.append(this.description);
/*     */     }
/* 112 */     sb.append(", value=");
/* 113 */     sb.append(this.value);
/* 114 */     sb.append(", override=");
/* 115 */     sb.append(this.override);
/* 116 */     sb.append("]");
/* 117 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ApplicationParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */