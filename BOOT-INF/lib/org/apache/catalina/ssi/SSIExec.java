/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintWriter;
/*    */ import org.apache.catalina.util.IOTools;
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
/*    */ public class SSIExec
/*    */   implements SSICommand
/*    */ {
/* 36 */   protected final SSIInclude ssiInclude = new SSIInclude();
/*    */   
/*    */ 
/*    */ 
/*    */   protected static final int BUFFER_SIZE = 1024;
/*    */   
/*    */ 
/*    */ 
/*    */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*    */   {
/* 46 */     long lastModified = 0L;
/* 47 */     String configErrMsg = ssiMediator.getConfigErrMsg();
/* 48 */     String paramName = paramNames[0];
/* 49 */     String paramValue = paramValues[0];
/* 50 */     String substitutedValue = ssiMediator.substituteVariables(paramValue);
/* 51 */     if (paramName.equalsIgnoreCase("cgi")) {
/* 52 */       lastModified = this.ssiInclude.process(ssiMediator, "include", new String[] { "virtual" }, new String[] { substitutedValue }, writer);
/*    */ 
/*    */     }
/* 55 */     else if (paramName.equalsIgnoreCase("cmd")) {
/* 56 */       boolean foundProgram = false;
/*    */       try {
/* 58 */         Runtime rt = Runtime.getRuntime();
/* 59 */         Process proc = rt.exec(substitutedValue);
/* 60 */         foundProgram = true;
/*    */         
/* 62 */         BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
/*    */         
/* 64 */         BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
/* 65 */         char[] buf = new char['Ѐ'];
/* 66 */         IOTools.flow(stdErrReader, writer, buf);
/* 67 */         IOTools.flow(stdOutReader, writer, buf);
/* 68 */         proc.waitFor();
/* 69 */         lastModified = System.currentTimeMillis();
/*    */       } catch (InterruptedException e) {
/* 71 */         ssiMediator.log("Couldn't exec file: " + substitutedValue, e);
/* 72 */         writer.write(configErrMsg);
/*    */       } catch (IOException e) {
/* 74 */         if (!foundProgram) {}
/*    */         
/*    */ 
/*    */ 
/* 78 */         ssiMediator.log("Couldn't exec file: " + substitutedValue, e);
/*    */       }
/*    */     }
/* 81 */     return lastModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIExec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */