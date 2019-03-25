/*    */ package org.apache.naming.factory;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import org.apache.naming.ResourceRef;
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
/*    */ public class ResourceFactory
/*    */   extends FactoryBase
/*    */ {
/*    */   protected boolean isReferenceTypeSupported(Object obj)
/*    */   {
/* 34 */     return obj instanceof ResourceRef;
/*    */   }
/*    */   
/*    */   protected ObjectFactory getDefaultFactory(Reference ref)
/*    */     throws NamingException
/*    */   {
/* 40 */     ObjectFactory factory = null;
/*    */     
/* 42 */     if (ref.getClassName().equals("javax.sql.DataSource"))
/*    */     {
/* 44 */       String javaxSqlDataSourceFactoryClassName = System.getProperty("javax.sql.DataSource.Factory", "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory");
/*    */       
/*    */       try
/*    */       {
/* 48 */         factory = (ObjectFactory)Class.forName(javaxSqlDataSourceFactoryClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */       } catch (Exception e) {
/* 50 */         NamingException ex = new NamingException("Could not create resource factory instance");
/*    */         
/* 52 */         ex.initCause(e);
/* 53 */         throw ex;
/*    */       }
/* 55 */     } else if (ref.getClassName().equals("javax.mail.Session"))
/*    */     {
/* 57 */       String javaxMailSessionFactoryClassName = System.getProperty("javax.mail.Session.Factory", "org.apache.naming.factory.MailSessionFactory");
/*    */       
/*    */       try
/*    */       {
/* 61 */         factory = (ObjectFactory)Class.forName(javaxMailSessionFactoryClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */       } catch (Throwable t) {
/* 63 */         if ((t instanceof NamingException)) {
/* 64 */           throw ((NamingException)t);
/*    */         }
/* 66 */         if ((t instanceof ThreadDeath)) {
/* 67 */           throw ((ThreadDeath)t);
/*    */         }
/* 69 */         if ((t instanceof VirtualMachineError)) {
/* 70 */           throw ((VirtualMachineError)t);
/*    */         }
/* 72 */         NamingException ex = new NamingException("Could not create resource factory instance");
/*    */         
/* 74 */         ex.initCause(t);
/* 75 */         throw ex;
/*    */       }
/*    */     }
/*    */     
/* 79 */     return factory;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Object getLinked(Reference ref)
/*    */   {
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\ResourceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */