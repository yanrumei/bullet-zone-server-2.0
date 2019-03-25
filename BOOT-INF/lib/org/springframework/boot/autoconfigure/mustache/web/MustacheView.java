/*    */ package org.springframework.boot.autoconfigure.mustache.web;
/*    */ 
/*    */ import com.samskivert.mustache.Template;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.servlet.view.AbstractTemplateView;
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
/*    */ public class MustacheView
/*    */   extends AbstractTemplateView
/*    */ {
/*    */   private Template template;
/*    */   
/*    */   public MustacheView() {}
/*    */   
/*    */   public MustacheView(Template template)
/*    */   {
/* 53 */     this.template = template;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setTemplate(Template template)
/*    */   {
/* 62 */     this.template = template;
/*    */   }
/*    */   
/*    */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 68 */     if (this.template != null) {
/* 69 */       this.template.execute(model, response.getWriter());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\web\MustacheView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */