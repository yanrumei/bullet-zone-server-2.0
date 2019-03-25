/*    */ package org.apache.catalina.ssi;
/*    */ 
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
/*    */ public final class SSIConfig
/*    */   implements SSICommand
/*    */ {
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 36 */     for (int i = 0; i < paramNames.length; i++) {
/* 37 */       String paramName = paramNames[i];
/* 38 */       String paramValue = paramValues[i];
/*    */       
/* 40 */       String substitutedValue = ssiMediator.substituteVariables(paramValue);
/* 41 */       if (paramName.equalsIgnoreCase("errmsg")) {
/* 42 */         ssiMediator.setConfigErrMsg(substitutedValue);
/* 43 */       } else if (paramName.equalsIgnoreCase("sizefmt")) {
/* 44 */         ssiMediator.setConfigSizeFmt(substitutedValue);
/* 45 */       } else if (paramName.equalsIgnoreCase("timefmt")) {
/* 46 */         ssiMediator.setConfigTimeFmt(substitutedValue);
/*    */       } else {
/* 48 */         ssiMediator.log("#config--Invalid attribute: " + paramName);
/*    */         
/*    */ 
/*    */ 
/* 52 */         String configErrMsg = ssiMediator.getConfigErrMsg();
/* 53 */         writer.write(configErrMsg);
/*    */       }
/*    */     }
/*    */     
/* 57 */     return 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */