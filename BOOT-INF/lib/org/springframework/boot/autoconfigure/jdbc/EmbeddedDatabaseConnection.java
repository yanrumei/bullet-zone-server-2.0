/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.dao.DataAccessException;
/*     */ import org.springframework.jdbc.core.ConnectionCallback;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum EmbeddedDatabaseConnection
/*     */ {
/*  44 */   NONE(null, null, null), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   H2(EmbeddedDatabaseType.H2, "org.h2.Driver", "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   DERBY(EmbeddedDatabaseType.DERBY, "org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:memory:%s;create=true"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   HSQL(EmbeddedDatabaseType.HSQL, "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:%s");
/*     */   
/*     */ 
/*     */   private static final String DEFAULT_DATABASE_NAME = "testdb";
/*     */   
/*     */   private final EmbeddedDatabaseType type;
/*     */   private final String driverClass;
/*     */   private final String url;
/*     */   static EmbeddedDatabaseConnection override;
/*     */   
/*     */   private EmbeddedDatabaseConnection(EmbeddedDatabaseType type, String driverClass, String url)
/*     */   {
/*  73 */     this.type = type;
/*  74 */     this.driverClass = driverClass;
/*  75 */     this.url = url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDriverClassName()
/*     */   {
/*  83 */     return this.driverClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmbeddedDatabaseType getType()
/*     */   {
/*  91 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl()
/*     */   {
/*  99 */     return getUrl("testdb");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl(String databaseName)
/*     */   {
/* 108 */     Assert.hasText(databaseName, "DatabaseName must not be null.");
/* 109 */     return this.url != null ? String.format(this.url, new Object[] { databaseName }) : null;
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
/*     */   public static boolean isEmbedded(String driverClass)
/*     */   {
/* 124 */     return (driverClass != null) && ((driverClass.equals(HSQL.driverClass)) || 
/* 125 */       (driverClass.equals(H2.driverClass)) || 
/* 126 */       (driverClass.equals(DERBY.driverClass)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isEmbedded(DataSource dataSource)
/*     */   {
/*     */     try
/*     */     {
/* 137 */       return ((Boolean)new JdbcTemplate(dataSource).execute(new IsEmbedded(null))).booleanValue();
/*     */     }
/*     */     catch (DataAccessException ex) {}
/*     */     
/* 141 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EmbeddedDatabaseConnection get(ClassLoader classLoader)
/*     */   {
/* 152 */     if (override != null) {
/* 153 */       return override;
/*     */     }
/* 155 */     for (EmbeddedDatabaseConnection candidate : values()) {
/* 156 */       if ((candidate != NONE) && (ClassUtils.isPresent(candidate.getDriverClassName(), classLoader)))
/*     */       {
/* 158 */         return candidate;
/*     */       }
/*     */     }
/* 161 */     return NONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class IsEmbedded
/*     */     implements ConnectionCallback<Boolean>
/*     */   {
/*     */     public Boolean doInConnection(Connection connection)
/*     */       throws SQLException, DataAccessException
/*     */     {
/* 172 */       String productName = connection.getMetaData().getDatabaseProductName();
/* 173 */       if (productName == null) {
/* 174 */         return Boolean.valueOf(false);
/*     */       }
/* 176 */       productName = productName.toUpperCase();
/* 177 */       EmbeddedDatabaseConnection[] candidates = EmbeddedDatabaseConnection.values();
/* 178 */       for (EmbeddedDatabaseConnection candidate : candidates) {
/* 179 */         if ((candidate != EmbeddedDatabaseConnection.NONE) && (productName.contains(candidate.name()))) {
/* 180 */           return Boolean.valueOf(true);
/*     */         }
/*     */       }
/* 183 */       return Boolean.valueOf(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\EmbeddedDatabaseConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */