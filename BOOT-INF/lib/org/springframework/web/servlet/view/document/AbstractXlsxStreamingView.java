/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.apache.poi.ss.usermodel.Workbook;
/*    */ import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
/*    */ public abstract class AbstractXlsxStreamingView
/*    */   extends AbstractXlsxView
/*    */ {
/*    */   protected SXSSFWorkbook createWorkbook(Map<String, Object> model, HttpServletRequest request)
/*    */   {
/* 44 */     return new SXSSFWorkbook();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void renderWorkbook(Workbook workbook, HttpServletResponse response)
/*    */     throws IOException
/*    */   {
/* 53 */     super.renderWorkbook(workbook, response);
/*    */     
/*    */ 
/* 56 */     ((SXSSFWorkbook)workbook).dispose();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractXlsxStreamingView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */