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
/*    */ public class SSISet
/*    */   implements SSICommand
/*    */ {
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */     throws SSIStopProcessingException
/*    */   {
/* 36 */     long lastModified = 0L;
/* 37 */     String errorMessage = ssiMediator.getConfigErrMsg();
/* 38 */     String variableName = null;
/* 39 */     for (int i = 0; i < paramNames.length; i++) {
/* 40 */       String paramName = paramNames[i];
/* 41 */       String paramValue = paramValues[i];
/* 42 */       if (paramName.equalsIgnoreCase("var")) {
/* 43 */         variableName = paramValue;
/* 44 */       } else if (paramName.equalsIgnoreCase("value")) {
/* 45 */         if (variableName != null)
/*    */         {
/* 47 */           String substitutedValue = ssiMediator.substituteVariables(paramValue);
/* 48 */           ssiMediator.setVariableValue(variableName, substitutedValue);
/*    */           
/* 50 */           lastModified = System.currentTimeMillis();
/*    */         } else {
/* 52 */           ssiMediator.log("#set--no variable specified");
/* 53 */           writer.write(errorMessage);
/* 54 */           throw new SSIStopProcessingException();
/*    */         }
/*    */       } else {
/* 57 */         ssiMediator.log("#set--Invalid attribute: " + paramName);
/* 58 */         writer.write(errorMessage);
/* 59 */         throw new SSIStopProcessingException();
/*    */       }
/*    */     }
/* 62 */     return lastModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSISet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */