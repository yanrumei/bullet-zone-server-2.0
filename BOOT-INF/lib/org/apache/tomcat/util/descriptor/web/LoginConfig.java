/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class LoginConfig
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public LoginConfig() {}
/*     */   
/*     */   public LoginConfig(String authMethod, String realmName, String loginPage, String errorPage)
/*     */   {
/*  61 */     setAuthMethod(authMethod);
/*  62 */     setRealmName(realmName);
/*  63 */     setLoginPage(loginPage);
/*  64 */     setErrorPage(errorPage);
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
/*     */ 
/*  76 */   private String authMethod = null;
/*     */   
/*     */   public String getAuthMethod() {
/*  79 */     return this.authMethod;
/*     */   }
/*     */   
/*     */   public void setAuthMethod(String authMethod) {
/*  83 */     this.authMethod = authMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private String errorPage = null;
/*     */   
/*     */   public String getErrorPage() {
/*  93 */     return this.errorPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setErrorPage(String errorPage)
/*     */   {
/* 100 */     this.errorPage = UDecoder.URLDecode(errorPage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */   private String loginPage = null;
/*     */   
/*     */   public String getLoginPage() {
/* 110 */     return this.loginPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLoginPage(String loginPage)
/*     */   {
/* 117 */     this.loginPage = UDecoder.URLDecode(loginPage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   private String realmName = null;
/*     */   
/*     */   public String getRealmName() {
/* 128 */     return this.realmName;
/*     */   }
/*     */   
/*     */   public void setRealmName(String realmName) {
/* 132 */     this.realmName = realmName;
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
/* 145 */     StringBuilder sb = new StringBuilder("LoginConfig[");
/* 146 */     sb.append("authMethod=");
/* 147 */     sb.append(this.authMethod);
/* 148 */     if (this.realmName != null) {
/* 149 */       sb.append(", realmName=");
/* 150 */       sb.append(this.realmName);
/*     */     }
/* 152 */     if (this.loginPage != null) {
/* 153 */       sb.append(", loginPage=");
/* 154 */       sb.append(this.loginPage);
/*     */     }
/* 156 */     if (this.errorPage != null) {
/* 157 */       sb.append(", errorPage=");
/* 158 */       sb.append(this.errorPage);
/*     */     }
/* 160 */     sb.append("]");
/* 161 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 171 */     int prime = 31;
/* 172 */     int result = 1;
/*     */     
/* 174 */     result = 31 * result + (this.authMethod == null ? 0 : this.authMethod.hashCode());
/*     */     
/* 176 */     result = 31 * result + (this.errorPage == null ? 0 : this.errorPage.hashCode());
/*     */     
/* 178 */     result = 31 * result + (this.loginPage == null ? 0 : this.loginPage.hashCode());
/*     */     
/* 180 */     result = 31 * result + (this.realmName == null ? 0 : this.realmName.hashCode());
/* 181 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 190 */     if (this == obj)
/* 191 */       return true;
/* 192 */     if (!(obj instanceof LoginConfig))
/* 193 */       return false;
/* 194 */     LoginConfig other = (LoginConfig)obj;
/* 195 */     if (this.authMethod == null) {
/* 196 */       if (other.authMethod != null)
/* 197 */         return false;
/* 198 */     } else if (!this.authMethod.equals(other.authMethod))
/* 199 */       return false;
/* 200 */     if (this.errorPage == null) {
/* 201 */       if (other.errorPage != null)
/* 202 */         return false;
/* 203 */     } else if (!this.errorPage.equals(other.errorPage))
/* 204 */       return false;
/* 205 */     if (this.loginPage == null) {
/* 206 */       if (other.loginPage != null)
/* 207 */         return false;
/* 208 */     } else if (!this.loginPage.equals(other.loginPage))
/* 209 */       return false;
/* 210 */     if (this.realmName == null) {
/* 211 */       if (other.realmName != null)
/* 212 */         return false;
/* 213 */     } else if (!this.realmName.equals(other.realmName))
/* 214 */       return false;
/* 215 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\LoginConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */