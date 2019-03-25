/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class NullSourceExtractor
/*    */   implements SourceExtractor
/*    */ {
/*    */   public Object extractSource(Object sourceCandidate, Resource definitionResource)
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\NullSourceExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */