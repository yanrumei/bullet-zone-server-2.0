/*    */ package org.springframework.http.converter.xml;
/*    */ 
/*    */ import org.springframework.http.converter.FormHttpMessageConverter;
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
/*    */ @Deprecated
/*    */ public class XmlAwareFormHttpMessageConverter
/*    */   extends FormHttpMessageConverter
/*    */ {
/*    */   public XmlAwareFormHttpMessageConverter()
/*    */   {
/* 37 */     addPartConverter(new SourceHttpMessageConverter());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\xml\XmlAwareFormHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */