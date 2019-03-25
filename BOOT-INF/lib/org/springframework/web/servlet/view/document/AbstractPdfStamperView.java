/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import com.lowagie.text.pdf.PdfReader;
/*    */ import com.lowagie.text.pdf.PdfStamper;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
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
/*    */ public abstract class AbstractPdfStamperView
/*    */   extends AbstractUrlBasedView
/*    */ {
/*    */   public AbstractPdfStamperView()
/*    */   {
/* 49 */     setContentType("application/pdf");
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean generatesDownloadContent()
/*    */   {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 63 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*    */     
/* 65 */     PdfReader reader = readPdfResource();
/* 66 */     PdfStamper stamper = new PdfStamper(reader, baos);
/* 67 */     mergePdfDocument(model, stamper, request, response);
/* 68 */     stamper.close();
/*    */     
/*    */ 
/* 71 */     writeToResponse(response, baos);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected PdfReader readPdfResource()
/*    */     throws IOException
/*    */   {
/* 83 */     return new PdfReader(getApplicationContext().getResource(getUrl()).getInputStream());
/*    */   }
/*    */   
/*    */   protected abstract void mergePdfDocument(Map<String, Object> paramMap, PdfStamper paramPdfStamper, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractPdfStamperView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */