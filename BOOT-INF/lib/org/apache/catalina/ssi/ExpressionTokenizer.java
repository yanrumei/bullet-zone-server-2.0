/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpressionTokenizer
/*     */ {
/*     */   public static final int TOKEN_STRING = 0;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_AND = 1;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_OR = 2;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_NOT = 3;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_EQ = 4;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_NOT_EQ = 5;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_RBRACE = 6;
/*     */   
/*     */ 
/*     */   public static final int TOKEN_LBRACE = 7;
/*     */   
/*     */   public static final int TOKEN_GE = 8;
/*     */   
/*     */   public static final int TOKEN_LE = 9;
/*     */   
/*     */   public static final int TOKEN_GT = 10;
/*     */   
/*     */   public static final int TOKEN_LT = 11;
/*     */   
/*     */   public static final int TOKEN_END = 12;
/*     */   
/*     */   private final char[] expr;
/*     */   
/*  42 */   private String tokenVal = null;
/*     */   
/*     */ 
/*     */   private int index;
/*     */   
/*     */   private final int length;
/*     */   
/*     */ 
/*     */   public ExpressionTokenizer(String expr)
/*     */   {
/*  52 */     this.expr = expr.trim().toCharArray();
/*  53 */     this.length = this.expr.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMoreTokens()
/*     */   {
/*  61 */     return this.index < this.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  69 */     return this.index;
/*     */   }
/*     */   
/*     */   protected boolean isMetaChar(char c)
/*     */   {
/*  74 */     return (Character.isWhitespace(c)) || (c == '(') || (c == ')') || (c == '!') || (c == '<') || (c == '>') || (c == '|') || (c == '&') || (c == '=');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int nextToken()
/*     */   {
/*  85 */     while ((this.index < this.length) && (Character.isWhitespace(this.expr[this.index]))) {
/*  86 */       this.index += 1;
/*     */     }
/*  88 */     this.tokenVal = null;
/*  89 */     if (this.index == this.length) return 12;
/*  90 */     int start = this.index;
/*  91 */     char currentChar = this.expr[this.index];
/*  92 */     char nextChar = '\000';
/*  93 */     this.index += 1;
/*  94 */     if (this.index < this.length) { nextChar = this.expr[this.index];
/*     */     }
/*  96 */     switch (currentChar) {
/*     */     case '(': 
/*  98 */       return 7;
/*     */     case ')': 
/* 100 */       return 6;
/*     */     case '=': 
/* 102 */       return 4;
/*     */     case '!': 
/* 104 */       if (nextChar == '=') {
/* 105 */         this.index += 1;
/* 106 */         return 5;
/*     */       }
/* 108 */       return 3;
/*     */     case '|': 
/* 110 */       if (nextChar == '|') {
/* 111 */         this.index += 1;
/* 112 */         return 2;
/*     */       }
/*     */       break;
/*     */     case '&': 
/* 116 */       if (nextChar == '&') {
/* 117 */         this.index += 1;
/* 118 */         return 1;
/*     */       }
/*     */       break;
/*     */     case '>': 
/* 122 */       if (nextChar == '=') {
/* 123 */         this.index += 1;
/* 124 */         return 8;
/*     */       }
/* 126 */       return 10;
/*     */     case '<': 
/* 128 */       if (nextChar == '=') {
/* 129 */         this.index += 1;
/* 130 */         return 9;
/*     */       }
/* 132 */       return 11;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 137 */     int end = this.index;
/* 138 */     if ((currentChar == '"') || (currentChar == '\''))
/*     */     {
/* 140 */       char endChar = currentChar;
/* 141 */       boolean escaped = false;
/* 142 */       start++;
/* 143 */       for (; this.index < this.length; this.index += 1)
/* 144 */         if ((this.expr[this.index] == '\\') && (!escaped)) {
/* 145 */           escaped = true;
/*     */         }
/*     */         else {
/* 148 */           if ((this.expr[this.index] == endChar) && (!escaped)) break;
/* 149 */           escaped = false;
/*     */         }
/* 151 */       end = this.index;
/* 152 */       this.index += 1;
/* 153 */     } else if (currentChar == '/')
/*     */     {
/* 155 */       char endChar = currentChar;
/* 156 */       boolean escaped = false;
/* 157 */       for (; this.index < this.length; this.index += 1)
/* 158 */         if ((this.expr[this.index] == '\\') && (!escaped)) {
/* 159 */           escaped = true;
/*     */         }
/*     */         else {
/* 162 */           if ((this.expr[this.index] == endChar) && (!escaped)) break;
/* 163 */           escaped = false;
/*     */         }
/* 165 */       end = ++this.index;
/*     */     }
/*     */     else {
/* 168 */       while ((this.index < this.length) && 
/* 169 */         (!isMetaChar(this.expr[this.index]))) {
/* 168 */         this.index += 1;
/*     */       }
/*     */       
/* 171 */       end = this.index;
/*     */     }
/*     */     
/* 174 */     this.tokenVal = new String(this.expr, start, end - start);
/* 175 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTokenValue()
/*     */   {
/* 184 */     return this.tokenVal;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\ExpressionTokenizer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */