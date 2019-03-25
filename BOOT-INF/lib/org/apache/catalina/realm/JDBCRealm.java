/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.Principal;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Properties;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.catalina.LifecycleException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBCRealm
/*     */   extends RealmBase
/*     */ {
/*  61 */   protected String connectionName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   protected String connectionPassword = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   protected String connectionURL = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   protected Connection dbConnection = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   protected Driver driver = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected String driverName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "JDBCRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   protected PreparedStatement preparedCredentials = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   protected PreparedStatement preparedRoles = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   protected String roleNameCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   protected String userCredCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */   protected String userNameCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */   protected String userRoleTable = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   protected String userTable = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConnectionName()
/*     */   {
/* 151 */     return this.connectionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionName(String connectionName)
/*     */   {
/* 160 */     this.connectionName = connectionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionPassword()
/*     */   {
/* 167 */     return this.connectionPassword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionPassword(String connectionPassword)
/*     */   {
/* 176 */     this.connectionPassword = connectionPassword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionURL()
/*     */   {
/* 183 */     return this.connectionURL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionURL(String connectionURL)
/*     */   {
/* 192 */     this.connectionURL = connectionURL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDriverName()
/*     */   {
/* 199 */     return this.driverName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDriverName(String driverName)
/*     */   {
/* 208 */     this.driverName = driverName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRoleNameCol()
/*     */   {
/* 215 */     return this.roleNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoleNameCol(String roleNameCol)
/*     */   {
/* 224 */     this.roleNameCol = roleNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserCredCol()
/*     */   {
/* 231 */     return this.userCredCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserCredCol(String userCredCol)
/*     */   {
/* 240 */     this.userCredCol = userCredCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserNameCol()
/*     */   {
/* 247 */     return this.userNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserNameCol(String userNameCol)
/*     */   {
/* 256 */     this.userNameCol = userNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserRoleTable()
/*     */   {
/* 263 */     return this.userRoleTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserRoleTable(String userRoleTable)
/*     */   {
/* 272 */     this.userRoleTable = userRoleTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserTable()
/*     */   {
/* 279 */     return this.userTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserTable(String userTable)
/*     */   {
/* 288 */     this.userTable = userTable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Principal authenticate(String username, String credentials)
/*     */   {
/* 320 */     int numberOfTries = 2;
/* 321 */     while (numberOfTries > 0)
/*     */     {
/*     */       try
/*     */       {
/* 325 */         open();
/*     */         
/*     */ 
/* 328 */         return authenticate(this.dbConnection, username, credentials);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (SQLException e)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 338 */         this.containerLog.error(sm.getString("jdbcRealm.exception"), e);
/*     */         
/*     */ 
/* 341 */         if (this.dbConnection != null) {
/* 342 */           close(this.dbConnection);
/*     */         }
/*     */       }
/*     */       
/* 346 */       numberOfTries--;
/*     */     }
/*     */     
/*     */ 
/* 350 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Principal authenticate(Connection dbConnection, String username, String credentials)
/*     */   {
/* 377 */     if ((username == null) || (credentials == null)) {
/* 378 */       if (this.containerLog.isTraceEnabled()) {
/* 379 */         this.containerLog.trace(sm.getString("jdbcRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 381 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 385 */     String dbCredentials = getPassword(username);
/*     */     
/* 387 */     if (dbCredentials == null)
/*     */     {
/*     */ 
/* 390 */       getCredentialHandler().mutate(credentials);
/*     */       
/* 392 */       if (this.containerLog.isTraceEnabled()) {
/* 393 */         this.containerLog.trace(sm.getString("jdbcRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 395 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 399 */     boolean validated = getCredentialHandler().matches(credentials, dbCredentials);
/*     */     
/* 401 */     if (validated) {
/* 402 */       if (this.containerLog.isTraceEnabled()) {
/* 403 */         this.containerLog.trace(sm.getString("jdbcRealm.authenticateSuccess", new Object[] { username }));
/*     */       }
/*     */     } else {
/* 406 */       if (this.containerLog.isTraceEnabled()) {
/* 407 */         this.containerLog.trace(sm.getString("jdbcRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 409 */       return null;
/*     */     }
/*     */     
/* 412 */     ArrayList<String> roles = getRoles(username);
/*     */     
/*     */ 
/* 415 */     return new GenericPrincipal(username, credentials, roles);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAvailable()
/*     */   {
/* 421 */     return this.dbConnection != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void close(Connection dbConnection)
/*     */   {
/* 433 */     if (dbConnection == null) {
/* 434 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 438 */       this.preparedCredentials.close();
/*     */     } catch (Throwable f) {
/* 440 */       ExceptionUtils.handleThrowable(f);
/*     */     }
/* 442 */     this.preparedCredentials = null;
/*     */     
/*     */     try
/*     */     {
/* 446 */       this.preparedRoles.close();
/*     */     } catch (Throwable f) {
/* 448 */       ExceptionUtils.handleThrowable(f);
/*     */     }
/* 450 */     this.preparedRoles = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 455 */       dbConnection.close();
/*     */     } catch (SQLException e) {
/* 457 */       this.containerLog.warn(sm.getString("jdbcRealm.close"), e);
/*     */     } finally {
/* 459 */       this.dbConnection = null;
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
/*     */ 
/*     */   protected PreparedStatement credentials(Connection dbConnection, String username)
/*     */     throws SQLException
/*     */   {
/* 478 */     if (this.preparedCredentials == null) {
/* 479 */       StringBuilder sb = new StringBuilder("SELECT ");
/* 480 */       sb.append(this.userCredCol);
/* 481 */       sb.append(" FROM ");
/* 482 */       sb.append(this.userTable);
/* 483 */       sb.append(" WHERE ");
/* 484 */       sb.append(this.userNameCol);
/* 485 */       sb.append(" = ?");
/*     */       
/* 487 */       if (this.containerLog.isDebugEnabled()) {
/* 488 */         this.containerLog.debug("credentials query: " + sb.toString());
/*     */       }
/*     */       
/*     */ 
/* 492 */       this.preparedCredentials = dbConnection.prepareStatement(sb.toString());
/*     */     }
/*     */     
/* 495 */     if (username == null) {
/* 496 */       this.preparedCredentials.setNull(1, 12);
/*     */     } else {
/* 498 */       this.preparedCredentials.setString(1, username);
/*     */     }
/*     */     
/* 501 */     return this.preparedCredentials;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 508 */     return "JDBCRealm";
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
/*     */   protected synchronized String getPassword(String username)
/*     */   {
/* 521 */     String dbCredentials = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 531 */     int numberOfTries = 2;
/* 532 */     while (numberOfTries > 0) {
/*     */       try
/*     */       {
/* 535 */         open();
/*     */         
/* 537 */         PreparedStatement stmt = credentials(this.dbConnection, username);
/* 538 */         ResultSet rs = stmt.executeQuery();Throwable localThrowable3 = null;
/* 539 */         try { if (rs.next()) {
/* 540 */             dbCredentials = rs.getString(1);
/*     */           }
/*     */           
/* 543 */           this.dbConnection.commit();
/*     */           
/* 545 */           if (dbCredentials != null) {
/* 546 */             dbCredentials = dbCredentials.trim();
/*     */           }
/*     */           
/* 549 */           return dbCredentials;
/*     */         }
/*     */         catch (Throwable localThrowable4)
/*     */         {
/* 538 */           localThrowable3 = localThrowable4;throw localThrowable4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 550 */           if (rs != null) { if (localThrowable3 != null) try { rs.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { rs.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 561 */         numberOfTries--;
/*     */       }
/*     */       catch (SQLException e)
/*     */       {
/* 553 */         this.containerLog.error(sm.getString("jdbcRealm.exception"), e);
/*     */         
/*     */ 
/*     */ 
/* 557 */         if (this.dbConnection != null) {
/* 558 */           close(this.dbConnection);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 564 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized Principal getPrincipal(String username)
/*     */   {
/* 575 */     return new GenericPrincipal(username, 
/* 576 */       getPassword(username), 
/* 577 */       getRoles(username));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected ArrayList<String> getRoles(String username)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 69	org/apache/catalina/realm/JDBCRealm:allRolesMode	Lorg/apache/catalina/realm/RealmBase$AllRolesMode;
/*     */     //   4: getstatic 70	org/apache/catalina/realm/RealmBase$AllRolesMode:STRICT_MODE	Lorg/apache/catalina/realm/RealmBase$AllRolesMode;
/*     */     //   7: if_acmpeq +12 -> 19
/*     */     //   10: aload_0
/*     */     //   11: invokespecial 71	org/apache/catalina/realm/JDBCRealm:isRoleStoreDefined	()Z
/*     */     //   14: ifne +5 -> 19
/*     */     //   17: aconst_null
/*     */     //   18: areturn
/*     */     //   19: iconst_2
/*     */     //   20: istore_2
/*     */     //   21: iload_2
/*     */     //   22: ifle +240 -> 262
/*     */     //   25: aload_0
/*     */     //   26: invokevirtual 15	org/apache/catalina/realm/JDBCRealm:open	()Ljava/sql/Connection;
/*     */     //   29: pop
/*     */     //   30: aload_0
/*     */     //   31: aload_0
/*     */     //   32: getfield 5	org/apache/catalina/realm/JDBCRealm:dbConnection	Ljava/sql/Connection;
/*     */     //   35: aload_1
/*     */     //   36: invokevirtual 72	org/apache/catalina/realm/JDBCRealm:roles	(Ljava/sql/Connection;Ljava/lang/String;)Ljava/sql/PreparedStatement;
/*     */     //   39: astore_3
/*     */     //   40: aload_3
/*     */     //   41: invokeinterface 62 1 0
/*     */     //   46: astore 4
/*     */     //   48: aconst_null
/*     */     //   49: astore 5
/*     */     //   51: new 73	java/util/ArrayList
/*     */     //   54: dup
/*     */     //   55: invokespecial 74	java/util/ArrayList:<init>	()V
/*     */     //   58: astore 6
/*     */     //   60: aload 4
/*     */     //   62: invokeinterface 63 1 0
/*     */     //   67: ifeq +33 -> 100
/*     */     //   70: aload 4
/*     */     //   72: iconst_1
/*     */     //   73: invokeinterface 64 2 0
/*     */     //   78: astore 7
/*     */     //   80: aconst_null
/*     */     //   81: aload 7
/*     */     //   83: if_acmpeq +14 -> 97
/*     */     //   86: aload 6
/*     */     //   88: aload 7
/*     */     //   90: invokevirtual 66	java/lang/String:trim	()Ljava/lang/String;
/*     */     //   93: invokevirtual 75	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*     */     //   96: pop
/*     */     //   97: goto -37 -> 60
/*     */     //   100: aload 6
/*     */     //   102: astore 7
/*     */     //   104: aload 4
/*     */     //   106: ifnull +37 -> 143
/*     */     //   109: aload 5
/*     */     //   111: ifnull +25 -> 136
/*     */     //   114: aload 4
/*     */     //   116: invokeinterface 67 1 0
/*     */     //   121: goto +22 -> 143
/*     */     //   124: astore 8
/*     */     //   126: aload 5
/*     */     //   128: aload 8
/*     */     //   130: invokevirtual 68	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   133: goto +10 -> 143
/*     */     //   136: aload 4
/*     */     //   138: invokeinterface 67 1 0
/*     */     //   143: aload_0
/*     */     //   144: getfield 5	org/apache/catalina/realm/JDBCRealm:dbConnection	Ljava/sql/Connection;
/*     */     //   147: invokeinterface 65 1 0
/*     */     //   152: aload 7
/*     */     //   154: areturn
/*     */     //   155: astore 6
/*     */     //   157: aload 6
/*     */     //   159: astore 5
/*     */     //   161: aload 6
/*     */     //   163: athrow
/*     */     //   164: astore 9
/*     */     //   166: aload 4
/*     */     //   168: ifnull +37 -> 205
/*     */     //   171: aload 5
/*     */     //   173: ifnull +25 -> 198
/*     */     //   176: aload 4
/*     */     //   178: invokeinterface 67 1 0
/*     */     //   183: goto +22 -> 205
/*     */     //   186: astore 10
/*     */     //   188: aload 5
/*     */     //   190: aload 10
/*     */     //   192: invokevirtual 68	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   195: goto +10 -> 205
/*     */     //   198: aload 4
/*     */     //   200: invokeinterface 67 1 0
/*     */     //   205: aload 9
/*     */     //   207: athrow
/*     */     //   208: astore 11
/*     */     //   210: aload_0
/*     */     //   211: getfield 5	org/apache/catalina/realm/JDBCRealm:dbConnection	Ljava/sql/Connection;
/*     */     //   214: invokeinterface 65 1 0
/*     */     //   219: aload 11
/*     */     //   221: athrow
/*     */     //   222: astore_3
/*     */     //   223: aload_0
/*     */     //   224: getfield 18	org/apache/catalina/realm/JDBCRealm:containerLog	Lorg/apache/juli/logging/Log;
/*     */     //   227: getstatic 19	org/apache/catalina/realm/JDBCRealm:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   230: ldc 20
/*     */     //   232: invokevirtual 21	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   235: aload_3
/*     */     //   236: invokeinterface 22 3 0
/*     */     //   241: aload_0
/*     */     //   242: getfield 5	org/apache/catalina/realm/JDBCRealm:dbConnection	Ljava/sql/Connection;
/*     */     //   245: ifnull +11 -> 256
/*     */     //   248: aload_0
/*     */     //   249: aload_0
/*     */     //   250: getfield 5	org/apache/catalina/realm/JDBCRealm:dbConnection	Ljava/sql/Connection;
/*     */     //   253: invokevirtual 23	org/apache/catalina/realm/JDBCRealm:close	(Ljava/sql/Connection;)V
/*     */     //   256: iinc 2 -1
/*     */     //   259: goto -238 -> 21
/*     */     //   262: aconst_null
/*     */     //   263: areturn
/*     */     // Line number table:
/*     */     //   Java source line #589	-> byte code offset #0
/*     */     //   Java source line #592	-> byte code offset #17
/*     */     //   Java source line #603	-> byte code offset #19
/*     */     //   Java source line #604	-> byte code offset #21
/*     */     //   Java source line #607	-> byte code offset #25
/*     */     //   Java source line #609	-> byte code offset #30
/*     */     //   Java source line #610	-> byte code offset #40
/*     */     //   Java source line #612	-> byte code offset #51
/*     */     //   Java source line #614	-> byte code offset #60
/*     */     //   Java source line #615	-> byte code offset #70
/*     */     //   Java source line #616	-> byte code offset #80
/*     */     //   Java source line #617	-> byte code offset #86
/*     */     //   Java source line #619	-> byte code offset #97
/*     */     //   Java source line #621	-> byte code offset #100
/*     */     //   Java source line #622	-> byte code offset #104
/*     */     //   Java source line #623	-> byte code offset #143
/*     */     //   Java source line #621	-> byte code offset #152
/*     */     //   Java source line #610	-> byte code offset #155
/*     */     //   Java source line #622	-> byte code offset #164
/*     */     //   Java source line #623	-> byte code offset #208
/*     */     //   Java source line #625	-> byte code offset #222
/*     */     //   Java source line #627	-> byte code offset #223
/*     */     //   Java source line #630	-> byte code offset #241
/*     */     //   Java source line #631	-> byte code offset #248
/*     */     //   Java source line #634	-> byte code offset #256
/*     */     //   Java source line #637	-> byte code offset #262
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	264	0	this	JDBCRealm
/*     */     //   0	264	1	username	String
/*     */     //   20	237	2	numberOfTries	int
/*     */     //   39	2	3	stmt	PreparedStatement
/*     */     //   222	14	3	e	SQLException
/*     */     //   46	153	4	rs	ResultSet
/*     */     //   49	140	5	localThrowable3	Throwable
/*     */     //   58	43	6	roleList	ArrayList<String>
/*     */     //   155	7	6	localThrowable1	Throwable
/*     */     //   78	75	7	role	String
/*     */     //   124	5	8	localThrowable	Throwable
/*     */     //   164	42	9	localObject1	Object
/*     */     //   186	5	10	localThrowable2	Throwable
/*     */     //   208	12	11	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   114	121	124	java/lang/Throwable
/*     */     //   51	104	155	java/lang/Throwable
/*     */     //   51	104	164	finally
/*     */     //   155	166	164	finally
/*     */     //   176	183	186	java/lang/Throwable
/*     */     //   40	143	208	finally
/*     */     //   155	210	208	finally
/*     */     //   25	152	222	java/sql/SQLException
/*     */     //   155	222	222	java/sql/SQLException
/*     */   }
/*     */   
/*     */   protected Connection open()
/*     */     throws SQLException
/*     */   {
/* 650 */     if (this.dbConnection != null) {
/* 651 */       return this.dbConnection;
/*     */     }
/*     */     
/* 654 */     if (this.driver == null) {
/*     */       try {
/* 656 */         Class<?> clazz = Class.forName(this.driverName);
/* 657 */         this.driver = ((Driver)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       } catch (Throwable e) {
/* 659 */         ExceptionUtils.handleThrowable(e);
/* 660 */         throw new SQLException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 665 */     Properties props = new Properties();
/* 666 */     if (this.connectionName != null)
/* 667 */       props.put("user", this.connectionName);
/* 668 */     if (this.connectionPassword != null)
/* 669 */       props.put("password", this.connectionPassword);
/* 670 */     this.dbConnection = this.driver.connect(this.connectionURL, props);
/* 671 */     if (this.dbConnection == null) {
/* 672 */       throw new SQLException(sm.getString("jdbcRealm.open.invalidurl", new Object[] { this.driverName, this.connectionURL }));
/*     */     }
/*     */     
/* 675 */     this.dbConnection.setAutoCommit(false);
/* 676 */     return this.dbConnection;
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
/*     */   protected synchronized PreparedStatement roles(Connection dbConnection, String username)
/*     */     throws SQLException
/*     */   {
/* 694 */     if (this.preparedRoles == null) {
/* 695 */       StringBuilder sb = new StringBuilder("SELECT ");
/* 696 */       sb.append(this.roleNameCol);
/* 697 */       sb.append(" FROM ");
/* 698 */       sb.append(this.userRoleTable);
/* 699 */       sb.append(" WHERE ");
/* 700 */       sb.append(this.userNameCol);
/* 701 */       sb.append(" = ?");
/*     */       
/* 703 */       this.preparedRoles = dbConnection.prepareStatement(sb.toString());
/*     */     }
/*     */     
/* 706 */     this.preparedRoles.setString(1, username);
/* 707 */     return this.preparedRoles;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isRoleStoreDefined()
/*     */   {
/* 713 */     return (this.userRoleTable != null) || (this.roleNameCol != null);
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     try
/*     */     {
/* 733 */       open();
/*     */     } catch (SQLException e) {
/* 735 */       this.containerLog.error(sm.getString("jdbcRealm.open"), e);
/*     */     }
/*     */     
/* 738 */     super.startInternal();
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
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 753 */     super.stopInternal();
/*     */     
/*     */ 
/* 756 */     close(this.dbConnection);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\JDBCRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */