/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import jxl.Workbook;
/*     */ import jxl.write.WritableWorkbook;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.LocalizedResourceHelper;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractJExcelView
/*     */   extends AbstractView
/*     */ {
/*     */   private static final String CONTENT_TYPE = "application/vnd.ms-excel";
/*     */   private static final String EXTENSION = ".xls";
/*     */   private String url;
/*     */   
/*     */   public AbstractJExcelView()
/*     */   {
/* 103 */     setContentType("application/vnd.ms-excel");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUrl(String url)
/*     */   {
/* 110 */     this.url = url;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/* 116 */     return true;
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
/* 127 */     response.setContentType(getContentType());
/* 128 */     OutputStream out = response.getOutputStream();
/*     */     WritableWorkbook workbook;
/*     */     WritableWorkbook workbook;
/* 131 */     if (this.url != null) {
/* 132 */       Workbook template = getTemplateSource(this.url, request);
/* 133 */       workbook = Workbook.createWorkbook(out, template);
/*     */     }
/*     */     else {
/* 136 */       this.logger.debug("Creating Excel Workbook from scratch");
/* 137 */       workbook = Workbook.createWorkbook(out);
/*     */     }
/*     */     
/* 140 */     buildExcelDocument(model, workbook, request, response);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 145 */     workbook.write();
/* 146 */     out.flush();
/* 147 */     workbook.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Workbook getTemplateSource(String url, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 158 */     LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
/* 159 */     Locale userLocale = RequestContextUtils.getLocale(request);
/* 160 */     Resource inputFile = helper.findLocalizedResource(url, ".xls", userLocale);
/*     */     
/*     */ 
/* 163 */     if (this.logger.isDebugEnabled()) {
/* 164 */       this.logger.debug("Loading Excel workbook from " + inputFile);
/*     */     }
/* 166 */     return Workbook.getWorkbook(inputFile.getInputStream());
/*     */   }
/*     */   
/*     */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, WritableWorkbook paramWritableWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractJExcelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */