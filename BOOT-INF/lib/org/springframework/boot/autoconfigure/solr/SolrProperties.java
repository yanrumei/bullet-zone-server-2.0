/*    */ package org.springframework.boot.autoconfigure.solr;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*    */ @ConfigurationProperties(prefix="spring.data.solr")
/*    */ public class SolrProperties
/*    */ {
/* 33 */   private String host = "http://127.0.0.1:8983/solr";
/*    */   
/*    */ 
/*    */   private String zkHost;
/*    */   
/*    */ 
/*    */   public String getHost()
/*    */   {
/* 41 */     return this.host;
/*    */   }
/*    */   
/*    */   public void setHost(String host) {
/* 45 */     this.host = host;
/*    */   }
/*    */   
/*    */   public String getZkHost() {
/* 49 */     return this.zkHost;
/*    */   }
/*    */   
/*    */   public void setZkHost(String zkHost) {
/* 53 */     this.zkHost = zkHost;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\solr\SolrProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */