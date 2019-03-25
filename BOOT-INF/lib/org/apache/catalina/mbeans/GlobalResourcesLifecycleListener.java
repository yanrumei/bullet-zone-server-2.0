/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class GlobalResourcesLifecycleListener
/*     */   implements LifecycleListener
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(GlobalResourcesLifecycleListener.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   protected Lifecycle component = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   protected static final Registry registry = MBeanUtils.createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*  80 */     if ("start".equals(event.getType())) {
/*  81 */       this.component = event.getLifecycle();
/*  82 */       createMBeans();
/*  83 */     } else if ("stop".equals(event.getType())) {
/*  84 */       destroyMBeans();
/*  85 */       this.component = null;
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
/*     */   protected void createMBeans()
/*     */   {
/* 100 */     Context context = null;
/*     */     try {
/* 102 */       context = (Context)new InitialContext().lookup("java:/");
/*     */     } catch (NamingException e) {
/* 104 */       log.error("No global naming context defined for server");
/* 105 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 110 */       createMBeans("", context);
/*     */     } catch (NamingException e) {
/* 112 */       log.error("Exception processing Global JNDI Resources", e);
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
/*     */ 
/*     */ 
/*     */   protected void createMBeans(String prefix, Context context)
/*     */     throws NamingException
/*     */   {
/* 130 */     if (log.isDebugEnabled()) {
/* 131 */       log.debug("Creating MBeans for Global JNDI Resources in Context '" + prefix + "'");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 136 */       NamingEnumeration<Binding> bindings = context.listBindings("");
/* 137 */       while (bindings.hasMore()) {
/* 138 */         Binding binding = (Binding)bindings.next();
/* 139 */         String name = prefix + binding.getName();
/* 140 */         Object value = context.lookup(binding.getName());
/* 141 */         if (log.isDebugEnabled()) {
/* 142 */           log.debug("Checking resource " + name);
/*     */         }
/* 144 */         if ((value instanceof Context)) {
/* 145 */           createMBeans(name + "/", (Context)value);
/* 146 */         } else if ((value instanceof UserDatabase)) {
/*     */           try {
/* 148 */             createMBeans(name, (UserDatabase)value);
/*     */           } catch (Exception e) {
/* 150 */             log.error("Exception creating UserDatabase MBeans for " + name, e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 156 */       log.error("RuntimeException " + ex);
/*     */     } catch (OperationNotSupportedException ex) {
/* 158 */       log.error("Operation not supported " + ex);
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
/*     */ 
/*     */ 
/*     */   protected void createMBeans(String name, UserDatabase database)
/*     */     throws Exception
/*     */   {
/* 176 */     if (log.isDebugEnabled()) {
/* 177 */       log.debug("Creating UserDatabase MBeans for resource " + name);
/* 178 */       log.debug("Database=" + database);
/*     */     }
/*     */     try {
/* 181 */       MBeanUtils.createMBean(database);
/*     */     } catch (Exception e) {
/* 183 */       throw new IllegalArgumentException("Cannot create UserDatabase MBean for resource " + name, e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 188 */     Iterator<Role> roles = database.getRoles();
/* 189 */     while (roles.hasNext()) {
/* 190 */       Role role = (Role)roles.next();
/* 191 */       if (log.isDebugEnabled()) {
/* 192 */         log.debug("  Creating Role MBean for role " + role);
/*     */       }
/*     */       try {
/* 195 */         MBeanUtils.createMBean(role);
/*     */       } catch (Exception e) {
/* 197 */         throw new IllegalArgumentException("Cannot create Role MBean for role " + role, e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 203 */     Iterator<Group> groups = database.getGroups();
/* 204 */     while (groups.hasNext()) {
/* 205 */       Group group = (Group)groups.next();
/* 206 */       if (log.isDebugEnabled()) {
/* 207 */         log.debug("  Creating Group MBean for group " + group);
/*     */       }
/*     */       try {
/* 210 */         MBeanUtils.createMBean(group);
/*     */       } catch (Exception e) {
/* 212 */         throw new IllegalArgumentException("Cannot create Group MBean for group " + group, e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 218 */     Iterator<User> users = database.getUsers();
/* 219 */     while (users.hasNext()) {
/* 220 */       User user = (User)users.next();
/* 221 */       if (log.isDebugEnabled()) {
/* 222 */         log.debug("  Creating User MBean for user " + user);
/*     */       }
/*     */       try {
/* 225 */         MBeanUtils.createMBean(user);
/*     */       } catch (Exception e) {
/* 227 */         throw new IllegalArgumentException("Cannot create User MBean for user " + user, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyMBeans()
/*     */   {
/* 240 */     if (log.isDebugEnabled()) {
/* 241 */       log.debug("Destroying MBeans for Global JNDI Resources");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\GlobalResourcesLifecycleListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */