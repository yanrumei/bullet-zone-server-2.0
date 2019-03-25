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
/*     */ public class ContextResource
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
/*  40 */   private String auth = null;
/*     */   
/*     */   public String getAuth() {
/*  43 */     return this.auth;
/*     */   }
/*     */   
/*     */   public void setAuth(String auth) {
/*  47 */     this.auth = auth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private String scope = "Shareable";
/*     */   
/*     */   public String getScope() {
/*  57 */     return this.scope;
/*     */   }
/*     */   
/*     */   public void setScope(String scope) {
/*  61 */     this.scope = scope;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private boolean singleton = true;
/*     */   
/*     */   public boolean getSingleton() {
/*  73 */     return this.singleton;
/*     */   }
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  77 */     this.singleton = singleton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private String closeMethod = null;
/*     */   
/*     */   public String getCloseMethod() {
/*  90 */     return this.closeMethod;
/*     */   }
/*     */   
/*     */   public void setCloseMethod(String closeMethod) {
/*  94 */     this.closeMethod = closeMethod;
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
/* 107 */     StringBuilder sb = new StringBuilder("ContextResource[");
/* 108 */     sb.append("name=");
/* 109 */     sb.append(getName());
/* 110 */     if (getDescription() != null) {
/* 111 */       sb.append(", description=");
/* 112 */       sb.append(getDescription());
/*     */     }
/* 114 */     if (getType() != null) {
/* 115 */       sb.append(", type=");
/* 116 */       sb.append(getType());
/*     */     }
/* 118 */     if (this.auth != null) {
/* 119 */       sb.append(", auth=");
/* 120 */       sb.append(this.auth);
/*     */     }
/* 122 */     if (this.scope != null) {
/* 123 */       sb.append(", scope=");
/* 124 */       sb.append(this.scope);
/*     */     }
/* 126 */     sb.append("]");
/* 127 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 133 */     int prime = 31;
/* 134 */     int result = super.hashCode();
/* 135 */     result = 31 * result + (this.auth == null ? 0 : this.auth.hashCode());
/*     */     
/* 137 */     result = 31 * result + (this.closeMethod == null ? 0 : this.closeMethod.hashCode());
/* 138 */     result = 31 * result + (this.scope == null ? 0 : this.scope.hashCode());
/* 139 */     result = 31 * result + (this.singleton ? 1231 : 1237);
/* 140 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 146 */     if (this == obj) {
/* 147 */       return true;
/*     */     }
/* 149 */     if (!super.equals(obj)) {
/* 150 */       return false;
/*     */     }
/* 152 */     if (getClass() != obj.getClass()) {
/* 153 */       return false;
/*     */     }
/* 155 */     ContextResource other = (ContextResource)obj;
/* 156 */     if (this.auth == null) {
/* 157 */       if (other.auth != null) {
/* 158 */         return false;
/*     */       }
/* 160 */     } else if (!this.auth.equals(other.auth)) {
/* 161 */       return false;
/*     */     }
/* 163 */     if (this.closeMethod == null) {
/* 164 */       if (other.closeMethod != null) {
/* 165 */         return false;
/*     */       }
/* 167 */     } else if (!this.closeMethod.equals(other.closeMethod)) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (this.scope == null) {
/* 171 */       if (other.scope != null) {
/* 172 */         return false;
/*     */       }
/* 174 */     } else if (!this.scope.equals(other.scope)) {
/* 175 */       return false;
/*     */     }
/* 177 */     if (this.singleton != other.singleton) {
/* 178 */       return false;
/*     */     }
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */