/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailSessionFactory
/*     */   implements ObjectFactory
/*     */ {
/*     */   protected static final String factoryType = "javax.mail.Session";
/*     */   
/*     */   public Object getObjectInstance(Object refObj, Name name, Context context, Hashtable<?, ?> env)
/*     */     throws Exception
/*     */   {
/*  95 */     final Reference ref = (Reference)refObj;
/*  96 */     if (!ref.getClassName().equals("javax.mail.Session")) {
/*  97 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */ 
/*     */       public Session run()
/*     */       {
/* 109 */         Properties props = new Properties();
/* 110 */         props.put("mail.transport.protocol", "smtp");
/* 111 */         props.put("mail.smtp.host", "localhost");
/*     */         
/* 113 */         String password = null;
/*     */         
/* 115 */         Enumeration<RefAddr> attrs = ref.getAll();
/* 116 */         while (attrs.hasMoreElements()) {
/* 117 */           RefAddr attr = (RefAddr)attrs.nextElement();
/* 118 */           if (!"factory".equals(attr.getType()))
/*     */           {
/*     */ 
/*     */ 
/* 122 */             if ("password".equals(attr.getType())) {
/* 123 */               password = (String)attr.getContent();
/*     */             }
/*     */             else
/*     */             {
/* 127 */               props.put(attr.getType(), attr.getContent()); }
/*     */           }
/*     */         }
/* 130 */         Authenticator auth = null;
/* 131 */         if (password != null) {
/* 132 */           String user = props.getProperty("mail.smtp.user");
/* 133 */           if (user == null) {
/* 134 */             user = props.getProperty("mail.user");
/*     */           }
/*     */           
/* 137 */           if (user != null) {
/* 138 */             final PasswordAuthentication pa = new PasswordAuthentication(user, password);
/* 139 */             auth = new Authenticator()
/*     */             {
/*     */               protected PasswordAuthentication getPasswordAuthentication() {
/* 142 */                 return pa;
/*     */               }
/*     */             };
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 149 */         Session session = Session.getInstance(props, auth);
/* 150 */         return session;
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\MailSessionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */