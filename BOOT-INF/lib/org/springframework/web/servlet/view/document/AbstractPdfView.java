/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import com.lowagie.text.Document;
/*     */ import com.lowagie.text.DocumentException;
/*     */ import com.lowagie.text.PageSize;
/*     */ import com.lowagie.text.pdf.PdfWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.servlet.view.AbstractView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPdfView
/*     */   extends AbstractView
/*     */ {
/*     */   public AbstractPdfView()
/*     */   {
/*  58 */     setContentType("application/pdf");
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/*  64 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  72 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*     */     
/*     */ 
/*  75 */     Document document = newDocument();
/*  76 */     PdfWriter writer = newWriter(document, baos);
/*  77 */     prepareWriter(model, writer, request);
/*  78 */     buildPdfMetadata(model, document, request);
/*     */     
/*     */ 
/*  81 */     document.open();
/*  82 */     buildPdfDocument(model, document, writer, request, response);
/*  83 */     document.close();
/*     */     
/*     */ 
/*  86 */     writeToResponse(response, baos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Document newDocument()
/*     */   {
/*  97 */     return new Document(PageSize.A4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PdfWriter newWriter(Document document, OutputStream os)
/*     */     throws DocumentException
/*     */   {
/* 108 */     return PdfWriter.getInstance(document, os);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
/*     */     throws DocumentException
/*     */   {
/* 129 */     writer.setViewerPreferences(getViewerPreferences());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getViewerPreferences()
/*     */   {
/* 143 */     return 2053;
/*     */   }
/*     */   
/*     */   protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {}
/*     */   
/*     */   protected abstract void buildPdfDocument(Map<String, Object> paramMap, Document paramDocument, PdfWriter paramPdfWriter, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractPdfView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */