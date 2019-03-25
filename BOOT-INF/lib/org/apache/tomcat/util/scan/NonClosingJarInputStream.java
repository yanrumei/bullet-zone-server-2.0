/*    */ package org.apache.tomcat.util.scan;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.jar.JarInputStream;
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
/*    */ public class NonClosingJarInputStream
/*    */   extends JarInputStream
/*    */ {
/*    */   public NonClosingJarInputStream(InputStream in, boolean verify)
/*    */     throws IOException
/*    */   {
/* 33 */     super(in, verify);
/*    */   }
/*    */   
/*    */   public NonClosingJarInputStream(InputStream in) throws IOException {
/* 37 */     super(in);
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void reallyClose() throws IOException
/*    */   {
/* 46 */     super.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\NonClosingJarInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */