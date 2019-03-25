/*    */ package org.springframework.boot.orm.jpa.hibernate;
/*    */ 
/*    */ import org.hibernate.cfg.ImprovedNamingStrategy;
/*    */ import org.hibernate.internal.util.StringHelper;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class SpringNamingStrategy
/*    */   extends ImprovedNamingStrategy
/*    */ {
/*    */   public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName)
/*    */   {
/* 41 */     String name = propertyTableName;
/* 42 */     if (propertyName != null) {
/* 43 */       name = StringHelper.unqualify(propertyName);
/*    */     }
/* 45 */     Assert.state(StringUtils.hasLength(name), "Unable to generate foreignKeyColumnName");
/*    */     
/* 47 */     return columnName(name) + "_" + referencedColumnName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\orm\jpa\hibernate\SpringNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */