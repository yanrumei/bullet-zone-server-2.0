/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*     */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*     */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
/*     */ public abstract class AbstractExcelView
/*     */   extends AbstractView
/*     */ {
/*     */   private static final String CONTENT_TYPE = "application/vnd.ms-excel";
/*     */   private static final String EXTENSION = ".xls";
/*     */   private String url;
/*     */   
/*     */   public AbstractExcelView()
/*     */   {
/* 115 */     setContentType("application/vnd.ms-excel");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUrl(String url)
/*     */   {
/* 122 */     this.url = url;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     HSSFWorkbook workbook;
/*     */     
/*     */     HSSFWorkbook workbook;
/*     */     
/* 139 */     if (this.url != null) {
/* 140 */       workbook = getTemplateSource(this.url, request);
/*     */     }
/*     */     else {
/* 143 */       workbook = new HSSFWorkbook();
/* 144 */       this.logger.debug("Created Excel Workbook from scratch");
/*     */     }
/*     */     
/* 147 */     buildExcelDocument(model, workbook, request, response);
/*     */     
/*     */ 
/* 150 */     response.setContentType(getContentType());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */     ServletOutputStream out = response.getOutputStream();
/* 157 */     workbook.write(out);
/* 158 */     out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HSSFWorkbook getTemplateSource(String url, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 169 */     LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
/* 170 */     Locale userLocale = RequestContextUtils.getLocale(request);
/* 171 */     Resource inputFile = helper.findLocalizedResource(url, ".xls", userLocale);
/*     */     
/*     */ 
/* 174 */     if (this.logger.isDebugEnabled()) {
/* 175 */       this.logger.debug("Loading Excel workbook from " + inputFile);
/*     */     }
/* 177 */     return new HSSFWorkbook(inputFile.getInputStream());
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
/*     */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, HSSFWorkbook paramHSSFWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
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
/*     */   protected HSSFCell getCell(HSSFSheet sheet, int row, int col)
/*     */   {
/* 203 */     HSSFRow sheetRow = sheet.getRow(row);
/* 204 */     if (sheetRow == null) {
/* 205 */       sheetRow = sheet.createRow(row);
/*     */     }
/* 207 */     HSSFCell cell = sheetRow.getCell(col);
/* 208 */     if (cell == null) {
/* 209 */       cell = sheetRow.createCell(col);
/*     */     }
/* 211 */     return cell;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setText(HSSFCell cell, String text)
/*     */   {
/* 220 */     cell.setCellType(1);
/* 221 */     cell.setCellValue(text);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractExcelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */