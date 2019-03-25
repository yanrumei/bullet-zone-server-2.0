/*     */ package org.springframework.boot.autoconfigure.cassandra;
/*     */ 
/*     */ import com.datastax.driver.core.ConsistencyLevel;
/*     */ import com.datastax.driver.core.ProtocolOptions.Compression;
/*     */ import com.datastax.driver.core.policies.LoadBalancingPolicy;
/*     */ import com.datastax.driver.core.policies.ReconnectionPolicy;
/*     */ import com.datastax.driver.core.policies.RetryPolicy;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.data.cassandra")
/*     */ public class CassandraProperties
/*     */ {
/*     */   private String keyspaceName;
/*     */   private String clusterName;
/*  54 */   private String contactPoints = "localhost";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private int port = 9042;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String username;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private ProtocolOptions.Compression compression = ProtocolOptions.Compression.NONE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<? extends LoadBalancingPolicy> loadBalancingPolicy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConsistencyLevel consistencyLevel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConsistencyLevel serialConsistencyLevel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private int fetchSize = 5000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<? extends ReconnectionPolicy> reconnectionPolicy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<? extends RetryPolicy> retryPolicy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 109 */   private int connectTimeoutMillis = 5000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private int readTimeoutMillis = 12000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private String schemaAction = "none";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 124 */   private boolean ssl = false;
/*     */   
/*     */   public String getKeyspaceName() {
/* 127 */     return this.keyspaceName;
/*     */   }
/*     */   
/*     */   public void setKeyspaceName(String keyspaceName) {
/* 131 */     this.keyspaceName = keyspaceName;
/*     */   }
/*     */   
/*     */   public String getClusterName() {
/* 135 */     return this.clusterName;
/*     */   }
/*     */   
/*     */   public void setClusterName(String clusterName) {
/* 139 */     this.clusterName = clusterName;
/*     */   }
/*     */   
/*     */   public String getContactPoints() {
/* 143 */     return this.contactPoints;
/*     */   }
/*     */   
/*     */   public void setContactPoints(String contactPoints) {
/* 147 */     this.contactPoints = contactPoints;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 151 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/* 155 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 159 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 163 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 167 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 171 */     this.password = password;
/*     */   }
/*     */   
/*     */   public ProtocolOptions.Compression getCompression() {
/* 175 */     return this.compression;
/*     */   }
/*     */   
/*     */   public void setCompression(ProtocolOptions.Compression compression) {
/* 179 */     this.compression = compression;
/*     */   }
/*     */   
/*     */   public Class<? extends LoadBalancingPolicy> getLoadBalancingPolicy() {
/* 183 */     return this.loadBalancingPolicy;
/*     */   }
/*     */   
/*     */   public void setLoadBalancingPolicy(Class<? extends LoadBalancingPolicy> loadBalancingPolicy)
/*     */   {
/* 188 */     this.loadBalancingPolicy = loadBalancingPolicy;
/*     */   }
/*     */   
/*     */   public ConsistencyLevel getConsistencyLevel() {
/* 192 */     return this.consistencyLevel;
/*     */   }
/*     */   
/*     */   public void setConsistencyLevel(ConsistencyLevel consistency) {
/* 196 */     this.consistencyLevel = consistency;
/*     */   }
/*     */   
/*     */   public ConsistencyLevel getSerialConsistencyLevel() {
/* 200 */     return this.serialConsistencyLevel;
/*     */   }
/*     */   
/*     */   public void setSerialConsistencyLevel(ConsistencyLevel serialConsistency) {
/* 204 */     this.serialConsistencyLevel = serialConsistency;
/*     */   }
/*     */   
/*     */   public int getFetchSize() {
/* 208 */     return this.fetchSize;
/*     */   }
/*     */   
/*     */   public void setFetchSize(int fetchSize) {
/* 212 */     this.fetchSize = fetchSize;
/*     */   }
/*     */   
/*     */   public Class<? extends ReconnectionPolicy> getReconnectionPolicy() {
/* 216 */     return this.reconnectionPolicy;
/*     */   }
/*     */   
/*     */   public void setReconnectionPolicy(Class<? extends ReconnectionPolicy> reconnectionPolicy)
/*     */   {
/* 221 */     this.reconnectionPolicy = reconnectionPolicy;
/*     */   }
/*     */   
/*     */   public Class<? extends RetryPolicy> getRetryPolicy() {
/* 225 */     return this.retryPolicy;
/*     */   }
/*     */   
/*     */   public void setRetryPolicy(Class<? extends RetryPolicy> retryPolicy) {
/* 229 */     this.retryPolicy = retryPolicy;
/*     */   }
/*     */   
/*     */   public int getConnectTimeoutMillis() {
/* 233 */     return this.connectTimeoutMillis;
/*     */   }
/*     */   
/*     */   public void setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 237 */     this.connectTimeoutMillis = connectTimeoutMillis;
/*     */   }
/*     */   
/*     */   public int getReadTimeoutMillis() {
/* 241 */     return this.readTimeoutMillis;
/*     */   }
/*     */   
/*     */   public void setReadTimeoutMillis(int readTimeoutMillis) {
/* 245 */     this.readTimeoutMillis = readTimeoutMillis;
/*     */   }
/*     */   
/*     */   public boolean isSsl() {
/* 249 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public void setSsl(boolean ssl) {
/* 253 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */   public String getSchemaAction() {
/* 257 */     return this.schemaAction;
/*     */   }
/*     */   
/*     */   public void setSchemaAction(String schemaAction) {
/* 261 */     this.schemaAction = schemaAction;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cassandra\CassandraProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */