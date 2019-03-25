/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class Token
/*    */ {
/* 24 */   private static final Pattern ESCAPED_OPENING_CURLY_BRACE = Pattern.compile("\\\\\\{");
/* 25 */   private static final Pattern ESCAPED_CLOSING_CURLY_BRACE = Pattern.compile("\\\\\\}");
/*    */   
/*    */   private boolean isParameter;
/*    */   private boolean isEL;
/*    */   private boolean terminated;
/*    */   private String value;
/*    */   private StringBuilder builder;
/*    */   
/*    */   public Token(String tokenStart)
/*    */   {
/* 35 */     this.builder = new StringBuilder();
/* 36 */     this.builder.append(tokenStart);
/*    */   }
/*    */   
/*    */   public Token(char tokenStart) {
/* 40 */     this(String.valueOf(tokenStart));
/*    */   }
/*    */   
/*    */   public void append(char character) {
/* 44 */     this.builder.append(character);
/*    */   }
/*    */   
/*    */   public void makeParameterToken() {
/* 48 */     this.isParameter = true;
/*    */   }
/*    */   
/*    */   public void makeELToken() {
/* 52 */     makeParameterToken();
/* 53 */     this.isEL = true;
/*    */   }
/*    */   
/*    */   public void terminate() {
/* 57 */     this.value = this.builder.toString();
/* 58 */     if (this.isEL)
/*    */     {
/* 60 */       Matcher matcher = ESCAPED_OPENING_CURLY_BRACE.matcher(this.value);
/* 61 */       this.value = matcher.replaceAll("{");
/*    */       
/* 63 */       matcher = ESCAPED_CLOSING_CURLY_BRACE.matcher(this.value);
/* 64 */       this.value = matcher.replaceAll("}");
/*    */     }
/* 66 */     this.builder = null;
/* 67 */     this.terminated = true;
/*    */   }
/*    */   
/*    */   public boolean isParameter() {
/* 71 */     return this.isParameter;
/*    */   }
/*    */   
/*    */   public String getTokenValue() {
/* 75 */     if (!this.terminated) {
/* 76 */       throw new IllegalStateException("Trying to retrieve token value for unterminated token");
/*    */     }
/* 78 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     StringBuilder sb = new StringBuilder("Token{");
/* 84 */     sb.append("value='").append(this.value).append('\'');
/* 85 */     sb.append(", terminated=").append(this.terminated);
/* 86 */     sb.append(", isEL=").append(this.isEL);
/* 87 */     sb.append(", isParameter=").append(this.isParameter);
/* 88 */     sb.append('}');
/* 89 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */