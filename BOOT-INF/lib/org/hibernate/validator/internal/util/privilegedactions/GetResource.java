/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.security.PrivilegedAction;
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
/*    */ public final class GetResource
/*    */   implements PrivilegedAction<URL>
/*    */ {
/*    */   private final String resourceName;
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public static GetResource action(ClassLoader classLoader, String resourceName)
/*    */   {
/* 25 */     return new GetResource(classLoader, resourceName);
/*    */   }
/*    */   
/*    */   private GetResource(ClassLoader classLoader, String resourceName) {
/* 29 */     this.classLoader = classLoader;
/* 30 */     this.resourceName = resourceName;
/*    */   }
/*    */   
/*    */   public URL run()
/*    */   {
/* 35 */     return this.classLoader.getResource(this.resourceName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */