/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileCopyUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*     */   
/*     */   public static int copy(File in, File out)
/*     */     throws IOException
/*     */   {
/*  61 */     Assert.notNull(in, "No input File specified");
/*  62 */     Assert.notNull(out, "No output File specified");
/*     */     
/*  64 */     return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void copy(byte[] in, File out)
/*     */     throws IOException
/*     */   {
/*  75 */     Assert.notNull(in, "No input byte array specified");
/*  76 */     Assert.notNull(out, "No output File specified");
/*     */     
/*  78 */     ByteArrayInputStream inStream = new ByteArrayInputStream(in);
/*  79 */     OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
/*  80 */     copy(inStream, outStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] copyToByteArray(File in)
/*     */     throws IOException
/*     */   {
/*  90 */     Assert.notNull(in, "No input File specified");
/*     */     
/*  92 */     return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
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
/*     */   public static int copy(InputStream in, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 109 */     Assert.notNull(in, "No InputStream specified");
/* 110 */     Assert.notNull(out, "No OutputStream specified");
/*     */     try
/*     */     {
/* 113 */       return StreamUtils.copy(in, out);
/*     */     }
/*     */     finally {
/*     */       try {
/* 117 */         in.close();
/*     */       }
/*     */       catch (IOException localIOException2) {}
/*     */       try
/*     */       {
/* 122 */         out.close();
/*     */       }
/*     */       catch (IOException localIOException3) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void copy(byte[] in, OutputStream out)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ldc 15
/*     */     //   3: invokestatic 4	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/*     */     //   6: aload_1
/*     */     //   7: ldc 20
/*     */     //   9: invokestatic 4	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/*     */     //   12: aload_1
/*     */     //   13: aload_0
/*     */     //   14: invokevirtual 25	java/io/OutputStream:write	([B)V
/*     */     //   17: aload_1
/*     */     //   18: invokevirtual 24	java/io/OutputStream:close	()V
/*     */     //   21: goto +19 -> 40
/*     */     //   24: astore_2
/*     */     //   25: goto +15 -> 40
/*     */     //   28: astore_3
/*     */     //   29: aload_1
/*     */     //   30: invokevirtual 24	java/io/OutputStream:close	()V
/*     */     //   33: goto +5 -> 38
/*     */     //   36: astore 4
/*     */     //   38: aload_3
/*     */     //   39: athrow
/*     */     //   40: return
/*     */     // Line number table:
/*     */     //   Java source line #137	-> byte code offset #0
/*     */     //   Java source line #138	-> byte code offset #6
/*     */     //   Java source line #141	-> byte code offset #12
/*     */     //   Java source line #145	-> byte code offset #17
/*     */     //   Java source line #148	-> byte code offset #21
/*     */     //   Java source line #147	-> byte code offset #24
/*     */     //   Java source line #149	-> byte code offset #25
/*     */     //   Java source line #144	-> byte code offset #28
/*     */     //   Java source line #145	-> byte code offset #29
/*     */     //   Java source line #148	-> byte code offset #33
/*     */     //   Java source line #147	-> byte code offset #36
/*     */     //   Java source line #148	-> byte code offset #38
/*     */     //   Java source line #150	-> byte code offset #40
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	41	0	in	byte[]
/*     */     //   0	41	1	out	OutputStream
/*     */     //   24	1	2	localIOException	IOException
/*     */     //   28	11	3	localObject	Object
/*     */     //   36	1	4	localIOException1	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   17	21	24	java/io/IOException
/*     */     //   12	17	28	finally
/*     */     //   29	33	36	java/io/IOException
/*     */   }
/*     */   
/*     */   public static byte[] copyToByteArray(InputStream in)
/*     */     throws IOException
/*     */   {
/* 160 */     if (in == null) {
/* 161 */       return new byte[0];
/*     */     }
/*     */     
/* 164 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 165 */     copy(in, out);
/* 166 */     return out.toByteArray();
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
/*     */   public static int copy(Reader in, Writer out)
/*     */     throws IOException
/*     */   {
/* 183 */     Assert.notNull(in, "No Reader specified");
/* 184 */     Assert.notNull(out, "No Writer specified");
/*     */     try
/*     */     {
/* 187 */       int byteCount = 0;
/* 188 */       char[] buffer = new char['က'];
/* 189 */       int bytesRead = -1;
/* 190 */       while ((bytesRead = in.read(buffer)) != -1) {
/* 191 */         out.write(buffer, 0, bytesRead);
/* 192 */         byteCount += bytesRead;
/*     */       }
/* 194 */       out.flush();
/* 195 */       return byteCount;
/*     */     }
/*     */     finally {
/*     */       try {
/* 199 */         in.close();
/*     */       }
/*     */       catch (IOException localIOException2) {}
/*     */       try
/*     */       {
/* 204 */         out.close();
/*     */       }
/*     */       catch (IOException localIOException3) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void copy(String in, Writer out)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ldc 37
/*     */     //   3: invokestatic 4	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/*     */     //   6: aload_1
/*     */     //   7: ldc 31
/*     */     //   9: invokestatic 4	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/*     */     //   12: aload_1
/*     */     //   13: aload_0
/*     */     //   14: invokevirtual 38	java/io/Writer:write	(Ljava/lang/String;)V
/*     */     //   17: aload_1
/*     */     //   18: invokevirtual 36	java/io/Writer:close	()V
/*     */     //   21: goto +19 -> 40
/*     */     //   24: astore_2
/*     */     //   25: goto +15 -> 40
/*     */     //   28: astore_3
/*     */     //   29: aload_1
/*     */     //   30: invokevirtual 36	java/io/Writer:close	()V
/*     */     //   33: goto +5 -> 38
/*     */     //   36: astore 4
/*     */     //   38: aload_3
/*     */     //   39: athrow
/*     */     //   40: return
/*     */     // Line number table:
/*     */     //   Java source line #219	-> byte code offset #0
/*     */     //   Java source line #220	-> byte code offset #6
/*     */     //   Java source line #223	-> byte code offset #12
/*     */     //   Java source line #227	-> byte code offset #17
/*     */     //   Java source line #230	-> byte code offset #21
/*     */     //   Java source line #229	-> byte code offset #24
/*     */     //   Java source line #231	-> byte code offset #25
/*     */     //   Java source line #226	-> byte code offset #28
/*     */     //   Java source line #227	-> byte code offset #29
/*     */     //   Java source line #230	-> byte code offset #33
/*     */     //   Java source line #229	-> byte code offset #36
/*     */     //   Java source line #230	-> byte code offset #38
/*     */     //   Java source line #232	-> byte code offset #40
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	41	0	in	String
/*     */     //   0	41	1	out	Writer
/*     */     //   24	1	2	localIOException	IOException
/*     */     //   28	11	3	localObject	Object
/*     */     //   36	1	4	localIOException1	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   17	21	24	java/io/IOException
/*     */     //   12	17	28	finally
/*     */     //   29	33	36	java/io/IOException
/*     */   }
/*     */   
/*     */   public static String copyToString(Reader in)
/*     */     throws IOException
/*     */   {
/* 242 */     if (in == null) {
/* 243 */       return "";
/*     */     }
/*     */     
/* 246 */     StringWriter out = new StringWriter();
/* 247 */     copy(in, out);
/* 248 */     return out.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\FileCopyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */