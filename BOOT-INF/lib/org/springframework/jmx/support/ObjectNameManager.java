/*    */ package org.springframework.jmx.support;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
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
/*    */ 
/*    */ public class ObjectNameManager
/*    */ {
/*    */   public static ObjectName getInstance(Object objectName)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 43 */     if ((objectName instanceof ObjectName)) {
/* 44 */       return (ObjectName)objectName;
/*    */     }
/* 46 */     if (!(objectName instanceof String))
/*    */     {
/* 48 */       throw new MalformedObjectNameException("Invalid ObjectName value type [" + objectName.getClass().getName() + "]: only ObjectName and String supported.");
/*    */     }
/* 50 */     return getInstance((String)objectName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ObjectName getInstance(String objectName)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 62 */     return ObjectName.getInstance(objectName);
/*    */   }
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
/*    */   public static ObjectName getInstance(String domainName, String key, String value)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 79 */     return ObjectName.getInstance(domainName, key, value);
/*    */   }
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
/*    */   public static ObjectName getInstance(String domainName, Hashtable<String, String> properties)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 95 */     return ObjectName.getInstance(domainName, properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\ObjectNameManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */