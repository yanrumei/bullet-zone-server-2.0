/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import javax.naming.Context;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.naming.ContextBindings;
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
/*     */ public class DataSourceRealm
/*     */   extends RealmBase
/*     */ {
/*  53 */   private String preparedRoles = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private String preparedCredentials = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   protected String dataSourceName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected boolean localDataSource = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "DataSourceRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   protected String roleNameCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected String userCredCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   protected String userNameCol = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */   protected String userRoleTable = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected String userTable = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   private volatile boolean connectionSuccess = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDataSourceName()
/*     */   {
/* 125 */     return this.dataSourceName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDataSourceName(String dataSourceName)
/*     */   {
/* 134 */     this.dataSourceName = dataSourceName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getLocalDataSource()
/*     */   {
/* 141 */     return this.localDataSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocalDataSource(boolean localDataSource)
/*     */   {
/* 151 */     this.localDataSource = localDataSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRoleNameCol()
/*     */   {
/* 158 */     return this.roleNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoleNameCol(String roleNameCol)
/*     */   {
/* 167 */     this.roleNameCol = roleNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserCredCol()
/*     */   {
/* 174 */     return this.userCredCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserCredCol(String userCredCol)
/*     */   {
/* 183 */     this.userCredCol = userCredCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserNameCol()
/*     */   {
/* 190 */     return this.userNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserNameCol(String userNameCol)
/*     */   {
/* 199 */     this.userNameCol = userNameCol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserRoleTable()
/*     */   {
/* 206 */     return this.userRoleTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserRoleTable(String userRoleTable)
/*     */   {
/* 215 */     this.userRoleTable = userRoleTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUserTable()
/*     */   {
/* 222 */     return this.userTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserTable(String userTable)
/*     */   {
/* 231 */     this.userTable = userTable;
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
/*     */   public Principal authenticate(String username, String credentials)
/*     */   {
/* 256 */     if ((username == null) || (credentials == null)) {
/* 257 */       return null;
/*     */     }
/*     */     
/* 260 */     Connection dbConnection = null;
/*     */     
/*     */ 
/* 263 */     dbConnection = open();
/* 264 */     if (dbConnection == null)
/*     */     {
/* 266 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 272 */       return authenticate(dbConnection, username, credentials);
/*     */     }
/*     */     finally
/*     */     {
/* 276 */       close(dbConnection);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAvailable()
/*     */   {
/* 283 */     return this.connectionSuccess;
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
/*     */   protected Principal authenticate(Connection dbConnection, String username, String credentials)
/*     */   {
/* 307 */     if ((username == null) || (credentials == null)) {
/* 308 */       if (this.containerLog.isTraceEnabled()) {
/* 309 */         this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 311 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 315 */     String dbCredentials = getPassword(dbConnection, username);
/*     */     
/* 317 */     if (dbCredentials == null)
/*     */     {
/*     */ 
/* 320 */       getCredentialHandler().mutate(credentials);
/*     */       
/* 322 */       if (this.containerLog.isTraceEnabled()) {
/* 323 */         this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 325 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 329 */     boolean validated = getCredentialHandler().matches(credentials, dbCredentials);
/*     */     
/* 331 */     if (validated) {
/* 332 */       if (this.containerLog.isTraceEnabled()) {
/* 333 */         this.containerLog.trace(sm.getString("dataSourceRealm.authenticateSuccess", new Object[] { username }));
/*     */       }
/*     */     } else {
/* 336 */       if (this.containerLog.isTraceEnabled()) {
/* 337 */         this.containerLog.trace(sm.getString("dataSourceRealm.authenticateFailure", new Object[] { username }));
/*     */       }
/* 339 */       return null;
/*     */     }
/*     */     
/* 342 */     ArrayList<String> list = getRoles(dbConnection, username);
/*     */     
/*     */ 
/* 345 */     return new GenericPrincipal(username, credentials, list);
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
/* 357 */     if (dbConnection == null) {
/* 358 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 362 */       if (!dbConnection.getAutoCommit()) {
/* 363 */         dbConnection.commit();
/*     */       }
/*     */     } catch (SQLException e) {
/* 366 */       this.containerLog.error("Exception committing connection before closing:", e);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 371 */       dbConnection.close();
/*     */     } catch (SQLException e) {
/* 373 */       this.containerLog.error(sm.getString("dataSourceRealm.close"), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Connection open()
/*     */   {
/*     */     try
/*     */     {
/* 386 */       Context context = null;
/* 387 */       if (this.localDataSource) {
/* 388 */         context = ContextBindings.getClassLoader();
/* 389 */         context = (Context)context.lookup("comp/env");
/*     */       } else {
/* 391 */         context = getServer().getGlobalNamingContext();
/*     */       }
/* 393 */       DataSource dataSource = (DataSource)context.lookup(this.dataSourceName);
/* 394 */       Connection connection = dataSource.getConnection();
/* 395 */       this.connectionSuccess = true;
/* 396 */       return connection;
/*     */     } catch (Exception e) {
/* 398 */       this.connectionSuccess = false;
/*     */       
/* 400 */       this.containerLog.error(sm.getString("dataSourceRealm.exception"), e);
/*     */     }
/* 402 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 408 */     return "DataSourceRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPassword(String username)
/*     */   {
/* 417 */     Connection dbConnection = null;
/*     */     
/*     */ 
/* 420 */     dbConnection = open();
/* 421 */     if (dbConnection == null) {
/* 422 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 426 */       return getPassword(dbConnection, username);
/*     */     } finally {
/* 428 */       close(dbConnection);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected String getPassword(Connection dbConnection, String username)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_3
/*     */     //   2: aload_0
/*     */     //   3: aload_1
/*     */     //   4: aload_2
/*     */     //   5: invokespecial 50	org/apache/catalina/realm/DataSourceRealm:credentials	(Ljava/sql/Connection;Ljava/lang/String;)Ljava/sql/PreparedStatement;
/*     */     //   8: astore 4
/*     */     //   10: aconst_null
/*     */     //   11: astore 5
/*     */     //   13: aload 4
/*     */     //   15: invokeinterface 51 1 0
/*     */     //   20: astore 6
/*     */     //   22: aconst_null
/*     */     //   23: astore 7
/*     */     //   25: aload 6
/*     */     //   27: invokeinterface 52 1 0
/*     */     //   32: ifeq +12 -> 44
/*     */     //   35: aload 6
/*     */     //   37: iconst_1
/*     */     //   38: invokeinterface 53 2 0
/*     */     //   43: astore_3
/*     */     //   44: aload_3
/*     */     //   45: ifnull +10 -> 55
/*     */     //   48: aload_3
/*     */     //   49: invokevirtual 54	java/lang/String:trim	()Ljava/lang/String;
/*     */     //   52: goto +4 -> 56
/*     */     //   55: aconst_null
/*     */     //   56: astore 8
/*     */     //   58: aload 6
/*     */     //   60: ifnull +37 -> 97
/*     */     //   63: aload 7
/*     */     //   65: ifnull +25 -> 90
/*     */     //   68: aload 6
/*     */     //   70: invokeinterface 55 1 0
/*     */     //   75: goto +22 -> 97
/*     */     //   78: astore 9
/*     */     //   80: aload 7
/*     */     //   82: aload 9
/*     */     //   84: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   87: goto +10 -> 97
/*     */     //   90: aload 6
/*     */     //   92: invokeinterface 55 1 0
/*     */     //   97: aload 4
/*     */     //   99: ifnull +37 -> 136
/*     */     //   102: aload 5
/*     */     //   104: ifnull +25 -> 129
/*     */     //   107: aload 4
/*     */     //   109: invokeinterface 58 1 0
/*     */     //   114: goto +22 -> 136
/*     */     //   117: astore 9
/*     */     //   119: aload 5
/*     */     //   121: aload 9
/*     */     //   123: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   126: goto +10 -> 136
/*     */     //   129: aload 4
/*     */     //   131: invokeinterface 58 1 0
/*     */     //   136: aload 8
/*     */     //   138: areturn
/*     */     //   139: astore 8
/*     */     //   141: aload 8
/*     */     //   143: astore 7
/*     */     //   145: aload 8
/*     */     //   147: athrow
/*     */     //   148: astore 10
/*     */     //   150: aload 6
/*     */     //   152: ifnull +37 -> 189
/*     */     //   155: aload 7
/*     */     //   157: ifnull +25 -> 182
/*     */     //   160: aload 6
/*     */     //   162: invokeinterface 55 1 0
/*     */     //   167: goto +22 -> 189
/*     */     //   170: astore 11
/*     */     //   172: aload 7
/*     */     //   174: aload 11
/*     */     //   176: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   179: goto +10 -> 189
/*     */     //   182: aload 6
/*     */     //   184: invokeinterface 55 1 0
/*     */     //   189: aload 10
/*     */     //   191: athrow
/*     */     //   192: astore 6
/*     */     //   194: aload 6
/*     */     //   196: astore 5
/*     */     //   198: aload 6
/*     */     //   200: athrow
/*     */     //   201: astore 12
/*     */     //   203: aload 4
/*     */     //   205: ifnull +37 -> 242
/*     */     //   208: aload 5
/*     */     //   210: ifnull +25 -> 235
/*     */     //   213: aload 4
/*     */     //   215: invokeinterface 58 1 0
/*     */     //   220: goto +22 -> 242
/*     */     //   223: astore 13
/*     */     //   225: aload 5
/*     */     //   227: aload 13
/*     */     //   229: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   232: goto +10 -> 242
/*     */     //   235: aload 4
/*     */     //   237: invokeinterface 58 1 0
/*     */     //   242: aload 12
/*     */     //   244: athrow
/*     */     //   245: astore 4
/*     */     //   247: aload_0
/*     */     //   248: getfield 15	org/apache/catalina/realm/DataSourceRealm:containerLog	Lorg/apache/juli/logging/Log;
/*     */     //   251: getstatic 17	org/apache/catalina/realm/DataSourceRealm:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   254: ldc 59
/*     */     //   256: iconst_1
/*     */     //   257: anewarray 19	java/lang/Object
/*     */     //   260: dup
/*     */     //   261: iconst_0
/*     */     //   262: aload_2
/*     */     //   263: aastore
/*     */     //   264: invokevirtual 20	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   267: aload 4
/*     */     //   269: invokeinterface 34 3 0
/*     */     //   274: aconst_null
/*     */     //   275: areturn
/*     */     // Line number table:
/*     */     //   Java source line #441	-> byte code offset #0
/*     */     //   Java source line #443	-> byte code offset #2
/*     */     //   Java source line #444	-> byte code offset #13
/*     */     //   Java source line #443	-> byte code offset #22
/*     */     //   Java source line #445	-> byte code offset #25
/*     */     //   Java source line #446	-> byte code offset #35
/*     */     //   Java source line #449	-> byte code offset #44
/*     */     //   Java source line #451	-> byte code offset #58
/*     */     //   Java source line #449	-> byte code offset #136
/*     */     //   Java source line #443	-> byte code offset #139
/*     */     //   Java source line #451	-> byte code offset #148
/*     */     //   Java source line #443	-> byte code offset #192
/*     */     //   Java source line #451	-> byte code offset #201
/*     */     //   Java source line #452	-> byte code offset #247
/*     */     //   Java source line #453	-> byte code offset #264
/*     */     //   Java source line #452	-> byte code offset #269
/*     */     //   Java source line #457	-> byte code offset #274
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	276	0	this	DataSourceRealm
/*     */     //   0	276	1	dbConnection	Connection
/*     */     //   0	276	2	username	String
/*     */     //   1	48	3	dbCredentials	String
/*     */     //   8	228	4	stmt	PreparedStatement
/*     */     //   245	23	4	e	SQLException
/*     */     //   11	215	5	localThrowable6	Throwable
/*     */     //   20	163	6	rs	java.sql.ResultSet
/*     */     //   192	7	6	localThrowable4	Throwable
/*     */     //   23	150	7	localThrowable7	Throwable
/*     */     //   139	7	8	localThrowable2	Throwable
/*     */     //   139	7	8	localThrowable8	Throwable
/*     */     //   78	5	9	localThrowable	Throwable
/*     */     //   117	5	9	localThrowable1	Throwable
/*     */     //   148	42	10	localObject1	Object
/*     */     //   170	5	11	localThrowable3	Throwable
/*     */     //   201	42	12	localObject2	Object
/*     */     //   223	5	13	localThrowable5	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   68	75	78	java/lang/Throwable
/*     */     //   107	114	117	java/lang/Throwable
/*     */     //   25	58	139	java/lang/Throwable
/*     */     //   25	58	148	finally
/*     */     //   139	150	148	finally
/*     */     //   160	167	170	java/lang/Throwable
/*     */     //   13	97	192	java/lang/Throwable
/*     */     //   139	192	192	java/lang/Throwable
/*     */     //   13	97	201	finally
/*     */     //   139	203	201	finally
/*     */     //   213	220	223	java/lang/Throwable
/*     */     //   2	136	245	java/sql/SQLException
/*     */     //   139	245	245	java/sql/SQLException
/*     */   }
/*     */   
/*     */   protected Principal getPrincipal(String username)
/*     */   {
/* 468 */     Connection dbConnection = open();
/* 469 */     if (dbConnection == null) {
/* 470 */       return new GenericPrincipal(username, null, null);
/*     */     }
/*     */     try {
/* 473 */       return new GenericPrincipal(username, 
/* 474 */         getPassword(dbConnection, username), 
/* 475 */         getRoles(dbConnection, username));
/*     */     } finally {
/* 477 */       close(dbConnection);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayList<String> getRoles(String username)
/*     */   {
/* 489 */     Connection dbConnection = null;
/*     */     
/*     */ 
/* 492 */     dbConnection = open();
/* 493 */     if (dbConnection == null) {
/* 494 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 498 */       return getRoles(dbConnection, username);
/*     */     } finally {
/* 500 */       close(dbConnection);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected ArrayList<String> getRoles(Connection dbConnection, String username)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 60	org/apache/catalina/realm/DataSourceRealm:allRolesMode	Lorg/apache/catalina/realm/RealmBase$AllRolesMode;
/*     */     //   4: getstatic 61	org/apache/catalina/realm/RealmBase$AllRolesMode:STRICT_MODE	Lorg/apache/catalina/realm/RealmBase$AllRolesMode;
/*     */     //   7: if_acmpeq +12 -> 19
/*     */     //   10: aload_0
/*     */     //   11: invokespecial 62	org/apache/catalina/realm/DataSourceRealm:isRoleStoreDefined	()Z
/*     */     //   14: ifne +5 -> 19
/*     */     //   17: aconst_null
/*     */     //   18: areturn
/*     */     //   19: aconst_null
/*     */     //   20: astore_3
/*     */     //   21: aload_0
/*     */     //   22: aload_1
/*     */     //   23: aload_2
/*     */     //   24: invokespecial 63	org/apache/catalina/realm/DataSourceRealm:roles	(Ljava/sql/Connection;Ljava/lang/String;)Ljava/sql/PreparedStatement;
/*     */     //   27: astore 4
/*     */     //   29: aconst_null
/*     */     //   30: astore 5
/*     */     //   32: aload 4
/*     */     //   34: invokeinterface 51 1 0
/*     */     //   39: astore 6
/*     */     //   41: aconst_null
/*     */     //   42: astore 7
/*     */     //   44: new 64	java/util/ArrayList
/*     */     //   47: dup
/*     */     //   48: invokespecial 65	java/util/ArrayList:<init>	()V
/*     */     //   51: astore_3
/*     */     //   52: aload 6
/*     */     //   54: invokeinterface 52 1 0
/*     */     //   59: ifeq +31 -> 90
/*     */     //   62: aload 6
/*     */     //   64: iconst_1
/*     */     //   65: invokeinterface 53 2 0
/*     */     //   70: astore 8
/*     */     //   72: aload 8
/*     */     //   74: ifnull +13 -> 87
/*     */     //   77: aload_3
/*     */     //   78: aload 8
/*     */     //   80: invokevirtual 54	java/lang/String:trim	()Ljava/lang/String;
/*     */     //   83: invokevirtual 66	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*     */     //   86: pop
/*     */     //   87: goto -35 -> 52
/*     */     //   90: aload_3
/*     */     //   91: astore 8
/*     */     //   93: aload 6
/*     */     //   95: ifnull +37 -> 132
/*     */     //   98: aload 7
/*     */     //   100: ifnull +25 -> 125
/*     */     //   103: aload 6
/*     */     //   105: invokeinterface 55 1 0
/*     */     //   110: goto +22 -> 132
/*     */     //   113: astore 9
/*     */     //   115: aload 7
/*     */     //   117: aload 9
/*     */     //   119: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   122: goto +10 -> 132
/*     */     //   125: aload 6
/*     */     //   127: invokeinterface 55 1 0
/*     */     //   132: aload 4
/*     */     //   134: ifnull +37 -> 171
/*     */     //   137: aload 5
/*     */     //   139: ifnull +25 -> 164
/*     */     //   142: aload 4
/*     */     //   144: invokeinterface 58 1 0
/*     */     //   149: goto +22 -> 171
/*     */     //   152: astore 9
/*     */     //   154: aload 5
/*     */     //   156: aload 9
/*     */     //   158: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   161: goto +10 -> 171
/*     */     //   164: aload 4
/*     */     //   166: invokeinterface 58 1 0
/*     */     //   171: aload 8
/*     */     //   173: areturn
/*     */     //   174: astore 8
/*     */     //   176: aload 8
/*     */     //   178: astore 7
/*     */     //   180: aload 8
/*     */     //   182: athrow
/*     */     //   183: astore 10
/*     */     //   185: aload 6
/*     */     //   187: ifnull +37 -> 224
/*     */     //   190: aload 7
/*     */     //   192: ifnull +25 -> 217
/*     */     //   195: aload 6
/*     */     //   197: invokeinterface 55 1 0
/*     */     //   202: goto +22 -> 224
/*     */     //   205: astore 11
/*     */     //   207: aload 7
/*     */     //   209: aload 11
/*     */     //   211: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   214: goto +10 -> 224
/*     */     //   217: aload 6
/*     */     //   219: invokeinterface 55 1 0
/*     */     //   224: aload 10
/*     */     //   226: athrow
/*     */     //   227: astore 6
/*     */     //   229: aload 6
/*     */     //   231: astore 5
/*     */     //   233: aload 6
/*     */     //   235: athrow
/*     */     //   236: astore 12
/*     */     //   238: aload 4
/*     */     //   240: ifnull +37 -> 277
/*     */     //   243: aload 5
/*     */     //   245: ifnull +25 -> 270
/*     */     //   248: aload 4
/*     */     //   250: invokeinterface 58 1 0
/*     */     //   255: goto +22 -> 277
/*     */     //   258: astore 13
/*     */     //   260: aload 5
/*     */     //   262: aload 13
/*     */     //   264: invokevirtual 57	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   267: goto +10 -> 277
/*     */     //   270: aload 4
/*     */     //   272: invokeinterface 58 1 0
/*     */     //   277: aload 12
/*     */     //   279: athrow
/*     */     //   280: astore 4
/*     */     //   282: aload_0
/*     */     //   283: getfield 15	org/apache/catalina/realm/DataSourceRealm:containerLog	Lorg/apache/juli/logging/Log;
/*     */     //   286: getstatic 17	org/apache/catalina/realm/DataSourceRealm:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   289: ldc 67
/*     */     //   291: iconst_1
/*     */     //   292: anewarray 19	java/lang/Object
/*     */     //   295: dup
/*     */     //   296: iconst_0
/*     */     //   297: aload_2
/*     */     //   298: aastore
/*     */     //   299: invokevirtual 20	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   302: aload 4
/*     */     //   304: invokeinterface 34 3 0
/*     */     //   309: aconst_null
/*     */     //   310: areturn
/*     */     // Line number table:
/*     */     //   Java source line #513	-> byte code offset #0
/*     */     //   Java source line #516	-> byte code offset #17
/*     */     //   Java source line #519	-> byte code offset #19
/*     */     //   Java source line #521	-> byte code offset #21
/*     */     //   Java source line #522	-> byte code offset #32
/*     */     //   Java source line #521	-> byte code offset #41
/*     */     //   Java source line #523	-> byte code offset #44
/*     */     //   Java source line #525	-> byte code offset #52
/*     */     //   Java source line #526	-> byte code offset #62
/*     */     //   Java source line #527	-> byte code offset #72
/*     */     //   Java source line #528	-> byte code offset #77
/*     */     //   Java source line #530	-> byte code offset #87
/*     */     //   Java source line #531	-> byte code offset #90
/*     */     //   Java source line #532	-> byte code offset #93
/*     */     //   Java source line #531	-> byte code offset #171
/*     */     //   Java source line #521	-> byte code offset #174
/*     */     //   Java source line #532	-> byte code offset #183
/*     */     //   Java source line #521	-> byte code offset #227
/*     */     //   Java source line #532	-> byte code offset #236
/*     */     //   Java source line #533	-> byte code offset #282
/*     */     //   Java source line #534	-> byte code offset #299
/*     */     //   Java source line #533	-> byte code offset #304
/*     */     //   Java source line #537	-> byte code offset #309
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	311	0	this	DataSourceRealm
/*     */     //   0	311	1	dbConnection	Connection
/*     */     //   0	311	2	username	String
/*     */     //   20	71	3	list	ArrayList<String>
/*     */     //   27	244	4	stmt	PreparedStatement
/*     */     //   280	23	4	e	SQLException
/*     */     //   30	231	5	localThrowable6	Throwable
/*     */     //   39	179	6	rs	java.sql.ResultSet
/*     */     //   227	7	6	localThrowable4	Throwable
/*     */     //   42	166	7	localThrowable7	Throwable
/*     */     //   70	102	8	role	String
/*     */     //   174	7	8	localThrowable2	Throwable
/*     */     //   113	5	9	localThrowable	Throwable
/*     */     //   152	5	9	localThrowable1	Throwable
/*     */     //   183	42	10	localObject1	Object
/*     */     //   205	5	11	localThrowable3	Throwable
/*     */     //   236	42	12	localObject2	Object
/*     */     //   258	5	13	localThrowable5	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   103	110	113	java/lang/Throwable
/*     */     //   142	149	152	java/lang/Throwable
/*     */     //   44	93	174	java/lang/Throwable
/*     */     //   44	93	183	finally
/*     */     //   174	185	183	finally
/*     */     //   195	202	205	java/lang/Throwable
/*     */     //   32	132	227	java/lang/Throwable
/*     */     //   174	227	227	java/lang/Throwable
/*     */     //   32	132	236	finally
/*     */     //   174	238	236	finally
/*     */     //   248	255	258	java/lang/Throwable
/*     */     //   21	171	280	java/sql/SQLException
/*     */     //   174	280	280	java/sql/SQLException
/*     */   }
/*     */   
/*     */   private PreparedStatement credentials(Connection dbConnection, String username)
/*     */     throws SQLException
/*     */   {
/* 554 */     PreparedStatement credentials = dbConnection.prepareStatement(this.preparedCredentials);
/*     */     
/* 556 */     credentials.setString(1, username);
/* 557 */     return credentials;
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
/*     */   private PreparedStatement roles(Connection dbConnection, String username)
/*     */     throws SQLException
/*     */   {
/* 574 */     PreparedStatement roles = dbConnection.prepareStatement(this.preparedRoles);
/*     */     
/* 576 */     roles.setString(1, username);
/* 577 */     return roles;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isRoleStoreDefined()
/*     */   {
/* 583 */     return (this.userRoleTable != null) || (this.roleNameCol != null);
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
/* 601 */     StringBuilder temp = new StringBuilder("SELECT ");
/* 602 */     temp.append(this.roleNameCol);
/* 603 */     temp.append(" FROM ");
/* 604 */     temp.append(this.userRoleTable);
/* 605 */     temp.append(" WHERE ");
/* 606 */     temp.append(this.userNameCol);
/* 607 */     temp.append(" = ?");
/* 608 */     this.preparedRoles = temp.toString();
/*     */     
/*     */ 
/* 611 */     temp = new StringBuilder("SELECT ");
/* 612 */     temp.append(this.userCredCol);
/* 613 */     temp.append(" FROM ");
/* 614 */     temp.append(this.userTable);
/* 615 */     temp.append(" WHERE ");
/* 616 */     temp.append(this.userNameCol);
/* 617 */     temp.append(" = ?");
/* 618 */     this.preparedCredentials = temp.toString();
/*     */     
/* 620 */     super.startInternal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\DataSourceRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */