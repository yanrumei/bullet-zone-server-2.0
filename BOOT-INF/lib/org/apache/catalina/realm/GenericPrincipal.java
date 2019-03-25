/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import org.apache.catalina.TomcatPrincipal;
/*     */ import org.ietf.jgss.GSSCredential;
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
/*     */ public class GenericPrincipal
/*     */   implements TomcatPrincipal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String name;
/*     */   protected final String password;
/*     */   protected final String[] roles;
/*     */   protected final Principal userPrincipal;
/*     */   protected final transient LoginContext loginContext;
/*     */   
/*     */   public GenericPrincipal(String name, String password, List<String> roles)
/*     */   {
/*  52 */     this(name, password, roles, null);
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
/*     */ 
/*     */ 
/*     */   public GenericPrincipal(String name, String password, List<String> roles, Principal userPrincipal)
/*     */   {
/*  68 */     this(name, password, roles, userPrincipal, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericPrincipal(String name, String password, List<String> roles, Principal userPrincipal, LoginContext loginContext)
/*     */   {
/*  86 */     this(name, password, roles, userPrincipal, loginContext, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericPrincipal(String name, String password, List<String> roles, Principal userPrincipal, LoginContext loginContext, GSSCredential gssCredential)
/*     */   {
/* 107 */     this.name = name;
/* 108 */     this.password = password;
/* 109 */     this.userPrincipal = userPrincipal;
/* 110 */     if (roles == null) {
/* 111 */       this.roles = new String[0];
/*     */     } else {
/* 113 */       this.roles = ((String[])roles.toArray(new String[roles.size()]));
/* 114 */       if (this.roles.length > 1) {
/* 115 */         Arrays.sort(this.roles);
/*     */       }
/*     */     }
/* 118 */     this.loginContext = loginContext;
/* 119 */     this.gssCredential = gssCredential;
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
/*     */   public String getName()
/*     */   {
/* 132 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPassword()
/*     */   {
/* 143 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRoles()
/*     */   {
/* 153 */     return this.roles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 164 */     if (this.userPrincipal != null) {
/* 165 */       return this.userPrincipal;
/*     */     }
/* 167 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/* 182 */   protected transient GSSCredential gssCredential = null;
/*     */   
/*     */   public GSSCredential getGssCredential()
/*     */   {
/* 186 */     return this.gssCredential;
/*     */   }
/*     */   
/* 189 */   protected void setGssCredential(GSSCredential gssCredential) { this.gssCredential = gssCredential; }
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
/*     */   public boolean hasRole(String role)
/*     */   {
/* 204 */     if ("*".equals(role)) {
/* 205 */       return true;
/*     */     }
/* 207 */     if (role == null) {
/* 208 */       return false;
/*     */     }
/* 210 */     return Arrays.binarySearch(this.roles, role) >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 220 */     StringBuilder sb = new StringBuilder("GenericPrincipal[");
/* 221 */     sb.append(this.name);
/* 222 */     sb.append("(");
/* 223 */     for (int i = 0; i < this.roles.length; i++) {
/* 224 */       sb.append(this.roles[i]).append(",");
/*     */     }
/* 226 */     sb.append(")]");
/* 227 */     return sb.toString();
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
/*     */   public void logout()
/*     */     throws Exception
/*     */   {
/* 242 */     if (this.loginContext != null) {
/* 243 */       this.loginContext.logout();
/*     */     }
/* 245 */     if (this.gssCredential != null) {
/* 246 */       this.gssCredential.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 254 */     return new SerializablePrincipal(this.name, this.password, this.roles, this.userPrincipal);
/*     */   }
/*     */   
/*     */   private static class SerializablePrincipal implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final String name;
/*     */     private final String password;
/*     */     private final String[] roles;
/*     */     private final Principal principal;
/*     */     
/*     */     public SerializablePrincipal(String name, String password, String[] roles, Principal principal)
/*     */     {
/* 267 */       this.name = name;
/* 268 */       this.password = password;
/* 269 */       this.roles = roles;
/* 270 */       if ((principal instanceof Serializable)) {
/* 271 */         this.principal = principal;
/*     */       } else {
/* 273 */         this.principal = null;
/*     */       }
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 278 */       return new GenericPrincipal(this.name, this.password, Arrays.asList(this.roles), this.principal);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\GenericPrincipal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */