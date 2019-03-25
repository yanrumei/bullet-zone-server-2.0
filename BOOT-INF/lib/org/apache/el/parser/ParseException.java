/*     */ package org.apache.el.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParseException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Token currentToken;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[][] expectedTokenSequences;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] tokenImage;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal)
/*     */   {
/*  35 */     super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
/*  36 */     this.currentToken = currentTokenVal;
/*  37 */     this.expectedTokenSequences = expectedTokenSequencesVal;
/*  38 */     this.tokenImage = tokenImageVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParseException() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParseException(String message)
/*     */   {
/*  57 */     super(message);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage)
/*     */   {
/*  92 */     StringBuffer expected = new StringBuffer();
/*  93 */     int maxSize = 0;
/*  94 */     for (int i = 0; i < expectedTokenSequences.length; i++) {
/*  95 */       if (maxSize < expectedTokenSequences[i].length) {
/*  96 */         maxSize = expectedTokenSequences[i].length;
/*     */       }
/*  98 */       for (int j = 0; j < expectedTokenSequences[i].length; j++) {
/*  99 */         expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
/*     */       }
/* 101 */       if (expectedTokenSequences[i][(expectedTokenSequences[i].length - 1)] != 0) {
/* 102 */         expected.append("...");
/*     */       }
/* 104 */       expected.append(System.lineSeparator()).append("    ");
/*     */     }
/* 106 */     String retval = "Encountered \"";
/* 107 */     Token tok = currentToken.next;
/* 108 */     for (int i = 0; i < maxSize; i++) {
/* 109 */       if (i != 0) retval = retval + " ";
/* 110 */       if (tok.kind == 0) {
/* 111 */         retval = retval + tokenImage[0];
/* 112 */         break;
/*     */       }
/* 114 */       retval = retval + " " + tokenImage[tok.kind];
/* 115 */       retval = retval + " \"";
/* 116 */       retval = retval + add_escapes(tok.image);
/* 117 */       retval = retval + " \"";
/* 118 */       tok = tok.next;
/*     */     }
/* 120 */     retval = retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
/* 121 */     retval = retval + "." + System.lineSeparator();
/* 122 */     if (expectedTokenSequences.length == 1) {
/* 123 */       retval = retval + "Was expecting:" + System.lineSeparator() + "    ";
/*     */     } else {
/* 125 */       retval = retval + "Was expecting one of:" + System.lineSeparator() + "    ";
/*     */     }
/* 127 */     retval = retval + expected.toString();
/* 128 */     return retval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String add_escapes(String str)
/*     */   {
/* 137 */     StringBuffer retval = new StringBuffer();
/*     */     
/* 139 */     for (int i = 0; i < str.length(); i++) {
/* 140 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000': 
/*     */         break;
/*     */       case '\b': 
/* 145 */         retval.append("\\b");
/* 146 */         break;
/*     */       case '\t': 
/* 148 */         retval.append("\\t");
/* 149 */         break;
/*     */       case '\n': 
/* 151 */         retval.append("\\n");
/* 152 */         break;
/*     */       case '\f': 
/* 154 */         retval.append("\\f");
/* 155 */         break;
/*     */       case '\r': 
/* 157 */         retval.append("\\r");
/* 158 */         break;
/*     */       case '"': 
/* 160 */         retval.append("\\\"");
/* 161 */         break;
/*     */       case '\'': 
/* 163 */         retval.append("\\'");
/* 164 */         break;
/*     */       case '\\': 
/* 166 */         retval.append("\\\\");
/* 167 */         break;
/*     */       default:  char ch;
/* 169 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/* 170 */           String s = "0000" + Integer.toString(ch, 16);
/* 171 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/* 173 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 178 */     return retval.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\ParseException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */