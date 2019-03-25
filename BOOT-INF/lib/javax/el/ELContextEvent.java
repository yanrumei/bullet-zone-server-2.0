/*    */ package javax.el;
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
/*    */ public class ELContextEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1255131906285426769L;
/*    */   
/*    */   public ELContextEvent(ELContext source)
/*    */   {
/* 30 */     super(source);
/*    */   }
/*    */   
/*    */   public ELContext getELContext() {
/* 34 */     return (ELContext)getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ELContextEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */