/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Workbook;
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
/*     */ public abstract class AbstractXlsView
/*     */   extends AbstractView
/*     */ {
/*     */   public AbstractXlsView()
/*     */   {
/*  48 */     setContentType("application/vnd.ms-excel");
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/*  54 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  65 */     Workbook workbook = createWorkbook(model, request);
/*     */     
/*     */ 
/*  68 */     buildExcelDocument(model, workbook, request, response);
/*     */     
/*     */ 
/*  71 */     response.setContentType(getContentType());
/*     */     
/*     */ 
/*  74 */     renderWorkbook(workbook, response);
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
/*     */   protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request)
/*     */   {
/*  88 */     return new HSSFWorkbook();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderWorkbook(Workbook workbook, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/*  99 */     ServletOutputStream out = response.getOutputStream();
/* 100 */     workbook.write(out);
/*     */     
/*     */ 
/* 103 */     if ((workbook instanceof Closeable)) {
/* 104 */       workbook.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, Workbook paramWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractXlsView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */