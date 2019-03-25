/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.atom.Entry;
/*    */ import com.rometools.rome.feed.atom.Feed;
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
/*    */ 
/*    */ public abstract class AbstractAtomFeedView
/*    */   extends AbstractFeedView<Feed>
/*    */ {
/*    */   public static final String DEFAULT_FEED_TYPE = "atom_1.0";
/* 51 */   private String feedType = "atom_1.0";
/*    */   
/*    */   public AbstractAtomFeedView()
/*    */   {
/* 55 */     setContentType("application/atom+xml");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFeedType(String feedType)
/*    */   {
/* 65 */     this.feedType = feedType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Feed newFeed()
/*    */   {
/* 75 */     return new Feed(this.feedType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final void buildFeedEntries(Map<String, Object> model, Feed feed, HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 86 */     List<Entry> entries = buildFeedEntries(model, request, response);
/* 87 */     feed.setEntries(entries);
/*    */   }
/*    */   
/*    */   protected abstract List<Entry> buildFeedEntries(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\feed\AbstractAtomFeedView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */