/*    */ package org.yaml.snakeyaml.parser;
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
/*    */ public class ParserException
/*    */   extends MarkedYAMLException
/*    */ {
/*    */   private static final long serialVersionUID = -2349253802798398038L;
/*    */   
/*    */   public ParserException(String context, Mark contextMark, String problem, Mark problemMark)
/*    */   {
/* 42 */     super(context, contextMark, problem, problemMark, null, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\parser\ParserException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */