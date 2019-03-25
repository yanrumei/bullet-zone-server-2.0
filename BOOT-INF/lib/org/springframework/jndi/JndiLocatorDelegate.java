/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.core.SpringProperties;
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
/*    */ public class JndiLocatorDelegate
/*    */   extends JndiLocatorSupport
/*    */ {
/*    */   public static final String IGNORE_JNDI_PROPERTY_NAME = "spring.jndi.ignore";
/* 53 */   private static final boolean shouldIgnoreDefaultJndiEnvironment = SpringProperties.getFlag("spring.jndi.ignore");
/*    */   
/*    */   public Object lookup(String jndiName)
/*    */     throws NamingException
/*    */   {
/* 58 */     return super.lookup(jndiName);
/*    */   }
/*    */   
/*    */   public <T> T lookup(String jndiName, Class<T> requiredType) throws NamingException
/*    */   {
/* 63 */     return (T)super.lookup(jndiName, requiredType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static JndiLocatorDelegate createDefaultResourceRefLocator()
/*    */   {
/* 73 */     JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/* 74 */     jndiLocator.setResourceRef(true);
/* 75 */     return jndiLocator;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isDefaultJndiEnvironmentAvailable()
/*    */   {
/* 85 */     if (shouldIgnoreDefaultJndiEnvironment) {
/* 86 */       return false;
/*    */     }
/*    */     try {
/* 89 */       new InitialContext().getEnvironment();
/* 90 */       return true;
/*    */     }
/*    */     catch (Throwable ex) {}
/* 93 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiLocatorDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */