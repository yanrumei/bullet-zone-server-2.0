/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class MIME2Java
/*     */ {
/* 482 */   private static final Map<String, String> s_enchash = new HashMap();
/*     */   
/* 484 */   static { s_enchash.put("UTF-8", "UTF8");
/* 485 */     s_enchash.put("US-ASCII", "8859_1");
/* 486 */     s_enchash.put("ISO-8859-1", "8859_1");
/* 487 */     s_enchash.put("ISO-8859-2", "8859_2");
/* 488 */     s_enchash.put("ISO-8859-3", "8859_3");
/* 489 */     s_enchash.put("ISO-8859-4", "8859_4");
/* 490 */     s_enchash.put("ISO-8859-5", "8859_5");
/* 491 */     s_enchash.put("ISO-8859-6", "8859_6");
/* 492 */     s_enchash.put("ISO-8859-7", "8859_7");
/* 493 */     s_enchash.put("ISO-8859-8", "8859_8");
/* 494 */     s_enchash.put("ISO-8859-9", "8859_9");
/* 495 */     s_enchash.put("ISO-2022-JP", "JIS");
/* 496 */     s_enchash.put("SHIFT_JIS", "SJIS");
/* 497 */     s_enchash.put("EUC-JP", "EUCJIS");
/* 498 */     s_enchash.put("GB2312", "GB2312");
/* 499 */     s_enchash.put("BIG5", "Big5");
/* 500 */     s_enchash.put("EUC-KR", "KSC5601");
/* 501 */     s_enchash.put("ISO-2022-KR", "ISO2022KR");
/* 502 */     s_enchash.put("KOI8-R", "KOI8_R");
/*     */     
/* 504 */     s_enchash.put("EBCDIC-CP-US", "CP037");
/* 505 */     s_enchash.put("EBCDIC-CP-CA", "CP037");
/* 506 */     s_enchash.put("EBCDIC-CP-NL", "CP037");
/* 507 */     s_enchash.put("EBCDIC-CP-DK", "CP277");
/* 508 */     s_enchash.put("EBCDIC-CP-NO", "CP277");
/* 509 */     s_enchash.put("EBCDIC-CP-FI", "CP278");
/* 510 */     s_enchash.put("EBCDIC-CP-SE", "CP278");
/* 511 */     s_enchash.put("EBCDIC-CP-IT", "CP280");
/* 512 */     s_enchash.put("EBCDIC-CP-ES", "CP284");
/* 513 */     s_enchash.put("EBCDIC-CP-GB", "CP285");
/* 514 */     s_enchash.put("EBCDIC-CP-FR", "CP297");
/* 515 */     s_enchash.put("EBCDIC-CP-AR1", "CP420");
/* 516 */     s_enchash.put("EBCDIC-CP-HE", "CP424");
/* 517 */     s_enchash.put("EBCDIC-CP-CH", "CP500");
/* 518 */     s_enchash.put("EBCDIC-CP-ROECE", "CP870");
/* 519 */     s_enchash.put("EBCDIC-CP-YU", "CP870");
/* 520 */     s_enchash.put("EBCDIC-CP-IS", "CP871");
/* 521 */     s_enchash.put("EBCDIC-CP-AR2", "CP918");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 526 */     s_revhash = new HashMap();
/*     */     
/* 528 */     s_revhash.put("UTF8", "UTF-8");
/*     */     
/* 530 */     s_revhash.put("8859_1", "ISO-8859-1");
/* 531 */     s_revhash.put("8859_2", "ISO-8859-2");
/* 532 */     s_revhash.put("8859_3", "ISO-8859-3");
/* 533 */     s_revhash.put("8859_4", "ISO-8859-4");
/* 534 */     s_revhash.put("8859_5", "ISO-8859-5");
/* 535 */     s_revhash.put("8859_6", "ISO-8859-6");
/* 536 */     s_revhash.put("8859_7", "ISO-8859-7");
/* 537 */     s_revhash.put("8859_8", "ISO-8859-8");
/* 538 */     s_revhash.put("8859_9", "ISO-8859-9");
/* 539 */     s_revhash.put("JIS", "ISO-2022-JP");
/* 540 */     s_revhash.put("SJIS", "Shift_JIS");
/* 541 */     s_revhash.put("EUCJIS", "EUC-JP");
/* 542 */     s_revhash.put("GB2312", "GB2312");
/* 543 */     s_revhash.put("BIG5", "Big5");
/* 544 */     s_revhash.put("KSC5601", "EUC-KR");
/* 545 */     s_revhash.put("ISO2022KR", "ISO-2022-KR");
/* 546 */     s_revhash.put("KOI8_R", "KOI8-R");
/*     */     
/* 548 */     s_revhash.put("CP037", "EBCDIC-CP-US");
/* 549 */     s_revhash.put("CP037", "EBCDIC-CP-CA");
/* 550 */     s_revhash.put("CP037", "EBCDIC-CP-NL");
/* 551 */     s_revhash.put("CP277", "EBCDIC-CP-DK");
/* 552 */     s_revhash.put("CP277", "EBCDIC-CP-NO");
/* 553 */     s_revhash.put("CP278", "EBCDIC-CP-FI");
/* 554 */     s_revhash.put("CP278", "EBCDIC-CP-SE");
/* 555 */     s_revhash.put("CP280", "EBCDIC-CP-IT");
/* 556 */     s_revhash.put("CP284", "EBCDIC-CP-ES");
/* 557 */     s_revhash.put("CP285", "EBCDIC-CP-GB");
/* 558 */     s_revhash.put("CP297", "EBCDIC-CP-FR");
/* 559 */     s_revhash.put("CP420", "EBCDIC-CP-AR1");
/* 560 */     s_revhash.put("CP424", "EBCDIC-CP-HE");
/* 561 */     s_revhash.put("CP500", "EBCDIC-CP-CH");
/* 562 */     s_revhash.put("CP870", "EBCDIC-CP-ROECE");
/* 563 */     s_revhash.put("CP870", "EBCDIC-CP-YU");
/* 564 */     s_revhash.put("CP871", "EBCDIC-CP-IS");
/* 565 */     s_revhash.put("CP918", "EBCDIC-CP-AR2");
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
/*     */   private static final Map<String, String> s_revhash;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String convert(String mimeCharsetName)
/*     */   {
/* 587 */     return (String)s_enchash.get(mimeCharsetName.toUpperCase(Locale.ENGLISH));
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
/*     */   public static String reverse(String encoding)
/*     */   {
/* 605 */     return (String)s_revhash.get(encoding.toUpperCase(Locale.ENGLISH));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\MIME2Java.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */