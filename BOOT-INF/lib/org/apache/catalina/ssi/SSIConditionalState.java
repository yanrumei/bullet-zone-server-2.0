/*    */ package org.apache.catalina.ssi;
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
/*    */ 
/*    */ class SSIConditionalState
/*    */ {
/* 33 */   boolean branchTaken = false;
/*    */   
/*    */ 
/*    */ 
/* 37 */   int nestingCount = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   boolean processConditionalCommandsOnly = false;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIConditionalState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */