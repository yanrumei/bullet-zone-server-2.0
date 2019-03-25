/*    */ package org.apache.catalina.loader;
/*    */ 
/*    */ import java.sql.Driver;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.SQLException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
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
/*    */ public class JdbcLeakPrevention
/*    */ {
/*    */   public List<String> clearJdbcDriverRegistrations()
/*    */     throws SQLException
/*    */   {
/* 44 */     List<String> driverNames = new ArrayList();
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
/* 55 */     HashSet<Driver> originalDrivers = new HashSet();
/* 56 */     Enumeration<Driver> drivers = DriverManager.getDrivers();
/* 57 */     while (drivers.hasMoreElements()) {
/* 58 */       originalDrivers.add(drivers.nextElement());
/*    */     }
/* 60 */     drivers = DriverManager.getDrivers();
/* 61 */     while (drivers.hasMoreElements()) {
/* 62 */       Driver driver = (Driver)drivers.nextElement();
/*    */       
/*    */ 
/* 65 */       if (driver.getClass().getClassLoader() == getClass().getClassLoader())
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 70 */         if (originalDrivers.contains(driver)) {
/* 71 */           driverNames.add(driver.getClass().getCanonicalName());
/*    */         }
/* 73 */         DriverManager.deregisterDriver(driver);
/*    */       } }
/* 75 */     return driverNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\loader\JdbcLeakPrevention.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */