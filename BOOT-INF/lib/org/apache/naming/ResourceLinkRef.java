/*    */ package org.apache.naming;
/*    */ 
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.StringRefAddr;
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
/*    */ 
/*    */ public class ResourceLinkRef
/*    */   extends Reference
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.ResourceLinkFactory";
/*    */   public static final String GLOBALNAME = "globalName";
/*    */   
/*    */   public ResourceLinkRef(String resourceClass, String globalName, String factory, String factoryLocation)
/*    */   {
/* 57 */     super(resourceClass, factory, factoryLocation);
/* 58 */     StringRefAddr refAddr = null;
/* 59 */     if (globalName != null) {
/* 60 */       refAddr = new StringRefAddr("globalName", globalName);
/* 61 */       add(refAddr);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getFactoryClassName()
/*    */   {
/* 72 */     String factory = super.getFactoryClassName();
/* 73 */     if (factory != null) {
/* 74 */       return factory;
/*    */     }
/* 76 */     factory = System.getProperty("java.naming.factory.object");
/* 77 */     if (factory != null) {
/* 78 */       return null;
/*    */     }
/* 80 */     return "org.apache.naming.factory.ResourceLinkFactory";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ResourceLinkRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */