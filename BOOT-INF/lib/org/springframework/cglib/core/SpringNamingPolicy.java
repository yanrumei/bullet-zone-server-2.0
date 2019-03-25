/*    */ package org.springframework.cglib.core;
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
/*    */ public class SpringNamingPolicy
/*    */   extends DefaultNamingPolicy
/*    */ {
/* 32 */   public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();
/*    */   
/*    */   protected String getTag()
/*    */   {
/* 36 */     return "BySpringCGLIB";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\SpringNamingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */