/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.catalina.util.ContextName;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
/*     */ import org.apache.tomcat.util.descriptor.web.NamingResources;
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
/*     */ public class MBeanUtils
/*     */ {
/*  58 */   private static final String[][] exceptions = { { "org.apache.catalina.users.MemoryGroup", "Group" }, { "org.apache.catalina.users.MemoryRole", "Role" }, { "org.apache.catalina.users.MemoryUser", "User" } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static Registry registry = createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private static MBeanServer mserver = createServer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String createManagedName(Object component)
/*     */   {
/*  91 */     String className = component.getClass().getName();
/*  92 */     for (int i = 0; i < exceptions.length; i++) {
/*  93 */       if (className.equals(exceptions[i][0])) {
/*  94 */         return exceptions[i][1];
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     int period = className.lastIndexOf('.');
/* 100 */     if (period >= 0)
/* 101 */       className = className.substring(period + 1);
/* 102 */     return className;
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
/*     */   public static DynamicMBean createMBean(ContextEnvironment environment)
/*     */     throws Exception
/*     */   {
/* 118 */     String mname = createManagedName(environment);
/* 119 */     ManagedBean managed = registry.findManagedBean(mname);
/* 120 */     if (managed == null) {
/* 121 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 122 */       throw new MBeanException(e);
/*     */     }
/* 124 */     String domain = managed.getDomain();
/* 125 */     if (domain == null)
/* 126 */       domain = mserver.getDefaultDomain();
/* 127 */     DynamicMBean mbean = managed.createMBean(environment);
/* 128 */     ObjectName oname = createObjectName(domain, environment);
/* 129 */     if (mserver.isRegistered(oname)) {
/* 130 */       mserver.unregisterMBean(oname);
/*     */     }
/* 132 */     mserver.registerMBean(mbean, oname);
/* 133 */     return mbean;
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
/*     */   public static DynamicMBean createMBean(ContextResource resource)
/*     */     throws Exception
/*     */   {
/* 149 */     String mname = createManagedName(resource);
/* 150 */     ManagedBean managed = registry.findManagedBean(mname);
/* 151 */     if (managed == null) {
/* 152 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 153 */       throw new MBeanException(e);
/*     */     }
/* 155 */     String domain = managed.getDomain();
/* 156 */     if (domain == null)
/* 157 */       domain = mserver.getDefaultDomain();
/* 158 */     DynamicMBean mbean = managed.createMBean(resource);
/* 159 */     ObjectName oname = createObjectName(domain, resource);
/* 160 */     if (mserver.isRegistered(oname)) {
/* 161 */       mserver.unregisterMBean(oname);
/*     */     }
/* 163 */     mserver.registerMBean(mbean, oname);
/* 164 */     return mbean;
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
/*     */   public static DynamicMBean createMBean(ContextResourceLink resourceLink)
/*     */     throws Exception
/*     */   {
/* 180 */     String mname = createManagedName(resourceLink);
/* 181 */     ManagedBean managed = registry.findManagedBean(mname);
/* 182 */     if (managed == null) {
/* 183 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 184 */       throw new MBeanException(e);
/*     */     }
/* 186 */     String domain = managed.getDomain();
/* 187 */     if (domain == null)
/* 188 */       domain = mserver.getDefaultDomain();
/* 189 */     DynamicMBean mbean = managed.createMBean(resourceLink);
/* 190 */     ObjectName oname = createObjectName(domain, resourceLink);
/* 191 */     if (mserver.isRegistered(oname)) {
/* 192 */       mserver.unregisterMBean(oname);
/*     */     }
/* 194 */     mserver.registerMBean(mbean, oname);
/* 195 */     return mbean;
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
/*     */   static DynamicMBean createMBean(Group group)
/*     */     throws Exception
/*     */   {
/* 211 */     String mname = createManagedName(group);
/* 212 */     ManagedBean managed = registry.findManagedBean(mname);
/* 213 */     if (managed == null) {
/* 214 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 215 */       throw new MBeanException(e);
/*     */     }
/* 217 */     String domain = managed.getDomain();
/* 218 */     if (domain == null)
/* 219 */       domain = mserver.getDefaultDomain();
/* 220 */     DynamicMBean mbean = managed.createMBean(group);
/* 221 */     ObjectName oname = createObjectName(domain, group);
/* 222 */     if (mserver.isRegistered(oname)) {
/* 223 */       mserver.unregisterMBean(oname);
/*     */     }
/* 225 */     mserver.registerMBean(mbean, oname);
/* 226 */     return mbean;
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
/*     */   static DynamicMBean createMBean(Role role)
/*     */     throws Exception
/*     */   {
/* 242 */     String mname = createManagedName(role);
/* 243 */     ManagedBean managed = registry.findManagedBean(mname);
/* 244 */     if (managed == null) {
/* 245 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 246 */       throw new MBeanException(e);
/*     */     }
/* 248 */     String domain = managed.getDomain();
/* 249 */     if (domain == null)
/* 250 */       domain = mserver.getDefaultDomain();
/* 251 */     DynamicMBean mbean = managed.createMBean(role);
/* 252 */     ObjectName oname = createObjectName(domain, role);
/* 253 */     if (mserver.isRegistered(oname)) {
/* 254 */       mserver.unregisterMBean(oname);
/*     */     }
/* 256 */     mserver.registerMBean(mbean, oname);
/* 257 */     return mbean;
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
/*     */   static DynamicMBean createMBean(User user)
/*     */     throws Exception
/*     */   {
/* 273 */     String mname = createManagedName(user);
/* 274 */     ManagedBean managed = registry.findManagedBean(mname);
/* 275 */     if (managed == null) {
/* 276 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 277 */       throw new MBeanException(e);
/*     */     }
/* 279 */     String domain = managed.getDomain();
/* 280 */     if (domain == null)
/* 281 */       domain = mserver.getDefaultDomain();
/* 282 */     DynamicMBean mbean = managed.createMBean(user);
/* 283 */     ObjectName oname = createObjectName(domain, user);
/* 284 */     if (mserver.isRegistered(oname)) {
/* 285 */       mserver.unregisterMBean(oname);
/*     */     }
/* 287 */     mserver.registerMBean(mbean, oname);
/* 288 */     return mbean;
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
/*     */   static DynamicMBean createMBean(UserDatabase userDatabase)
/*     */     throws Exception
/*     */   {
/* 304 */     String mname = createManagedName(userDatabase);
/* 305 */     ManagedBean managed = registry.findManagedBean(mname);
/* 306 */     if (managed == null) {
/* 307 */       Exception e = new Exception("ManagedBean is not found with " + mname);
/* 308 */       throw new MBeanException(e);
/*     */     }
/* 310 */     String domain = managed.getDomain();
/* 311 */     if (domain == null)
/* 312 */       domain = mserver.getDefaultDomain();
/* 313 */     DynamicMBean mbean = managed.createMBean(userDatabase);
/* 314 */     ObjectName oname = createObjectName(domain, userDatabase);
/* 315 */     if (mserver.isRegistered(oname)) {
/* 316 */       mserver.unregisterMBean(oname);
/*     */     }
/* 318 */     mserver.registerMBean(mbean, oname);
/* 319 */     return mbean;
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
/*     */   public static ObjectName createObjectName(String domain, ContextEnvironment environment)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 337 */     ObjectName name = null;
/*     */     
/* 339 */     Object container = environment.getNamingResources().getContainer();
/* 340 */     if ((container instanceof Server))
/*     */     {
/* 342 */       name = new ObjectName(domain + ":type=Environment,resourcetype=Global,name=" + environment.getName());
/* 343 */     } else if ((container instanceof Context)) {
/* 344 */       Context context = (Context)container;
/* 345 */       ContextName cn = new ContextName(context.getName(), false);
/* 346 */       Container host = context.getParent();
/*     */       
/*     */ 
/*     */ 
/* 350 */       name = new ObjectName(domain + ":type=Environment,resourcetype=Context,host=" + host.getName() + ",context=" + cn.getDisplayName() + ",name=" + environment.getName());
/*     */     }
/* 352 */     return name;
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
/*     */   public static ObjectName createObjectName(String domain, ContextResource resource)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 370 */     ObjectName name = null;
/* 371 */     String quotedResourceName = ObjectName.quote(resource.getName());
/*     */     
/* 373 */     Object container = resource.getNamingResources().getContainer();
/* 374 */     if ((container instanceof Server))
/*     */     {
/* 376 */       name = new ObjectName(domain + ":type=Resource,resourcetype=Global,class=" + resource.getType() + ",name=" + quotedResourceName);
/*     */     }
/* 378 */     else if ((container instanceof Context)) {
/* 379 */       Context context = (Context)container;
/* 380 */       ContextName cn = new ContextName(context.getName(), false);
/* 381 */       Container host = context.getParent();
/*     */       
/*     */ 
/*     */ 
/* 385 */       name = new ObjectName(domain + ":type=Resource,resourcetype=Context,host=" + host.getName() + ",context=" + cn.getDisplayName() + ",class=" + resource.getType() + ",name=" + quotedResourceName);
/*     */     }
/*     */     
/*     */ 
/* 389 */     return name;
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
/*     */   public static ObjectName createObjectName(String domain, ContextResourceLink resourceLink)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 407 */     ObjectName name = null;
/*     */     
/* 409 */     String quotedResourceLinkName = ObjectName.quote(resourceLink.getName());
/*     */     
/* 411 */     Object container = resourceLink.getNamingResources().getContainer();
/* 412 */     if ((container instanceof Server)) {
/* 413 */       name = new ObjectName(domain + ":type=ResourceLink,resourcetype=Global,name=" + quotedResourceLinkName);
/*     */ 
/*     */     }
/* 416 */     else if ((container instanceof Context)) {
/* 417 */       Context context = (Context)container;
/* 418 */       ContextName cn = new ContextName(context.getName(), false);
/* 419 */       Container host = context.getParent();
/*     */       
/*     */ 
/* 422 */       name = new ObjectName(domain + ":type=ResourceLink,resourcetype=Context,host=" + host.getName() + ",context=" + cn.getDisplayName() + ",name=" + quotedResourceLinkName);
/*     */     }
/*     */     
/*     */ 
/* 426 */     return name;
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
/*     */   static ObjectName createObjectName(String domain, Group group)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 444 */     ObjectName name = null;
/*     */     
/*     */ 
/* 447 */     name = new ObjectName(domain + ":type=Group,groupname=" + ObjectName.quote(group.getGroupname()) + ",database=" + group.getUserDatabase().getId());
/* 448 */     return name;
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
/*     */   static ObjectName createObjectName(String domain, Loader loader)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 465 */     ObjectName name = null;
/* 466 */     Context context = loader.getContext();
/*     */     
/* 468 */     ContextName cn = new ContextName(context.getName(), false);
/* 469 */     Container host = context.getParent();
/*     */     
/* 471 */     name = new ObjectName(domain + ":type=Loader,host=" + host.getName() + ",context=" + cn.getDisplayName());
/*     */     
/* 473 */     return name;
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
/*     */   static ObjectName createObjectName(String domain, Role role)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 491 */     ObjectName name = new ObjectName(domain + ":type=Role,rolename=" + ObjectName.quote(role.getRolename()) + ",database=" + role.getUserDatabase().getId());
/* 492 */     return name;
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
/*     */   static ObjectName createObjectName(String domain, User user)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 510 */     ObjectName name = new ObjectName(domain + ":type=User,username=" + ObjectName.quote(user.getUsername()) + ",database=" + user.getUserDatabase().getId());
/* 511 */     return name;
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
/*     */   static ObjectName createObjectName(String domain, UserDatabase userDatabase)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 528 */     ObjectName name = null;
/*     */     
/* 530 */     name = new ObjectName(domain + ":type=UserDatabase,database=" + userDatabase.getId());
/* 531 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized Registry createRegistry()
/*     */   {
/* 542 */     if (registry == null) {
/* 543 */       registry = Registry.getRegistry(null, null);
/* 544 */       ClassLoader cl = MBeanUtils.class.getClassLoader();
/*     */       
/* 546 */       registry.loadDescriptors("org.apache.catalina.mbeans", cl);
/* 547 */       registry.loadDescriptors("org.apache.catalina.authenticator", cl);
/* 548 */       registry.loadDescriptors("org.apache.catalina.core", cl);
/* 549 */       registry.loadDescriptors("org.apache.catalina", cl);
/* 550 */       registry.loadDescriptors("org.apache.catalina.deploy", cl);
/* 551 */       registry.loadDescriptors("org.apache.catalina.loader", cl);
/* 552 */       registry.loadDescriptors("org.apache.catalina.realm", cl);
/* 553 */       registry.loadDescriptors("org.apache.catalina.session", cl);
/* 554 */       registry.loadDescriptors("org.apache.catalina.startup", cl);
/* 555 */       registry.loadDescriptors("org.apache.catalina.users", cl);
/* 556 */       registry.loadDescriptors("org.apache.catalina.ha", cl);
/* 557 */       registry.loadDescriptors("org.apache.catalina.connector", cl);
/* 558 */       registry.loadDescriptors("org.apache.catalina.valves", cl);
/* 559 */       registry.loadDescriptors("org.apache.catalina.storeconfig", cl);
/* 560 */       registry.loadDescriptors("org.apache.tomcat.util.descriptor.web", cl);
/*     */     }
/* 562 */     return registry;
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
/*     */   public static synchronized MBeanServer createServer()
/*     */   {
/* 575 */     if (mserver == null) {
/* 576 */       mserver = Registry.getRegistry(null, null).getMBeanServer();
/*     */     }
/* 578 */     return mserver;
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
/*     */   public static void destroyMBean(ContextEnvironment environment)
/*     */     throws Exception
/*     */   {
/* 594 */     String mname = createManagedName(environment);
/* 595 */     ManagedBean managed = registry.findManagedBean(mname);
/* 596 */     if (managed == null) {
/* 597 */       return;
/*     */     }
/* 599 */     String domain = managed.getDomain();
/* 600 */     if (domain == null)
/* 601 */       domain = mserver.getDefaultDomain();
/* 602 */     ObjectName oname = createObjectName(domain, environment);
/* 603 */     if (mserver.isRegistered(oname)) {
/* 604 */       mserver.unregisterMBean(oname);
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
/*     */   public static void destroyMBean(ContextResource resource)
/*     */     throws Exception
/*     */   {
/* 622 */     if ("org.apache.catalina.UserDatabase".equals(resource.getType())) {
/* 623 */       destroyMBeanUserDatabase(resource.getName());
/*     */     }
/*     */     
/* 626 */     String mname = createManagedName(resource);
/* 627 */     ManagedBean managed = registry.findManagedBean(mname);
/* 628 */     if (managed == null) {
/* 629 */       return;
/*     */     }
/* 631 */     String domain = managed.getDomain();
/* 632 */     if (domain == null)
/* 633 */       domain = mserver.getDefaultDomain();
/* 634 */     ObjectName oname = createObjectName(domain, resource);
/* 635 */     if (mserver.isRegistered(oname)) {
/* 636 */       mserver.unregisterMBean(oname);
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
/*     */   public static void destroyMBean(ContextResourceLink resourceLink)
/*     */     throws Exception
/*     */   {
/* 652 */     String mname = createManagedName(resourceLink);
/* 653 */     ManagedBean managed = registry.findManagedBean(mname);
/* 654 */     if (managed == null) {
/* 655 */       return;
/*     */     }
/* 657 */     String domain = managed.getDomain();
/* 658 */     if (domain == null)
/* 659 */       domain = mserver.getDefaultDomain();
/* 660 */     ObjectName oname = createObjectName(domain, resourceLink);
/* 661 */     if (mserver.isRegistered(oname)) {
/* 662 */       mserver.unregisterMBean(oname);
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
/*     */   static void destroyMBean(Group group)
/*     */     throws Exception
/*     */   {
/* 677 */     String mname = createManagedName(group);
/* 678 */     ManagedBean managed = registry.findManagedBean(mname);
/* 679 */     if (managed == null) {
/* 680 */       return;
/*     */     }
/* 682 */     String domain = managed.getDomain();
/* 683 */     if (domain == null)
/* 684 */       domain = mserver.getDefaultDomain();
/* 685 */     ObjectName oname = createObjectName(domain, group);
/* 686 */     if (mserver.isRegistered(oname)) {
/* 687 */       mserver.unregisterMBean(oname);
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
/*     */   static void destroyMBean(Role role)
/*     */     throws Exception
/*     */   {
/* 703 */     String mname = createManagedName(role);
/* 704 */     ManagedBean managed = registry.findManagedBean(mname);
/* 705 */     if (managed == null) {
/* 706 */       return;
/*     */     }
/* 708 */     String domain = managed.getDomain();
/* 709 */     if (domain == null)
/* 710 */       domain = mserver.getDefaultDomain();
/* 711 */     ObjectName oname = createObjectName(domain, role);
/* 712 */     if (mserver.isRegistered(oname)) {
/* 713 */       mserver.unregisterMBean(oname);
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
/*     */   static void destroyMBean(User user)
/*     */     throws Exception
/*     */   {
/* 729 */     String mname = createManagedName(user);
/* 730 */     ManagedBean managed = registry.findManagedBean(mname);
/* 731 */     if (managed == null) {
/* 732 */       return;
/*     */     }
/* 734 */     String domain = managed.getDomain();
/* 735 */     if (domain == null)
/* 736 */       domain = mserver.getDefaultDomain();
/* 737 */     ObjectName oname = createObjectName(domain, user);
/* 738 */     if (mserver.isRegistered(oname)) {
/* 739 */       mserver.unregisterMBean(oname);
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
/*     */   static void destroyMBeanUserDatabase(String userDatabase)
/*     */     throws Exception
/*     */   {
/* 755 */     ObjectName query = null;
/* 756 */     Set<ObjectName> results = null;
/*     */     
/*     */ 
/* 759 */     query = new ObjectName("Users:type=Group,database=" + userDatabase + ",*");
/*     */     
/* 761 */     results = mserver.queryNames(query, null);
/* 762 */     for (ObjectName result : results) {
/* 763 */       mserver.unregisterMBean(result);
/*     */     }
/*     */     
/*     */ 
/* 767 */     query = new ObjectName("Users:type=Role,database=" + userDatabase + ",*");
/*     */     
/* 769 */     results = mserver.queryNames(query, null);
/* 770 */     for (ObjectName result : results) {
/* 771 */       mserver.unregisterMBean(result);
/*     */     }
/*     */     
/*     */ 
/* 775 */     query = new ObjectName("Users:type=User,database=" + userDatabase + ",*");
/*     */     
/* 777 */     results = mserver.queryNames(query, null);
/* 778 */     for (ObjectName result : results) {
/* 779 */       mserver.unregisterMBean(result);
/*     */     }
/*     */     
/*     */ 
/* 783 */     ObjectName db = new ObjectName("Users:type=UserDatabase,database=" + userDatabase);
/*     */     
/* 785 */     if (mserver.isRegistered(db)) {
/* 786 */       mserver.unregisterMBean(db);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\MBeanUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */