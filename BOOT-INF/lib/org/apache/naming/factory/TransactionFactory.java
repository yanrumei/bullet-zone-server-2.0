/*    */ package org.apache.naming.factory;
/*    */ 
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import org.apache.naming.TransactionRef;
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
/*    */ public class TransactionFactory
/*    */   extends FactoryBase
/*    */ {
/*    */   protected boolean isReferenceTypeSupported(Object obj)
/*    */   {
/* 33 */     return obj instanceof TransactionRef;
/*    */   }
/*    */   
/*    */ 
/*    */   protected ObjectFactory getDefaultFactory(Reference ref)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Object getLinked(Reference ref)
/*    */   {
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\TransactionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */