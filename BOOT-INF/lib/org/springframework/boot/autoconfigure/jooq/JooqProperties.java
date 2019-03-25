/*    */ package org.springframework.boot.autoconfigure.jooq;
/*    */ 
/*    */ import org.jooq.SQLDialect;
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
/*    */ @ConfigurationProperties(prefix="spring.jooq")
/*    */ public class JooqProperties
/*    */ {
/*    */   private SQLDialect sqlDialect;
/*    */   
/*    */   public SQLDialect getSqlDialect()
/*    */   {
/* 39 */     return this.sqlDialect;
/*    */   }
/*    */   
/*    */   public void setSqlDialect(SQLDialect sqlDialect) {
/* 43 */     this.sqlDialect = sqlDialect;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jooq\JooqProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */