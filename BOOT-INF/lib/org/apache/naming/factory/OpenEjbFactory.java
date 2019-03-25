/*    */ package org.apache.naming.factory;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Properties;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.Name;
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
/*    */ public class OpenEjbFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   protected static final String DEFAULT_OPENEJB_FACTORY = "org.openejb.client.LocalInitialContextFactory";
/*    */   
/*    */   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*    */     throws Exception
/*    */   {
/* 62 */     Object beanObj = null;
/*    */     
/* 64 */     if ((obj instanceof EjbRef))
/*    */     {
/* 66 */       Reference ref = (Reference)obj;
/*    */       
/* 68 */       String factory = "org.openejb.client.LocalInitialContextFactory";
/* 69 */       RefAddr factoryRefAddr = ref.get("openejb.factory");
/* 70 */       if (factoryRefAddr != null)
/*    */       {
/* 72 */         factory = factoryRefAddr.getContent().toString();
/*    */       }
/*    */       
/* 75 */       Properties env = new Properties();
/* 76 */       env.put("java.naming.factory.initial", factory);
/*    */       
/* 78 */       RefAddr linkRefAddr = ref.get("openejb.link");
/* 79 */       if (linkRefAddr != null) {
/* 80 */         String ejbLink = linkRefAddr.getContent().toString();
/* 81 */         beanObj = new InitialContext(env).lookup(ejbLink);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 86 */     return beanObj;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\OpenEjbFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */