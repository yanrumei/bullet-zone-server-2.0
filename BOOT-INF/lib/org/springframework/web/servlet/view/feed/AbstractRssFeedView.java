/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.rss.Channel;
/*    */ import com.rometools.rome.feed.rss.Item;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public abstract class AbstractRssFeedView
/*    */   extends AbstractFeedView<Channel>
/*    */ {
/*    */   public AbstractRssFeedView()
/*    */   {
/* 51 */     setContentType("application/rss+xml");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Channel newFeed()
/*    */   {
/* 61 */     return new Channel("rss_2.0");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final void buildFeedEntries(Map<String, Object> model, Channel channel, HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 72 */     List<Item> items = buildFeedItems(model, request, response);
/* 73 */     channel.setItems(items);
/*    */   }
/*    */   
/*    */   protected abstract List<Item> buildFeedItems(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\feed\AbstractRssFeedView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */