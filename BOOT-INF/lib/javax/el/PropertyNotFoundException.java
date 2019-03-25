/*    */ package javax.el;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropertyNotFoundException
/*    */   extends ELException
/*    */ {
/*    */   private static final long serialVersionUID = -3799200961303506745L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyNotFoundException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyNotFoundException(String message)
/*    */   {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public PropertyNotFoundException(Throwable cause) {
/* 32 */     super(cause);
/*    */   }
/*    */   
/*    */   public PropertyNotFoundException(String message, Throwable cause) {
/* 36 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\PropertyNotFoundException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */