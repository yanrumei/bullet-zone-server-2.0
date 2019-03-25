/*     */ package org.apache.catalina.users;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryUserDatabaseFactory
/*     */   implements ObjectFactory
/*     */ {
/*     */   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*     */     throws Exception
/*     */   {
/*  78 */     if ((obj == null) || (!(obj instanceof Reference))) {
/*  79 */       return null;
/*     */     }
/*  81 */     Reference ref = (Reference)obj;
/*  82 */     if (!"org.apache.catalina.UserDatabase".equals(ref.getClassName())) {
/*  83 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  88 */     MemoryUserDatabase database = new MemoryUserDatabase(name.toString());
/*  89 */     RefAddr ra = null;
/*     */     
/*  91 */     ra = ref.get("pathname");
/*  92 */     if (ra != null) {
/*  93 */       database.setPathname(ra.getContent().toString());
/*     */     }
/*     */     
/*  96 */     ra = ref.get("readonly");
/*  97 */     if (ra != null) {
/*  98 */       database.setReadonly(Boolean.parseBoolean(ra.getContent().toString()));
/*     */     }
/*     */     
/*     */ 
/* 102 */     database.open();
/*     */     
/* 104 */     if (!database.getReadonly())
/* 105 */       database.save();
/* 106 */     return database;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\users\MemoryUserDatabaseFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */