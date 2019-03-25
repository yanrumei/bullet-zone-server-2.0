/*    */ package ch.qos.logback.core.joran.event;
/*    */ 
/*    */ import org.xml.sax.Locator;
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
/*    */ public class EndEvent
/*    */   extends SaxEvent
/*    */ {
/*    */   EndEvent(String namespaceURI, String localName, String qName, Locator locator)
/*    */   {
/* 21 */     super(namespaceURI, localName, qName, locator);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 26 */     return "  EndEvent(" + getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\event\EndEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */