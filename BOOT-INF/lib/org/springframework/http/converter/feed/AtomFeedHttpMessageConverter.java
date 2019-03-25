/*    */ package org.springframework.http.converter.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.atom.Feed;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class AtomFeedHttpMessageConverter
/*    */   extends AbstractWireFeedHttpMessageConverter<Feed>
/*    */ {
/*    */   public AtomFeedHttpMessageConverter()
/*    */   {
/* 41 */     super(new MediaType("application", "atom+xml"));
/*    */   }
/*    */   
/*    */   protected boolean supports(Class<?> clazz)
/*    */   {
/* 46 */     return Feed.class.isAssignableFrom(clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\feed\AtomFeedHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */