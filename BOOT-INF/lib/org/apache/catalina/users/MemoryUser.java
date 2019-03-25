/*     */ package org.apache.catalina.users;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.apache.catalina.Group;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.buf.StringUtils.Function;
/*     */ import org.apache.tomcat.util.security.Escape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryUser
/*     */   extends AbstractUser
/*     */ {
/*     */   protected final MemoryUserDatabase database;
/*     */   
/*     */   MemoryUser(MemoryUserDatabase database, String username, String password, String fullName)
/*     */   {
/*  58 */     this.database = database;
/*  59 */     setUsername(username);
/*  60 */     setPassword(password);
/*  61 */     setFullName(fullName);
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
/*  78 */   protected final ArrayList<Group> groups = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected final ArrayList<Role> roles = new ArrayList();
/*     */   
/*     */   /* Error */
/*     */   public java.util.Iterator<Group> getGroups()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/users/MemoryUser:groups	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/users/MemoryUser:groups	Ljava/util/ArrayList;
/*     */     //   11: invokevirtual 10	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #96	-> byte code offset #0
/*     */     //   Java source line #97	-> byte code offset #7
/*     */     //   Java source line #98	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	MemoryUser
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public java.util.Iterator<Role> getRoles()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/apache/catalina/users/MemoryUser:roles	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/apache/catalina/users/MemoryUser:roles	Ljava/util/ArrayList;
/*     */     //   11: invokevirtual 10	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #109	-> byte code offset #0
/*     */     //   Java source line #110	-> byte code offset #7
/*     */     //   Java source line #111	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	MemoryUser
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   public UserDatabase getUserDatabase()
/*     */   {
/* 122 */     return this.database;
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
/*     */   public void addGroup(Group group)
/*     */   {
/* 138 */     synchronized (this.groups) {
/* 139 */       if (!this.groups.contains(group)) {
/* 140 */         this.groups.add(group);
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
/*     */   public void addRole(Role role)
/*     */   {
/* 155 */     synchronized (this.roles) {
/* 156 */       if (!this.roles.contains(role)) {
/* 157 */         this.roles.add(role);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isInGroup(Group group)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/users/MemoryUser:groups	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/users/MemoryUser:groups	Ljava/util/ArrayList;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 11	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: ireturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #172	-> byte code offset #0
/*     */     //   Java source line #173	-> byte code offset #7
/*     */     //   Java source line #174	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	MemoryUser
/*     */     //   0	23	1	group	Group
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isInRole(Role role)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/apache/catalina/users/MemoryUser:roles	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/apache/catalina/users/MemoryUser:roles	Ljava/util/ArrayList;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 11	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: ireturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #189	-> byte code offset #0
/*     */     //   Java source line #190	-> byte code offset #7
/*     */     //   Java source line #191	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	MemoryUser
/*     */     //   0	23	1	role	Role
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   public void removeGroup(Group group)
/*     */   {
/* 204 */     synchronized (this.groups) {
/* 205 */       this.groups.remove(group);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeGroups()
/*     */   {
/* 217 */     synchronized (this.groups) {
/* 218 */       this.groups.clear();
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
/*     */   public void removeRole(Role role)
/*     */   {
/* 232 */     synchronized (this.roles) {
/* 233 */       this.roles.remove(role);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRoles()
/*     */   {
/* 245 */     synchronized (this.roles) {
/* 246 */       this.roles.clear();
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
/*     */   public String toXml()
/*     */   {
/* 263 */     StringBuilder sb = new StringBuilder("<user username=\"");
/* 264 */     sb.append(Escape.xml(this.username));
/* 265 */     sb.append("\" password=\"");
/* 266 */     sb.append(Escape.xml(this.password));
/* 267 */     sb.append("\"");
/* 268 */     if (this.fullName != null) {
/* 269 */       sb.append(" fullName=\"");
/* 270 */       sb.append(Escape.xml(this.fullName));
/* 271 */       sb.append("\"");
/*     */     }
/* 273 */     synchronized (this.groups) {
/* 274 */       if (this.groups.size() > 0) {
/* 275 */         sb.append(" groups=\"");
/* 276 */         StringUtils.join(this.groups, ',', new StringUtils.Function()
/*     */         {
/* 278 */           public String apply(Group t) { return Escape.xml(t.getGroupname()); } }, sb);
/*     */         
/*     */ 
/* 281 */         sb.append("\"");
/*     */       }
/*     */     }
/* 284 */     synchronized (this.roles) {
/* 285 */       if (this.roles.size() > 0) {
/* 286 */         sb.append(" roles=\"");
/* 287 */         StringUtils.join(this.roles, ',', new StringUtils.Function()
/*     */         {
/* 289 */           public String apply(Role t) { return Escape.xml(t.getRolename()); } }, sb);
/*     */         
/*     */ 
/* 292 */         sb.append("\"");
/*     */       }
/*     */     }
/* 295 */     sb.append("/>");
/* 296 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 306 */     StringBuilder sb = new StringBuilder("User username=\"");
/* 307 */     sb.append(Escape.xml(this.username));
/* 308 */     sb.append("\"");
/* 309 */     if (this.fullName != null) {
/* 310 */       sb.append(", fullName=\"");
/* 311 */       sb.append(Escape.xml(this.fullName));
/* 312 */       sb.append("\"");
/*     */     }
/* 314 */     synchronized (this.groups) {
/* 315 */       if (this.groups.size() > 0) {
/* 316 */         sb.append(", groups=\"");
/* 317 */         StringUtils.join(this.groups, ',', new StringUtils.Function()
/*     */         {
/* 319 */           public String apply(Group t) { return Escape.xml(t.getGroupname()); } }, sb);
/*     */         
/*     */ 
/* 322 */         sb.append("\"");
/*     */       }
/*     */     }
/* 325 */     synchronized (this.roles) {
/* 326 */       if (this.roles.size() > 0) {
/* 327 */         sb.append(", roles=\"");
/* 328 */         StringUtils.join(this.roles, ',', new StringUtils.Function()
/*     */         {
/* 330 */           public String apply(Role t) { return Escape.xml(t.getRolename()); } }, sb);
/*     */         
/*     */ 
/* 333 */         sb.append("\"");
/*     */       }
/*     */     }
/* 336 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\users\MemoryUser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */