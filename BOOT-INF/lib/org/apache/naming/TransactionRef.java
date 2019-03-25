/*    */ package org.apache.naming;
/*    */ 
/*    */ import javax.naming.Reference;
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
/*    */ public class TransactionRef
/*    */   extends Reference
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.TransactionFactory";
/*    */   
/*    */   public TransactionRef()
/*    */   {
/* 49 */     this(null, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TransactionRef(String factory, String factoryLocation)
/*    */   {
/* 60 */     super("javax.transaction.UserTransaction", factory, factoryLocation);
/*    */   }
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
/*    */   public String getFactoryClassName()
/*    */   {
/* 76 */     String factory = super.getFactoryClassName();
/* 77 */     if (factory != null) {
/* 78 */       return factory;
/*    */     }
/* 80 */     factory = System.getProperty("java.naming.factory.object");
/* 81 */     if (factory != null) {
/* 82 */       return null;
/*    */     }
/* 84 */     return "org.apache.naming.factory.TransactionFactory";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\TransactionRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */