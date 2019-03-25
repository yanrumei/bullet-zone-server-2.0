/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.env.PropertySource;
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
/*     */ public class JndiPropertySource
/*     */   extends PropertySource<JndiLocatorDelegate>
/*     */ {
/*     */   public JndiPropertySource(String name)
/*     */   {
/*  61 */     this(name, JndiLocatorDelegate.createDefaultResourceRefLocator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JndiPropertySource(String name, JndiLocatorDelegate jndiLocator)
/*     */   {
/*  69 */     super(name, jndiLocator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getProperty(String name)
/*     */   {
/*  81 */     if ((((JndiLocatorDelegate)getSource()).isResourceRef()) && (name.indexOf(':') != -1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */       return null;
/*     */     }
/*     */     try
/*     */     {
/*  91 */       Object value = ((JndiLocatorDelegate)this.source).lookup(name);
/*  92 */       if (this.logger.isDebugEnabled()) {
/*  93 */         this.logger.debug("JNDI lookup for name [" + name + "] returned: [" + value + "]");
/*     */       }
/*  95 */       return value;
/*     */     }
/*     */     catch (NamingException ex) {
/*  98 */       if (this.logger.isDebugEnabled())
/*  99 */         this.logger.debug("JNDI lookup for name [" + name + "] threw NamingException with message: " + ex
/* 100 */           .getMessage() + ". Returning null.");
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */