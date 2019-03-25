/*     */ package org.apache.tomcat.util.descriptor.web;
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
/*     */ public class ContextResourceLink
/*     */   extends ResourceBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
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
/*  38 */   private String global = null;
/*     */   
/*     */ 
/*     */ 
/*  42 */   private String factory = null;
/*     */   
/*     */   public String getGlobal() {
/*  45 */     return this.global;
/*     */   }
/*     */   
/*     */   public void setGlobal(String global) {
/*  49 */     this.global = global;
/*     */   }
/*     */   
/*     */   public String getFactory() {
/*  53 */     return this.factory;
/*     */   }
/*     */   
/*     */   public void setFactory(String factory) {
/*  57 */     this.factory = factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  68 */     StringBuilder sb = new StringBuilder("ContextResourceLink[");
/*  69 */     sb.append("name=");
/*  70 */     sb.append(getName());
/*  71 */     if (getType() != null) {
/*  72 */       sb.append(", type=");
/*  73 */       sb.append(getType());
/*     */     }
/*  75 */     if (getGlobal() != null) {
/*  76 */       sb.append(", global=");
/*  77 */       sb.append(getGlobal());
/*     */     }
/*  79 */     sb.append("]");
/*  80 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  86 */     int prime = 31;
/*  87 */     int result = super.hashCode();
/*  88 */     result = 31 * result + (this.factory == null ? 0 : this.factory.hashCode());
/*  89 */     result = 31 * result + (this.global == null ? 0 : this.global.hashCode());
/*  90 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  96 */     if (this == obj) {
/*  97 */       return true;
/*     */     }
/*  99 */     if (!super.equals(obj)) {
/* 100 */       return false;
/*     */     }
/* 102 */     if (getClass() != obj.getClass()) {
/* 103 */       return false;
/*     */     }
/* 105 */     ContextResourceLink other = (ContextResourceLink)obj;
/* 106 */     if (this.factory == null) {
/* 107 */       if (other.factory != null) {
/* 108 */         return false;
/*     */       }
/* 110 */     } else if (!this.factory.equals(other.factory)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (this.global == null) {
/* 114 */       if (other.global != null) {
/* 115 */         return false;
/*     */       }
/* 117 */     } else if (!this.global.equals(other.global)) {
/* 118 */       return false;
/*     */     }
/* 120 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextResourceLink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */