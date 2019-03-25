/*    */ package org.springframework.web.servlet.view.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.WireFeed;
/*    */ import com.rometools.rome.io.WireFeedOutput;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletOutputStream;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.servlet.view.AbstractView;
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
/*    */ public abstract class AbstractFeedView<T extends WireFeed>
/*    */   extends AbstractView
/*    */ {
/*    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 56 */     T wireFeed = newFeed();
/* 57 */     buildFeedMetadata(model, wireFeed, request);
/* 58 */     buildFeedEntries(model, wireFeed, request, response);
/*    */     
/* 60 */     setResponseContentType(request, response);
/* 61 */     if (!StringUtils.hasText(wireFeed.getEncoding())) {
/* 62 */       wireFeed.setEncoding("UTF-8");
/*    */     }
/*    */     
/* 65 */     WireFeedOutput feedOutput = new WireFeedOutput();
/* 66 */     ServletOutputStream out = response.getOutputStream();
/* 67 */     feedOutput.output(wireFeed, new OutputStreamWriter(out, wireFeed.getEncoding()));
/* 68 */     out.flush();
/*    */   }
/*    */   
/*    */   protected abstract T newFeed();
/*    */   
/*    */   protected void buildFeedMetadata(Map<String, Object> model, T feed, HttpServletRequest request) {}
/*    */   
/*    */   protected abstract void buildFeedEntries(Map<String, Object> paramMap, T paramT, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\feed\AbstractFeedView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */