/*    */ package org.apache.coyote.ajp;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.AprEndpoint;
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
/*    */ public class AjpAprProtocol
/*    */   extends AbstractAjpProtocol<Long>
/*    */ {
/* 29 */   private static final Log log = LogFactory.getLog(AjpAprProtocol.class);
/*    */   
/*    */   protected Log getLog() {
/* 32 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isAprRequired()
/*    */   {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public AjpAprProtocol()
/*    */   {
/* 46 */     super(new AprEndpoint());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 52 */   public int getPollTime() { return ((AprEndpoint)getEndpoint()).getPollTime(); }
/* 53 */   public void setPollTime(int pollTime) { ((AprEndpoint)getEndpoint()).setPollTime(pollTime); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 60 */     return "ajp-apr";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpAprProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */