/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*    */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*    */ import java.io.Serializable;
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
/*    */ public abstract class NopAnnotationIntrospector
/*    */   extends AnnotationIntrospector
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 23 */   public static final NopAnnotationIntrospector instance = new NopAnnotationIntrospector()
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public Version version() {
/* 28 */       return PackageVersion.VERSION;
/*    */     }
/*    */   };
/*    */   
/*    */   public Version version()
/*    */   {
/* 34 */     return Version.unknownVersion();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\NopAnnotationIntrospector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */