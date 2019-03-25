/*     */ package org.apache.el.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenMgrError
/*     */   extends Error
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int LOOP_DETECTED = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int errorCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String addEscapes(String str)
/*     */   {
/*  52 */     StringBuffer retval = new StringBuffer();
/*     */     
/*  54 */     for (int i = 0; i < str.length(); i++) {
/*  55 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000': 
/*     */         break;
/*     */       case '\b': 
/*  60 */         retval.append("\\b");
/*  61 */         break;
/*     */       case '\t': 
/*  63 */         retval.append("\\t");
/*  64 */         break;
/*     */       case '\n': 
/*  66 */         retval.append("\\n");
/*  67 */         break;
/*     */       case '\f': 
/*  69 */         retval.append("\\f");
/*  70 */         break;
/*     */       case '\r': 
/*  72 */         retval.append("\\r");
/*  73 */         break;
/*     */       case '"': 
/*  75 */         retval.append("\\\"");
/*  76 */         break;
/*     */       case '\'': 
/*  78 */         retval.append("\\'");
/*  79 */         break;
/*     */       case '\\': 
/*  81 */         retval.append("\\\\");
/*  82 */         break;
/*     */       default:  char ch;
/*  84 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/*  85 */           String s = "0000" + Integer.toString(ch, 16);
/*  86 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/*  88 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  93 */     return retval.toString();
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
/*     */   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar)
/*     */   {
/* 109 */     return 
/*     */     
/*     */ 
/*     */ 
/* 113 */       "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : new StringBuilder().append("\"").append(addEscapes(String.valueOf(curChar))).append("\"").append(" (").append(curChar).append("), ").toString()) + "after : \"" + addEscapes(errorAfter) + "\"";
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
/*     */   public String getMessage()
/*     */   {
/* 126 */     return super.getMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenMgrError() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenMgrError(String message, int reason)
/*     */   {
/* 139 */     super(message);
/* 140 */     this.errorCode = reason;
/*     */   }
/*     */   
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason)
/*     */   {
/* 145 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\TokenMgrError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */