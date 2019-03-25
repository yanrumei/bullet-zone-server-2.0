/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.tomcat.util.modeler.BaseModelMBean;
/*     */ import org.apache.tomcat.util.modeler.ManagedBean;
/*     */ import org.apache.tomcat.util.modeler.Registry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryUserDatabaseMBean
/*     */   extends BaseModelMBean
/*     */ {
/*  72 */   protected final Registry registry = MBeanUtils.createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected final ManagedBean managed = this.registry
/*  79 */     .findManagedBean("MemoryUserDatabase");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   protected final ManagedBean managedGroup = this.registry
/*  86 */     .findManagedBean("Group");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   protected final ManagedBean managedRole = this.registry
/*  93 */     .findManagedBean("Role");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected final ManagedBean managedUser = this.registry
/* 100 */     .findManagedBean("User");
/*     */   
/*     */ 
/*     */ 
/*     */   public MemoryUserDatabaseMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */   public String[] getGroups()
/*     */   {
/* 111 */     UserDatabase database = (UserDatabase)this.resource;
/* 112 */     ArrayList<String> results = new ArrayList();
/* 113 */     Iterator<Group> groups = database.getGroups();
/* 114 */     while (groups.hasNext()) {
/* 115 */       Group group = (Group)groups.next();
/* 116 */       results.add(findGroup(group.getGroupname()));
/*     */     }
/* 118 */     return (String[])results.toArray(new String[results.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRoles()
/*     */   {
/* 128 */     UserDatabase database = (UserDatabase)this.resource;
/* 129 */     ArrayList<String> results = new ArrayList();
/* 130 */     Iterator<Role> roles = database.getRoles();
/* 131 */     while (roles.hasNext()) {
/* 132 */       Role role = (Role)roles.next();
/* 133 */       results.add(findRole(role.getRolename()));
/*     */     }
/* 135 */     return (String[])results.toArray(new String[results.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getUsers()
/*     */   {
/* 145 */     UserDatabase database = (UserDatabase)this.resource;
/* 146 */     ArrayList<String> results = new ArrayList();
/* 147 */     Iterator<User> users = database.getUsers();
/* 148 */     while (users.hasNext()) {
/* 149 */       User user = (User)users.next();
/* 150 */       results.add(findUser(user.getUsername()));
/*     */     }
/* 152 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public String createGroup(String groupname, String description)
/*     */   {
/* 169 */     UserDatabase database = (UserDatabase)this.resource;
/* 170 */     Group group = database.createGroup(groupname, description);
/*     */     try {
/* 172 */       MBeanUtils.createMBean(group);
/*     */     } catch (Exception e) {
/* 174 */       IllegalArgumentException iae = new IllegalArgumentException("Exception creating group [" + groupname + "] MBean");
/*     */       
/* 176 */       iae.initCause(e);
/* 177 */       throw iae;
/*     */     }
/* 179 */     return findGroup(groupname);
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
/*     */   public String createRole(String rolename, String description)
/*     */   {
/* 193 */     UserDatabase database = (UserDatabase)this.resource;
/* 194 */     Role role = database.createRole(rolename, description);
/*     */     try {
/* 196 */       MBeanUtils.createMBean(role);
/*     */     } catch (Exception e) {
/* 198 */       IllegalArgumentException iae = new IllegalArgumentException("Exception creating role [" + rolename + "] MBean");
/*     */       
/* 200 */       iae.initCause(e);
/* 201 */       throw iae;
/*     */     }
/* 203 */     return findRole(rolename);
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
/*     */   public String createUser(String username, String password, String fullName)
/*     */   {
/* 219 */     UserDatabase database = (UserDatabase)this.resource;
/* 220 */     User user = database.createUser(username, password, fullName);
/*     */     try {
/* 222 */       MBeanUtils.createMBean(user);
/*     */     } catch (Exception e) {
/* 224 */       IllegalArgumentException iae = new IllegalArgumentException("Exception creating user [" + username + "] MBean");
/*     */       
/* 226 */       iae.initCause(e);
/* 227 */       throw iae;
/*     */     }
/* 229 */     return findUser(username);
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
/*     */   public String findGroup(String groupname)
/*     */   {
/* 243 */     UserDatabase database = (UserDatabase)this.resource;
/* 244 */     Group group = database.findGroup(groupname);
/* 245 */     if (group == null) {
/* 246 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 250 */       ObjectName oname = MBeanUtils.createObjectName(this.managedGroup.getDomain(), group);
/* 251 */       return oname.toString();
/*     */     } catch (MalformedObjectNameException e) {
/* 253 */       IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for group [" + groupname + "]");
/*     */       
/* 255 */       iae.initCause(e);
/* 256 */       throw iae;
/*     */     }
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
/*     */   public String findRole(String rolename)
/*     */   {
/* 271 */     UserDatabase database = (UserDatabase)this.resource;
/* 272 */     Role role = database.findRole(rolename);
/* 273 */     if (role == null) {
/* 274 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 278 */       ObjectName oname = MBeanUtils.createObjectName(this.managedRole.getDomain(), role);
/* 279 */       return oname.toString();
/*     */     } catch (MalformedObjectNameException e) {
/* 281 */       IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for role [" + rolename + "]");
/*     */       
/* 283 */       iae.initCause(e);
/* 284 */       throw iae;
/*     */     }
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
/*     */   public String findUser(String username)
/*     */   {
/* 299 */     UserDatabase database = (UserDatabase)this.resource;
/* 300 */     User user = database.findUser(username);
/* 301 */     if (user == null) {
/* 302 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 306 */       ObjectName oname = MBeanUtils.createObjectName(this.managedUser.getDomain(), user);
/* 307 */       return oname.toString();
/*     */     } catch (MalformedObjectNameException e) {
/* 309 */       IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for user [" + username + "]");
/*     */       
/* 311 */       iae.initCause(e);
/* 312 */       throw iae;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeGroup(String groupname)
/*     */   {
/* 325 */     UserDatabase database = (UserDatabase)this.resource;
/* 326 */     Group group = database.findGroup(groupname);
/* 327 */     if (group == null) {
/* 328 */       return;
/*     */     }
/*     */     try {
/* 331 */       MBeanUtils.destroyMBean(group);
/* 332 */       database.removeGroup(group);
/*     */     } catch (Exception e) {
/* 334 */       IllegalArgumentException iae = new IllegalArgumentException("Exception destroying group [" + groupname + "] MBean");
/*     */       
/* 336 */       iae.initCause(e);
/* 337 */       throw iae;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRole(String rolename)
/*     */   {
/* 350 */     UserDatabase database = (UserDatabase)this.resource;
/* 351 */     Role role = database.findRole(rolename);
/* 352 */     if (role == null) {
/* 353 */       return;
/*     */     }
/*     */     try {
/* 356 */       MBeanUtils.destroyMBean(role);
/* 357 */       database.removeRole(role);
/*     */     } catch (Exception e) {
/* 359 */       IllegalArgumentException iae = new IllegalArgumentException("Exception destroying role [" + rolename + "] MBean");
/*     */       
/* 361 */       iae.initCause(e);
/* 362 */       throw iae;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeUser(String username)
/*     */   {
/* 375 */     UserDatabase database = (UserDatabase)this.resource;
/* 376 */     User user = database.findUser(username);
/* 377 */     if (user == null) {
/* 378 */       return;
/*     */     }
/*     */     try {
/* 381 */       MBeanUtils.destroyMBean(user);
/* 382 */       database.removeUser(user);
/*     */     } catch (Exception e) {
/* 384 */       IllegalArgumentException iae = new IllegalArgumentException("Exception destroying user [" + username + "] MBean");
/*     */       
/* 386 */       iae.initCause(e);
/* 387 */       throw iae;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\MemoryUserDatabaseMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */