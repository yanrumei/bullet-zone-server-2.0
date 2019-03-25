/*    */ package ch.qos.logback.classic.html;
/*    */ 
/*    */ import ch.qos.logback.core.html.CssBuilder;
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
/*    */ public class UrlCssBuilder
/*    */   implements CssBuilder
/*    */ {
/* 27 */   String url = "http://logback.qos.ch/css/classic.css";
/*    */   
/*    */   public String getUrl() {
/* 30 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 34 */     this.url = url;
/*    */   }
/*    */   
/*    */   public void addCss(StringBuilder sbuf) {
/* 38 */     sbuf.append("<link REL=StyleSheet HREF=\"");
/* 39 */     sbuf.append(this.url);
/* 40 */     sbuf.append("\" TITLE=\"Basic\" />");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\html\UrlCssBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */