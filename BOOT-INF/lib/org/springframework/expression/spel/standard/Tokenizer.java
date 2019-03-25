/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.expression.spel.InternalParseException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelParseException;
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
/*     */ class Tokenizer
/*     */ {
/*  38 */   private static final String[] ALTERNATIVE_OPERATOR_NAMES = { "DIV", "EQ", "GE", "GT", "LE", "LT", "MOD", "NE", "NOT" };
/*     */   
/*     */ 
/*  41 */   private static final byte[] FLAGS = new byte['Ā'];
/*     */   private static final byte IS_DIGIT = 1;
/*     */   private static final byte IS_HEXDIGIT = 2;
/*     */   private static final byte IS_ALPHA = 4;
/*     */   private String expressionString;
/*     */   private char[] charsToProcess;
/*     */   private int pos;
/*     */   private int max;
/*     */   
/*  50 */   static { for (int ch = 48; ch <= 57; ch++) {
/*  51 */       int tmp77_76 = ch; byte[] tmp77_73 = FLAGS;tmp77_73[tmp77_76] = ((byte)(tmp77_73[tmp77_76] | 0x3));
/*     */     }
/*  53 */     for (int ch = 65; ch <= 70; ch++) {
/*  54 */       int tmp102_101 = ch; byte[] tmp102_98 = FLAGS;tmp102_98[tmp102_101] = ((byte)(tmp102_98[tmp102_101] | 0x2));
/*     */     }
/*  56 */     for (int ch = 97; ch <= 102; ch++) {
/*  57 */       int tmp127_126 = ch; byte[] tmp127_123 = FLAGS;tmp127_123[tmp127_126] = ((byte)(tmp127_123[tmp127_126] | 0x2));
/*     */     }
/*  59 */     for (int ch = 65; ch <= 90; ch++) {
/*  60 */       int tmp152_151 = ch; byte[] tmp152_148 = FLAGS;tmp152_148[tmp152_151] = ((byte)(tmp152_148[tmp152_151] | 0x4));
/*     */     }
/*  62 */     for (int ch = 97; ch <= 122; ch++) {
/*  63 */       int tmp177_176 = ch; byte[] tmp177_173 = FLAGS;tmp177_173[tmp177_176] = ((byte)(tmp177_173[tmp177_176] | 0x4));
/*     */     }
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
/*  76 */   private List<Token> tokens = new ArrayList();
/*     */   
/*     */   public Tokenizer(String inputData)
/*     */   {
/*  80 */     this.expressionString = inputData;
/*  81 */     this.charsToProcess = (inputData + "\000").toCharArray();
/*  82 */     this.max = this.charsToProcess.length;
/*  83 */     this.pos = 0;
/*     */   }
/*     */   
/*     */   public List<Token> process()
/*     */   {
/*  88 */     while (this.pos < this.max) {
/*  89 */       char ch = this.charsToProcess[this.pos];
/*  90 */       if (isAlphabetic(ch)) {
/*  91 */         lexIdentifier();
/*     */       }
/*     */       else {
/*  94 */         switch (ch) {
/*     */         case '+': 
/*  96 */           if (isTwoCharToken(TokenKind.INC)) {
/*  97 */             pushPairToken(TokenKind.INC);
/*     */           }
/*     */           else {
/* 100 */             pushCharToken(TokenKind.PLUS);
/*     */           }
/* 102 */           break;
/*     */         case '_': 
/* 104 */           lexIdentifier();
/* 105 */           break;
/*     */         case '-': 
/* 107 */           if (isTwoCharToken(TokenKind.DEC)) {
/* 108 */             pushPairToken(TokenKind.DEC);
/*     */           }
/*     */           else {
/* 111 */             pushCharToken(TokenKind.MINUS);
/*     */           }
/* 113 */           break;
/*     */         case ':': 
/* 115 */           pushCharToken(TokenKind.COLON);
/* 116 */           break;
/*     */         case '.': 
/* 118 */           pushCharToken(TokenKind.DOT);
/* 119 */           break;
/*     */         case ',': 
/* 121 */           pushCharToken(TokenKind.COMMA);
/* 122 */           break;
/*     */         case '*': 
/* 124 */           pushCharToken(TokenKind.STAR);
/* 125 */           break;
/*     */         case '/': 
/* 127 */           pushCharToken(TokenKind.DIV);
/* 128 */           break;
/*     */         case '%': 
/* 130 */           pushCharToken(TokenKind.MOD);
/* 131 */           break;
/*     */         case '(': 
/* 133 */           pushCharToken(TokenKind.LPAREN);
/* 134 */           break;
/*     */         case ')': 
/* 136 */           pushCharToken(TokenKind.RPAREN);
/* 137 */           break;
/*     */         case '[': 
/* 139 */           pushCharToken(TokenKind.LSQUARE);
/* 140 */           break;
/*     */         case '#': 
/* 142 */           pushCharToken(TokenKind.HASH);
/* 143 */           break;
/*     */         case ']': 
/* 145 */           pushCharToken(TokenKind.RSQUARE);
/* 146 */           break;
/*     */         case '{': 
/* 148 */           pushCharToken(TokenKind.LCURLY);
/* 149 */           break;
/*     */         case '}': 
/* 151 */           pushCharToken(TokenKind.RCURLY);
/* 152 */           break;
/*     */         case '@': 
/* 154 */           pushCharToken(TokenKind.BEAN_REF);
/* 155 */           break;
/*     */         case '^': 
/* 157 */           if (isTwoCharToken(TokenKind.SELECT_FIRST)) {
/* 158 */             pushPairToken(TokenKind.SELECT_FIRST);
/*     */           }
/*     */           else {
/* 161 */             pushCharToken(TokenKind.POWER);
/*     */           }
/* 163 */           break;
/*     */         case '!': 
/* 165 */           if (isTwoCharToken(TokenKind.NE)) {
/* 166 */             pushPairToken(TokenKind.NE);
/*     */           }
/* 168 */           else if (isTwoCharToken(TokenKind.PROJECT)) {
/* 169 */             pushPairToken(TokenKind.PROJECT);
/*     */           }
/*     */           else {
/* 172 */             pushCharToken(TokenKind.NOT);
/*     */           }
/* 174 */           break;
/*     */         case '=': 
/* 176 */           if (isTwoCharToken(TokenKind.EQ)) {
/* 177 */             pushPairToken(TokenKind.EQ);
/*     */           }
/*     */           else {
/* 180 */             pushCharToken(TokenKind.ASSIGN);
/*     */           }
/* 182 */           break;
/*     */         case '&': 
/* 184 */           if (isTwoCharToken(TokenKind.SYMBOLIC_AND)) {
/* 185 */             pushPairToken(TokenKind.SYMBOLIC_AND);
/*     */           }
/*     */           else {
/* 188 */             pushCharToken(TokenKind.FACTORY_BEAN_REF);
/*     */           }
/* 190 */           break;
/*     */         case '|': 
/* 192 */           if (!isTwoCharToken(TokenKind.SYMBOLIC_OR)) {
/* 193 */             raiseParseException(this.pos, SpelMessage.MISSING_CHARACTER, new Object[] { "|" });
/*     */           }
/* 195 */           pushPairToken(TokenKind.SYMBOLIC_OR);
/* 196 */           break;
/*     */         case '?': 
/* 198 */           if (isTwoCharToken(TokenKind.SELECT)) {
/* 199 */             pushPairToken(TokenKind.SELECT);
/*     */           }
/* 201 */           else if (isTwoCharToken(TokenKind.ELVIS)) {
/* 202 */             pushPairToken(TokenKind.ELVIS);
/*     */           }
/* 204 */           else if (isTwoCharToken(TokenKind.SAFE_NAVI)) {
/* 205 */             pushPairToken(TokenKind.SAFE_NAVI);
/*     */           }
/*     */           else {
/* 208 */             pushCharToken(TokenKind.QMARK);
/*     */           }
/* 210 */           break;
/*     */         case '$': 
/* 212 */           if (isTwoCharToken(TokenKind.SELECT_LAST)) {
/* 213 */             pushPairToken(TokenKind.SELECT_LAST);
/*     */           }
/*     */           else {
/* 216 */             lexIdentifier();
/*     */           }
/* 218 */           break;
/*     */         case '>': 
/* 220 */           if (isTwoCharToken(TokenKind.GE)) {
/* 221 */             pushPairToken(TokenKind.GE);
/*     */           }
/*     */           else {
/* 224 */             pushCharToken(TokenKind.GT);
/*     */           }
/* 226 */           break;
/*     */         case '<': 
/* 228 */           if (isTwoCharToken(TokenKind.LE)) {
/* 229 */             pushPairToken(TokenKind.LE);
/*     */           }
/*     */           else {
/* 232 */             pushCharToken(TokenKind.LT);
/*     */           }
/* 234 */           break;
/*     */         case '0': 
/*     */         case '1': 
/*     */         case '2': 
/*     */         case '3': 
/*     */         case '4': 
/*     */         case '5': 
/*     */         case '6': 
/*     */         case '7': 
/*     */         case '8': 
/*     */         case '9': 
/* 245 */           lexNumericLiteral(ch == '0');
/* 246 */           break;
/*     */         
/*     */         case '\t': 
/*     */         case '\n': 
/*     */         case '\r': 
/*     */         case ' ': 
/* 252 */           this.pos += 1;
/* 253 */           break;
/*     */         case '\'': 
/* 255 */           lexQuotedStringLiteral();
/* 256 */           break;
/*     */         case '"': 
/* 258 */           lexDoubleQuotedStringLiteral();
/* 259 */           break;
/*     */         
/*     */         case '\000': 
/* 262 */           this.pos += 1;
/* 263 */           break;
/*     */         case '\\': 
/* 265 */           raiseParseException(this.pos, SpelMessage.UNEXPECTED_ESCAPE_CHAR, new Object[0]);
/* 266 */           break;
/*     */         case '\001': case '\002': case '\003': case '\004': case '\005': case '\006': case '\007': case '\b': case '\013': case '\f': case '\016': case '\017': case '\020': case '\021': case '\022': case '\023': case '\024': case '\025': case '\026': case '\027': case '\030': case '\031': case '\032': case '\033': case '\034': case '\035': case '\036': case '\037': case ';': case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z': case '`': case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p': case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x': case 'y': case 'z': default: 
/* 268 */           throw new IllegalStateException("Cannot handle (" + Integer.valueOf(ch) + ") '" + ch + "'");
/*     */         }
/*     */       }
/*     */     }
/* 272 */     return this.tokens;
/*     */   }
/*     */   
/*     */ 
/*     */   private void lexQuotedStringLiteral()
/*     */   {
/* 278 */     int start = this.pos;
/* 279 */     boolean terminated = false;
/* 280 */     while (!terminated) {
/* 281 */       this.pos += 1;
/* 282 */       char ch = this.charsToProcess[this.pos];
/* 283 */       if (ch == '\'')
/*     */       {
/* 285 */         if (this.charsToProcess[(this.pos + 1)] == '\'') {
/* 286 */           this.pos += 1;
/*     */         }
/*     */         else {
/* 289 */           terminated = true;
/*     */         }
/*     */       }
/* 292 */       if (isExhausted()) {
/* 293 */         raiseParseException(start, SpelMessage.NON_TERMINATING_QUOTED_STRING, new Object[0]);
/*     */       }
/*     */     }
/* 296 */     this.pos += 1;
/* 297 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
/*     */   }
/*     */   
/*     */   private void lexDoubleQuotedStringLiteral()
/*     */   {
/* 302 */     int start = this.pos;
/* 303 */     boolean terminated = false;
/* 304 */     while (!terminated) {
/* 305 */       this.pos += 1;
/* 306 */       char ch = this.charsToProcess[this.pos];
/* 307 */       if (ch == '"')
/*     */       {
/* 309 */         if (this.charsToProcess[(this.pos + 1)] == '"') {
/* 310 */           this.pos += 1;
/*     */         }
/*     */         else {
/* 313 */           terminated = true;
/*     */         }
/*     */       }
/* 316 */       if (isExhausted()) {
/* 317 */         raiseParseException(start, SpelMessage.NON_TERMINATING_DOUBLE_QUOTED_STRING, new Object[0]);
/*     */       }
/*     */     }
/* 320 */     this.pos += 1;
/* 321 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
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
/*     */   private void lexNumericLiteral(boolean firstCharIsZero)
/*     */   {
/* 341 */     boolean isReal = false;
/* 342 */     int start = this.pos;
/* 343 */     char ch = this.charsToProcess[(this.pos + 1)];
/* 344 */     boolean isHex = (ch == 'x') || (ch == 'X');
/*     */     
/*     */ 
/* 347 */     if ((firstCharIsZero) && (isHex)) {
/* 348 */       this.pos += 1;
/*     */       do {
/* 350 */         this.pos += 1;
/*     */       }
/* 352 */       while (isHexadecimalDigit(this.charsToProcess[this.pos]));
/* 353 */       if (isChar('L', 'l')) {
/* 354 */         pushHexIntToken(subarray(start + 2, this.pos), true, start, this.pos);
/* 355 */         this.pos += 1;
/*     */       }
/*     */       else {
/* 358 */         pushHexIntToken(subarray(start + 2, this.pos), false, start, this.pos);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */       do
/*     */       {
/* 367 */         this.pos += 1;
/*     */       }
/* 369 */       while (isDigit(this.charsToProcess[this.pos]));
/*     */       
/*     */ 
/* 372 */       ch = this.charsToProcess[this.pos];
/* 373 */       if (ch == '.') {
/* 374 */         isReal = true;
/* 375 */         int dotpos = this.pos;
/*     */         do
/*     */         {
/* 378 */           this.pos += 1;
/*     */         }
/* 380 */         while (isDigit(this.charsToProcess[this.pos]));
/* 381 */         if (this.pos == dotpos + 1)
/*     */         {
/*     */ 
/*     */ 
/* 385 */           this.pos = dotpos;
/* 386 */           pushIntToken(subarray(start, this.pos), false, start, this.pos);
/* 387 */           return;
/*     */         }
/*     */       }
/*     */       
/* 391 */       int endOfNumber = this.pos;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 396 */       if (isChar('L', 'l')) {
/* 397 */         if (isReal) {
/* 398 */           raiseParseException(start, SpelMessage.REAL_CANNOT_BE_LONG, new Object[0]);
/*     */         }
/* 400 */         pushIntToken(subarray(start, endOfNumber), true, start, endOfNumber);
/* 401 */         this.pos += 1;
/*     */       }
/* 403 */       else if (isExponentChar(this.charsToProcess[this.pos])) {
/* 404 */         isReal = true;
/* 405 */         this.pos += 1;
/* 406 */         char possibleSign = this.charsToProcess[this.pos];
/* 407 */         if (isSign(possibleSign)) {
/* 408 */           this.pos += 1;
/*     */         }
/*     */         
/*     */         do
/*     */         {
/* 413 */           this.pos += 1;
/*     */         }
/* 415 */         while (isDigit(this.charsToProcess[this.pos]));
/* 416 */         boolean isFloat = false;
/* 417 */         if (isFloatSuffix(this.charsToProcess[this.pos])) {
/* 418 */           isFloat = true;
/* 419 */           endOfNumber = ++this.pos;
/*     */         }
/* 421 */         else if (isDoubleSuffix(this.charsToProcess[this.pos])) {
/* 422 */           endOfNumber = ++this.pos;
/*     */         }
/* 424 */         pushRealToken(subarray(start, this.pos), isFloat, start, this.pos);
/*     */       }
/*     */       else {
/* 427 */         ch = this.charsToProcess[this.pos];
/* 428 */         boolean isFloat = false;
/* 429 */         if (isFloatSuffix(ch)) {
/* 430 */           isReal = true;
/* 431 */           isFloat = true;
/* 432 */           endOfNumber = ++this.pos;
/*     */         }
/* 434 */         else if (isDoubleSuffix(ch)) {
/* 435 */           isReal = true;
/* 436 */           endOfNumber = ++this.pos;
/*     */         }
/* 438 */         if (isReal) {
/* 439 */           pushRealToken(subarray(start, endOfNumber), isFloat, start, endOfNumber);
/*     */         }
/*     */         else
/* 442 */           pushIntToken(subarray(start, endOfNumber), false, start, endOfNumber);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void lexIdentifier() {
/* 448 */     int start = this.pos;
/*     */     do {
/* 450 */       this.pos += 1;
/*     */     }
/* 452 */     while (isIdentifier(this.charsToProcess[this.pos]));
/* 453 */     char[] subarray = subarray(start, this.pos);
/*     */     
/*     */ 
/*     */ 
/* 457 */     if ((this.pos - start == 2) || (this.pos - start == 3)) {
/* 458 */       String asString = new String(subarray).toUpperCase();
/* 459 */       int idx = Arrays.binarySearch(ALTERNATIVE_OPERATOR_NAMES, asString);
/* 460 */       if (idx >= 0) {
/* 461 */         pushOneCharOrTwoCharToken(TokenKind.valueOf(asString), start, subarray);
/* 462 */         return;
/*     */       }
/*     */     }
/* 465 */     this.tokens.add(new Token(TokenKind.IDENTIFIER, subarray, start, this.pos));
/*     */   }
/*     */   
/*     */   private void pushIntToken(char[] data, boolean isLong, int start, int end) {
/* 469 */     if (isLong) {
/* 470 */       this.tokens.add(new Token(TokenKind.LITERAL_LONG, data, start, end));
/*     */     }
/*     */     else {
/* 473 */       this.tokens.add(new Token(TokenKind.LITERAL_INT, data, start, end));
/*     */     }
/*     */   }
/*     */   
/*     */   private void pushHexIntToken(char[] data, boolean isLong, int start, int end) {
/* 478 */     if (data.length == 0) {
/* 479 */       if (isLong) {
/* 480 */         raiseParseException(start, SpelMessage.NOT_A_LONG, new Object[] { this.expressionString.substring(start, end + 1) });
/*     */       }
/*     */       else {
/* 483 */         raiseParseException(start, SpelMessage.NOT_AN_INTEGER, new Object[] { this.expressionString.substring(start, end) });
/*     */       }
/*     */     }
/* 486 */     if (isLong) {
/* 487 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXLONG, data, start, end));
/*     */     }
/*     */     else {
/* 490 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXINT, data, start, end));
/*     */     }
/*     */   }
/*     */   
/*     */   private void pushRealToken(char[] data, boolean isFloat, int start, int end) {
/* 495 */     if (isFloat) {
/* 496 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL_FLOAT, data, start, end));
/*     */     }
/*     */     else {
/* 499 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL, data, start, end));
/*     */     }
/*     */   }
/*     */   
/*     */   private char[] subarray(int start, int end) {
/* 504 */     char[] result = new char[end - start];
/* 505 */     System.arraycopy(this.charsToProcess, start, result, 0, end - start);
/* 506 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isTwoCharToken(TokenKind kind)
/*     */   {
/* 513 */     return (kind.tokenChars.length == 2) && (this.charsToProcess[this.pos] == kind.tokenChars[0]) && (this.charsToProcess[(this.pos + 1)] == kind.tokenChars[1]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void pushCharToken(TokenKind kind)
/*     */   {
/* 522 */     this.tokens.add(new Token(kind, this.pos, this.pos + 1));
/* 523 */     this.pos += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void pushPairToken(TokenKind kind)
/*     */   {
/* 530 */     this.tokens.add(new Token(kind, this.pos, this.pos + 2));
/* 531 */     this.pos += 2;
/*     */   }
/*     */   
/*     */   private void pushOneCharOrTwoCharToken(TokenKind kind, int pos, char[] data) {
/* 535 */     this.tokens.add(new Token(kind, data, pos, pos + kind.getLength()));
/*     */   }
/*     */   
/*     */   private boolean isIdentifier(char ch)
/*     */   {
/* 540 */     return (isAlphabetic(ch)) || (isDigit(ch)) || (ch == '_') || (ch == '$');
/*     */   }
/*     */   
/*     */   private boolean isChar(char a, char b) {
/* 544 */     char ch = this.charsToProcess[this.pos];
/* 545 */     return (ch == a) || (ch == b);
/*     */   }
/*     */   
/*     */   private boolean isExponentChar(char ch) {
/* 549 */     return (ch == 'e') || (ch == 'E');
/*     */   }
/*     */   
/*     */   private boolean isFloatSuffix(char ch) {
/* 553 */     return (ch == 'f') || (ch == 'F');
/*     */   }
/*     */   
/*     */   private boolean isDoubleSuffix(char ch) {
/* 557 */     return (ch == 'd') || (ch == 'D');
/*     */   }
/*     */   
/*     */   private boolean isSign(char ch) {
/* 561 */     return (ch == '+') || (ch == '-');
/*     */   }
/*     */   
/*     */   private boolean isDigit(char ch) {
/* 565 */     if (ch > 'ÿ') {
/* 566 */       return false;
/*     */     }
/* 568 */     return (FLAGS[ch] & 0x1) != 0;
/*     */   }
/*     */   
/*     */   private boolean isAlphabetic(char ch) {
/* 572 */     if (ch > 'ÿ') {
/* 573 */       return false;
/*     */     }
/* 575 */     return (FLAGS[ch] & 0x4) != 0;
/*     */   }
/*     */   
/*     */   private boolean isHexadecimalDigit(char ch) {
/* 579 */     if (ch > 'ÿ') {
/* 580 */       return false;
/*     */     }
/* 582 */     return (FLAGS[ch] & 0x2) != 0;
/*     */   }
/*     */   
/*     */   private boolean isExhausted() {
/* 586 */     return this.pos == this.max - 1;
/*     */   }
/*     */   
/*     */   private void raiseParseException(int start, SpelMessage msg, Object... inserts) {
/* 590 */     throw new InternalParseException(new SpelParseException(this.expressionString, start, msg, inserts));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\standard\Tokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */