/*    */ package org.apache.naming.factory;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.RefAddr;
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import org.apache.naming.EjbRef;
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
/*    */ public class EjbFactory
/*    */   extends FactoryBase
/*    */ {
/*    */   protected boolean isReferenceTypeSupported(Object obj)
/*    */   {
/* 36 */     return obj instanceof EjbRef;
/*    */   }
/*    */   
/*    */ 
/*    */   protected ObjectFactory getDefaultFactory(Reference ref)
/*    */     throws NamingException
/*    */   {
/* 43 */     String javaxEjbFactoryClassName = System.getProperty("javax.ejb.Factory", "org.apache.naming.factory.OpenEjbFactory");
/*    */     
/*    */     try
/*    */     {
/* 47 */       factory = (ObjectFactory)Class.forName(javaxEjbFactoryClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */     } catch (Throwable t) { ObjectFactory factory;
/* 49 */       if ((t instanceof NamingException)) {
/* 50 */         throw ((NamingException)t);
/*    */       }
/* 52 */       if ((t instanceof ThreadDeath)) {
/* 53 */         throw ((ThreadDeath)t);
/*    */       }
/* 55 */       if ((t instanceof VirtualMachineError)) {
/* 56 */         throw ((VirtualMachineError)t);
/*    */       }
/* 58 */       NamingException ex = new NamingException("Could not create resource factory instance");
/*    */       
/* 60 */       ex.initCause(t);
/* 61 */       throw ex; }
/*    */     ObjectFactory factory;
/* 63 */     return factory;
/*    */   }
/*    */   
/*    */   protected Object getLinked(Reference ref)
/*    */     throws NamingException
/*    */   {
/* 69 */     RefAddr linkRefAddr = ref.get("link");
/* 70 */     if (linkRefAddr != null)
/*    */     {
/* 72 */       String ejbLink = linkRefAddr.getContent().toString();
/* 73 */       Object beanObj = new InitialContext().lookup(ejbLink);
/* 74 */       return beanObj;
/*    */     }
/* 76 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\EjbFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */