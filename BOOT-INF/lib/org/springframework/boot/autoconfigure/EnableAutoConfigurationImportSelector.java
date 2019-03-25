/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ @Deprecated
/*    */ public class EnableAutoConfigurationImportSelector
/*    */   extends AutoConfigurationImportSelector
/*    */ {
/*    */   protected boolean isEnabled(AnnotationMetadata metadata)
/*    */   {
/* 41 */     if (getClass().equals(EnableAutoConfigurationImportSelector.class)) {
/* 42 */       return ((Boolean)getEnvironment().getProperty("spring.boot.enableautoconfiguration", Boolean.class, 
/*    */       
/* 44 */         Boolean.valueOf(true))).booleanValue();
/*    */     }
/*    */     
/*    */ 
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\EnableAutoConfigurationImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */