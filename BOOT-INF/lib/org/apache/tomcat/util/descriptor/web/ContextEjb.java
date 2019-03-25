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
/*     */ 
/*     */ public class ContextEjb
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
/*     */ 
/*  40 */   private String home = null;
/*     */   
/*     */   public String getHome() {
/*  43 */     return this.home;
/*     */   }
/*     */   
/*     */   public void setHome(String home) {
/*  47 */     this.home = home;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private String link = null;
/*     */   
/*     */   public String getLink() {
/*  57 */     return this.link;
/*     */   }
/*     */   
/*     */   public void setLink(String link) {
/*  61 */     this.link = link;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private String remote = null;
/*     */   
/*     */   public String getRemote() {
/*  70 */     return this.remote;
/*     */   }
/*     */   
/*     */   public void setRemote(String remote) {
/*  74 */     this.remote = remote;
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
/*  87 */     StringBuilder sb = new StringBuilder("ContextEjb[");
/*  88 */     sb.append("name=");
/*  89 */     sb.append(getName());
/*  90 */     if (getDescription() != null) {
/*  91 */       sb.append(", description=");
/*  92 */       sb.append(getDescription());
/*     */     }
/*  94 */     if (getType() != null) {
/*  95 */       sb.append(", type=");
/*  96 */       sb.append(getType());
/*     */     }
/*  98 */     if (this.home != null) {
/*  99 */       sb.append(", home=");
/* 100 */       sb.append(this.home);
/*     */     }
/* 102 */     if (this.remote != null) {
/* 103 */       sb.append(", remote=");
/* 104 */       sb.append(this.remote);
/*     */     }
/* 106 */     if (this.link != null) {
/* 107 */       sb.append(", link=");
/* 108 */       sb.append(this.link);
/*     */     }
/* 110 */     sb.append("]");
/* 111 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 118 */     int prime = 31;
/* 119 */     int result = super.hashCode();
/* 120 */     result = 31 * result + (this.home == null ? 0 : this.home.hashCode());
/* 121 */     result = 31 * result + (this.link == null ? 0 : this.link.hashCode());
/* 122 */     result = 31 * result + (this.remote == null ? 0 : this.remote.hashCode());
/* 123 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 129 */     if (this == obj) {
/* 130 */       return true;
/*     */     }
/* 132 */     if (!super.equals(obj)) {
/* 133 */       return false;
/*     */     }
/* 135 */     if (getClass() != obj.getClass()) {
/* 136 */       return false;
/*     */     }
/* 138 */     ContextEjb other = (ContextEjb)obj;
/* 139 */     if (this.home == null) {
/* 140 */       if (other.home != null) {
/* 141 */         return false;
/*     */       }
/* 143 */     } else if (!this.home.equals(other.home)) {
/* 144 */       return false;
/*     */     }
/* 146 */     if (this.link == null) {
/* 147 */       if (other.link != null) {
/* 148 */         return false;
/*     */       }
/* 150 */     } else if (!this.link.equals(other.link)) {
/* 151 */       return false;
/*     */     }
/* 153 */     if (this.remote == null) {
/* 154 */       if (other.remote != null) {
/* 155 */         return false;
/*     */       }
/* 157 */     } else if (!this.remote.equals(other.remote)) {
/* 158 */       return false;
/*     */     }
/* 160 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextEjb.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */