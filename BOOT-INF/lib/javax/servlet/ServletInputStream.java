/*    */ package javax.servlet;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public abstract class ServletInputStream
/*    */   extends InputStream
/*    */ {
/*    */   public int readLine(byte[] b, int off, int len)
/*    */     throws IOException
/*    */   {
/* 68 */     if (len <= 0) {
/* 69 */       return 0;
/*    */     }
/* 71 */     int count = 0;
/*    */     int c;
/* 73 */     while ((c = read()) != -1) {
/* 74 */       b[(off++)] = ((byte)c);
/* 75 */       count++;
/* 76 */       if (c != 10) if (count == len) {
/*    */           break;
/*    */         }
/*    */     }
/* 80 */     return count > 0 ? count : -1;
/*    */   }
/*    */   
/*    */   public abstract boolean isFinished();
/*    */   
/*    */   public abstract boolean isReady();
/*    */   
/*    */   public abstract void setReadListener(ReadListener paramReadListener);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */