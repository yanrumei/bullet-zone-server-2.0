/*    */ package org.apache.tomcat.util.http;
/*    */ 
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ public class ServerCookies
/*    */ {
/* 26 */   private static final StringManager sm = StringManager.getManager(ServerCookies.class);
/*    */   
/*    */   private ServerCookie[] serverCookies;
/*    */   
/* 30 */   private int cookieCount = 0;
/* 31 */   private int limit = 200;
/*    */   
/*    */   public ServerCookies(int initialSize)
/*    */   {
/* 35 */     this.serverCookies = new ServerCookie[initialSize];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServerCookie addCookie()
/*    */   {
/* 46 */     if ((this.limit > -1) && (this.cookieCount >= this.limit))
/*    */     {
/* 48 */       throw new IllegalArgumentException(sm.getString("cookies.maxCountFail", new Object[] {Integer.valueOf(this.limit) }));
/*    */     }
/*    */     
/* 51 */     if (this.cookieCount >= this.serverCookies.length) {
/* 52 */       int newSize = this.limit > -1 ? Math.min(2 * this.cookieCount, this.limit) : 2 * this.cookieCount;
/* 53 */       ServerCookie[] scookiesTmp = new ServerCookie[newSize];
/* 54 */       System.arraycopy(this.serverCookies, 0, scookiesTmp, 0, this.cookieCount);
/* 55 */       this.serverCookies = scookiesTmp;
/*    */     }
/*    */     
/* 58 */     ServerCookie c = this.serverCookies[this.cookieCount];
/* 59 */     if (c == null) {
/* 60 */       c = new ServerCookie();
/* 61 */       this.serverCookies[this.cookieCount] = c;
/*    */     }
/* 63 */     this.cookieCount += 1;
/* 64 */     return c;
/*    */   }
/*    */   
/*    */   public ServerCookie getCookie(int idx)
/*    */   {
/* 69 */     return this.serverCookies[idx];
/*    */   }
/*    */   
/*    */   public int getCookieCount()
/*    */   {
/* 74 */     return this.cookieCount;
/*    */   }
/*    */   
/*    */   public void setLimit(int limit)
/*    */   {
/* 79 */     this.limit = limit;
/* 80 */     if ((limit > -1) && (this.serverCookies.length > limit) && (this.cookieCount <= limit))
/*    */     {
/* 82 */       ServerCookie[] scookiesTmp = new ServerCookie[limit];
/* 83 */       System.arraycopy(this.serverCookies, 0, scookiesTmp, 0, this.cookieCount);
/* 84 */       this.serverCookies = scookiesTmp;
/*    */     }
/*    */   }
/*    */   
/*    */   public void recycle()
/*    */   {
/* 90 */     for (int i = 0; i < this.cookieCount; i++) {
/* 91 */       this.serverCookies[i].recycle();
/*    */     }
/* 93 */     this.cookieCount = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\ServerCookies.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */