/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
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
/*    */ public final class SSIInclude
/*    */   implements SSICommand
/*    */ {
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 37 */     long lastModified = 0L;
/* 38 */     String configErrMsg = ssiMediator.getConfigErrMsg();
/* 39 */     for (int i = 0; i < paramNames.length; i++) {
/* 40 */       String paramName = paramNames[i];
/* 41 */       String paramValue = paramValues[i];
/*    */       
/* 43 */       String substitutedValue = ssiMediator.substituteVariables(paramValue);
/*    */       try {
/* 45 */         if ((paramName.equalsIgnoreCase("file")) || 
/* 46 */           (paramName.equalsIgnoreCase("virtual"))) {
/* 47 */           boolean virtual = paramName.equalsIgnoreCase("virtual");
/* 48 */           lastModified = ssiMediator.getFileLastModified(substitutedValue, virtual);
/*    */           
/* 50 */           String text = ssiMediator.getFileText(substitutedValue, virtual);
/*    */           
/* 52 */           writer.write(text);
/*    */         } else {
/* 54 */           ssiMediator.log("#include--Invalid attribute: " + paramName);
/*    */           
/* 56 */           writer.write(configErrMsg);
/*    */         }
/*    */       } catch (IOException e) {
/* 59 */         ssiMediator.log("#include--Couldn't include file: " + substitutedValue, e);
/*    */         
/* 61 */         writer.write(configErrMsg);
/*    */       }
/*    */     }
/* 64 */     return lastModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIInclude.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */