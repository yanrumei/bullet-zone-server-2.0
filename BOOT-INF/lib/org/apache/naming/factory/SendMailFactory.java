/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimePartDataSource;
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
/*     */ public class SendMailFactory
/*     */   implements ObjectFactory
/*     */ {
/*     */   protected static final String DataSourceClassName = "javax.mail.internet.MimePartDataSource";
/*     */   
/*     */   public Object getObjectInstance(Object refObj, Name name, Context ctx, Hashtable<?, ?> env)
/*     */     throws Exception
/*     */   {
/*  84 */     final Reference ref = (Reference)refObj;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  89 */     if (ref.getClassName().equals("javax.mail.internet.MimePartDataSource")) {
/*  90 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */ 
/*     */         public MimePartDataSource run()
/*     */         {
/*     */ 
/*  96 */           Properties props = new Properties();
/*     */           
/*  98 */           Enumeration<RefAddr> list = ref.getAll();
/*     */           
/*     */ 
/*     */ 
/* 102 */           props.put("mail.transport.protocol", "smtp");
/*     */           
/* 104 */           while (list.hasMoreElements()) {
/* 105 */             RefAddr refaddr = (RefAddr)list.nextElement();
/*     */             
/*     */ 
/* 108 */             props.put(refaddr.getType(), refaddr.getContent());
/*     */           }
/*     */           
/* 111 */           MimeMessage message = new MimeMessage(Session.getInstance(props));
/*     */           try {
/* 113 */             RefAddr fromAddr = ref.get("mail.from");
/* 114 */             String from = null;
/* 115 */             if (fromAddr != null) {
/* 116 */               from = (String)ref.get("mail.from").getContent();
/*     */             }
/* 118 */             if (from != null) {
/* 119 */               message.setFrom(new InternetAddress(from));
/*     */             }
/* 121 */             message.setSubject("");
/*     */           } catch (Exception localException) {}
/* 123 */           MimePartDataSource mds = new MimePartDataSource(message);
/* 124 */           return mds;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 129 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\SendMailFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */