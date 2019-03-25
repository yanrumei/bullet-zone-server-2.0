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
/*     */ public class GroupMBean
/*     */   extends BaseModelMBean
/*     */ {
/*  71 */   protected final Registry registry = MBeanUtils.createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   protected final ManagedBean managed = this.registry.findManagedBean("Group");
/*     */   
/*     */ 
/*     */ 
/*     */   public GroupMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */   public String[] getRoles()
/*     */   {
/*  88 */     Group group = (Group)this.resource;
/*  89 */     ArrayList<String> results = new ArrayList();
/*  90 */     Iterator<Role> roles = group.getRoles();
/*  91 */     while (roles.hasNext()) {
/*  92 */       Role role = null;
/*     */       try {
/*  94 */         role = (Role)roles.next();
/*     */         
/*  96 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), role);
/*  97 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/*  99 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for role " + role);
/*     */         
/* 101 */         iae.initCause(e);
/* 102 */         throw iae;
/*     */       }
/*     */     }
/* 105 */     return (String[])results.toArray(new String[results.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getUsers()
/*     */   {
/* 115 */     Group group = (Group)this.resource;
/* 116 */     ArrayList<String> results = new ArrayList();
/* 117 */     Iterator<User> users = group.getUsers();
/* 118 */     while (users.hasNext()) {
/* 119 */       User user = null;
/*     */       try {
/* 121 */         user = (User)users.next();
/*     */         
/* 123 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), user);
/* 124 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/* 126 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for user " + user);
/*     */         
/* 128 */         iae.initCause(e);
/* 129 */         throw iae;
/*     */       }
/*     */     }
/* 132 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public void addRole(String rolename)
/*     */   {
/* 147 */     Group group = (Group)this.resource;
/* 148 */     if (group == null) {
/* 149 */       return;
/*     */     }
/* 151 */     Role role = group.getUserDatabase().findRole(rolename);
/* 152 */     if (role == null) {
/* 153 */       throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
/*     */     }
/*     */     
/* 156 */     group.addRole(role);
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
/* 168 */     Group group = (Group)this.resource;
/* 169 */     if (group == null) {
/* 170 */       return;
/*     */     }
/* 172 */     Role role = group.getUserDatabase().findRole(rolename);
/* 173 */     if (role == null) {
/* 174 */       throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
/*     */     }
/*     */     
/* 177 */     group.removeRole(role);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\GroupMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */