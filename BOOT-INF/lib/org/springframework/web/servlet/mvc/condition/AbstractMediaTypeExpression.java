/*    */ package org.springframework.web.servlet.mvc.condition;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.MediaType;
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
/*    */ abstract class AbstractMediaTypeExpression
/*    */   implements MediaTypeExpression, Comparable<AbstractMediaTypeExpression>
/*    */ {
/* 35 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/*    */   private final MediaType mediaType;
/*    */   
/*    */   private final boolean isNegated;
/*    */   
/*    */   AbstractMediaTypeExpression(String expression)
/*    */   {
/* 43 */     if (expression.startsWith("!")) {
/* 44 */       this.isNegated = true;
/* 45 */       expression = expression.substring(1);
/*    */     }
/*    */     else {
/* 48 */       this.isNegated = false;
/*    */     }
/* 50 */     this.mediaType = MediaType.parseMediaType(expression);
/*    */   }
/*    */   
/*    */   AbstractMediaTypeExpression(MediaType mediaType, boolean negated) {
/* 54 */     this.mediaType = mediaType;
/* 55 */     this.isNegated = negated;
/*    */   }
/*    */   
/*    */ 
/*    */   public MediaType getMediaType()
/*    */   {
/* 61 */     return this.mediaType;
/*    */   }
/*    */   
/*    */   public boolean isNegated()
/*    */   {
/* 66 */     return this.isNegated;
/*    */   }
/*    */   
/*    */ 
/*    */   public int compareTo(AbstractMediaTypeExpression other)
/*    */   {
/* 72 */     return MediaType.SPECIFICITY_COMPARATOR.compare(getMediaType(), other.getMediaType());
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 77 */     if (this == obj) {
/* 78 */       return true;
/*    */     }
/* 80 */     if ((obj != null) && (getClass() == obj.getClass())) {
/* 81 */       AbstractMediaTypeExpression other = (AbstractMediaTypeExpression)obj;
/* 82 */       return (this.mediaType.equals(other.mediaType)) && (this.isNegated == other.isNegated);
/*    */     }
/* 84 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 89 */     return this.mediaType.hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 94 */     StringBuilder builder = new StringBuilder();
/* 95 */     if (this.isNegated) {
/* 96 */       builder.append('!');
/*    */     }
/* 98 */     builder.append(this.mediaType.toString());
/* 99 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\AbstractMediaTypeExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */