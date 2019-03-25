/*    */ package org.springframework.boot.orm.jpa.hibernate;
/*    */ 
/*    */ import org.hibernate.boot.model.naming.Identifier;
/*    */ import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
/*    */ import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
/*    */ import org.hibernate.boot.model.source.spi.AttributePath;
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
/*    */ public class SpringImplicitNamingStrategy
/*    */   extends ImplicitNamingStrategyJpaCompliantImpl
/*    */ {
/*    */   public Identifier determineJoinTableName(ImplicitJoinTableNameSource source)
/*    */   {
/* 39 */     String name = source.getOwningPhysicalTableName() + "_" + source.getAssociationOwningAttributePath().getProperty();
/* 40 */     return toIdentifier(name, source.getBuildingContext());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\orm\jpa\hibernate\SpringImplicitNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */