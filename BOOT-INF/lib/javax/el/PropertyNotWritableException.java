/*    */ package javax.el;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropertyNotWritableException
/*    */   extends ELException
/*    */ {
/*    */   private static final long serialVersionUID = 827987155471214717L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyNotWritableException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyNotWritableException(String message)
/*    */   {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public PropertyNotWritableException(Throwable cause) {
/* 32 */     super(cause);
/*    */   }
/*    */   
/*    */   public PropertyNotWritableException(String message, Throwable cause) {
/* 36 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\PropertyNotWritableException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */