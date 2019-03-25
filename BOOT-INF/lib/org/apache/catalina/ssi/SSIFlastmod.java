/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import org.apache.catalina.util.Strftime;
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
/*    */ public final class SSIFlastmod
/*    */   implements SSICommand
/*    */ {
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 41 */     long lastModified = 0L;
/* 42 */     String configErrMsg = ssiMediator.getConfigErrMsg();
/* 43 */     for (int i = 0; i < paramNames.length; i++) {
/* 44 */       String paramName = paramNames[i];
/* 45 */       String paramValue = paramValues[i];
/*    */       
/* 47 */       String substitutedValue = ssiMediator.substituteVariables(paramValue);
/*    */       try {
/* 49 */         if ((paramName.equalsIgnoreCase("file")) || 
/* 50 */           (paramName.equalsIgnoreCase("virtual"))) {
/* 51 */           boolean virtual = paramName.equalsIgnoreCase("virtual");
/* 52 */           lastModified = ssiMediator.getFileLastModified(substitutedValue, virtual);
/*    */           
/* 54 */           Date date = new Date(lastModified);
/* 55 */           String configTimeFmt = ssiMediator.getConfigTimeFmt();
/* 56 */           writer.write(formatDate(date, configTimeFmt));
/*    */         } else {
/* 58 */           ssiMediator.log("#flastmod--Invalid attribute: " + paramName);
/*    */           
/* 60 */           writer.write(configErrMsg);
/*    */         }
/*    */       } catch (IOException e) {
/* 63 */         ssiMediator.log("#flastmod--Couldn't get last modified for file: " + substitutedValue, e);
/*    */         
/*    */ 
/* 66 */         writer.write(configErrMsg);
/*    */       }
/*    */     }
/* 69 */     return lastModified;
/*    */   }
/*    */   
/*    */   protected String formatDate(Date date, String configTimeFmt)
/*    */   {
/* 74 */     Strftime strftime = new Strftime(configTimeFmt, Locale.US);
/* 75 */     return strftime.format(date);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIFlastmod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */