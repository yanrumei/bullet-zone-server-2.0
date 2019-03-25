/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import com.couchbase.client.java.Bucket;
/*    */ import com.couchbase.client.java.Cluster;
/*    */ import com.couchbase.client.java.cluster.ClusterInfo;
/*    */ import com.couchbase.client.java.env.CouchbaseEnvironment;
/*    */ import org.springframework.data.couchbase.config.CouchbaseConfigurer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpringBootCouchbaseConfigurer
/*    */   implements CouchbaseConfigurer
/*    */ {
/*    */   private final CouchbaseEnvironment env;
/*    */   private final Cluster cluster;
/*    */   private final ClusterInfo clusterInfo;
/*    */   private final Bucket bucket;
/*    */   
/*    */   public SpringBootCouchbaseConfigurer(CouchbaseEnvironment env, Cluster cluster, ClusterInfo clusterInfo, Bucket bucket)
/*    */   {
/* 44 */     this.env = env;
/* 45 */     this.cluster = cluster;
/* 46 */     this.clusterInfo = clusterInfo;
/* 47 */     this.bucket = bucket;
/*    */   }
/*    */   
/*    */   public CouchbaseEnvironment couchbaseEnvironment() throws Exception
/*    */   {
/* 52 */     return this.env;
/*    */   }
/*    */   
/*    */   public Cluster couchbaseCluster() throws Exception
/*    */   {
/* 57 */     return this.cluster;
/*    */   }
/*    */   
/*    */   public ClusterInfo couchbaseClusterInfo() throws Exception
/*    */   {
/* 62 */     return this.clusterInfo;
/*    */   }
/*    */   
/*    */   public Bucket couchbaseClient() throws Exception
/*    */   {
/* 67 */     return this.bucket;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\SpringBootCouchbaseConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */