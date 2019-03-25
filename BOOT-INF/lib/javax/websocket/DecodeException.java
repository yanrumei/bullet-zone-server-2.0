/*    */ package javax.websocket;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ public class DecodeException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private ByteBuffer bb;
/*    */   private String encodedString;
/*    */   
/*    */   public DecodeException(ByteBuffer bb, String message, Throwable cause)
/*    */   {
/* 29 */     super(message, cause);
/* 30 */     this.bb = bb;
/*    */   }
/*    */   
/*    */   public DecodeException(String encodedString, String message, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/* 36 */     this.encodedString = encodedString;
/*    */   }
/*    */   
/*    */   public DecodeException(ByteBuffer bb, String message) {
/* 40 */     super(message);
/* 41 */     this.bb = bb;
/*    */   }
/*    */   
/*    */   public DecodeException(String encodedString, String message) {
/* 45 */     super(message);
/* 46 */     this.encodedString = encodedString;
/*    */   }
/*    */   
/*    */   public ByteBuffer getBytes() {
/* 50 */     return this.bb;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 54 */     return this.encodedString;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\DecodeException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */