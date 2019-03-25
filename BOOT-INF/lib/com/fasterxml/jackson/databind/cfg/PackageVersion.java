/*    */ package com.fasterxml.jackson.databind.cfg;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.core.Versioned;
/*    */ import com.fasterxml.jackson.core.util.VersionUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PackageVersion
/*    */   implements Versioned
/*    */ {
/* 13 */   public static final Version VERSION = VersionUtil.parseVersion("2.8.10", "com.fasterxml.jackson.core", "jackson-databind");
/*    */   
/*    */ 
/*    */   public Version version()
/*    */   {
/* 18 */     return VERSION;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\PackageVersion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */