/*    */ package org.apache.tomcat.jni;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LibraryNotFoundError
/*    */   extends UnsatisfiedLinkError
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String libraryNames;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LibraryNotFoundError(String libraryNames, String errors)
/*    */   {
/* 33 */     super(errors);
/* 34 */     this.libraryNames = libraryNames;
/*    */   }
/*    */   
/*    */   public String getLibraryNames() {
/* 38 */     return this.libraryNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\LibraryNotFoundError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */