/*     */ package org.apache.catalina.users;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.file.ConfigFileLoader;
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
/*     */ public class MemoryUserDatabase
/*     */   implements UserDatabase
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(MemoryUserDatabase.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryUserDatabase()
/*     */   {
/*  61 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryUserDatabase(String id)
/*     */   {
/*  71 */     this.id = id;
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
/*  82 */   protected final HashMap<String, Group> groups = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String id;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   protected String pathname = "conf/tomcat-users.xml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   protected String pathnameOld = this.pathname + ".old";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected String pathnameNew = this.pathname + ".new";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   protected boolean readonly = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   protected final HashMap<String, Role> roles = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.users");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */   protected final HashMap<String, User> users = new HashMap();
/*     */   
/*     */   /* Error */
/*     */   public Iterator<Group> getGroups()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/apache/catalina/users/MemoryUserDatabase:groups	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/apache/catalina/users/MemoryUserDatabase:groups	Ljava/util/HashMap;
/*     */     //   11: invokevirtual 20	java/util/HashMap:values	()Ljava/util/Collection;
/*     */     //   14: invokeinterface 21 1 0
/*     */     //   19: aload_1
/*     */     //   20: monitorexit
/*     */     //   21: areturn
/*     */     //   22: astore_2
/*     */     //   23: aload_1
/*     */     //   24: monitorexit
/*     */     //   25: aload_2
/*     */     //   26: athrow
/*     */     // Line number table:
/*     */     //   Java source line #147	-> byte code offset #0
/*     */     //   Java source line #148	-> byte code offset #7
/*     */     //   Java source line #149	-> byte code offset #22
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	27	0	this	MemoryUserDatabase
/*     */     //   5	19	1	Ljava/lang/Object;	Object
/*     */     //   22	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	21	22	finally
/*     */     //   22	25	22	finally
/*     */   }
/*     */   
/*     */   public String getId()
/*     */   {
/* 160 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathname()
/*     */   {
/* 170 */     return this.pathname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathname(String pathname)
/*     */   {
/* 182 */     this.pathname = pathname;
/* 183 */     this.pathnameOld = (pathname + ".old");
/* 184 */     this.pathnameNew = (pathname + ".new");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getReadonly()
/*     */   {
/* 194 */     return this.readonly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadonly(boolean readonly)
/*     */   {
/* 206 */     this.readonly = readonly;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Iterator<Role> getRoles()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 17	org/apache/catalina/users/MemoryUserDatabase:roles	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 17	org/apache/catalina/users/MemoryUserDatabase:roles	Ljava/util/HashMap;
/*     */     //   11: invokevirtual 20	java/util/HashMap:values	()Ljava/util/Collection;
/*     */     //   14: invokeinterface 21 1 0
/*     */     //   19: aload_1
/*     */     //   20: monitorexit
/*     */     //   21: areturn
/*     */     //   22: astore_2
/*     */     //   23: aload_1
/*     */     //   24: monitorexit
/*     */     //   25: aload_2
/*     */     //   26: athrow
/*     */     // Line number table:
/*     */     //   Java source line #217	-> byte code offset #0
/*     */     //   Java source line #218	-> byte code offset #7
/*     */     //   Java source line #219	-> byte code offset #22
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	27	0	this	MemoryUserDatabase
/*     */     //   5	19	1	Ljava/lang/Object;	Object
/*     */     //   22	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	21	22	finally
/*     */     //   22	25	22	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Iterator<User> getUsers()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 18	org/apache/catalina/users/MemoryUserDatabase:users	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 18	org/apache/catalina/users/MemoryUserDatabase:users	Ljava/util/HashMap;
/*     */     //   11: invokevirtual 20	java/util/HashMap:values	()Ljava/util/Collection;
/*     */     //   14: invokeinterface 21 1 0
/*     */     //   19: aload_1
/*     */     //   20: monitorexit
/*     */     //   21: areturn
/*     */     //   22: astore_2
/*     */     //   23: aload_1
/*     */     //   24: monitorexit
/*     */     //   25: aload_2
/*     */     //   26: athrow
/*     */     // Line number table:
/*     */     //   Java source line #230	-> byte code offset #0
/*     */     //   Java source line #231	-> byte code offset #7
/*     */     //   Java source line #232	-> byte code offset #22
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	27	0	this	MemoryUserDatabase
/*     */     //   5	19	1	Ljava/lang/Object;	Object
/*     */     //   22	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	21	22	finally
/*     */     //   22	25	22	finally
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws Exception
/*     */   {
/* 249 */     save();
/*     */     
/* 251 */     synchronized (this.groups) {
/* 252 */       synchronized (this.users) {
/* 253 */         this.users.clear();
/* 254 */         this.groups.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Group createGroup(String groupname, String description)
/*     */   {
/* 270 */     if ((groupname == null) || (groupname.length() == 0)) {
/* 271 */       String msg = sm.getString("memoryUserDatabase.nullGroup");
/* 272 */       log.warn(msg);
/* 273 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 276 */     MemoryGroup group = new MemoryGroup(this, groupname, description);
/* 277 */     synchronized (this.groups) {
/* 278 */       this.groups.put(group.getGroupname(), group);
/*     */     }
/* 280 */     return group;
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
/*     */   public Role createRole(String rolename, String description)
/*     */   {
/* 294 */     if ((rolename == null) || (rolename.length() == 0)) {
/* 295 */       String msg = sm.getString("memoryUserDatabase.nullRole");
/* 296 */       log.warn(msg);
/* 297 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 300 */     MemoryRole role = new MemoryRole(this, rolename, description);
/* 301 */     synchronized (this.roles) {
/* 302 */       this.roles.put(role.getRolename(), role);
/*     */     }
/* 304 */     return role;
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
/*     */   public User createUser(String username, String password, String fullName)
/*     */   {
/* 320 */     if ((username == null) || (username.length() == 0)) {
/* 321 */       String msg = sm.getString("memoryUserDatabase.nullUser");
/* 322 */       log.warn(msg);
/* 323 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 326 */     MemoryUser user = new MemoryUser(this, username, password, fullName);
/* 327 */     synchronized (this.users) {
/* 328 */       this.users.put(user.getUsername(), user);
/*     */     }
/* 330 */     return user;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Group findGroup(String groupname)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/apache/catalina/users/MemoryUserDatabase:groups	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/apache/catalina/users/MemoryUserDatabase:groups	Ljava/util/HashMap;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 44	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: checkcast 45	org/apache/catalina/Group
/*     */     //   18: aload_2
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore_3
/*     */     //   22: aload_2
/*     */     //   23: monitorexit
/*     */     //   24: aload_3
/*     */     //   25: athrow
/*     */     // Line number table:
/*     */     //   Java source line #343	-> byte code offset #0
/*     */     //   Java source line #344	-> byte code offset #7
/*     */     //   Java source line #345	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	26	0	this	MemoryUserDatabase
/*     */     //   0	26	1	groupname	String
/*     */     //   5	18	2	Ljava/lang/Object;	Object
/*     */     //   21	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	24	21	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Role findRole(String rolename)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 17	org/apache/catalina/users/MemoryUserDatabase:roles	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 17	org/apache/catalina/users/MemoryUserDatabase:roles	Ljava/util/HashMap;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 44	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: checkcast 46	org/apache/catalina/Role
/*     */     //   18: aload_2
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore_3
/*     */     //   22: aload_2
/*     */     //   23: monitorexit
/*     */     //   24: aload_3
/*     */     //   25: athrow
/*     */     // Line number table:
/*     */     //   Java source line #359	-> byte code offset #0
/*     */     //   Java source line #360	-> byte code offset #7
/*     */     //   Java source line #361	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	26	0	this	MemoryUserDatabase
/*     */     //   0	26	1	rolename	String
/*     */     //   5	18	2	Ljava/lang/Object;	Object
/*     */     //   21	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	24	21	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public User findUser(String username)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 18	org/apache/catalina/users/MemoryUserDatabase:users	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 18	org/apache/catalina/users/MemoryUserDatabase:users	Ljava/util/HashMap;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 44	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: checkcast 47	org/apache/catalina/User
/*     */     //   18: aload_2
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore_3
/*     */     //   22: aload_2
/*     */     //   23: monitorexit
/*     */     //   24: aload_3
/*     */     //   25: athrow
/*     */     // Line number table:
/*     */     //   Java source line #375	-> byte code offset #0
/*     */     //   Java source line #376	-> byte code offset #7
/*     */     //   Java source line #377	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	26	0	this	MemoryUserDatabase
/*     */     //   0	26	1	username	String
/*     */     //   5	18	2	Ljava/lang/Object;	Object
/*     */     //   21	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	24	21	finally
/*     */   }
/*     */   
/*     */   public void open()
/*     */     throws Exception
/*     */   {
/* 390 */     synchronized (this.groups) {
/* 391 */       synchronized (this.users)
/*     */       {
/*     */ 
/* 394 */         this.users.clear();
/* 395 */         this.groups.clear();
/* 396 */         this.roles.clear();
/*     */         
/* 398 */         String pathName = getPathname();
/* 399 */         try { InputStream is = ConfigFileLoader.getInputStream(getPathname());Throwable localThrowable3 = null;
/*     */           try {
/* 401 */             Digester digester = new Digester();
/*     */             try {
/* 403 */               digester.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
/*     */             }
/*     */             catch (Exception e) {
/* 406 */               log.warn(sm.getString("memoryUserDatabase.xmlFeatureEncoding"), e);
/*     */             }
/* 408 */             digester.addFactoryCreate("tomcat-users/group", new MemoryGroupCreationFactory(this), true);
/*     */             
/* 410 */             digester.addFactoryCreate("tomcat-users/role", new MemoryRoleCreationFactory(this), true);
/*     */             
/* 412 */             digester.addFactoryCreate("tomcat-users/user", new MemoryUserCreationFactory(this), true);
/*     */             
/*     */ 
/*     */ 
/* 416 */             digester.parse(is);
/*     */           }
/*     */           catch (Throwable localThrowable1)
/*     */           {
/* 399 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           finally
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 417 */             if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 418 */           } } catch (IOException ioe) { log.error(sm.getString("memoryUserDatabase.fileNotFound", new Object[] { pathName }));
/* 419 */           return;
/*     */         }
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
/*     */ 
/*     */   public void removeGroup(Group group)
/*     */   {
/* 434 */     synchronized (this.groups) {
/* 435 */       Iterator<User> users = getUsers();
/* 436 */       while (users.hasNext()) {
/* 437 */         User user = (User)users.next();
/* 438 */         user.removeGroup(group);
/*     */       }
/* 440 */       this.groups.remove(group.getGroupname());
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
/*     */   public void removeRole(Role role)
/*     */   {
/* 453 */     synchronized (this.roles) {
/* 454 */       Iterator<Group> groups = getGroups();
/* 455 */       while (groups.hasNext()) {
/* 456 */         Group group = (Group)groups.next();
/* 457 */         group.removeRole(role);
/*     */       }
/* 459 */       Iterator<User> users = getUsers();
/* 460 */       while (users.hasNext()) {
/* 461 */         User user = (User)users.next();
/* 462 */         user.removeRole(role);
/*     */       }
/* 464 */       this.roles.remove(role.getRolename());
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
/*     */   public void removeUser(User user)
/*     */   {
/* 478 */     synchronized (this.users) {
/* 479 */       this.users.remove(user.getUsername());
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
/*     */   public boolean isWriteable()
/*     */   {
/* 492 */     File file = new File(this.pathname);
/* 493 */     if (!file.isAbsolute()) {
/* 494 */       file = new File(System.getProperty("catalina.base"), this.pathname);
/*     */     }
/*     */     
/* 497 */     File dir = file.getParentFile();
/* 498 */     return (dir.exists()) && (dir.isDirectory()) && (dir.canWrite());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save()
/*     */     throws Exception
/*     */   {
/* 511 */     if (getReadonly()) {
/* 512 */       log.error(sm.getString("memoryUserDatabase.readOnly"));
/* 513 */       return;
/*     */     }
/*     */     
/* 516 */     if (!isWriteable()) {
/* 517 */       log.warn(sm.getString("memoryUserDatabase.notPersistable"));
/* 518 */       return;
/*     */     }
/*     */     
/*     */ 
/* 522 */     File fileNew = new File(this.pathnameNew);
/* 523 */     if (!fileNew.isAbsolute())
/*     */     {
/* 525 */       fileNew = new File(System.getProperty("catalina.base"), this.pathnameNew);
/*     */     }
/* 527 */     PrintWriter writer = null;
/*     */     
/*     */     try
/*     */     {
/* 531 */       FileOutputStream fos = new FileOutputStream(fileNew);
/* 532 */       OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
/* 533 */       writer = new PrintWriter(osw);
/*     */       
/*     */ 
/* 536 */       writer.println("<?xml version='1.0' encoding='utf-8'?>");
/* 537 */       writer.println("<tomcat-users xmlns=\"http://tomcat.apache.org/xml\"");
/* 538 */       writer.println("              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
/* 539 */       writer.println("              xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\"");
/* 540 */       writer.println("              version=\"1.0\">");
/*     */       
/*     */ 
/* 543 */       Iterator<?> values = null;
/* 544 */       values = getRoles();
/* 545 */       while (values.hasNext()) {
/* 546 */         writer.print("  ");
/* 547 */         writer.println(values.next());
/*     */       }
/* 549 */       values = getGroups();
/* 550 */       while (values.hasNext()) {
/* 551 */         writer.print("  ");
/* 552 */         writer.println(values.next());
/*     */       }
/* 554 */       values = getUsers();
/* 555 */       while (values.hasNext()) {
/* 556 */         writer.print("  ");
/* 557 */         writer.println(((MemoryUser)values.next()).toXml());
/*     */       }
/*     */       
/*     */ 
/* 561 */       writer.println("</tomcat-users>");
/*     */       
/*     */ 
/* 564 */       if (writer.checkError()) {
/* 565 */         writer.close();
/* 566 */         fileNew.delete();
/*     */         
/* 568 */         throw new IOException(sm.getString("memoryUserDatabase.writeException", new Object[] {fileNew
/* 569 */           .getAbsolutePath() }));
/*     */       }
/* 571 */       writer.close();
/*     */     } catch (IOException e) {
/* 573 */       if (writer != null) {
/* 574 */         writer.close();
/*     */       }
/* 576 */       fileNew.delete();
/* 577 */       throw e;
/*     */     }
/*     */     
/*     */ 
/* 581 */     File fileOld = new File(this.pathnameOld);
/* 582 */     if (!fileOld.isAbsolute())
/*     */     {
/* 584 */       fileOld = new File(System.getProperty("catalina.base"), this.pathnameOld);
/*     */     }
/* 586 */     fileOld.delete();
/* 587 */     File fileOrig = new File(this.pathname);
/* 588 */     if (!fileOrig.isAbsolute())
/*     */     {
/* 590 */       fileOrig = new File(System.getProperty("catalina.base"), this.pathname);
/*     */     }
/* 592 */     if (fileOrig.exists()) {
/* 593 */       fileOld.delete();
/* 594 */       if (!fileOrig.renameTo(fileOld))
/*     */       {
/* 596 */         throw new IOException(sm.getString("memoryUserDatabase.renameOld", new Object[] {fileOld
/* 597 */           .getAbsolutePath() }));
/*     */       }
/*     */     }
/* 600 */     if (!fileNew.renameTo(fileOrig)) {
/* 601 */       if (fileOld.exists()) {
/* 602 */         fileOld.renameTo(fileOrig);
/*     */       }
/*     */       
/* 605 */       throw new IOException(sm.getString("memoryUserDatabase.renameNew", new Object[] {fileOrig
/* 606 */         .getAbsolutePath() }));
/*     */     }
/* 608 */     fileOld.delete();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 619 */     StringBuilder sb = new StringBuilder("MemoryUserDatabase[id=");
/* 620 */     sb.append(this.id);
/* 621 */     sb.append(",pathname=");
/* 622 */     sb.append(this.pathname);
/* 623 */     sb.append(",groupCount=");
/* 624 */     sb.append(this.groups.size());
/* 625 */     sb.append(",roleCount=");
/* 626 */     sb.append(this.roles.size());
/* 627 */     sb.append(",userCount=");
/* 628 */     sb.append(this.users.size());
/* 629 */     sb.append("]");
/* 630 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\users\MemoryUserDatabase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */