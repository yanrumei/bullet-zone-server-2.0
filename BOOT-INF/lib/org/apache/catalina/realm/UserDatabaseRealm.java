/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.naming.Context;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class UserDatabaseRealm
/*     */   extends RealmBase
/*     */ {
/*  59 */   protected UserDatabase database = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "UserDatabaseRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   protected String resourceName = "UserDatabase";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getResourceName()
/*     */   {
/*  85 */     return this.resourceName;
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
/*     */   public void setResourceName(String resourceName)
/*     */   {
/*  98 */     this.resourceName = resourceName;
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
/*     */   public boolean hasRole(Wrapper wrapper, Principal principal, String role)
/*     */   {
/* 119 */     if (wrapper != null) {
/* 120 */       String realRole = wrapper.findSecurityReference(role);
/* 121 */       if (realRole != null)
/* 122 */         role = realRole;
/*     */     }
/* 124 */     if ((principal instanceof GenericPrincipal)) {
/* 125 */       GenericPrincipal gp = (GenericPrincipal)principal;
/* 126 */       if ((gp.getUserPrincipal() instanceof User)) {
/* 127 */         principal = gp.getUserPrincipal();
/*     */       }
/*     */     }
/* 130 */     if (!(principal instanceof User))
/*     */     {
/* 132 */       return super.hasRole(null, principal, role);
/*     */     }
/* 134 */     if ("*".equals(role))
/* 135 */       return true;
/* 136 */     if (role == null) {
/* 137 */       return false;
/*     */     }
/* 139 */     User user = (User)principal;
/* 140 */     Role dbrole = this.database.findRole(role);
/* 141 */     if (dbrole == null) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (user.isInRole(dbrole)) {
/* 145 */       return true;
/*     */     }
/* 147 */     Iterator<Group> groups = user.getGroups();
/* 148 */     while (groups.hasNext()) {
/* 149 */       Group group = (Group)groups.next();
/* 150 */       if (group.isInRole(dbrole)) {
/* 151 */         return true;
/*     */       }
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 163 */     return "UserDatabaseRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPassword(String username)
/*     */   {
/* 173 */     User user = this.database.findUser(username);
/*     */     
/* 175 */     if (user == null) {
/* 176 */       return null;
/*     */     }
/*     */     
/* 179 */     return user.getPassword();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Principal getPrincipal(String username)
/*     */   {
/* 190 */     User user = this.database.findUser(username);
/* 191 */     if (user == null) {
/* 192 */       return null;
/*     */     }
/*     */     
/* 195 */     List<String> roles = new ArrayList();
/* 196 */     Iterator<Role> uroles = user.getRoles();
/* 197 */     while (uroles.hasNext()) {
/* 198 */       Role role = (Role)uroles.next();
/* 199 */       roles.add(role.getName());
/*     */     }
/* 201 */     Iterator<Group> groups = user.getGroups();
/* 202 */     while (groups.hasNext()) {
/* 203 */       Group group = (Group)groups.next();
/* 204 */       uroles = group.getRoles();
/* 205 */       while (uroles.hasNext()) {
/* 206 */         Role role = (Role)uroles.next();
/* 207 */         roles.add(role.getName());
/*     */       }
/*     */     }
/* 210 */     return new GenericPrincipal(username, user.getPassword(), roles, user);
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     try
/*     */     {
/* 229 */       Context context = getServer().getGlobalNamingContext();
/* 230 */       this.database = ((UserDatabase)context.lookup(this.resourceName));
/*     */     } catch (Throwable e) {
/* 232 */       ExceptionUtils.handleThrowable(e);
/* 233 */       this.containerLog.error(sm.getString("userDatabaseRealm.lookup", new Object[] { this.resourceName }), e);
/*     */       
/*     */ 
/* 236 */       this.database = null;
/*     */     }
/* 238 */     if (this.database == null)
/*     */     {
/* 240 */       throw new LifecycleException(sm.getString("userDatabaseRealm.noDatabase", new Object[] { this.resourceName }));
/*     */     }
/*     */     
/* 243 */     super.startInternal();
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
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 259 */     super.stopInternal();
/*     */     
/*     */ 
/* 262 */     this.database = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\UserDatabaseRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */