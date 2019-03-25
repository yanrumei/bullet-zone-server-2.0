/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.poi.ss.usermodel.Workbook;
/*    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
/*    */ public abstract class AbstractXlsxView
/*    */   extends AbstractXlsView
/*    */ {
/*    */   public AbstractXlsxView()
/*    */   {
/* 43 */     setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request)
/*    */   {
/* 51 */     return new XSSFWorkbook();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\document\AbstractXlsxView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */