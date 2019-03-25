/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ abstract class AbstractNameValueExpression<T>
/*     */   implements NameValueExpression<T>
/*     */ {
/*     */   protected final String name;
/*     */   protected final T value;
/*     */   protected final boolean isNegated;
/*     */   
/*     */   AbstractNameValueExpression(String expression)
/*     */   {
/*  40 */     int separator = expression.indexOf('=');
/*  41 */     if (separator == -1) {
/*  42 */       this.isNegated = expression.startsWith("!");
/*  43 */       this.name = (this.isNegated ? expression.substring(1) : expression);
/*  44 */       this.value = null;
/*     */     }
/*     */     else {
/*  47 */       this.isNegated = ((separator > 0) && (expression.charAt(separator - 1) == '!'));
/*  48 */       this.name = (this.isNegated ? expression.substring(0, separator - 1) : expression.substring(0, separator));
/*  49 */       this.value = parseValue(expression.substring(separator + 1));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/*  56 */     return this.name;
/*     */   }
/*     */   
/*     */   public T getValue()
/*     */   {
/*  61 */     return (T)this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  66 */   public boolean isNegated() { return this.isNegated; }
/*     */   
/*     */   public final boolean match(HttpServletRequest request) {
/*     */     boolean isMatch;
/*     */     boolean isMatch;
/*  71 */     if (this.value != null) {
/*  72 */       isMatch = matchValue(request);
/*     */     }
/*     */     else {
/*  75 */       isMatch = matchName(request);
/*     */     }
/*  77 */     return this.isNegated ? false : !isMatch ? true : isMatch;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract boolean isCaseSensitiveName();
/*     */   
/*     */ 
/*     */   protected abstract T parseValue(String paramString);
/*     */   
/*     */   protected abstract boolean matchName(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */   protected abstract boolean matchValue(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  92 */     if (this == obj) {
/*  93 */       return true;
/*     */     }
/*  95 */     if ((obj instanceof AbstractNameValueExpression)) {
/*  96 */       AbstractNameValueExpression<?> other = (AbstractNameValueExpression)obj;
/*  97 */       String thisName = isCaseSensitiveName() ? this.name : this.name.toLowerCase();
/*  98 */       String otherName = isCaseSensitiveName() ? other.name : other.name.toLowerCase();
/*  99 */       return (thisName.equalsIgnoreCase(otherName)) && (this.value != null ? this.value
/* 100 */         .equals(other.value) : other.value == null) && (this.isNegated == other.isNegated);
/*     */     }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 108 */     int result = isCaseSensitiveName() ? this.name.hashCode() : this.name.toLowerCase().hashCode();
/* 109 */     result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
/* 110 */     result = 31 * result + (this.isNegated ? 1 : 0);
/* 111 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 116 */     StringBuilder builder = new StringBuilder();
/* 117 */     if (this.value != null) {
/* 118 */       builder.append(this.name);
/* 119 */       if (this.isNegated) {
/* 120 */         builder.append('!');
/*     */       }
/* 122 */       builder.append('=');
/* 123 */       builder.append(this.value);
/*     */     }
/*     */     else {
/* 126 */       if (this.isNegated) {
/* 127 */         builder.append('!');
/*     */       }
/* 129 */       builder.append(this.name);
/*     */     }
/* 131 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\AbstractNameValueExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */