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
/*    */ public class ResourceEnvRef
/*    */   extends Reference
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.ResourceEnvFactory";
/*    */   
/*    */   public ResourceEnvRef(String resourceType)
/*    */   {
/* 45 */     super(resourceType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getFactoryClassName()
/*    */   {
/* 55 */     String factory = super.getFactoryClassName();
/* 56 */     if (factory != null) {
/* 57 */       return factory;
/*    */     }
/* 59 */     factory = System.getProperty("java.naming.factory.object");
/* 60 */     if (factory != null) {
/* 61 */       return null;
/*    */     }
/* 63 */     return "org.apache.naming.factory.ResourceEnvFactory";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ResourceEnvRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */