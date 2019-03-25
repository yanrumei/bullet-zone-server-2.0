/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JndiLocatorSupport
/*     */   extends JndiAccessor
/*     */ {
/*     */   public static final String CONTAINER_PREFIX = "java:comp/env/";
/*  46 */   private boolean resourceRef = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceRef(boolean resourceRef)
/*     */   {
/*  56 */     this.resourceRef = resourceRef;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isResourceRef()
/*     */   {
/*  63 */     return this.resourceRef;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object lookup(String jndiName)
/*     */     throws NamingException
/*     */   {
/*  77 */     return lookup(jndiName, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <T> T lookup(String jndiName, Class<T> requiredType)
/*     */     throws NamingException
/*     */   {
/*  91 */     Assert.notNull(jndiName, "'jndiName' must not be null");
/*  92 */     String convertedName = convertJndiName(jndiName);
/*     */     try
/*     */     {
/*  95 */       jndiObject = getJndiTemplate().lookup(convertedName, requiredType);
/*     */     } catch (NamingException ex) { T jndiObject;
/*     */       T jndiObject;
/*  98 */       if (!convertedName.equals(jndiName))
/*     */       {
/* 100 */         if (this.logger.isDebugEnabled()) {
/* 101 */           this.logger.debug("Converted JNDI name [" + convertedName + "] not found - trying original name [" + jndiName + "]. " + ex);
/*     */         }
/*     */         
/* 104 */         jndiObject = getJndiTemplate().lookup(jndiName, requiredType);
/*     */       }
/*     */       else {
/* 107 */         throw ex;
/*     */       } }
/*     */     T jndiObject;
/* 110 */     if (this.logger.isDebugEnabled()) {
/* 111 */       this.logger.debug("Located object with JNDI name [" + convertedName + "]");
/*     */     }
/* 113 */     return jndiObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String convertJndiName(String jndiName)
/*     */   {
/* 127 */     if ((isResourceRef()) && (!jndiName.startsWith("java:comp/env/")) && (jndiName.indexOf(':') == -1)) {
/* 128 */       jndiName = "java:comp/env/" + jndiName;
/*     */     }
/* 130 */     return jndiName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiLocatorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */