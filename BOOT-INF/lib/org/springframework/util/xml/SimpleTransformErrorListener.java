/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import javax.xml.transform.ErrorListener;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ public class SimpleTransformErrorListener
/*    */   implements ErrorListener
/*    */ {
/*    */   private final Log logger;
/*    */   
/*    */   public SimpleTransformErrorListener(Log logger)
/*    */   {
/* 42 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void warning(TransformerException ex)
/*    */     throws TransformerException
/*    */   {
/* 48 */     this.logger.warn("XSLT transformation warning", ex);
/*    */   }
/*    */   
/*    */   public void error(TransformerException ex) throws TransformerException
/*    */   {
/* 53 */     this.logger.error("XSLT transformation error", ex);
/*    */   }
/*    */   
/*    */   public void fatalError(TransformerException ex) throws TransformerException
/*    */   {
/* 58 */     throw ex;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\SimpleTransformErrorListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */