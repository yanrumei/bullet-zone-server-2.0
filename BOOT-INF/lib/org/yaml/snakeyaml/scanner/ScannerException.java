/*    */ package org.yaml.snakeyaml.scanner;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ import org.yaml.snakeyaml.error.MarkedYAMLException;
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
/*    */ public class ScannerException
/*    */   extends MarkedYAMLException
/*    */ {
/*    */   private static final long serialVersionUID = 4782293188600445954L;
/*    */   
/*    */   public ScannerException(String context, Mark contextMark, String problem, Mark problemMark, String note)
/*    */   {
/* 47 */     super(context, contextMark, problem, problemMark, note);
/*    */   }
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
/*    */   public ScannerException(String context, Mark contextMark, String problem, Mark problemMark)
/*    */   {
/* 64 */     this(context, contextMark, problem, problemMark, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\scanner\ScannerException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */