/*     */ package org.apache.naming;
/*     */ 
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EjbRef
/*     */   extends Reference
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.EjbFactory";
/*     */   public static final String TYPE = "type";
/*     */   public static final String REMOTE = "remote";
/*     */   public static final String LINK = "link";
/*     */   
/*     */   public EjbRef(String ejbType, String home, String remote, String link)
/*     */   {
/*  73 */     this(ejbType, home, remote, link, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public EjbRef(String ejbType, String home, String remote, String link, String factory, String factoryLocation)
/*     */   {
/*  90 */     super(home, factory, factoryLocation);
/*  91 */     StringRefAddr refAddr = null;
/*  92 */     if (ejbType != null) {
/*  93 */       refAddr = new StringRefAddr("type", ejbType);
/*  94 */       add(refAddr);
/*     */     }
/*  96 */     if (remote != null) {
/*  97 */       refAddr = new StringRefAddr("remote", remote);
/*  98 */       add(refAddr);
/*     */     }
/* 100 */     if (link != null) {
/* 101 */       refAddr = new StringRefAddr("link", link);
/* 102 */       add(refAddr);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFactoryClassName()
/*     */   {
/* 122 */     String factory = super.getFactoryClassName();
/* 123 */     if (factory != null) {
/* 124 */       return factory;
/*     */     }
/* 126 */     factory = System.getProperty("java.naming.factory.object");
/* 127 */     if (factory != null) {
/* 128 */       return null;
/*     */     }
/* 130 */     return "org.apache.naming.factory.EjbFactory";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\EjbRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */