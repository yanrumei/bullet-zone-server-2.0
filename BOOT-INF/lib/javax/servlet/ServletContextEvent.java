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
/*    */ public class ServletContextEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ServletContextEvent(ServletContext source)
/*    */   {
/* 37 */     super(source);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletContext getServletContext()
/*    */   {
/* 46 */     return (ServletContext)super.getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletContextEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */