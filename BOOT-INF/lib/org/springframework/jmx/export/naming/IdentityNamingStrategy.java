/*    */ package org.springframework.jmx.export.naming;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
/*    */ import org.springframework.jmx.support.ObjectNameManager;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ public class IdentityNamingStrategy
/*    */   implements ObjectNamingStrategy
/*    */ {
/*    */   public static final String TYPE_KEY = "type";
/*    */   public static final String HASH_CODE_KEY = "hashCode";
/*    */   
/*    */   public ObjectName getObjectName(Object managedBean, String beanKey)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 51 */     String domain = ClassUtils.getPackageName(managedBean.getClass());
/* 52 */     Hashtable<String, String> keys = new Hashtable();
/* 53 */     keys.put("type", ClassUtils.getShortName(managedBean.getClass()));
/* 54 */     keys.put("hashCode", ObjectUtils.getIdentityHexString(managedBean));
/* 55 */     return ObjectNameManager.getInstance(domain, keys);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\naming\IdentityNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */