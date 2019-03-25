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
/*     */ public class ContextEnvironment
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
/*  38 */   private boolean override = true;
/*     */   
/*     */   public boolean getOverride() {
/*  41 */     return this.override;
/*     */   }
/*     */   
/*     */   public void setOverride(boolean override) {
/*  45 */     this.override = override;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private String value = null;
/*     */   
/*     */   public String getValue() {
/*  55 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  59 */     this.value = value;
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
/*     */   public String toString()
/*     */   {
/*  72 */     StringBuilder sb = new StringBuilder("ContextEnvironment[");
/*  73 */     sb.append("name=");
/*  74 */     sb.append(getName());
/*  75 */     if (getDescription() != null) {
/*  76 */       sb.append(", description=");
/*  77 */       sb.append(getDescription());
/*     */     }
/*  79 */     if (getType() != null) {
/*  80 */       sb.append(", type=");
/*  81 */       sb.append(getType());
/*     */     }
/*  83 */     if (this.value != null) {
/*  84 */       sb.append(", value=");
/*  85 */       sb.append(this.value);
/*     */     }
/*  87 */     sb.append(", override=");
/*  88 */     sb.append(this.override);
/*  89 */     sb.append("]");
/*  90 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  97 */     int prime = 31;
/*  98 */     int result = super.hashCode();
/*  99 */     result = 31 * result + (this.override ? 1231 : 1237);
/* 100 */     result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 107 */     if (this == obj) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (!super.equals(obj)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (getClass() != obj.getClass()) {
/* 114 */       return false;
/*     */     }
/* 116 */     ContextEnvironment other = (ContextEnvironment)obj;
/* 117 */     if (this.override != other.override) {
/* 118 */       return false;
/*     */     }
/* 120 */     if (this.value == null) {
/* 121 */       if (other.value != null) {
/* 122 */         return false;
/*     */       }
/* 124 */     } else if (!this.value.equals(other.value)) {
/* 125 */       return false;
/*     */     }
/* 127 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextEnvironment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */