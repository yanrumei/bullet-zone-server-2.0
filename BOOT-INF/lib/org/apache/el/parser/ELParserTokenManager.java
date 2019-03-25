/*      */ package org.apache.el.parser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Deque;
/*      */ 
/*      */ public class ELParserTokenManager implements ELParserConstants
/*      */ {
/*    8 */   Deque<Integer> deque = new java.util.ArrayDeque();
/*      */   
/*      */ 
/*   11 */   public java.io.PrintStream debugStream = System.out;
/*      */   
/*   13 */   public void setDebugStream(java.io.PrintStream ds) { this.debugStream = ds; }
/*      */   
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*   16 */     switch (pos)
/*      */     {
/*      */     case 0: 
/*   19 */       if ((active0 & 0xC) != 0L)
/*      */       {
/*   21 */         this.jjmatchedKind = 1;
/*   22 */         return 5;
/*      */       }
/*   24 */       return -1;
/*      */     }
/*   26 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0)
/*      */   {
/*   31 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private int jjStopAtPos(int pos, int kind) {
/*   35 */     this.jjmatchedKind = kind;
/*   36 */     this.jjmatchedPos = pos;
/*   37 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa0_0() {
/*   41 */     switch (this.curChar)
/*      */     {
/*      */     case '#': 
/*   44 */       return jjMoveStringLiteralDfa1_0(8L);
/*      */     case '$': 
/*   46 */       return jjMoveStringLiteralDfa1_0(4L);
/*      */     }
/*   48 */     return jjMoveNfa_0(7, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*   53 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*   55 */       jjStopStringLiteralDfa_0(0, active0);
/*   56 */       return 1;
/*      */     }
/*   58 */     switch (this.curChar)
/*      */     {
/*      */     case '{': 
/*   61 */       if ((active0 & 0x4) != 0L)
/*   62 */         return jjStopAtPos(1, 2);
/*   63 */       if ((active0 & 0x8) != 0L) {
/*   64 */         return jjStopAtPos(1, 3);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*   69 */     return jjStartNfa_0(0, active0); }
/*      */   
/*   71 */   static final long[] jjbitVec0 = { -2L, -1L, -1L, -1L };
/*      */   
/*      */ 
/*   74 */   static final long[] jjbitVec2 = { 0L, 0L, -1L, -1L };
/*      */   
/*      */ 
/*      */   private int jjMoveNfa_0(int startState, int curPos)
/*      */   {
/*   79 */     int startsAt = 0;
/*   80 */     this.jjnewStateCnt = 8;
/*   81 */     int i = 1;
/*   82 */     this.jjstateSet[0] = startState;
/*   83 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/*   86 */       if (++this.jjround == Integer.MAX_VALUE)
/*   87 */         ReInitRounds();
/*   88 */       if (this.curChar < '@')
/*      */       {
/*   90 */         long l = 1L << this.curChar;
/*      */         do
/*      */         {
/*   93 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 7: 
/*   96 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*      */             {
/*   98 */               if (kind > 1)
/*   99 */                 kind = 1;
/*  100 */               jjCheckNAddStates(0, 4);
/*      */             }
/*  102 */             else if ((0x1800000000 & l) != 0L)
/*      */             {
/*  104 */               if (kind > 1)
/*  105 */                 kind = 1;
/*  106 */               jjCheckNAdd(5);
/*      */             }
/*  108 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*  109 */               jjCheckNAddTwoStates(0, 1);
/*      */             break;
/*      */           case 0: 
/*  112 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*  113 */               jjCheckNAddTwoStates(0, 1);
/*      */             break;
/*      */           case 2: 
/*  116 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*      */             {
/*  118 */               if (kind > 1)
/*  119 */                 kind = 1;
/*  120 */               jjCheckNAddStates(0, 4); }
/*  121 */             break;
/*      */           case 3: 
/*  123 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*  124 */               jjCheckNAddTwoStates(3, 4);
/*      */             break;
/*      */           case 4: 
/*  127 */             if ((0x1800000000 & l) != 0L)
/*  128 */               jjCheckNAdd(5);
/*      */             break;
/*      */           case 5: 
/*  131 */             if ((0xFFFFFFE7FFFFFFFF & l) != 0L)
/*      */             {
/*  133 */               if (kind > 1)
/*  134 */                 kind = 1;
/*  135 */               jjCheckNAddStates(5, 8); }
/*  136 */             break;
/*      */           case 6: 
/*  138 */             if ((0x1800000000 & l) != 0L)
/*      */             {
/*  140 */               if (kind > 1)
/*  141 */                 kind = 1;
/*  142 */               jjCheckNAddStates(9, 13);
/*      */             }
/*      */             break;
/*      */           }
/*  146 */         } while (i != startsAt);
/*      */       }
/*  148 */       else if (this.curChar < '')
/*      */       {
/*  150 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/*  153 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 7: 
/*  156 */             if (kind > 1)
/*  157 */               kind = 1;
/*  158 */             jjCheckNAddStates(0, 4);
/*  159 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L) {
/*  160 */               jjCheckNAddTwoStates(0, 1);
/*  161 */             } else if (this.curChar == '\\')
/*      */             {
/*  163 */               if (kind > 1)
/*  164 */                 kind = 1;
/*  165 */               jjCheckNAddStates(14, 17);
/*      */             }
/*      */             break;
/*      */           case 0: 
/*  169 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/*  170 */               jjCheckNAddTwoStates(0, 1);
/*      */             break;
/*      */           case 1: 
/*  173 */             if (this.curChar == '\\')
/*      */             {
/*  175 */               if (kind > 1)
/*  176 */                 kind = 1;
/*  177 */               jjCheckNAddStates(14, 17); }
/*  178 */             break;
/*      */           case 2: 
/*  180 */             if (kind > 1)
/*  181 */               kind = 1;
/*  182 */             jjCheckNAddStates(0, 4);
/*  183 */             break;
/*      */           case 3: 
/*  185 */             jjCheckNAddTwoStates(3, 4);
/*  186 */             break;
/*      */           case 5: 
/*  188 */             if ((0xF7FFFFFFEFFFFFFF & l) != 0L)
/*      */             {
/*  190 */               if (kind > 1)
/*  191 */                 kind = 1;
/*  192 */               jjCheckNAddStates(5, 8);
/*      */             }
/*      */             break;
/*      */           }
/*  196 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/*  200 */         int hiByte = this.curChar >> '\b';
/*  201 */         int i1 = hiByte >> 6;
/*  202 */         long l1 = 1L << (hiByte & 0x3F);
/*  203 */         int i2 = (this.curChar & 0xFF) >> '\006';
/*  204 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/*  207 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 7: 
/*  210 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*  211 */               jjCheckNAddTwoStates(0, 1);
/*  212 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */             {
/*  214 */               if (kind > 1)
/*  215 */                 kind = 1;
/*  216 */               jjCheckNAddStates(0, 4);
/*      */             }
/*      */             break;
/*      */           case 0: 
/*  220 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*  221 */               jjCheckNAddTwoStates(0, 1);
/*      */             break;
/*      */           case 2: 
/*  224 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */             {
/*  226 */               if (kind > 1)
/*  227 */                 kind = 1;
/*  228 */               jjCheckNAddStates(0, 4); }
/*  229 */             break;
/*      */           case 3: 
/*  231 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*  232 */               jjCheckNAddTwoStates(3, 4);
/*      */             break;
/*      */           case 5: 
/*  235 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */             {
/*  237 */               if (kind > 1)
/*  238 */                 kind = 1;
/*  239 */               jjCheckNAddStates(5, 8);
/*      */             }
/*      */             break;
/*      */           }
/*  243 */         } while (i != startsAt);
/*      */       }
/*  245 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/*  247 */         this.jjmatchedKind = kind;
/*  248 */         this.jjmatchedPos = curPos;
/*  249 */         kind = Integer.MAX_VALUE;
/*      */       }
/*  251 */       curPos++;
/*  252 */       if ((i = this.jjnewStateCnt) == (startsAt = 8 - (this.jjnewStateCnt = startsAt)))
/*  253 */         return curPos;
/*  254 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException e) {} }
/*  255 */     return curPos;
/*      */   }
/*      */   
/*      */   private final int jjStopStringLiteralDfa_2(int pos, long active0)
/*      */   {
/*  260 */     switch (pos)
/*      */     {
/*      */     case 0: 
/*  263 */       if ((active0 & 0x20000) != 0L)
/*  264 */         return 1;
/*  265 */       if ((active0 & 0x141D555401C000) != 0L)
/*      */       {
/*  267 */         this.jjmatchedKind = 56;
/*  268 */         return 30;
/*      */       }
/*  270 */       return -1;
/*      */     case 1: 
/*  272 */       if ((active0 & 0x41554000000) != 0L)
/*  273 */         return 30;
/*  274 */       if ((active0 & 0x1419400001C000) != 0L)
/*      */       {
/*  276 */         this.jjmatchedKind = 56;
/*  277 */         this.jjmatchedPos = 1;
/*  278 */         return 30;
/*      */       }
/*  280 */       return -1;
/*      */     case 2: 
/*  282 */       if ((active0 & 0x14014000000000) != 0L)
/*  283 */         return 30;
/*  284 */       if ((active0 & 0x18000001C000) != 0L)
/*      */       {
/*  286 */         this.jjmatchedKind = 56;
/*  287 */         this.jjmatchedPos = 2;
/*  288 */         return 30;
/*      */       }
/*  290 */       return -1;
/*      */     case 3: 
/*  292 */       if ((active0 & 0x14000) != 0L)
/*  293 */         return 30;
/*  294 */       if ((active0 & 0x180000008000) != 0L)
/*      */       {
/*  296 */         this.jjmatchedKind = 56;
/*  297 */         this.jjmatchedPos = 3;
/*  298 */         return 30;
/*      */       }
/*  300 */       return -1;
/*      */     case 4: 
/*  302 */       if ((active0 & 0x80000008000) != 0L)
/*  303 */         return 30;
/*  304 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/*  306 */         this.jjmatchedKind = 56;
/*  307 */         this.jjmatchedPos = 4;
/*  308 */         return 30;
/*      */       }
/*  310 */       return -1;
/*      */     case 5: 
/*  312 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/*  314 */         this.jjmatchedKind = 56;
/*  315 */         this.jjmatchedPos = 5;
/*  316 */         return 30;
/*      */       }
/*  318 */       return -1;
/*      */     case 6: 
/*  320 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/*  322 */         this.jjmatchedKind = 56;
/*  323 */         this.jjmatchedPos = 6;
/*  324 */         return 30;
/*      */       }
/*  326 */       return -1;
/*      */     case 7: 
/*  328 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/*  330 */         this.jjmatchedKind = 56;
/*  331 */         this.jjmatchedPos = 7;
/*  332 */         return 30;
/*      */       }
/*  334 */       return -1;
/*      */     case 8: 
/*  336 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/*  338 */         this.jjmatchedKind = 56;
/*  339 */         this.jjmatchedPos = 8;
/*  340 */         return 30;
/*      */       }
/*  342 */       return -1;
/*      */     }
/*  344 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_2(int pos, long active0)
/*      */   {
/*  349 */     return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa0_2() {
/*  353 */     switch (this.curChar)
/*      */     {
/*      */     case '!': 
/*  356 */       this.jjmatchedKind = 37;
/*  357 */       return jjMoveStringLiteralDfa1_2(34359738368L);
/*      */     case '%': 
/*  359 */       return jjStopAtPos(0, 51);
/*      */     case '&': 
/*  361 */       return jjMoveStringLiteralDfa1_2(549755813888L);
/*      */     case '(': 
/*  363 */       return jjStopAtPos(0, 18);
/*      */     case ')': 
/*  365 */       return jjStopAtPos(0, 19);
/*      */     case '*': 
/*  367 */       return jjStopAtPos(0, 45);
/*      */     case '+': 
/*  369 */       this.jjmatchedKind = 46;
/*  370 */       return jjMoveStringLiteralDfa1_2(9007199254740992L);
/*      */     case ',': 
/*  372 */       return jjStopAtPos(0, 24);
/*      */     case '-': 
/*  374 */       this.jjmatchedKind = 47;
/*  375 */       return jjMoveStringLiteralDfa1_2(36028797018963968L);
/*      */     case '.': 
/*  377 */       return jjStartNfaWithStates_2(0, 17, 1);
/*      */     case '/': 
/*  379 */       return jjStopAtPos(0, 49);
/*      */     case ':': 
/*  381 */       return jjStopAtPos(0, 22);
/*      */     case ';': 
/*  383 */       return jjStopAtPos(0, 23);
/*      */     case '<': 
/*  385 */       this.jjmatchedKind = 27;
/*  386 */       return jjMoveStringLiteralDfa1_2(2147483648L);
/*      */     case '=': 
/*  388 */       this.jjmatchedKind = 54;
/*  389 */       return jjMoveStringLiteralDfa1_2(8589934592L);
/*      */     case '>': 
/*  391 */       this.jjmatchedKind = 25;
/*  392 */       return jjMoveStringLiteralDfa1_2(536870912L);
/*      */     case '?': 
/*  394 */       return jjStopAtPos(0, 48);
/*      */     case '[': 
/*  396 */       return jjStopAtPos(0, 20);
/*      */     case ']': 
/*  398 */       return jjStopAtPos(0, 21);
/*      */     case 'a': 
/*  400 */       return jjMoveStringLiteralDfa1_2(1099511627776L);
/*      */     case 'd': 
/*  402 */       return jjMoveStringLiteralDfa1_2(1125899906842624L);
/*      */     case 'e': 
/*  404 */       return jjMoveStringLiteralDfa1_2(8813272891392L);
/*      */     case 'f': 
/*  406 */       return jjMoveStringLiteralDfa1_2(32768L);
/*      */     case 'g': 
/*  408 */       return jjMoveStringLiteralDfa1_2(1140850688L);
/*      */     case 'i': 
/*  410 */       return jjMoveStringLiteralDfa1_2(17592186044416L);
/*      */     case 'l': 
/*  412 */       return jjMoveStringLiteralDfa1_2(4563402752L);
/*      */     case 'm': 
/*  414 */       return jjMoveStringLiteralDfa1_2(4503599627370496L);
/*      */     case 'n': 
/*  416 */       return jjMoveStringLiteralDfa1_2(343597449216L);
/*      */     case 'o': 
/*  418 */       return jjMoveStringLiteralDfa1_2(4398046511104L);
/*      */     case 't': 
/*  420 */       return jjMoveStringLiteralDfa1_2(16384L);
/*      */     case '{': 
/*  422 */       return jjStopAtPos(0, 8);
/*      */     case '|': 
/*  424 */       return jjMoveStringLiteralDfa1_2(2199023255552L);
/*      */     case '}': 
/*  426 */       return jjStopAtPos(0, 9);
/*      */     }
/*  428 */     return jjMoveNfa_2(0, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa1_2(long active0) {
/*      */     try {
/*  433 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  435 */       jjStopStringLiteralDfa_2(0, active0);
/*  436 */       return 1;
/*      */     }
/*  438 */     switch (this.curChar)
/*      */     {
/*      */     case '&': 
/*  441 */       if ((active0 & 0x8000000000) != 0L)
/*  442 */         return jjStopAtPos(1, 39);
/*      */       break;
/*      */     case '=': 
/*  445 */       if ((active0 & 0x20000000) != 0L)
/*  446 */         return jjStopAtPos(1, 29);
/*  447 */       if ((active0 & 0x80000000) != 0L)
/*  448 */         return jjStopAtPos(1, 31);
/*  449 */       if ((active0 & 0x200000000) != 0L)
/*  450 */         return jjStopAtPos(1, 33);
/*  451 */       if ((active0 & 0x800000000) != 0L)
/*  452 */         return jjStopAtPos(1, 35);
/*  453 */       if ((active0 & 0x20000000000000) != 0L)
/*  454 */         return jjStopAtPos(1, 53);
/*      */       break;
/*      */     case '>': 
/*  457 */       if ((active0 & 0x80000000000000) != 0L)
/*  458 */         return jjStopAtPos(1, 55);
/*      */       break;
/*      */     case 'a': 
/*  461 */       return jjMoveStringLiteralDfa2_2(active0, 32768L);
/*      */     case 'e': 
/*  463 */       if ((active0 & 0x40000000) != 0L)
/*  464 */         return jjStartNfaWithStates_2(1, 30, 30);
/*  465 */       if ((active0 & 0x100000000) != 0L)
/*  466 */         return jjStartNfaWithStates_2(1, 32, 30);
/*  467 */       if ((active0 & 0x1000000000) != 0L)
/*  468 */         return jjStartNfaWithStates_2(1, 36, 30);
/*      */       break;
/*      */     case 'i': 
/*  471 */       return jjMoveStringLiteralDfa2_2(active0, 1125899906842624L);
/*      */     case 'm': 
/*  473 */       return jjMoveStringLiteralDfa2_2(active0, 8796093022208L);
/*      */     case 'n': 
/*  475 */       return jjMoveStringLiteralDfa2_2(active0, 18691697672192L);
/*      */     case 'o': 
/*  477 */       return jjMoveStringLiteralDfa2_2(active0, 4503874505277440L);
/*      */     case 'q': 
/*  479 */       if ((active0 & 0x400000000) != 0L)
/*  480 */         return jjStartNfaWithStates_2(1, 34, 30);
/*      */       break;
/*      */     case 'r': 
/*  483 */       if ((active0 & 0x40000000000) != 0L)
/*  484 */         return jjStartNfaWithStates_2(1, 42, 30);
/*  485 */       return jjMoveStringLiteralDfa2_2(active0, 16384L);
/*      */     case 't': 
/*  487 */       if ((active0 & 0x4000000) != 0L)
/*  488 */         return jjStartNfaWithStates_2(1, 26, 30);
/*  489 */       if ((active0 & 0x10000000) != 0L)
/*  490 */         return jjStartNfaWithStates_2(1, 28, 30);
/*      */       break;
/*      */     case 'u': 
/*  493 */       return jjMoveStringLiteralDfa2_2(active0, 65536L);
/*      */     case '|': 
/*  495 */       if ((active0 & 0x20000000000) != 0L) {
/*  496 */         return jjStopAtPos(1, 41);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  501 */     return jjStartNfa_2(0, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa2_2(long old0, long active0) {
/*  505 */     if ((active0 &= old0) == 0L)
/*  506 */       return jjStartNfa_2(0, old0);
/*  507 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  509 */       jjStopStringLiteralDfa_2(1, active0);
/*  510 */       return 2;
/*      */     }
/*  512 */     switch (this.curChar)
/*      */     {
/*      */     case 'd': 
/*  515 */       if ((active0 & 0x10000000000) != 0L)
/*  516 */         return jjStartNfaWithStates_2(2, 40, 30);
/*  517 */       if ((active0 & 0x10000000000000) != 0L)
/*  518 */         return jjStartNfaWithStates_2(2, 52, 30);
/*      */       break;
/*      */     case 'l': 
/*  521 */       return jjMoveStringLiteralDfa3_2(active0, 98304L);
/*      */     case 'p': 
/*  523 */       return jjMoveStringLiteralDfa3_2(active0, 8796093022208L);
/*      */     case 's': 
/*  525 */       return jjMoveStringLiteralDfa3_2(active0, 17592186044416L);
/*      */     case 't': 
/*  527 */       if ((active0 & 0x4000000000) != 0L)
/*  528 */         return jjStartNfaWithStates_2(2, 38, 30);
/*      */       break;
/*      */     case 'u': 
/*  531 */       return jjMoveStringLiteralDfa3_2(active0, 16384L);
/*      */     case 'v': 
/*  533 */       if ((active0 & 0x4000000000000) != 0L) {
/*  534 */         return jjStartNfaWithStates_2(2, 50, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  539 */     return jjStartNfa_2(1, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa3_2(long old0, long active0) {
/*  543 */     if ((active0 &= old0) == 0L)
/*  544 */       return jjStartNfa_2(1, old0);
/*  545 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  547 */       jjStopStringLiteralDfa_2(2, active0);
/*  548 */       return 3;
/*      */     }
/*  550 */     switch (this.curChar)
/*      */     {
/*      */     case 'e': 
/*  553 */       if ((active0 & 0x4000) != 0L)
/*  554 */         return jjStartNfaWithStates_2(3, 14, 30);
/*      */       break;
/*      */     case 'l': 
/*  557 */       if ((active0 & 0x10000) != 0L)
/*  558 */         return jjStartNfaWithStates_2(3, 16, 30);
/*      */       break;
/*      */     case 's': 
/*  561 */       return jjMoveStringLiteralDfa4_2(active0, 32768L);
/*      */     case 't': 
/*  563 */       return jjMoveStringLiteralDfa4_2(active0, 26388279066624L);
/*      */     }
/*      */     
/*      */     
/*  567 */     return jjStartNfa_2(2, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa4_2(long old0, long active0) {
/*  571 */     if ((active0 &= old0) == 0L)
/*  572 */       return jjStartNfa_2(2, old0);
/*  573 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  575 */       jjStopStringLiteralDfa_2(3, active0);
/*  576 */       return 4;
/*      */     }
/*  578 */     switch (this.curChar)
/*      */     {
/*      */     case 'a': 
/*  581 */       return jjMoveStringLiteralDfa5_2(active0, 17592186044416L);
/*      */     case 'e': 
/*  583 */       if ((active0 & 0x8000) != 0L)
/*  584 */         return jjStartNfaWithStates_2(4, 15, 30);
/*      */       break;
/*      */     case 'y': 
/*  587 */       if ((active0 & 0x80000000000) != 0L) {
/*  588 */         return jjStartNfaWithStates_2(4, 43, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  593 */     return jjStartNfa_2(3, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa5_2(long old0, long active0) {
/*  597 */     if ((active0 &= old0) == 0L)
/*  598 */       return jjStartNfa_2(3, old0);
/*  599 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  601 */       jjStopStringLiteralDfa_2(4, active0);
/*  602 */       return 5;
/*      */     }
/*  604 */     switch (this.curChar)
/*      */     {
/*      */     case 'n': 
/*  607 */       return jjMoveStringLiteralDfa6_2(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/*  611 */     return jjStartNfa_2(4, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa6_2(long old0, long active0) {
/*  615 */     if ((active0 &= old0) == 0L)
/*  616 */       return jjStartNfa_2(4, old0);
/*  617 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  619 */       jjStopStringLiteralDfa_2(5, active0);
/*  620 */       return 6;
/*      */     }
/*  622 */     switch (this.curChar)
/*      */     {
/*      */     case 'c': 
/*  625 */       return jjMoveStringLiteralDfa7_2(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/*  629 */     return jjStartNfa_2(5, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa7_2(long old0, long active0) {
/*  633 */     if ((active0 &= old0) == 0L)
/*  634 */       return jjStartNfa_2(5, old0);
/*  635 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  637 */       jjStopStringLiteralDfa_2(6, active0);
/*  638 */       return 7;
/*      */     }
/*  640 */     switch (this.curChar)
/*      */     {
/*      */     case 'e': 
/*  643 */       return jjMoveStringLiteralDfa8_2(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/*  647 */     return jjStartNfa_2(6, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa8_2(long old0, long active0) {
/*  651 */     if ((active0 &= old0) == 0L)
/*  652 */       return jjStartNfa_2(6, old0);
/*  653 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  655 */       jjStopStringLiteralDfa_2(7, active0);
/*  656 */       return 8;
/*      */     }
/*  658 */     switch (this.curChar)
/*      */     {
/*      */     case 'o': 
/*  661 */       return jjMoveStringLiteralDfa9_2(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/*  665 */     return jjStartNfa_2(7, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa9_2(long old0, long active0) {
/*  669 */     if ((active0 &= old0) == 0L)
/*  670 */       return jjStartNfa_2(7, old0);
/*  671 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/*  673 */       jjStopStringLiteralDfa_2(8, active0);
/*  674 */       return 9;
/*      */     }
/*  676 */     switch (this.curChar)
/*      */     {
/*      */     case 'f': 
/*  679 */       if ((active0 & 0x100000000000) != 0L) {
/*  680 */         return jjStartNfaWithStates_2(9, 44, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  685 */     return jjStartNfa_2(8, active0);
/*      */   }
/*      */   
/*      */   private int jjStartNfaWithStates_2(int pos, int kind, int state) {
/*  689 */     this.jjmatchedKind = kind;
/*  690 */     this.jjmatchedPos = pos;
/*  691 */     try { this.curChar = this.input_stream.readChar();
/*  692 */     } catch (IOException e) { return pos + 1; }
/*  693 */     return jjMoveNfa_2(state, pos + 1); }
/*      */   
/*  695 */   static final long[] jjbitVec3 = { 2301339413881290750L, -16384L, 4294967295L, 432345564227567616L };
/*      */   
/*      */ 
/*  698 */   static final long[] jjbitVec4 = { 0L, 0L, 0L, -36028797027352577L };
/*      */   
/*      */ 
/*  701 */   static final long[] jjbitVec5 = { 0L, -1L, -1L, -1L };
/*      */   
/*      */ 
/*  704 */   static final long[] jjbitVec6 = { -1L, -1L, 65535L, 0L };
/*      */   
/*      */ 
/*  707 */   static final long[] jjbitVec7 = { -1L, -1L, 0L, 0L };
/*      */   
/*      */ 
/*  710 */   static final long[] jjbitVec8 = { 70368744177663L, 0L, 0L, 0L };
/*      */   
/*      */ 
/*      */   private int jjMoveNfa_2(int startState, int curPos)
/*      */   {
/*  715 */     int startsAt = 0;
/*  716 */     this.jjnewStateCnt = 30;
/*  717 */     int i = 1;
/*  718 */     this.jjstateSet[0] = startState;
/*  719 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/*  722 */       if (++this.jjround == Integer.MAX_VALUE)
/*  723 */         ReInitRounds();
/*  724 */       if (this.curChar < '@')
/*      */       {
/*  726 */         long l = 1L << this.curChar;
/*      */         do
/*      */         {
/*  729 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/*  732 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  734 */               if (kind > 10)
/*  735 */                 kind = 10;
/*  736 */               jjCheckNAddStates(18, 22);
/*      */             }
/*  738 */             else if ((0x1800000000 & l) != 0L)
/*      */             {
/*  740 */               if (kind > 56)
/*  741 */                 kind = 56;
/*  742 */               jjCheckNAddTwoStates(28, 29);
/*      */             }
/*  744 */             else if (this.curChar == '\'') {
/*  745 */               jjCheckNAddStates(23, 25);
/*  746 */             } else if (this.curChar == '"') {
/*  747 */               jjCheckNAddStates(26, 28);
/*  748 */             } else if (this.curChar == '.') {
/*  749 */               jjCheckNAdd(1);
/*      */             }
/*      */             break;
/*  752 */           case 30:  if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  754 */               if (kind > 57)
/*  755 */                 kind = 57;
/*  756 */               jjCheckNAdd(29);
/*      */             }
/*  758 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  760 */               if (kind > 56)
/*  761 */                 kind = 56;
/*  762 */               jjCheckNAdd(28);
/*      */             }
/*      */             break;
/*      */           case 1: 
/*  766 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  768 */               if (kind > 11)
/*  769 */                 kind = 11;
/*  770 */               jjCheckNAddTwoStates(1, 2); }
/*  771 */             break;
/*      */           case 3: 
/*  773 */             if ((0x280000000000 & l) != 0L)
/*  774 */               jjCheckNAdd(4);
/*      */             break;
/*      */           case 4: 
/*  777 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  779 */               if (kind > 11)
/*  780 */                 kind = 11;
/*  781 */               jjCheckNAdd(4); }
/*  782 */             break;
/*      */           case 5: 
/*  784 */             if (this.curChar == '"')
/*  785 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 6: 
/*  788 */             if ((0xFFFFFFFBFFFFFFFF & l) != 0L)
/*  789 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 8: 
/*  792 */             if ((0x8400000000 & l) != 0L)
/*  793 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 9: 
/*  796 */             if ((this.curChar == '"') && (kind > 13))
/*  797 */               kind = 13;
/*      */             break;
/*      */           case 10: 
/*  800 */             if (this.curChar == '\'')
/*  801 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 11: 
/*  804 */             if ((0xFFFFFF7FFFFFFFFF & l) != 0L)
/*  805 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 13: 
/*  808 */             if ((0x8400000000 & l) != 0L)
/*  809 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 14: 
/*  812 */             if ((this.curChar == '\'') && (kind > 13))
/*  813 */               kind = 13;
/*      */             break;
/*      */           case 15: 
/*  816 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  818 */               if (kind > 10)
/*  819 */                 kind = 10;
/*  820 */               jjCheckNAddStates(18, 22); }
/*  821 */             break;
/*      */           case 16: 
/*  823 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  825 */               if (kind > 10)
/*  826 */                 kind = 10;
/*  827 */               jjCheckNAdd(16); }
/*  828 */             break;
/*      */           case 17: 
/*  830 */             if ((0x3FF000000000000 & l) != 0L)
/*  831 */               jjCheckNAddTwoStates(17, 18);
/*      */             break;
/*      */           case 18: 
/*  834 */             if (this.curChar == '.')
/*      */             {
/*  836 */               if (kind > 11)
/*  837 */                 kind = 11;
/*  838 */               jjCheckNAddTwoStates(19, 20); }
/*  839 */             break;
/*      */           case 19: 
/*  841 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  843 */               if (kind > 11)
/*  844 */                 kind = 11;
/*  845 */               jjCheckNAddTwoStates(19, 20); }
/*  846 */             break;
/*      */           case 21: 
/*  848 */             if ((0x280000000000 & l) != 0L)
/*  849 */               jjCheckNAdd(22);
/*      */             break;
/*      */           case 22: 
/*  852 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  854 */               if (kind > 11)
/*  855 */                 kind = 11;
/*  856 */               jjCheckNAdd(22); }
/*  857 */             break;
/*      */           case 23: 
/*  859 */             if ((0x3FF000000000000 & l) != 0L)
/*  860 */               jjCheckNAddTwoStates(23, 24);
/*      */             break;
/*      */           case 25: 
/*  863 */             if ((0x280000000000 & l) != 0L)
/*  864 */               jjCheckNAdd(26);
/*      */             break;
/*      */           case 26: 
/*  867 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  869 */               if (kind > 11)
/*  870 */                 kind = 11;
/*  871 */               jjCheckNAdd(26); }
/*  872 */             break;
/*      */           case 27: 
/*  874 */             if ((0x1800000000 & l) != 0L)
/*      */             {
/*  876 */               if (kind > 56)
/*  877 */                 kind = 56;
/*  878 */               jjCheckNAddTwoStates(28, 29); }
/*  879 */             break;
/*      */           case 28: 
/*  881 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  883 */               if (kind > 56)
/*  884 */                 kind = 56;
/*  885 */               jjCheckNAdd(28); }
/*  886 */             break;
/*      */           case 29: 
/*  888 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  890 */               if (kind > 57)
/*  891 */                 kind = 57;
/*  892 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/*  896 */         } while (i != startsAt);
/*      */       }
/*  898 */       else if (this.curChar < '')
/*      */       {
/*  900 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/*  903 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/*  906 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/*  908 */               if (kind > 56)
/*  909 */                 kind = 56;
/*  910 */               jjCheckNAddTwoStates(28, 29); }
/*  911 */             break;
/*      */           case 30: 
/*  913 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/*  915 */               if (kind > 57)
/*  916 */                 kind = 57;
/*  917 */               jjCheckNAdd(29);
/*      */             }
/*  919 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/*  921 */               if (kind > 56)
/*  922 */                 kind = 56;
/*  923 */               jjCheckNAdd(28);
/*      */             }
/*      */             break;
/*      */           case 2: 
/*  927 */             if ((0x2000000020 & l) != 0L)
/*  928 */               jjAddStates(29, 30);
/*      */             break;
/*      */           case 6: 
/*  931 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/*  932 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 7: 
/*  935 */             if (this.curChar == '\\')
/*  936 */               this.jjstateSet[(this.jjnewStateCnt++)] = 8;
/*      */             break;
/*      */           case 8: 
/*  939 */             if (this.curChar == '\\')
/*  940 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 11: 
/*  943 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/*  944 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 12: 
/*  947 */             if (this.curChar == '\\')
/*  948 */               this.jjstateSet[(this.jjnewStateCnt++)] = 13;
/*      */             break;
/*      */           case 13: 
/*  951 */             if (this.curChar == '\\')
/*  952 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 20: 
/*  955 */             if ((0x2000000020 & l) != 0L)
/*  956 */               jjAddStates(31, 32);
/*      */             break;
/*      */           case 24: 
/*  959 */             if ((0x2000000020 & l) != 0L)
/*  960 */               jjAddStates(33, 34);
/*      */             break;
/*      */           case 28: 
/*  963 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/*  965 */               if (kind > 56)
/*  966 */                 kind = 56;
/*  967 */               jjCheckNAdd(28); }
/*  968 */             break;
/*      */           case 29: 
/*  970 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/*  972 */               if (kind > 57)
/*  973 */                 kind = 57;
/*  974 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/*  978 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/*  982 */         int hiByte = this.curChar >> '\b';
/*  983 */         int i1 = hiByte >> 6;
/*  984 */         long l1 = 1L << (hiByte & 0x3F);
/*  985 */         int i2 = (this.curChar & 0xFF) >> '\006';
/*  986 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/*  989 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/*  992 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/*  994 */               if (kind > 56)
/*  995 */                 kind = 56;
/*  996 */               jjCheckNAddTwoStates(28, 29); }
/*  997 */             break;
/*      */           case 30: 
/*  999 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1001 */               if (kind > 56)
/* 1002 */                 kind = 56;
/* 1003 */               jjCheckNAdd(28);
/*      */             }
/* 1005 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1007 */               if (kind > 57)
/* 1008 */                 kind = 57;
/* 1009 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           case 6: 
/* 1013 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1014 */               jjAddStates(26, 28);
/*      */             break;
/*      */           case 11: 
/* 1017 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1018 */               jjAddStates(23, 25);
/*      */             break;
/*      */           case 28: 
/* 1021 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1023 */               if (kind > 56)
/* 1024 */                 kind = 56;
/* 1025 */               jjCheckNAdd(28); }
/* 1026 */             break;
/*      */           case 29: 
/* 1028 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1030 */               if (kind > 57)
/* 1031 */                 kind = 57;
/* 1032 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/* 1036 */         } while (i != startsAt);
/*      */       }
/* 1038 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/* 1040 */         this.jjmatchedKind = kind;
/* 1041 */         this.jjmatchedPos = curPos;
/* 1042 */         kind = Integer.MAX_VALUE;
/*      */       }
/* 1044 */       curPos++;
/* 1045 */       if ((i = this.jjnewStateCnt) == (startsAt = 30 - (this.jjnewStateCnt = startsAt)))
/* 1046 */         return curPos;
/* 1047 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException e) {} }
/* 1048 */     return curPos;
/*      */   }
/*      */   
/*      */   private final int jjStopStringLiteralDfa_1(int pos, long active0)
/*      */   {
/* 1053 */     switch (pos)
/*      */     {
/*      */     case 0: 
/* 1056 */       if ((active0 & 0x20000) != 0L)
/* 1057 */         return 1;
/* 1058 */       if ((active0 & 0x141D555401C000) != 0L)
/*      */       {
/* 1060 */         this.jjmatchedKind = 56;
/* 1061 */         return 30;
/*      */       }
/* 1063 */       return -1;
/*      */     case 1: 
/* 1065 */       if ((active0 & 0x41554000000) != 0L)
/* 1066 */         return 30;
/* 1067 */       if ((active0 & 0x1419400001C000) != 0L)
/*      */       {
/* 1069 */         this.jjmatchedKind = 56;
/* 1070 */         this.jjmatchedPos = 1;
/* 1071 */         return 30;
/*      */       }
/* 1073 */       return -1;
/*      */     case 2: 
/* 1075 */       if ((active0 & 0x14014000000000) != 0L)
/* 1076 */         return 30;
/* 1077 */       if ((active0 & 0x18000001C000) != 0L)
/*      */       {
/* 1079 */         this.jjmatchedKind = 56;
/* 1080 */         this.jjmatchedPos = 2;
/* 1081 */         return 30;
/*      */       }
/* 1083 */       return -1;
/*      */     case 3: 
/* 1085 */       if ((active0 & 0x14000) != 0L)
/* 1086 */         return 30;
/* 1087 */       if ((active0 & 0x180000008000) != 0L)
/*      */       {
/* 1089 */         this.jjmatchedKind = 56;
/* 1090 */         this.jjmatchedPos = 3;
/* 1091 */         return 30;
/*      */       }
/* 1093 */       return -1;
/*      */     case 4: 
/* 1095 */       if ((active0 & 0x80000008000) != 0L)
/* 1096 */         return 30;
/* 1097 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/* 1099 */         this.jjmatchedKind = 56;
/* 1100 */         this.jjmatchedPos = 4;
/* 1101 */         return 30;
/*      */       }
/* 1103 */       return -1;
/*      */     case 5: 
/* 1105 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/* 1107 */         this.jjmatchedKind = 56;
/* 1108 */         this.jjmatchedPos = 5;
/* 1109 */         return 30;
/*      */       }
/* 1111 */       return -1;
/*      */     case 6: 
/* 1113 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/* 1115 */         this.jjmatchedKind = 56;
/* 1116 */         this.jjmatchedPos = 6;
/* 1117 */         return 30;
/*      */       }
/* 1119 */       return -1;
/*      */     case 7: 
/* 1121 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/* 1123 */         this.jjmatchedKind = 56;
/* 1124 */         this.jjmatchedPos = 7;
/* 1125 */         return 30;
/*      */       }
/* 1127 */       return -1;
/*      */     case 8: 
/* 1129 */       if ((active0 & 0x100000000000) != 0L)
/*      */       {
/* 1131 */         this.jjmatchedKind = 56;
/* 1132 */         this.jjmatchedPos = 8;
/* 1133 */         return 30;
/*      */       }
/* 1135 */       return -1;
/*      */     }
/* 1137 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_1(int pos, long active0)
/*      */   {
/* 1142 */     return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa0_1() {
/* 1146 */     switch (this.curChar)
/*      */     {
/*      */     case '!': 
/* 1149 */       this.jjmatchedKind = 37;
/* 1150 */       return jjMoveStringLiteralDfa1_1(34359738368L);
/*      */     case '%': 
/* 1152 */       return jjStopAtPos(0, 51);
/*      */     case '&': 
/* 1154 */       return jjMoveStringLiteralDfa1_1(549755813888L);
/*      */     case '(': 
/* 1156 */       return jjStopAtPos(0, 18);
/*      */     case ')': 
/* 1158 */       return jjStopAtPos(0, 19);
/*      */     case '*': 
/* 1160 */       return jjStopAtPos(0, 45);
/*      */     case '+': 
/* 1162 */       this.jjmatchedKind = 46;
/* 1163 */       return jjMoveStringLiteralDfa1_1(9007199254740992L);
/*      */     case ',': 
/* 1165 */       return jjStopAtPos(0, 24);
/*      */     case '-': 
/* 1167 */       this.jjmatchedKind = 47;
/* 1168 */       return jjMoveStringLiteralDfa1_1(36028797018963968L);
/*      */     case '.': 
/* 1170 */       return jjStartNfaWithStates_1(0, 17, 1);
/*      */     case '/': 
/* 1172 */       return jjStopAtPos(0, 49);
/*      */     case ':': 
/* 1174 */       return jjStopAtPos(0, 22);
/*      */     case ';': 
/* 1176 */       return jjStopAtPos(0, 23);
/*      */     case '<': 
/* 1178 */       this.jjmatchedKind = 27;
/* 1179 */       return jjMoveStringLiteralDfa1_1(2147483648L);
/*      */     case '=': 
/* 1181 */       this.jjmatchedKind = 54;
/* 1182 */       return jjMoveStringLiteralDfa1_1(8589934592L);
/*      */     case '>': 
/* 1184 */       this.jjmatchedKind = 25;
/* 1185 */       return jjMoveStringLiteralDfa1_1(536870912L);
/*      */     case '?': 
/* 1187 */       return jjStopAtPos(0, 48);
/*      */     case '[': 
/* 1189 */       return jjStopAtPos(0, 20);
/*      */     case ']': 
/* 1191 */       return jjStopAtPos(0, 21);
/*      */     case 'a': 
/* 1193 */       return jjMoveStringLiteralDfa1_1(1099511627776L);
/*      */     case 'd': 
/* 1195 */       return jjMoveStringLiteralDfa1_1(1125899906842624L);
/*      */     case 'e': 
/* 1197 */       return jjMoveStringLiteralDfa1_1(8813272891392L);
/*      */     case 'f': 
/* 1199 */       return jjMoveStringLiteralDfa1_1(32768L);
/*      */     case 'g': 
/* 1201 */       return jjMoveStringLiteralDfa1_1(1140850688L);
/*      */     case 'i': 
/* 1203 */       return jjMoveStringLiteralDfa1_1(17592186044416L);
/*      */     case 'l': 
/* 1205 */       return jjMoveStringLiteralDfa1_1(4563402752L);
/*      */     case 'm': 
/* 1207 */       return jjMoveStringLiteralDfa1_1(4503599627370496L);
/*      */     case 'n': 
/* 1209 */       return jjMoveStringLiteralDfa1_1(343597449216L);
/*      */     case 'o': 
/* 1211 */       return jjMoveStringLiteralDfa1_1(4398046511104L);
/*      */     case 't': 
/* 1213 */       return jjMoveStringLiteralDfa1_1(16384L);
/*      */     case '{': 
/* 1215 */       return jjStopAtPos(0, 8);
/*      */     case '|': 
/* 1217 */       return jjMoveStringLiteralDfa1_1(2199023255552L);
/*      */     case '}': 
/* 1219 */       return jjStopAtPos(0, 9);
/*      */     }
/* 1221 */     return jjMoveNfa_1(0, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa1_1(long active0) {
/*      */     try {
/* 1226 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1228 */       jjStopStringLiteralDfa_1(0, active0);
/* 1229 */       return 1;
/*      */     }
/* 1231 */     switch (this.curChar)
/*      */     {
/*      */     case '&': 
/* 1234 */       if ((active0 & 0x8000000000) != 0L)
/* 1235 */         return jjStopAtPos(1, 39);
/*      */       break;
/*      */     case '=': 
/* 1238 */       if ((active0 & 0x20000000) != 0L)
/* 1239 */         return jjStopAtPos(1, 29);
/* 1240 */       if ((active0 & 0x80000000) != 0L)
/* 1241 */         return jjStopAtPos(1, 31);
/* 1242 */       if ((active0 & 0x200000000) != 0L)
/* 1243 */         return jjStopAtPos(1, 33);
/* 1244 */       if ((active0 & 0x800000000) != 0L)
/* 1245 */         return jjStopAtPos(1, 35);
/* 1246 */       if ((active0 & 0x20000000000000) != 0L)
/* 1247 */         return jjStopAtPos(1, 53);
/*      */       break;
/*      */     case '>': 
/* 1250 */       if ((active0 & 0x80000000000000) != 0L)
/* 1251 */         return jjStopAtPos(1, 55);
/*      */       break;
/*      */     case 'a': 
/* 1254 */       return jjMoveStringLiteralDfa2_1(active0, 32768L);
/*      */     case 'e': 
/* 1256 */       if ((active0 & 0x40000000) != 0L)
/* 1257 */         return jjStartNfaWithStates_1(1, 30, 30);
/* 1258 */       if ((active0 & 0x100000000) != 0L)
/* 1259 */         return jjStartNfaWithStates_1(1, 32, 30);
/* 1260 */       if ((active0 & 0x1000000000) != 0L)
/* 1261 */         return jjStartNfaWithStates_1(1, 36, 30);
/*      */       break;
/*      */     case 'i': 
/* 1264 */       return jjMoveStringLiteralDfa2_1(active0, 1125899906842624L);
/*      */     case 'm': 
/* 1266 */       return jjMoveStringLiteralDfa2_1(active0, 8796093022208L);
/*      */     case 'n': 
/* 1268 */       return jjMoveStringLiteralDfa2_1(active0, 18691697672192L);
/*      */     case 'o': 
/* 1270 */       return jjMoveStringLiteralDfa2_1(active0, 4503874505277440L);
/*      */     case 'q': 
/* 1272 */       if ((active0 & 0x400000000) != 0L)
/* 1273 */         return jjStartNfaWithStates_1(1, 34, 30);
/*      */       break;
/*      */     case 'r': 
/* 1276 */       if ((active0 & 0x40000000000) != 0L)
/* 1277 */         return jjStartNfaWithStates_1(1, 42, 30);
/* 1278 */       return jjMoveStringLiteralDfa2_1(active0, 16384L);
/*      */     case 't': 
/* 1280 */       if ((active0 & 0x4000000) != 0L)
/* 1281 */         return jjStartNfaWithStates_1(1, 26, 30);
/* 1282 */       if ((active0 & 0x10000000) != 0L)
/* 1283 */         return jjStartNfaWithStates_1(1, 28, 30);
/*      */       break;
/*      */     case 'u': 
/* 1286 */       return jjMoveStringLiteralDfa2_1(active0, 65536L);
/*      */     case '|': 
/* 1288 */       if ((active0 & 0x20000000000) != 0L) {
/* 1289 */         return jjStopAtPos(1, 41);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/* 1294 */     return jjStartNfa_1(0, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa2_1(long old0, long active0) {
/* 1298 */     if ((active0 &= old0) == 0L)
/* 1299 */       return jjStartNfa_1(0, old0);
/* 1300 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1302 */       jjStopStringLiteralDfa_1(1, active0);
/* 1303 */       return 2;
/*      */     }
/* 1305 */     switch (this.curChar)
/*      */     {
/*      */     case 'd': 
/* 1308 */       if ((active0 & 0x10000000000) != 0L)
/* 1309 */         return jjStartNfaWithStates_1(2, 40, 30);
/* 1310 */       if ((active0 & 0x10000000000000) != 0L)
/* 1311 */         return jjStartNfaWithStates_1(2, 52, 30);
/*      */       break;
/*      */     case 'l': 
/* 1314 */       return jjMoveStringLiteralDfa3_1(active0, 98304L);
/*      */     case 'p': 
/* 1316 */       return jjMoveStringLiteralDfa3_1(active0, 8796093022208L);
/*      */     case 's': 
/* 1318 */       return jjMoveStringLiteralDfa3_1(active0, 17592186044416L);
/*      */     case 't': 
/* 1320 */       if ((active0 & 0x4000000000) != 0L)
/* 1321 */         return jjStartNfaWithStates_1(2, 38, 30);
/*      */       break;
/*      */     case 'u': 
/* 1324 */       return jjMoveStringLiteralDfa3_1(active0, 16384L);
/*      */     case 'v': 
/* 1326 */       if ((active0 & 0x4000000000000) != 0L) {
/* 1327 */         return jjStartNfaWithStates_1(2, 50, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/* 1332 */     return jjStartNfa_1(1, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa3_1(long old0, long active0) {
/* 1336 */     if ((active0 &= old0) == 0L)
/* 1337 */       return jjStartNfa_1(1, old0);
/* 1338 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1340 */       jjStopStringLiteralDfa_1(2, active0);
/* 1341 */       return 3;
/*      */     }
/* 1343 */     switch (this.curChar)
/*      */     {
/*      */     case 'e': 
/* 1346 */       if ((active0 & 0x4000) != 0L)
/* 1347 */         return jjStartNfaWithStates_1(3, 14, 30);
/*      */       break;
/*      */     case 'l': 
/* 1350 */       if ((active0 & 0x10000) != 0L)
/* 1351 */         return jjStartNfaWithStates_1(3, 16, 30);
/*      */       break;
/*      */     case 's': 
/* 1354 */       return jjMoveStringLiteralDfa4_1(active0, 32768L);
/*      */     case 't': 
/* 1356 */       return jjMoveStringLiteralDfa4_1(active0, 26388279066624L);
/*      */     }
/*      */     
/*      */     
/* 1360 */     return jjStartNfa_1(2, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa4_1(long old0, long active0) {
/* 1364 */     if ((active0 &= old0) == 0L)
/* 1365 */       return jjStartNfa_1(2, old0);
/* 1366 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1368 */       jjStopStringLiteralDfa_1(3, active0);
/* 1369 */       return 4;
/*      */     }
/* 1371 */     switch (this.curChar)
/*      */     {
/*      */     case 'a': 
/* 1374 */       return jjMoveStringLiteralDfa5_1(active0, 17592186044416L);
/*      */     case 'e': 
/* 1376 */       if ((active0 & 0x8000) != 0L)
/* 1377 */         return jjStartNfaWithStates_1(4, 15, 30);
/*      */       break;
/*      */     case 'y': 
/* 1380 */       if ((active0 & 0x80000000000) != 0L) {
/* 1381 */         return jjStartNfaWithStates_1(4, 43, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/* 1386 */     return jjStartNfa_1(3, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa5_1(long old0, long active0) {
/* 1390 */     if ((active0 &= old0) == 0L)
/* 1391 */       return jjStartNfa_1(3, old0);
/* 1392 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1394 */       jjStopStringLiteralDfa_1(4, active0);
/* 1395 */       return 5;
/*      */     }
/* 1397 */     switch (this.curChar)
/*      */     {
/*      */     case 'n': 
/* 1400 */       return jjMoveStringLiteralDfa6_1(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/* 1404 */     return jjStartNfa_1(4, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa6_1(long old0, long active0) {
/* 1408 */     if ((active0 &= old0) == 0L)
/* 1409 */       return jjStartNfa_1(4, old0);
/* 1410 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1412 */       jjStopStringLiteralDfa_1(5, active0);
/* 1413 */       return 6;
/*      */     }
/* 1415 */     switch (this.curChar)
/*      */     {
/*      */     case 'c': 
/* 1418 */       return jjMoveStringLiteralDfa7_1(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/* 1422 */     return jjStartNfa_1(5, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa7_1(long old0, long active0) {
/* 1426 */     if ((active0 &= old0) == 0L)
/* 1427 */       return jjStartNfa_1(5, old0);
/* 1428 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1430 */       jjStopStringLiteralDfa_1(6, active0);
/* 1431 */       return 7;
/*      */     }
/* 1433 */     switch (this.curChar)
/*      */     {
/*      */     case 'e': 
/* 1436 */       return jjMoveStringLiteralDfa8_1(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/* 1440 */     return jjStartNfa_1(6, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa8_1(long old0, long active0) {
/* 1444 */     if ((active0 &= old0) == 0L)
/* 1445 */       return jjStartNfa_1(6, old0);
/* 1446 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1448 */       jjStopStringLiteralDfa_1(7, active0);
/* 1449 */       return 8;
/*      */     }
/* 1451 */     switch (this.curChar)
/*      */     {
/*      */     case 'o': 
/* 1454 */       return jjMoveStringLiteralDfa9_1(active0, 17592186044416L);
/*      */     }
/*      */     
/*      */     
/* 1458 */     return jjStartNfa_1(7, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa9_1(long old0, long active0) {
/* 1462 */     if ((active0 &= old0) == 0L)
/* 1463 */       return jjStartNfa_1(7, old0);
/* 1464 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException e) {
/* 1466 */       jjStopStringLiteralDfa_1(8, active0);
/* 1467 */       return 9;
/*      */     }
/* 1469 */     switch (this.curChar)
/*      */     {
/*      */     case 'f': 
/* 1472 */       if ((active0 & 0x100000000000) != 0L) {
/* 1473 */         return jjStartNfaWithStates_1(9, 44, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/* 1478 */     return jjStartNfa_1(8, active0);
/*      */   }
/*      */   
/*      */   private int jjStartNfaWithStates_1(int pos, int kind, int state) {
/* 1482 */     this.jjmatchedKind = kind;
/* 1483 */     this.jjmatchedPos = pos;
/* 1484 */     try { this.curChar = this.input_stream.readChar();
/* 1485 */     } catch (IOException e) { return pos + 1; }
/* 1486 */     return jjMoveNfa_1(state, pos + 1);
/*      */   }
/*      */   
/*      */   private int jjMoveNfa_1(int startState, int curPos) {
/* 1490 */     int startsAt = 0;
/* 1491 */     this.jjnewStateCnt = 30;
/* 1492 */     int i = 1;
/* 1493 */     this.jjstateSet[0] = startState;
/* 1494 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/* 1497 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1498 */         ReInitRounds();
/* 1499 */       if (this.curChar < '@')
/*      */       {
/* 1501 */         long l = 1L << this.curChar;
/*      */         do
/*      */         {
/* 1504 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1507 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1509 */               if (kind > 10)
/* 1510 */                 kind = 10;
/* 1511 */               jjCheckNAddStates(18, 22);
/*      */             }
/* 1513 */             else if ((0x1800000000 & l) != 0L)
/*      */             {
/* 1515 */               if (kind > 56)
/* 1516 */                 kind = 56;
/* 1517 */               jjCheckNAddTwoStates(28, 29);
/*      */             }
/* 1519 */             else if (this.curChar == '\'') {
/* 1520 */               jjCheckNAddStates(23, 25);
/* 1521 */             } else if (this.curChar == '"') {
/* 1522 */               jjCheckNAddStates(26, 28);
/* 1523 */             } else if (this.curChar == '.') {
/* 1524 */               jjCheckNAdd(1);
/*      */             }
/*      */             break;
/* 1527 */           case 30:  if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/* 1529 */               if (kind > 57)
/* 1530 */                 kind = 57;
/* 1531 */               jjCheckNAdd(29);
/*      */             }
/* 1533 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/* 1535 */               if (kind > 56)
/* 1536 */                 kind = 56;
/* 1537 */               jjCheckNAdd(28);
/*      */             }
/*      */             break;
/*      */           case 1: 
/* 1541 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1543 */               if (kind > 11)
/* 1544 */                 kind = 11;
/* 1545 */               jjCheckNAddTwoStates(1, 2); }
/* 1546 */             break;
/*      */           case 3: 
/* 1548 */             if ((0x280000000000 & l) != 0L)
/* 1549 */               jjCheckNAdd(4);
/*      */             break;
/*      */           case 4: 
/* 1552 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1554 */               if (kind > 11)
/* 1555 */                 kind = 11;
/* 1556 */               jjCheckNAdd(4); }
/* 1557 */             break;
/*      */           case 5: 
/* 1559 */             if (this.curChar == '"')
/* 1560 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 6: 
/* 1563 */             if ((0xFFFFFFFBFFFFFFFF & l) != 0L)
/* 1564 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 8: 
/* 1567 */             if ((0x8400000000 & l) != 0L)
/* 1568 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 9: 
/* 1571 */             if ((this.curChar == '"') && (kind > 13))
/* 1572 */               kind = 13;
/*      */             break;
/*      */           case 10: 
/* 1575 */             if (this.curChar == '\'')
/* 1576 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 11: 
/* 1579 */             if ((0xFFFFFF7FFFFFFFFF & l) != 0L)
/* 1580 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 13: 
/* 1583 */             if ((0x8400000000 & l) != 0L)
/* 1584 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 14: 
/* 1587 */             if ((this.curChar == '\'') && (kind > 13))
/* 1588 */               kind = 13;
/*      */             break;
/*      */           case 15: 
/* 1591 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1593 */               if (kind > 10)
/* 1594 */                 kind = 10;
/* 1595 */               jjCheckNAddStates(18, 22); }
/* 1596 */             break;
/*      */           case 16: 
/* 1598 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1600 */               if (kind > 10)
/* 1601 */                 kind = 10;
/* 1602 */               jjCheckNAdd(16); }
/* 1603 */             break;
/*      */           case 17: 
/* 1605 */             if ((0x3FF000000000000 & l) != 0L)
/* 1606 */               jjCheckNAddTwoStates(17, 18);
/*      */             break;
/*      */           case 18: 
/* 1609 */             if (this.curChar == '.')
/*      */             {
/* 1611 */               if (kind > 11)
/* 1612 */                 kind = 11;
/* 1613 */               jjCheckNAddTwoStates(19, 20); }
/* 1614 */             break;
/*      */           case 19: 
/* 1616 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1618 */               if (kind > 11)
/* 1619 */                 kind = 11;
/* 1620 */               jjCheckNAddTwoStates(19, 20); }
/* 1621 */             break;
/*      */           case 21: 
/* 1623 */             if ((0x280000000000 & l) != 0L)
/* 1624 */               jjCheckNAdd(22);
/*      */             break;
/*      */           case 22: 
/* 1627 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1629 */               if (kind > 11)
/* 1630 */                 kind = 11;
/* 1631 */               jjCheckNAdd(22); }
/* 1632 */             break;
/*      */           case 23: 
/* 1634 */             if ((0x3FF000000000000 & l) != 0L)
/* 1635 */               jjCheckNAddTwoStates(23, 24);
/*      */             break;
/*      */           case 25: 
/* 1638 */             if ((0x280000000000 & l) != 0L)
/* 1639 */               jjCheckNAdd(26);
/*      */             break;
/*      */           case 26: 
/* 1642 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1644 */               if (kind > 11)
/* 1645 */                 kind = 11;
/* 1646 */               jjCheckNAdd(26); }
/* 1647 */             break;
/*      */           case 27: 
/* 1649 */             if ((0x1800000000 & l) != 0L)
/*      */             {
/* 1651 */               if (kind > 56)
/* 1652 */                 kind = 56;
/* 1653 */               jjCheckNAddTwoStates(28, 29); }
/* 1654 */             break;
/*      */           case 28: 
/* 1656 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/* 1658 */               if (kind > 56)
/* 1659 */                 kind = 56;
/* 1660 */               jjCheckNAdd(28); }
/* 1661 */             break;
/*      */           case 29: 
/* 1663 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/* 1665 */               if (kind > 57)
/* 1666 */                 kind = 57;
/* 1667 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/* 1671 */         } while (i != startsAt);
/*      */       }
/* 1673 */       else if (this.curChar < '')
/*      */       {
/* 1675 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1678 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1681 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1683 */               if (kind > 56)
/* 1684 */                 kind = 56;
/* 1685 */               jjCheckNAddTwoStates(28, 29); }
/* 1686 */             break;
/*      */           case 30: 
/* 1688 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1690 */               if (kind > 57)
/* 1691 */                 kind = 57;
/* 1692 */               jjCheckNAdd(29);
/*      */             }
/* 1694 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1696 */               if (kind > 56)
/* 1697 */                 kind = 56;
/* 1698 */               jjCheckNAdd(28);
/*      */             }
/*      */             break;
/*      */           case 2: 
/* 1702 */             if ((0x2000000020 & l) != 0L)
/* 1703 */               jjAddStates(29, 30);
/*      */             break;
/*      */           case 6: 
/* 1706 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/* 1707 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 7: 
/* 1710 */             if (this.curChar == '\\')
/* 1711 */               this.jjstateSet[(this.jjnewStateCnt++)] = 8;
/*      */             break;
/*      */           case 8: 
/* 1714 */             if (this.curChar == '\\')
/* 1715 */               jjCheckNAddStates(26, 28);
/*      */             break;
/*      */           case 11: 
/* 1718 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/* 1719 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 12: 
/* 1722 */             if (this.curChar == '\\')
/* 1723 */               this.jjstateSet[(this.jjnewStateCnt++)] = 13;
/*      */             break;
/*      */           case 13: 
/* 1726 */             if (this.curChar == '\\')
/* 1727 */               jjCheckNAddStates(23, 25);
/*      */             break;
/*      */           case 20: 
/* 1730 */             if ((0x2000000020 & l) != 0L)
/* 1731 */               jjAddStates(31, 32);
/*      */             break;
/*      */           case 24: 
/* 1734 */             if ((0x2000000020 & l) != 0L)
/* 1735 */               jjAddStates(33, 34);
/*      */             break;
/*      */           case 28: 
/* 1738 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1740 */               if (kind > 56)
/* 1741 */                 kind = 56;
/* 1742 */               jjCheckNAdd(28); }
/* 1743 */             break;
/*      */           case 29: 
/* 1745 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1747 */               if (kind > 57)
/* 1748 */                 kind = 57;
/* 1749 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/* 1753 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/* 1757 */         int hiByte = this.curChar >> '\b';
/* 1758 */         int i1 = hiByte >> 6;
/* 1759 */         long l1 = 1L << (hiByte & 0x3F);
/* 1760 */         int i2 = (this.curChar & 0xFF) >> '\006';
/* 1761 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1764 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1767 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1769 */               if (kind > 56)
/* 1770 */                 kind = 56;
/* 1771 */               jjCheckNAddTwoStates(28, 29); }
/* 1772 */             break;
/*      */           case 30: 
/* 1774 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1776 */               if (kind > 56)
/* 1777 */                 kind = 56;
/* 1778 */               jjCheckNAdd(28);
/*      */             }
/* 1780 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1782 */               if (kind > 57)
/* 1783 */                 kind = 57;
/* 1784 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           case 6: 
/* 1788 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1789 */               jjAddStates(26, 28);
/*      */             break;
/*      */           case 11: 
/* 1792 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1793 */               jjAddStates(23, 25);
/*      */             break;
/*      */           case 28: 
/* 1796 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1798 */               if (kind > 56)
/* 1799 */                 kind = 56;
/* 1800 */               jjCheckNAdd(28); }
/* 1801 */             break;
/*      */           case 29: 
/* 1803 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1805 */               if (kind > 57)
/* 1806 */                 kind = 57;
/* 1807 */               jjCheckNAdd(29);
/*      */             }
/*      */             break;
/*      */           }
/* 1811 */         } while (i != startsAt);
/*      */       }
/* 1813 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/* 1815 */         this.jjmatchedKind = kind;
/* 1816 */         this.jjmatchedPos = curPos;
/* 1817 */         kind = Integer.MAX_VALUE;
/*      */       }
/* 1819 */       curPos++;
/* 1820 */       if ((i = this.jjnewStateCnt) == (startsAt = 30 - (this.jjnewStateCnt = startsAt)))
/* 1821 */         return curPos;
/* 1822 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException e) {} }
/* 1823 */     return curPos;
/*      */   }
/*      */   
/* 1826 */   static final int[] jjnextStates = { 0, 1, 3, 4, 2, 0, 1, 4, 2, 0, 1, 4, 5, 2, 0, 1, 2, 6, 16, 17, 18, 23, 24, 11, 12, 14, 6, 7, 9, 3, 4, 21, 22, 25, 26 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
/*      */   {
/* 1833 */     switch (hiByte)
/*      */     {
/*      */     case 0: 
/* 1836 */       return (jjbitVec2[i2] & l2) != 0L;
/*      */     }
/* 1838 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 1839 */       return true;
/* 1840 */     return false;
/*      */   }
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
/*      */   {
/* 1845 */     switch (hiByte)
/*      */     {
/*      */     case 0: 
/* 1848 */       return (jjbitVec4[i2] & l2) != 0L;
/*      */     case 48: 
/* 1850 */       return (jjbitVec5[i2] & l2) != 0L;
/*      */     case 49: 
/* 1852 */       return (jjbitVec6[i2] & l2) != 0L;
/*      */     case 51: 
/* 1854 */       return (jjbitVec7[i2] & l2) != 0L;
/*      */     case 61: 
/* 1856 */       return (jjbitVec8[i2] & l2) != 0L;
/*      */     }
/* 1858 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1859 */       return true;
/* 1860 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1865 */   public static final String[] jjstrLiteralImages = { "", null, "${", "#{", null, null, null, null, "{", "}", null, null, null, null, "true", "false", "null", ".", "(", ")", "[", "]", ":", ";", ",", ">", "gt", "<", "lt", ">=", "ge", "<=", "le", "==", "eq", "!=", "ne", "!", "not", "&&", "and", "||", "or", "empty", "instanceof", "*", "+", "-", "?", "/", "div", "%", "mod", "+=", "=", "->", null, null, null, null, null, null };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1876 */   public static final String[] lexStateNames = { "DEFAULT", "IN_EXPRESSION", "IN_SET_OR_MAP" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1883 */   public static final int[] jjnewLexState = { -1, -1, 1, 1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1888 */   static final long[] jjtoToken = { 2594073385365401359L };
/*      */   
/*      */ 
/* 1891 */   static final long[] jjtoSkip = { 240L };
/*      */   
/*      */   protected SimpleCharStream input_stream;
/*      */   
/* 1895 */   private final int[] jjrounds = new int[30];
/* 1896 */   private final int[] jjstateSet = new int[60];
/* 1897 */   private final StringBuilder jjimage = new StringBuilder();
/* 1898 */   private StringBuilder image = this.jjimage;
/*      */   
/*      */   private int jjimageLen;
/*      */   private int lengthOfMatch;
/*      */   protected char curChar;
/*      */   
/*      */   public ELParserTokenManager(SimpleCharStream stream)
/*      */   {
/* 1906 */     this.input_stream = stream;
/*      */   }
/*      */   
/*      */   public ELParserTokenManager(SimpleCharStream stream, int lexState)
/*      */   {
/* 1911 */     this(stream);
/* 1912 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */ 
/*      */   public void ReInit(SimpleCharStream stream)
/*      */   {
/* 1918 */     this.jjmatchedPos = (this.jjnewStateCnt = 0);
/* 1919 */     this.curLexState = this.defaultLexState;
/* 1920 */     this.input_stream = stream;
/* 1921 */     ReInitRounds();
/*      */   }
/*      */   
/*      */   private void ReInitRounds()
/*      */   {
/* 1926 */     this.jjround = -2147483647;
/* 1927 */     for (int i = 30; i-- > 0;) {
/* 1928 */       this.jjrounds[i] = Integer.MIN_VALUE;
/*      */     }
/*      */   }
/*      */   
/*      */   public void ReInit(SimpleCharStream stream, int lexState)
/*      */   {
/* 1934 */     ReInit(stream);
/* 1935 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */ 
/*      */   public void SwitchTo(int lexState)
/*      */   {
/* 1941 */     if ((lexState >= 3) || (lexState < 0)) {
/* 1942 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 1944 */     this.curLexState = lexState;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Token jjFillToken()
/*      */   {
/* 1955 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 1956 */     String curTokenImage = im == null ? this.input_stream.GetImage() : im;
/* 1957 */     int beginLine = this.input_stream.getBeginLine();
/* 1958 */     int beginColumn = this.input_stream.getBeginColumn();
/* 1959 */     int endLine = this.input_stream.getEndLine();
/* 1960 */     int endColumn = this.input_stream.getEndColumn();
/* 1961 */     Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
/*      */     
/* 1963 */     t.beginLine = beginLine;
/* 1964 */     t.endLine = endLine;
/* 1965 */     t.beginColumn = beginColumn;
/* 1966 */     t.endColumn = endColumn;
/*      */     
/* 1968 */     return t;
/*      */   }
/*      */   
/* 1971 */   int curLexState = 0;
/* 1972 */   int defaultLexState = 0;
/*      */   
/*      */   int jjnewStateCnt;
/*      */   
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public Token getNextToken()
/*      */   {
/* 1982 */     int curPos = 0;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/*      */       try
/*      */       {
/* 1989 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1993 */         this.jjmatchedKind = 0;
/* 1994 */         return jjFillToken();
/*      */       }
/*      */       
/* 1997 */       this.image = this.jjimage;
/* 1998 */       this.image.setLength(0);
/* 1999 */       this.jjimageLen = 0;
/*      */       
/* 2001 */       switch (this.curLexState)
/*      */       {
/*      */       case 0: 
/* 2004 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 2005 */         this.jjmatchedPos = 0;
/* 2006 */         curPos = jjMoveStringLiteralDfa0_0();
/* 2007 */         break;
/*      */       case 1:  try {
/* 2009 */           this.input_stream.backup(0);
/* 2010 */           while ((this.curChar <= ' ') && ((0x100002600 & 1L << this.curChar) != 0L))
/* 2011 */             this.curChar = this.input_stream.BeginToken();
/*      */         } catch (IOException e1) {}
/* 2013 */         continue;
/* 2014 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 2015 */         this.jjmatchedPos = 0;
/* 2016 */         curPos = jjMoveStringLiteralDfa0_1();
/* 2017 */         if ((this.jjmatchedPos == 0) && (this.jjmatchedKind > 61))
/*      */         {
/* 2019 */           this.jjmatchedKind = 61; }
/*      */         break;
/*      */       case 2: 
/*      */         try {
/* 2023 */           this.input_stream.backup(0);
/* 2024 */           while ((this.curChar <= ' ') && ((0x100002600 & 1L << this.curChar) != 0L))
/* 2025 */             this.curChar = this.input_stream.BeginToken();
/*      */         } catch (IOException e1) {}
/* 2027 */         continue;
/* 2028 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 2029 */         this.jjmatchedPos = 0;
/* 2030 */         curPos = jjMoveStringLiteralDfa0_2();
/* 2031 */         if ((this.jjmatchedPos == 0) && (this.jjmatchedKind > 61))
/*      */         {
/* 2033 */           this.jjmatchedKind = 61;
/*      */         }
/*      */       
/*      */       default: 
/* 2037 */         if (this.jjmatchedKind == Integer.MAX_VALUE)
/*      */           break label407;
/* 2039 */         if (this.jjmatchedPos + 1 < curPos)
/* 2040 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1);
/* 2041 */         if ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
/*      */         {
/* 2043 */           Token matchedToken = jjFillToken();
/* 2044 */           TokenLexicalActions(matchedToken);
/* 2045 */           if (jjnewLexState[this.jjmatchedKind] != -1)
/* 2046 */             this.curLexState = jjnewLexState[this.jjmatchedKind];
/* 2047 */           return matchedToken;
/*      */         }
/*      */         
/*      */ 
/* 2051 */         if (jjnewLexState[this.jjmatchedKind] != -1)
/* 2052 */           this.curLexState = jjnewLexState[this.jjmatchedKind];
/*      */         break; }
/*      */     }
/*      */     label407:
/* 2056 */     int error_line = this.input_stream.getEndLine();
/* 2057 */     int error_column = this.input_stream.getEndColumn();
/* 2058 */     String error_after = null;
/* 2059 */     boolean EOFSeen = false;
/* 2060 */     try { this.input_stream.readChar();this.input_stream.backup(1);
/*      */     } catch (IOException e1) {
/* 2062 */       EOFSeen = true;
/* 2063 */       error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
/* 2064 */       if ((this.curChar == '\n') || (this.curChar == '\r')) {
/* 2065 */         error_line++;
/* 2066 */         error_column = 0;
/*      */       }
/*      */       else {
/* 2069 */         error_column++;
/*      */       } }
/* 2071 */     if (!EOFSeen) {
/* 2072 */       this.input_stream.backup(1);
/* 2073 */       error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
/*      */     }
/* 2075 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   void TokenLexicalActions(Token matchedToken)
/*      */   {
/* 2081 */     switch (this.jjmatchedKind)
/*      */     {
/*      */     case 2: 
/* 2084 */       this.image.append(jjstrLiteralImages[2]);
/* 2085 */       this.lengthOfMatch = jjstrLiteralImages[2].length();
/* 2086 */       this.deque.push(Integer.valueOf(0));
/* 2087 */       break;
/*      */     case 3: 
/* 2089 */       this.image.append(jjstrLiteralImages[3]);
/* 2090 */       this.lengthOfMatch = jjstrLiteralImages[3].length();
/* 2091 */       this.deque.push(Integer.valueOf(0));
/* 2092 */       break;
/*      */     case 8: 
/* 2094 */       this.image.append(jjstrLiteralImages[8]);
/* 2095 */       this.lengthOfMatch = jjstrLiteralImages[8].length();
/* 2096 */       this.deque.push(Integer.valueOf(this.curLexState));
/* 2097 */       break;
/*      */     case 9: 
/* 2099 */       this.image.append(jjstrLiteralImages[9]);
/* 2100 */       this.lengthOfMatch = jjstrLiteralImages[9].length();
/* 2101 */       SwitchTo(((Integer)this.deque.pop()).intValue());
/* 2102 */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   private void jjCheckNAdd(int state)
/*      */   {
/* 2109 */     if (this.jjrounds[state] != this.jjround)
/*      */     {
/* 2111 */       this.jjstateSet[(this.jjnewStateCnt++)] = state;
/* 2112 */       this.jjrounds[state] = this.jjround;
/*      */     }
/*      */   }
/*      */   
/*      */   private void jjAddStates(int start, int end) {
/*      */     do {
/* 2118 */       this.jjstateSet[(this.jjnewStateCnt++)] = jjnextStates[start];
/* 2119 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private void jjCheckNAddTwoStates(int state1, int state2) {
/* 2123 */     jjCheckNAdd(state1);
/* 2124 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private void jjCheckNAddStates(int start, int end)
/*      */   {
/*      */     do {
/* 2130 */       jjCheckNAdd(jjnextStates[start]);
/* 2131 */     } while (start++ != end);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\ELParserTokenManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */