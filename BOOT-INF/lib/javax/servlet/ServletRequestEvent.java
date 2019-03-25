/*    */ package javax.servlet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletRequestEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final transient ServletRequest request;
/*    */   
/*    */   public ServletRequestEvent(ServletContext sc, ServletRequest request)
/*    */   {
/* 41 */     super(sc);
/* 42 */     this.request = request;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletRequest getServletRequest()
/*    */   {
/* 50 */     return this.request;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletContext getServletContext()
/*    */   {
/* 58 */     return (ServletContext)super.getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletRequestEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */