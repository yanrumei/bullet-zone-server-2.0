/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ public class SSIPrintenv
/*    */   implements SSICommand
/*    */ {
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 36 */     long lastModified = 0L;
/*    */     
/* 38 */     if (paramNames.length > 0) {
/* 39 */       String errorMessage = ssiMediator.getConfigErrMsg();
/* 40 */       writer.write(errorMessage);
/*    */     } else {
/* 42 */       Collection<String> variableNames = ssiMediator.getVariableNames();
/* 43 */       Iterator<String> iter = variableNames.iterator();
/* 44 */       while (iter.hasNext()) {
/* 45 */         String variableName = (String)iter.next();
/*    */         
/* 47 */         String variableValue = ssiMediator.getVariableValue(variableName);
/*    */         
/*    */ 
/* 50 */         if (variableValue == null) {
/* 51 */           variableValue = "(none)";
/*    */         }
/* 53 */         writer.write(variableName);
/* 54 */         writer.write(61);
/* 55 */         writer.write(variableValue);
/* 56 */         writer.write(10);
/* 57 */         lastModified = System.currentTimeMillis();
/*    */       }
/*    */     }
/* 60 */     return lastModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIPrintenv.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */