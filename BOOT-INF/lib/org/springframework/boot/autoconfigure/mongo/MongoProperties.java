/*     */ package org.springframework.boot.autoconfigure.mongo;
/*     */ 
/*     */ import com.mongodb.MongoClient;
/*     */ import com.mongodb.MongoClientOptions;
/*     */ import com.mongodb.MongoClientOptions.Builder;
/*     */ import com.mongodb.MongoClientURI;
/*     */ import com.mongodb.MongoCredential;
/*     */ import com.mongodb.ServerAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.core.env.Environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.data.mongodb")
/*     */ public class MongoProperties
/*     */ {
/*     */   public static final int DEFAULT_PORT = 27017;
/*     */   public static final String DEFAULT_URI = "mongodb://localhost/test";
/*     */   private String host;
/*  63 */   private Integer port = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String uri;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String database;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String authenticationDatabase;
/*     */   
/*     */ 
/*     */ 
/*     */   private String gridFsDatabase;
/*     */   
/*     */ 
/*     */ 
/*     */   private String username;
/*     */   
/*     */ 
/*     */ 
/*     */   private char[] password;
/*     */   
/*     */ 
/*     */ 
/*     */   private Class<?> fieldNamingStrategy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 101 */     return this.host;
/*     */   }
/*     */   
/*     */   public void setHost(String host) {
/* 105 */     this.host = host;
/*     */   }
/*     */   
/*     */   public String getDatabase() {
/* 109 */     return this.database;
/*     */   }
/*     */   
/*     */   public void setDatabase(String database) {
/* 113 */     this.database = database;
/*     */   }
/*     */   
/*     */   public String getAuthenticationDatabase() {
/* 117 */     return this.authenticationDatabase;
/*     */   }
/*     */   
/*     */   public void setAuthenticationDatabase(String authenticationDatabase) {
/* 121 */     this.authenticationDatabase = authenticationDatabase;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 125 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 129 */     this.username = username;
/*     */   }
/*     */   
/*     */   public char[] getPassword() {
/* 133 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(char[] password) {
/* 137 */     this.password = password;
/*     */   }
/*     */   
/*     */   public Class<?> getFieldNamingStrategy() {
/* 141 */     return this.fieldNamingStrategy;
/*     */   }
/*     */   
/*     */   public void setFieldNamingStrategy(Class<?> fieldNamingStrategy) {
/* 145 */     this.fieldNamingStrategy = fieldNamingStrategy;
/*     */   }
/*     */   
/*     */   public void clearPassword() {
/* 149 */     if (this.password == null) {
/* 150 */       return;
/*     */     }
/* 152 */     for (int i = 0; i < this.password.length; i++) {
/* 153 */       this.password[i] = '\000';
/*     */     }
/*     */   }
/*     */   
/*     */   public String getUri() {
/* 158 */     return this.uri;
/*     */   }
/*     */   
/*     */   public String determineUri() {
/* 162 */     return this.uri != null ? this.uri : "mongodb://localhost/test";
/*     */   }
/*     */   
/*     */   public void setUri(String uri) {
/* 166 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public Integer getPort() {
/* 170 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(Integer port) {
/* 174 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getGridFsDatabase() {
/* 178 */     return this.gridFsDatabase;
/*     */   }
/*     */   
/*     */   public void setGridFsDatabase(String gridFsDatabase) {
/* 182 */     this.gridFsDatabase = gridFsDatabase;
/*     */   }
/*     */   
/*     */   public String getMongoClientDatabase() {
/* 186 */     if (this.database != null) {
/* 187 */       return this.database;
/*     */     }
/* 189 */     return new MongoClientURI(determineUri()).getDatabase();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MongoClient createMongoClient(MongoClientOptions options, Environment environment)
/*     */     throws UnknownHostException
/*     */   {
/*     */     try
/*     */     {
/* 204 */       Integer embeddedPort = getEmbeddedPort(environment);
/* 205 */       MongoClient localMongoClient; if (embeddedPort != null) {
/* 206 */         return createEmbeddedMongoClient(options, embeddedPort.intValue());
/*     */       }
/* 208 */       return createNetworkMongoClient(options);
/*     */     }
/*     */     finally {
/* 211 */       clearPassword();
/*     */     }
/*     */   }
/*     */   
/*     */   private Integer getEmbeddedPort(Environment environment) {
/* 216 */     if (environment != null) {
/* 217 */       String localPort = environment.getProperty("local.mongo.port");
/* 218 */       if (localPort != null) {
/* 219 */         return Integer.valueOf(localPort);
/*     */       }
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */   
/*     */   private MongoClient createEmbeddedMongoClient(MongoClientOptions options, int port) {
/* 226 */     if (options == null) {
/* 227 */       options = MongoClientOptions.builder().build();
/*     */     }
/* 229 */     String host = this.host == null ? "localhost" : this.host;
/* 230 */     return new MongoClient(Collections.singletonList(new ServerAddress(host, port)), 
/* 231 */       Collections.emptyList(), options);
/*     */   }
/*     */   
/*     */   private MongoClient createNetworkMongoClient(MongoClientOptions options) {
/* 235 */     if ((hasCustomAddress()) || (hasCustomCredentials())) {
/* 236 */       if (this.uri != null) {
/* 237 */         throw new IllegalStateException("Invalid mongo configuration, either uri or host/port/credentials must be specified");
/*     */       }
/*     */       
/* 240 */       if (options == null) {
/* 241 */         options = MongoClientOptions.builder().build();
/*     */       }
/* 243 */       List<MongoCredential> credentials = new ArrayList();
/* 244 */       if (hasCustomCredentials())
/*     */       {
/* 246 */         String database = this.authenticationDatabase == null ? getMongoClientDatabase() : this.authenticationDatabase;
/* 247 */         credentials.add(MongoCredential.createCredential(this.username, database, this.password));
/*     */       }
/*     */       
/* 250 */       String host = this.host == null ? "localhost" : this.host;
/* 251 */       int port = this.port != null ? this.port.intValue() : 27017;
/* 252 */       return new MongoClient(
/* 253 */         Collections.singletonList(new ServerAddress(host, port)), credentials, options);
/*     */     }
/*     */     
/*     */ 
/* 257 */     return new MongoClient(new MongoClientURI(determineUri(), builder(options)));
/*     */   }
/*     */   
/*     */   private boolean hasCustomAddress() {
/* 261 */     return (this.host != null) || (this.port != null);
/*     */   }
/*     */   
/*     */   private boolean hasCustomCredentials() {
/* 265 */     return (this.username != null) && (this.password != null);
/*     */   }
/*     */   
/*     */   private MongoClientOptions.Builder builder(MongoClientOptions options) {
/* 269 */     if (options != null) {
/* 270 */       return MongoClientOptions.builder(options);
/*     */     }
/* 272 */     return MongoClientOptions.builder();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mongo\MongoProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */