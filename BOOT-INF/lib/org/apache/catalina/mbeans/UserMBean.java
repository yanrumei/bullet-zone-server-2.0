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
/*     */ public class UserMBean
/*     */   extends BaseModelMBean
/*     */ {
/*  71 */   protected final Registry registry = MBeanUtils.createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   protected final ManagedBean managed = this.registry
/*  78 */     .findManagedBean("User");
/*     */   
/*     */ 
/*     */ 
/*     */   public UserMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */   public String[] getGroups()
/*     */   {
/*  89 */     User user = (User)this.resource;
/*  90 */     ArrayList<String> results = new ArrayList();
/*  91 */     Iterator<Group> groups = user.getGroups();
/*  92 */     while (groups.hasNext()) {
/*  93 */       Group group = null;
/*     */       try {
/*  95 */         group = (Group)groups.next();
/*     */         
/*  97 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), group);
/*  98 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/* 100 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for group " + group);
/*     */         
/* 102 */         iae.initCause(e);
/* 103 */         throw iae;
/*     */       }
/*     */     }
/* 106 */     return (String[])results.toArray(new String[results.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRoles()
/*     */   {
/* 116 */     User user = (User)this.resource;
/* 117 */     ArrayList<String> results = new ArrayList();
/* 118 */     Iterator<Role> roles = user.getRoles();
/* 119 */     while (roles.hasNext()) {
/* 120 */       Role role = null;
/*     */       try {
/* 122 */         role = (Role)roles.next();
/*     */         
/* 124 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), role);
/* 125 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/* 127 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for role " + role);
/*     */         
/* 129 */         iae.initCause(e);
/* 130 */         throw iae;
/*     */       }
/*     */     }
/* 133 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public void addGroup(String groupname)
/*     */   {
/* 148 */     User user = (User)this.resource;
/* 149 */     if (user == null) {
/* 150 */       return;
/*     */     }
/* 152 */     Group group = user.getUserDatabase().findGroup(groupname);
/* 153 */     if (group == null) {
/* 154 */       throw new IllegalArgumentException("Invalid group name '" + groupname + "'");
/*     */     }
/*     */     
/* 157 */     user.addGroup(group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRole(String rolename)
/*     */   {
/* 169 */     User user = (User)this.resource;
/* 170 */     if (user == null) {
/* 171 */       return;
/*     */     }
/* 173 */     Role role = user.getUserDatabase().findRole(rolename);
/* 174 */     if (role == null) {
/* 175 */       throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
/*     */     }
/*     */     
/* 178 */     user.addRole(role);
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
/* 190 */     User user = (User)this.resource;
/* 191 */     if (user == null) {
/* 192 */       return;
/*     */     }
/* 194 */     Group group = user.getUserDatabase().findGroup(groupname);
/* 195 */     if (group == null) {
/* 196 */       throw new IllegalArgumentException("Invalid group name '" + groupname + "'");
/*     */     }
/*     */     
/* 199 */     user.removeGroup(group);
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
/* 211 */     User user = (User)this.resource;
/* 212 */     if (user == null) {
/* 213 */       return;
/*     */     }
/* 215 */     Role role = user.getUserDatabase().findRole(rolename);
/* 216 */     if (role == null) {
/* 217 */       throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
/*     */     }
/*     */     
/* 220 */     user.removeRole(role);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\UserMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */