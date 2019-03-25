/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.Collections;
/*    */ import java.util.Enumeration;
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
/*    */ public final class GetResources
/*    */   implements PrivilegedAction<Enumeration<URL>>
/*    */ {
/*    */   private final String resourceName;
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public static GetResources action(ClassLoader classLoader, String resourceName)
/*    */   {
/* 28 */     return new GetResources(classLoader, resourceName);
/*    */   }
/*    */   
/*    */   private GetResources(ClassLoader classLoader, String resourceName) {
/* 32 */     this.classLoader = classLoader;
/* 33 */     this.resourceName = resourceName;
/*    */   }
/*    */   
/*    */   public Enumeration<URL> run()
/*    */   {
/*    */     try {
/* 39 */       return this.classLoader.getResources(this.resourceName);
/*    */     }
/*    */     catch (IOException e) {}
/*    */     
/* 43 */     return Collections.enumeration(Collections.emptyList());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetResources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */