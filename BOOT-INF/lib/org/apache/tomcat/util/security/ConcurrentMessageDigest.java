/*     */ package org.apache.tomcat.util.security;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcurrentMessageDigest
/*     */ {
/*     */   private static final String MD5 = "MD5";
/*     */   private static final String SHA1 = "SHA-1";
/*  36 */   private static final Map<String, Queue<MessageDigest>> queues = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  47 */       init("MD5");
/*  48 */       init("SHA-1");
/*     */     } catch (NoSuchAlgorithmException e) {
/*  50 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] digestMD5(byte[]... input) {
/*  55 */     return digest("MD5", input);
/*     */   }
/*     */   
/*     */   public static byte[] digestSHA1(byte[]... input) {
/*  59 */     return digest("SHA-1", input);
/*     */   }
/*     */   
/*     */   public static byte[] digest(String algorithm, byte[]... input) {
/*  63 */     return digest(algorithm, 1, input);
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] digest(String algorithm, int rounds, byte[]... input)
/*     */   {
/*  69 */     Queue<MessageDigest> queue = (Queue)queues.get(algorithm);
/*  70 */     if (queue == null) {
/*  71 */       throw new IllegalStateException("Must call init() first");
/*     */     }
/*     */     
/*  74 */     MessageDigest md = (MessageDigest)queue.poll();
/*  75 */     if (md == null) {
/*     */       try {
/*  77 */         md = MessageDigest.getInstance(algorithm);
/*     */       }
/*     */       catch (NoSuchAlgorithmException e)
/*     */       {
/*  81 */         throw new IllegalStateException("Must call init() first");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  86 */     for (byte[] bytes : input) {
/*  87 */       md.update(bytes);
/*     */     }
/*  89 */     byte[] result = md.digest();
/*     */     
/*     */ 
/*  92 */     if (rounds > 1) {
/*  93 */       for (int i = 1; i < rounds; i++) {
/*  94 */         md.update(result);
/*  95 */         result = md.digest();
/*     */       }
/*     */     }
/*     */     
/*  99 */     queue.add(md);
/*     */     
/* 101 */     return result;
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
/*     */   public static void init(String algorithm)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 116 */     synchronized (queues) {
/* 117 */       if (!queues.containsKey(algorithm)) {
/* 118 */         MessageDigest md = MessageDigest.getInstance(algorithm);
/* 119 */         Queue<MessageDigest> queue = new ConcurrentLinkedQueue();
/* 120 */         queue.add(md);
/* 121 */         queues.put(algorithm, queue);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\security\ConcurrentMessageDigest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */