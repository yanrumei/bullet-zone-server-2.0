/*    */ package org.springframework.web.servlet.mvc.condition;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ public abstract class AbstractRequestCondition<T extends AbstractRequestCondition<T>>
/*    */   implements RequestCondition<T>
/*    */ {
/*    */   public boolean equals(Object obj)
/*    */   {
/* 33 */     if (this == obj) {
/* 34 */       return true;
/*    */     }
/* 36 */     if ((obj != null) && (getClass() == obj.getClass())) {
/* 37 */       AbstractRequestCondition<?> other = (AbstractRequestCondition)obj;
/* 38 */       return getContent().equals(other.getContent());
/*    */     }
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 45 */     return getContent().hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 50 */     StringBuilder builder = new StringBuilder("[");
/* 51 */     for (Iterator<?> iterator = getContent().iterator(); iterator.hasNext();) {
/* 52 */       Object expression = iterator.next();
/* 53 */       builder.append(expression.toString());
/* 54 */       if (iterator.hasNext()) {
/* 55 */         builder.append(getToStringInfix());
/*    */       }
/*    */     }
/* 58 */     builder.append("]");
/* 59 */     return builder.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEmpty()
/*    */   {
/* 68 */     return getContent().isEmpty();
/*    */   }
/*    */   
/*    */   protected abstract Collection<?> getContent();
/*    */   
/*    */   protected abstract String getToStringInfix();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\AbstractRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */