/*    */ package org.springframework.boot.autoconfigure.data.elasticsearch;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.data.elasticsearch")
/*    */ public class ElasticsearchProperties
/*    */ {
/* 37 */   private String clusterName = "elasticsearch";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String clusterNodes;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 48 */   private Map<String, String> properties = new HashMap();
/*    */   
/*    */   public String getClusterName() {
/* 51 */     return this.clusterName;
/*    */   }
/*    */   
/*    */   public void setClusterName(String clusterName) {
/* 55 */     this.clusterName = clusterName;
/*    */   }
/*    */   
/*    */   public String getClusterNodes() {
/* 59 */     return this.clusterNodes;
/*    */   }
/*    */   
/*    */   public void setClusterNodes(String clusterNodes) {
/* 63 */     this.clusterNodes = clusterNodes;
/*    */   }
/*    */   
/*    */   public Map<String, String> getProperties() {
/* 67 */     return this.properties;
/*    */   }
/*    */   
/*    */   public void setProperties(Map<String, String> properties) {
/* 71 */     this.properties = properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\elasticsearch\ElasticsearchProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */