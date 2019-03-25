/*     */ package org.springframework.boot.autoconfigure.session;
/*     */ 
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.web.ServerProperties;
/*     */ import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.session.data.redis.RedisFlushMode;
/*     */ import org.springframework.session.hazelcast.HazelcastFlushMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.session")
/*     */ public class SessionProperties
/*     */ {
/*     */   private StoreType storeType;
/*     */   private final Integer timeout;
/*  43 */   private final Hazelcast hazelcast = new Hazelcast();
/*     */   
/*  45 */   private final Jdbc jdbc = new Jdbc();
/*     */   
/*  47 */   private final Mongo mongo = new Mongo();
/*     */   
/*  49 */   private final Redis redis = new Redis();
/*     */   
/*     */   public SessionProperties(ObjectProvider<ServerProperties> serverProperties) {
/*  52 */     ServerProperties properties = (ServerProperties)serverProperties.getIfUnique();
/*  53 */     this.timeout = (properties != null ? properties.getSession().getTimeout() : null);
/*     */   }
/*     */   
/*     */   public StoreType getStoreType() {
/*  57 */     return this.storeType;
/*     */   }
/*     */   
/*     */   public void setStoreType(StoreType storeType) {
/*  61 */     this.storeType = storeType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getTimeout()
/*     */   {
/*  70 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public Hazelcast getHazelcast() {
/*  74 */     return this.hazelcast;
/*     */   }
/*     */   
/*     */   public Jdbc getJdbc() {
/*  78 */     return this.jdbc;
/*     */   }
/*     */   
/*     */   public Mongo getMongo() {
/*  82 */     return this.mongo;
/*     */   }
/*     */   
/*     */   public Redis getRedis() {
/*  86 */     return this.redis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Hazelcast
/*     */   {
/*  94 */     private String mapName = "spring:session:sessions";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  99 */     private HazelcastFlushMode flushMode = HazelcastFlushMode.ON_SAVE;
/*     */     
/*     */     public String getMapName() {
/* 102 */       return this.mapName;
/*     */     }
/*     */     
/*     */     public void setMapName(String mapName) {
/* 106 */       this.mapName = mapName;
/*     */     }
/*     */     
/*     */     public HazelcastFlushMode getFlushMode() {
/* 110 */       return this.flushMode;
/*     */     }
/*     */     
/*     */     public void setFlushMode(HazelcastFlushMode flushMode) {
/* 114 */       this.flushMode = flushMode;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Jdbc
/*     */   {
/*     */     private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/session/jdbc/schema-@@platform@@.sql";
/*     */     
/*     */ 
/*     */     private static final String DEFAULT_TABLE_NAME = "SPRING_SESSION";
/*     */     
/*     */ 
/* 129 */     private String schema = "classpath:org/springframework/session/jdbc/schema-@@platform@@.sql";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 134 */     private String tableName = "SPRING_SESSION";
/*     */     
/* 136 */     private final Initializer initializer = new Initializer();
/*     */     
/*     */     public String getSchema() {
/* 139 */       return this.schema;
/*     */     }
/*     */     
/*     */     public void setSchema(String schema) {
/* 143 */       this.schema = schema;
/*     */     }
/*     */     
/*     */     public String getTableName() {
/* 147 */       return this.tableName;
/*     */     }
/*     */     
/*     */     public void setTableName(String tableName) {
/* 151 */       this.tableName = tableName;
/*     */     }
/*     */     
/*     */     public Initializer getInitializer() {
/* 155 */       return this.initializer;
/*     */     }
/*     */     
/*     */ 
/*     */     public class Initializer
/*     */     {
/*     */       private Boolean enabled;
/*     */       
/*     */ 
/*     */       public Initializer() {}
/*     */       
/*     */       public boolean isEnabled()
/*     */       {
/* 168 */         if (this.enabled != null) {
/* 169 */           return this.enabled.booleanValue();
/*     */         }
/*     */         
/* 172 */         boolean defaultTableName = "SPRING_SESSION".equals(SessionProperties.Jdbc.this.getTableName());
/*     */         
/* 174 */         boolean customSchema = !"classpath:org/springframework/session/jdbc/schema-@@platform@@.sql".equals(SessionProperties.Jdbc.this.getSchema());
/* 175 */         return (defaultTableName) || (customSchema);
/*     */       }
/*     */       
/*     */       public void setEnabled(boolean enabled) {
/* 179 */         this.enabled = Boolean.valueOf(enabled);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Mongo
/*     */   {
/* 191 */     private String collectionName = "sessions";
/*     */     
/*     */     public String getCollectionName() {
/* 194 */       return this.collectionName;
/*     */     }
/*     */     
/*     */     public void setCollectionName(String collectionName) {
/* 198 */       this.collectionName = collectionName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Redis
/*     */   {
/* 208 */     private String namespace = "";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 213 */     private RedisFlushMode flushMode = RedisFlushMode.ON_SAVE;
/*     */     
/*     */     public String getNamespace() {
/* 216 */       return this.namespace;
/*     */     }
/*     */     
/*     */     public void setNamespace(String namespace) {
/* 220 */       this.namespace = namespace;
/*     */     }
/*     */     
/*     */     public RedisFlushMode getFlushMode() {
/* 224 */       return this.flushMode;
/*     */     }
/*     */     
/*     */     public void setFlushMode(RedisFlushMode flushMode) {
/* 228 */       this.flushMode = flushMode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\SessionProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */