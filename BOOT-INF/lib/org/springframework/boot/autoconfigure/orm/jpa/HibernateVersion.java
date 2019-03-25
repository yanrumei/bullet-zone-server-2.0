/*    */ package org.springframework.boot.autoconfigure.orm.jpa;
/*    */ 
/*    */ import org.springframework.util.ClassUtils;
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
/*    */  enum HibernateVersion
/*    */ {
/* 31 */   V4, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 36 */   V5;
/*    */   
/*    */   private static final String HIBERNATE_5_CLASS = "org.hibernate.boot.model.naming.PhysicalNamingStrategy";
/*    */   private static HibernateVersion running;
/*    */   
/*    */   private HibernateVersion() {}
/*    */   
/*    */   public static HibernateVersion getRunning() {
/* 44 */     if (running == null) {
/* 45 */       setRunning(ClassUtils.isPresent("org.hibernate.boot.model.naming.PhysicalNamingStrategy", null) ? V5 : V4);
/*    */     }
/* 47 */     return running;
/*    */   }
/*    */   
/*    */   static void setRunning(HibernateVersion running) {
/* 51 */     running = running;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\HibernateVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */