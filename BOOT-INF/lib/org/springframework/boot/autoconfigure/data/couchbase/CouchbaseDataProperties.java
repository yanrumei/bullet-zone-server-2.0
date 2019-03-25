/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.data.couchbase.core.query.Consistency;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.data.couchbase")
/*    */ public class CouchbaseDataProperties
/*    */ {
/*    */   private boolean autoIndex;
/* 40 */   private Consistency consistency = Consistency.READ_YOUR_OWN_WRITES;
/*    */   
/*    */   public boolean isAutoIndex() {
/* 43 */     return this.autoIndex;
/*    */   }
/*    */   
/*    */   public void setAutoIndex(boolean autoIndex) {
/* 47 */     this.autoIndex = autoIndex;
/*    */   }
/*    */   
/*    */   public Consistency getConsistency() {
/* 51 */     return this.consistency;
/*    */   }
/*    */   
/*    */   public void setConsistency(Consistency consistency) {
/* 55 */     this.consistency = consistency;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\CouchbaseDataProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */