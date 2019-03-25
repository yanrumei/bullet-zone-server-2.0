/*    */ package org.apache.catalina.session;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.apache.catalina.valves.CrawlerSessionManagerValve;
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
/*    */ public class Constants
/*    */ {
/*    */   public static final Set<String> excludedAttributeNames;
/*    */   
/*    */   static
/*    */   {
/* 43 */     Set<String> names = new HashSet();
/* 44 */     names.add("javax.security.auth.subject");
/* 45 */     names.add("org.apache.catalina.realm.GSS_CREDENTIAL");
/* 46 */     names.add(CrawlerSessionManagerValve.class.getName());
/* 47 */     excludedAttributeNames = Collections.unmodifiableSet(names);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */