/*    */ package javax.servlet.http;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class HttpSessionEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public HttpSessionEvent(HttpSession source)
/*    */   {
/* 34 */     super(source);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpSession getSession()
/*    */   {
/* 43 */     return (HttpSession)super.getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSessionEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */