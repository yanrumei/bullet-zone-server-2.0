/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Target({java.lang.annotation.ElementType.METHOD})
/*    */ @Retention(RetentionPolicy.CLASS)
/*    */ @Documented
/*    */ @Deprecated
/*    */ public @interface Message
/*    */ {
/*    */   public static final int NONE = 0;
/*    */   public static final int INHERIT = -1;
/*    */   
/*    */   int id() default -1;
/*    */   
/*    */   String value();
/*    */   
/*    */   Format format() default Format.PRINTF;
/*    */   
/*    */   public static enum Format
/*    */   {
/* 81 */     PRINTF, 
/*    */     
/*    */ 
/*    */ 
/* 85 */     MESSAGE_FORMAT, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 90 */     NO_FORMAT;
/*    */     
/*    */     private Format() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */