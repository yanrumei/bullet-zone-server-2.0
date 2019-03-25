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
/*     */ public class ContextLocalEjb
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
/*  38 */   private String home = null;
/*     */   
/*     */   public String getHome() {
/*  41 */     return this.home;
/*     */   }
/*     */   
/*     */   public void setHome(String home) {
/*  45 */     this.home = home;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private String link = null;
/*     */   
/*     */   public String getLink() {
/*  55 */     return this.link;
/*     */   }
/*     */   
/*     */   public void setLink(String link) {
/*  59 */     this.link = link;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private String local = null;
/*     */   
/*     */   public String getLocal() {
/*  69 */     return this.local;
/*     */   }
/*     */   
/*     */   public void setLocal(String local) {
/*  73 */     this.local = local;
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
/*  86 */     StringBuilder sb = new StringBuilder("ContextLocalEjb[");
/*  87 */     sb.append("name=");
/*  88 */     sb.append(getName());
/*  89 */     if (getDescription() != null) {
/*  90 */       sb.append(", description=");
/*  91 */       sb.append(getDescription());
/*     */     }
/*  93 */     if (getType() != null) {
/*  94 */       sb.append(", type=");
/*  95 */       sb.append(getType());
/*     */     }
/*  97 */     if (this.home != null) {
/*  98 */       sb.append(", home=");
/*  99 */       sb.append(this.home);
/*     */     }
/* 101 */     if (this.link != null) {
/* 102 */       sb.append(", link=");
/* 103 */       sb.append(this.link);
/*     */     }
/* 105 */     if (this.local != null) {
/* 106 */       sb.append(", local=");
/* 107 */       sb.append(this.local);
/*     */     }
/* 109 */     sb.append("]");
/* 110 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 116 */     int prime = 31;
/* 117 */     int result = super.hashCode();
/* 118 */     result = 31 * result + (this.home == null ? 0 : this.home.hashCode());
/* 119 */     result = 31 * result + (this.link == null ? 0 : this.link.hashCode());
/* 120 */     result = 31 * result + (this.local == null ? 0 : this.local.hashCode());
/* 121 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 127 */     if (this == obj) {
/* 128 */       return true;
/*     */     }
/* 130 */     if (!super.equals(obj)) {
/* 131 */       return false;
/*     */     }
/* 133 */     if (getClass() != obj.getClass()) {
/* 134 */       return false;
/*     */     }
/* 136 */     ContextLocalEjb other = (ContextLocalEjb)obj;
/* 137 */     if (this.home == null) {
/* 138 */       if (other.home != null) {
/* 139 */         return false;
/*     */       }
/* 141 */     } else if (!this.home.equals(other.home)) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.link == null) {
/* 145 */       if (other.link != null) {
/* 146 */         return false;
/*     */       }
/* 148 */     } else if (!this.link.equals(other.link)) {
/* 149 */       return false;
/*     */     }
/* 151 */     if (this.local == null) {
/* 152 */       if (other.local != null) {
/* 153 */         return false;
/*     */       }
/* 155 */     } else if (!this.local.equals(other.local)) {
/* 156 */       return false;
/*     */     }
/* 158 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextLocalEjb.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */