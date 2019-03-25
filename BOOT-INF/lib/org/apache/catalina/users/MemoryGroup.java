/*     */ package org.apache.catalina.users;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.apache.catalina.Role;
/*     */ import org.apache.catalina.User;
/*     */ import org.apache.catalina.UserDatabase;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.buf.StringUtils.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryGroup
/*     */   extends AbstractGroup
/*     */ {
/*     */   protected final MemoryUserDatabase database;
/*     */   
/*     */   MemoryGroup(MemoryUserDatabase database, String groupname, String description)
/*     */   {
/*  57 */     this.database = database;
/*  58 */     setGroupname(groupname);
/*  59 */     setDescription(description);
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
/*  76 */   protected final ArrayList<Role> roles = new ArrayList();
/*     */   
/*     */   /* Error */
/*     */   public Iterator<Role> getRoles()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/users/MemoryGroup:roles	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/users/MemoryGroup:roles	Ljava/util/ArrayList;
/*     */     //   11: invokevirtual 8	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #88	-> byte code offset #0
/*     */     //   Java source line #89	-> byte code offset #7
/*     */     //   Java source line #90	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	MemoryGroup
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
/* 101 */     return this.database;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<User> getUsers()
/*     */   {
/* 112 */     ArrayList<User> results = new ArrayList();
/* 113 */     Iterator<User> users = this.database.getUsers();
/* 114 */     while (users.hasNext()) {
/* 115 */       User user = (User)users.next();
/* 116 */       if (user.isInGroup(this)) {
/* 117 */         results.add(user);
/*     */       }
/*     */     }
/* 120 */     return results.iterator();
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
/*     */   public void addRole(Role role)
/*     */   {
/* 136 */     synchronized (this.roles) {
/* 137 */       if (!this.roles.contains(role)) {
/* 138 */         this.roles.add(role);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isInRole(Role role)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/users/MemoryGroup:roles	Ljava/util/ArrayList;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/users/MemoryGroup:roles	Ljava/util/ArrayList;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 15	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: ireturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #153	-> byte code offset #0
/*     */     //   Java source line #154	-> byte code offset #7
/*     */     //   Java source line #155	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	MemoryGroup
/*     */     //   0	23	1	role	Role
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   public void removeRole(Role role)
/*     */   {
/* 168 */     synchronized (this.roles) {
/* 169 */       this.roles.remove(role);
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
/* 181 */     synchronized (this.roles) {
/* 182 */       this.roles.clear();
/*     */     }
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
/* 194 */     StringBuilder sb = new StringBuilder("<group groupname=\"");
/* 195 */     sb.append(this.groupname);
/* 196 */     sb.append("\"");
/* 197 */     if (this.description != null) {
/* 198 */       sb.append(" description=\"");
/* 199 */       sb.append(this.description);
/* 200 */       sb.append("\"");
/*     */     }
/* 202 */     synchronized (this.roles) {
/* 203 */       if (this.roles.size() > 0) {
/* 204 */         sb.append(" roles=\"");
/* 205 */         StringUtils.join(this.roles, ',', new StringUtils.Function() {
/* 206 */           public String apply(Role t) { return t.getRolename(); } }, sb);
/* 207 */         sb.append("\"");
/*     */       }
/*     */     }
/* 210 */     sb.append("/>");
/* 211 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\users\MemoryGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */