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
/*    */ 
/*    */ public class SSIEcho
/*    */   implements SSICommand
/*    */ {
/*    */   protected static final String DEFAULT_ENCODING = "entity";
/*    */   protected static final String MISSING_VARIABLE_VALUE = "(none)";
/*    */   
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 40 */     String encoding = "entity";
/* 41 */     String originalValue = null;
/* 42 */     String errorMessage = ssiMediator.getConfigErrMsg();
/* 43 */     for (int i = 0; i < paramNames.length; i++) {
/* 44 */       String paramName = paramNames[i];
/* 45 */       String paramValue = paramValues[i];
/* 46 */       if (paramName.equalsIgnoreCase("var")) {
/* 47 */         originalValue = paramValue;
/* 48 */       } else if (paramName.equalsIgnoreCase("encoding")) {
/* 49 */         if (isValidEncoding(paramValue)) {
/* 50 */           encoding = paramValue;
/*    */         } else {
/* 52 */           ssiMediator.log("#echo--Invalid encoding: " + paramValue);
/* 53 */           writer.write(errorMessage);
/*    */         }
/*    */       } else {
/* 56 */         ssiMediator.log("#echo--Invalid attribute: " + paramName);
/* 57 */         writer.write(errorMessage);
/*    */       }
/*    */     }
/* 60 */     String variableValue = ssiMediator.getVariableValue(originalValue, encoding);
/*    */     
/* 62 */     if (variableValue == null) {
/* 63 */       variableValue = "(none)";
/*    */     }
/* 65 */     writer.write(variableValue);
/* 66 */     return System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   protected boolean isValidEncoding(String encoding)
/*    */   {
/* 71 */     return (encoding.equalsIgnoreCase("url")) || 
/* 72 */       (encoding.equalsIgnoreCase("entity")) || 
/* 73 */       (encoding.equalsIgnoreCase("none"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIEcho.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */