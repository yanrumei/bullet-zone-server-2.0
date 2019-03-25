/*    */ package org.springframework.boot.autoconfigure.flyway;
/*    */ 
/*    */ import org.flywaydb.core.Flyway;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class FlywayMigrationInitializer
/*    */   implements InitializingBean, Ordered
/*    */ {
/*    */   private final Flyway flyway;
/*    */   private final FlywayMigrationStrategy migrationStrategy;
/* 38 */   private int order = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FlywayMigrationInitializer(Flyway flyway)
/*    */   {
/* 45 */     this(flyway, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FlywayMigrationInitializer(Flyway flyway, FlywayMigrationStrategy migrationStrategy)
/*    */   {
/* 55 */     Assert.notNull(flyway, "Flyway must not be null");
/* 56 */     this.flyway = flyway;
/* 57 */     this.migrationStrategy = migrationStrategy;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws Exception
/*    */   {
/* 62 */     if (this.migrationStrategy != null) {
/* 63 */       this.migrationStrategy.migrate(this.flyway);
/*    */     }
/*    */     else {
/* 66 */       this.flyway.migrate();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 72 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 76 */     this.order = order;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\flyway\FlywayMigrationInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */