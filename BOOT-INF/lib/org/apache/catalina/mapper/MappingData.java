/*    */ package org.apache.catalina.mapper;
/*    */ 
/*    */ import org.apache.catalina.Context;
/*    */ import org.apache.catalina.Host;
/*    */ import org.apache.catalina.Wrapper;
/*    */ import org.apache.catalina.servlet4preview.http.MappingMatch;
/*    */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*    */ public class MappingData
/*    */ {
/* 33 */   public Host host = null;
/* 34 */   public Context context = null;
/* 35 */   public int contextSlashCount = 0;
/* 36 */   public Context[] contexts = null;
/* 37 */   public Wrapper wrapper = null;
/* 38 */   public boolean jspWildCard = false;
/*    */   
/* 40 */   public final MessageBytes contextPath = MessageBytes.newInstance();
/* 41 */   public final MessageBytes requestPath = MessageBytes.newInstance();
/* 42 */   public final MessageBytes wrapperPath = MessageBytes.newInstance();
/* 43 */   public final MessageBytes pathInfo = MessageBytes.newInstance();
/*    */   
/* 45 */   public final MessageBytes redirectPath = MessageBytes.newInstance();
/*    */   
/*    */ 
/* 48 */   public MappingMatch matchType = MappingMatch.UNKNOWN;
/*    */   
/*    */   public void recycle() {
/* 51 */     this.host = null;
/* 52 */     this.context = null;
/* 53 */     this.contextSlashCount = 0;
/* 54 */     this.contexts = null;
/* 55 */     this.wrapper = null;
/* 56 */     this.jspWildCard = false;
/* 57 */     this.contextPath.recycle();
/* 58 */     this.requestPath.recycle();
/* 59 */     this.wrapperPath.recycle();
/* 60 */     this.pathInfo.recycle();
/* 61 */     this.redirectPath.recycle();
/* 62 */     this.matchType = MappingMatch.UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mapper\MappingData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */