/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.context.annotation.ImportSelector;
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
/*    */ @Configuration
/*    */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*    */ @Deprecated
/*    */ @Import({Selector.class})
/*    */ public class MessageSourceAutoConfiguration
/*    */ {
/* 41 */   private static final String[] REPLACEMENT = { "org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration" };
/*    */   
/*    */ 
/*    */   static class Selector
/*    */     implements ImportSelector
/*    */   {
/*    */     public String[] selectImports(AnnotationMetadata importingClassMetadata)
/*    */     {
/* 49 */       return MessageSourceAutoConfiguration.REPLACEMENT;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\MessageSourceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */