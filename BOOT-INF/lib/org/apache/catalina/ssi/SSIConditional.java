/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.text.ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSIConditional
/*     */   implements SSICommand
/*     */ {
/*     */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*     */     throws SSIStopProcessingException
/*     */   {
/*  37 */     long lastModified = System.currentTimeMillis();
/*     */     
/*  39 */     SSIConditionalState state = ssiMediator.getConditionalState();
/*  40 */     if ("if".equalsIgnoreCase(commandName))
/*     */     {
/*     */ 
/*  43 */       if (state.processConditionalCommandsOnly) {
/*  44 */         state.nestingCount += 1;
/*  45 */         return lastModified;
/*     */       }
/*  47 */       state.nestingCount = 0;
/*     */       
/*  49 */       if (evaluateArguments(paramNames, paramValues, ssiMediator))
/*     */       {
/*  51 */         state.branchTaken = true;
/*     */       }
/*     */       else {
/*  54 */         state.processConditionalCommandsOnly = true;
/*  55 */         state.branchTaken = false;
/*     */       }
/*  57 */     } else if ("elif".equalsIgnoreCase(commandName))
/*     */     {
/*     */ 
/*  60 */       if (state.nestingCount > 0) { return lastModified;
/*     */       }
/*     */       
/*  63 */       if (state.branchTaken) {
/*  64 */         state.processConditionalCommandsOnly = true;
/*  65 */         return lastModified;
/*     */       }
/*     */       
/*  68 */       if (evaluateArguments(paramNames, paramValues, ssiMediator))
/*     */       {
/*  70 */         state.processConditionalCommandsOnly = false;
/*  71 */         state.branchTaken = true;
/*     */       }
/*     */       else {
/*  74 */         state.processConditionalCommandsOnly = true;
/*  75 */         state.branchTaken = false;
/*     */       }
/*  77 */     } else if ("else".equalsIgnoreCase(commandName))
/*     */     {
/*     */ 
/*  80 */       if (state.nestingCount > 0) { return lastModified;
/*     */       }
/*     */       
/*  83 */       state.processConditionalCommandsOnly = state.branchTaken;
/*     */       
/*     */ 
/*  86 */       state.branchTaken = true;
/*  87 */     } else if ("endif".equalsIgnoreCase(commandName))
/*     */     {
/*     */ 
/*  90 */       if (state.nestingCount > 0) {
/*  91 */         state.nestingCount -= 1;
/*  92 */         return lastModified;
/*     */       }
/*     */       
/*  95 */       state.processConditionalCommandsOnly = false;
/*     */       
/*     */ 
/*     */ 
/*  99 */       state.branchTaken = true;
/*     */     } else {
/* 101 */       throw new SSIStopProcessingException();
/*     */     }
/*     */     
/*     */ 
/* 105 */     return lastModified;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean evaluateArguments(String[] names, String[] values, SSIMediator ssiMediator)
/*     */     throws SSIStopProcessingException
/*     */   {
/* 115 */     String expr = getExpression(names, values);
/* 116 */     if (expr == null) {
/* 117 */       throw new SSIStopProcessingException();
/*     */     }
/*     */     try
/*     */     {
/* 121 */       ExpressionParseTree tree = new ExpressionParseTree(expr, ssiMediator);
/*     */       
/* 123 */       return tree.evaluateTree();
/*     */     }
/*     */     catch (ParseException e) {
/* 126 */       throw new SSIStopProcessingException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getExpression(String[] paramNames, String[] paramValues)
/*     */   {
/* 136 */     if ("expr".equalsIgnoreCase(paramNames[0])) return paramValues[0];
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIConditional.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */