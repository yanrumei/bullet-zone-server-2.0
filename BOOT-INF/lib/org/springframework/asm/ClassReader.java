/*      */ package org.springframework.asm;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*      */   static final boolean SIGNATURES = true;
/*      */   static final boolean ANNOTATIONS = true;
/*      */   static final boolean FRAMES = true;
/*      */   static final boolean WRITER = true;
/*      */   static final boolean RESIZE = true;
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   static final int EXPAND_ASM_INSNS = 256;
/*      */   public final byte[] b;
/*      */   private final int[] items;
/*      */   private final String[] strings;
/*      */   private final int maxStringLength;
/*      */   public final int header;
/*      */   
/*      */   public ClassReader(byte[] b)
/*      */   {
/*  168 */     this(b, 0, b.length);
/*      */   }
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
/*      */   public ClassReader(byte[] b, int off, int len)
/*      */   {
/*  182 */     this.b = b;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  190 */     this.items = new int[readUnsignedShort(off + 8)];
/*  191 */     int n = this.items.length;
/*  192 */     this.strings = new String[n];
/*  193 */     int max = 0;
/*  194 */     int index = off + 10;
/*  195 */     for (int i = 1; i < n; i++) {
/*  196 */       this.items[i] = (index + 1);
/*      */       int size;
/*  198 */       int size; int size; switch (b[index]) {
/*      */       case 3: 
/*      */       case 4: 
/*      */       case 9: 
/*      */       case 10: 
/*      */       case 11: 
/*      */       case 12: 
/*      */       case 18: 
/*  206 */         size = 5;
/*  207 */         break;
/*      */       case 5: 
/*      */       case 6: 
/*  210 */         int size = 9;
/*  211 */         i++;
/*  212 */         break;
/*      */       case 1: 
/*  214 */         int size = 3 + readUnsignedShort(index + 1);
/*  215 */         if (size > max) {
/*  216 */           max = size;
/*      */         }
/*      */         break;
/*      */       case 15: 
/*  220 */         size = 4;
/*  221 */         break;
/*      */       case 2: case 7: 
/*      */       case 8: case 13: 
/*      */       case 14: 
/*      */       case 16: 
/*      */       case 17: 
/*      */       default: 
/*  228 */         size = 3;
/*      */       }
/*      */       
/*  231 */       index += size;
/*      */     }
/*  233 */     this.maxStringLength = max;
/*      */     
/*  235 */     this.header = index;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getAccess()
/*      */   {
/*  248 */     return readUnsignedShort(this.header);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  260 */     return readClass(this.header + 2, new char[this.maxStringLength]);
/*      */   }
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
/*      */   public String getSuperName()
/*      */   {
/*  274 */     return readClass(this.header + 4, new char[this.maxStringLength]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getInterfaces()
/*      */   {
/*  287 */     int index = this.header + 6;
/*  288 */     int n = readUnsignedShort(index);
/*  289 */     String[] interfaces = new String[n];
/*  290 */     if (n > 0) {
/*  291 */       char[] buf = new char[this.maxStringLength];
/*  292 */       for (int i = 0; i < n; i++) {
/*  293 */         index += 2;
/*  294 */         interfaces[i] = readClass(index, buf);
/*      */       }
/*      */     }
/*  297 */     return interfaces;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void copyPool(ClassWriter classWriter)
/*      */   {
/*  308 */     char[] buf = new char[this.maxStringLength];
/*  309 */     int ll = this.items.length;
/*  310 */     Item[] items2 = new Item[ll];
/*  311 */     for (int i = 1; i < ll; i++) {
/*  312 */       int index = this.items[i];
/*  313 */       int tag = this.b[(index - 1)];
/*  314 */       Item item = new Item(i);
/*      */       
/*  316 */       switch (tag) {
/*      */       case 9: 
/*      */       case 10: 
/*      */       case 11: 
/*  320 */         int nameType = this.items[readUnsignedShort(index + 2)];
/*  321 */         item.set(tag, readClass(index, buf), readUTF8(nameType, buf), 
/*  322 */           readUTF8(nameType + 2, buf));
/*  323 */         break;
/*      */       case 3: 
/*  325 */         item.set(readInt(index));
/*  326 */         break;
/*      */       case 4: 
/*  328 */         item.set(Float.intBitsToFloat(readInt(index)));
/*  329 */         break;
/*      */       case 12: 
/*  331 */         item.set(tag, readUTF8(index, buf), readUTF8(index + 2, buf), null);
/*      */         
/*  333 */         break;
/*      */       case 5: 
/*  335 */         item.set(readLong(index));
/*  336 */         i++;
/*  337 */         break;
/*      */       case 6: 
/*  339 */         item.set(Double.longBitsToDouble(readLong(index)));
/*  340 */         i++;
/*  341 */         break;
/*      */       case 1: 
/*  343 */         String s = this.strings[i];
/*  344 */         if (s == null) {
/*  345 */           index = this.items[i];
/*  346 */           s = this.strings[i] = readUTF(index + 2, 
/*  347 */             readUnsignedShort(index), buf);
/*      */         }
/*  349 */         item.set(tag, s, null, null);
/*  350 */         break;
/*      */       
/*      */       case 15: 
/*  353 */         int fieldOrMethodRef = this.items[readUnsignedShort(index + 1)];
/*  354 */         int nameType = this.items[readUnsignedShort(fieldOrMethodRef + 2)];
/*  355 */         item.set(20 + readByte(index), 
/*  356 */           readClass(fieldOrMethodRef, buf), 
/*  357 */           readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
/*  358 */         break;
/*      */       
/*      */       case 18: 
/*  361 */         if (classWriter.bootstrapMethods == null) {
/*  362 */           copyBootstrapMethods(classWriter, items2, buf);
/*      */         }
/*  364 */         int nameType = this.items[readUnsignedShort(index + 2)];
/*  365 */         item.set(readUTF8(nameType, buf), readUTF8(nameType + 2, buf), 
/*  366 */           readUnsignedShort(index));
/*  367 */         break;
/*      */       case 2: case 7: 
/*      */       case 8: case 13: 
/*      */       case 14: 
/*      */       case 16: 
/*      */       case 17: 
/*      */       default: 
/*  374 */         item.set(tag, readUTF8(index, buf), null, null);
/*      */       }
/*      */       
/*      */       
/*  378 */       int index2 = item.hashCode % items2.length;
/*  379 */       item.next = items2[index2];
/*  380 */       items2[index2] = item;
/*      */     }
/*      */     
/*  383 */     int off = this.items[1] - 1;
/*  384 */     classWriter.pool.putByteArray(this.b, off, this.header - off);
/*  385 */     classWriter.items = items2;
/*  386 */     classWriter.threshold = ((int)(0.75D * ll));
/*  387 */     classWriter.index = ll;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void copyBootstrapMethods(ClassWriter classWriter, Item[] items, char[] c)
/*      */   {
/*  400 */     int u = getAttributes();
/*  401 */     boolean found = false;
/*  402 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/*  403 */       String attrName = readUTF8(u + 2, c);
/*  404 */       if ("BootstrapMethods".equals(attrName)) {
/*  405 */         found = true;
/*  406 */         break;
/*      */       }
/*  408 */       u += 6 + readInt(u + 4);
/*      */     }
/*  410 */     if (!found) {
/*  411 */       return;
/*      */     }
/*      */     
/*  414 */     int boostrapMethodCount = readUnsignedShort(u + 8);
/*  415 */     int j = 0; for (int v = u + 10; j < boostrapMethodCount; j++) {
/*  416 */       int position = v - u - 10;
/*  417 */       int hashCode = readConst(readUnsignedShort(v), c).hashCode();
/*  418 */       for (int k = readUnsignedShort(v + 2); k > 0; k--) {
/*  419 */         hashCode ^= readConst(readUnsignedShort(v + 4), c).hashCode();
/*  420 */         v += 2;
/*      */       }
/*  422 */       v += 4;
/*  423 */       Item item = new Item(j);
/*  424 */       item.set(position, hashCode & 0x7FFFFFFF);
/*  425 */       int index = item.hashCode % items.length;
/*  426 */       item.next = items[index];
/*  427 */       items[index] = item;
/*      */     }
/*  429 */     int attrSize = readInt(u + 4);
/*  430 */     ByteVector bootstrapMethods = new ByteVector(attrSize + 62);
/*  431 */     bootstrapMethods.putByteArray(this.b, u + 10, attrSize - 2);
/*  432 */     classWriter.bootstrapMethodsCount = boostrapMethodCount;
/*  433 */     classWriter.bootstrapMethods = bootstrapMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassReader(InputStream is)
/*      */     throws IOException
/*      */   {
/*  445 */     this(readClass(is, false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassReader(String name)
/*      */     throws IOException
/*      */   {
/*  457 */     this(readClass(
/*  458 */       ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"), true));
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static byte[] readClass(InputStream is, boolean close)
/*      */     throws IOException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: ifnonnull +13 -> 14
/*      */     //   4: new 57	java/io/IOException
/*      */     //   7: dup
/*      */     //   8: ldc 58
/*      */     //   10: invokespecial 59	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   13: athrow
/*      */     //   14: aload_0
/*      */     //   15: invokevirtual 60	java/io/InputStream:available	()I
/*      */     //   18: newarray <illegal type>
/*      */     //   20: astore_2
/*      */     //   21: iconst_0
/*      */     //   22: istore_3
/*      */     //   23: aload_0
/*      */     //   24: aload_2
/*      */     //   25: iload_3
/*      */     //   26: aload_2
/*      */     //   27: arraylength
/*      */     //   28: iload_3
/*      */     //   29: isub
/*      */     //   30: invokevirtual 61	java/io/InputStream:read	([BII)I
/*      */     //   33: istore 4
/*      */     //   35: iload 4
/*      */     //   37: iconst_m1
/*      */     //   38: if_icmpne +40 -> 78
/*      */     //   41: iload_3
/*      */     //   42: aload_2
/*      */     //   43: arraylength
/*      */     //   44: if_icmpge +20 -> 64
/*      */     //   47: iload_3
/*      */     //   48: newarray <illegal type>
/*      */     //   50: astore 5
/*      */     //   52: aload_2
/*      */     //   53: iconst_0
/*      */     //   54: aload 5
/*      */     //   56: iconst_0
/*      */     //   57: iload_3
/*      */     //   58: invokestatic 62	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*      */     //   61: aload 5
/*      */     //   63: astore_2
/*      */     //   64: aload_2
/*      */     //   65: astore 5
/*      */     //   67: iload_1
/*      */     //   68: ifeq +7 -> 75
/*      */     //   71: aload_0
/*      */     //   72: invokevirtual 63	java/io/InputStream:close	()V
/*      */     //   75: aload 5
/*      */     //   77: areturn
/*      */     //   78: iload_3
/*      */     //   79: iload 4
/*      */     //   81: iadd
/*      */     //   82: istore_3
/*      */     //   83: iload_3
/*      */     //   84: aload_2
/*      */     //   85: arraylength
/*      */     //   86: if_icmpne +60 -> 146
/*      */     //   89: aload_0
/*      */     //   90: invokevirtual 64	java/io/InputStream:read	()I
/*      */     //   93: istore 5
/*      */     //   95: iload 5
/*      */     //   97: ifge +17 -> 114
/*      */     //   100: aload_2
/*      */     //   101: astore 6
/*      */     //   103: iload_1
/*      */     //   104: ifeq +7 -> 111
/*      */     //   107: aload_0
/*      */     //   108: invokevirtual 63	java/io/InputStream:close	()V
/*      */     //   111: aload 6
/*      */     //   113: areturn
/*      */     //   114: aload_2
/*      */     //   115: arraylength
/*      */     //   116: sipush 1000
/*      */     //   119: iadd
/*      */     //   120: newarray <illegal type>
/*      */     //   122: astore 6
/*      */     //   124: aload_2
/*      */     //   125: iconst_0
/*      */     //   126: aload 6
/*      */     //   128: iconst_0
/*      */     //   129: iload_3
/*      */     //   130: invokestatic 62	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*      */     //   133: aload 6
/*      */     //   135: iload_3
/*      */     //   136: iinc 3 1
/*      */     //   139: iload 5
/*      */     //   141: i2b
/*      */     //   142: bastore
/*      */     //   143: aload 6
/*      */     //   145: astore_2
/*      */     //   146: goto -123 -> 23
/*      */     //   149: astore 7
/*      */     //   151: iload_1
/*      */     //   152: ifeq +7 -> 159
/*      */     //   155: aload_0
/*      */     //   156: invokevirtual 63	java/io/InputStream:close	()V
/*      */     //   159: aload 7
/*      */     //   161: athrow
/*      */     // Line number table:
/*      */     //   Java source line #475	-> byte code offset #0
/*      */     //   Java source line #476	-> byte code offset #4
/*      */     //   Java source line #479	-> byte code offset #14
/*      */     //   Java source line #480	-> byte code offset #21
/*      */     //   Java source line #482	-> byte code offset #23
/*      */     //   Java source line #483	-> byte code offset #35
/*      */     //   Java source line #484	-> byte code offset #41
/*      */     //   Java source line #485	-> byte code offset #47
/*      */     //   Java source line #486	-> byte code offset #52
/*      */     //   Java source line #487	-> byte code offset #61
/*      */     //   Java source line #489	-> byte code offset #64
/*      */     //   Java source line #504	-> byte code offset #67
/*      */     //   Java source line #505	-> byte code offset #71
/*      */     //   Java source line #489	-> byte code offset #75
/*      */     //   Java source line #491	-> byte code offset #78
/*      */     //   Java source line #492	-> byte code offset #83
/*      */     //   Java source line #493	-> byte code offset #89
/*      */     //   Java source line #494	-> byte code offset #95
/*      */     //   Java source line #495	-> byte code offset #100
/*      */     //   Java source line #504	-> byte code offset #103
/*      */     //   Java source line #505	-> byte code offset #107
/*      */     //   Java source line #495	-> byte code offset #111
/*      */     //   Java source line #497	-> byte code offset #114
/*      */     //   Java source line #498	-> byte code offset #124
/*      */     //   Java source line #499	-> byte code offset #133
/*      */     //   Java source line #500	-> byte code offset #143
/*      */     //   Java source line #502	-> byte code offset #146
/*      */     //   Java source line #504	-> byte code offset #149
/*      */     //   Java source line #505	-> byte code offset #155
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	162	0	is	InputStream
/*      */     //   0	162	1	close	boolean
/*      */     //   20	126	2	b	byte[]
/*      */     //   22	114	3	len	int
/*      */     //   33	47	4	n	int
/*      */     //   50	26	5	c	byte[]
/*      */     //   93	47	5	last	int
/*      */     //   101	11	6	arrayOfByte1	byte[]
/*      */     //   122	22	6	c	byte[]
/*      */     //   149	11	7	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	67	149	finally
/*      */     //   78	103	149	finally
/*      */     //   114	151	149	finally
/*      */   }
/*      */   
/*      */   public void accept(ClassVisitor classVisitor, int flags)
/*      */   {
/*  527 */     accept(classVisitor, new Attribute[0], flags);
/*      */   }
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
/*      */ 
/*      */ 
/*      */   public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags)
/*      */   {
/*  553 */     int u = this.header;
/*  554 */     char[] c = new char[this.maxStringLength];
/*      */     
/*  556 */     Context context = new Context();
/*  557 */     context.attrs = attrs;
/*  558 */     context.flags = flags;
/*  559 */     context.buffer = c;
/*      */     
/*      */ 
/*  562 */     int access = readUnsignedShort(u);
/*  563 */     String name = readClass(u + 2, c);
/*  564 */     String superClass = readClass(u + 4, c);
/*  565 */     String[] interfaces = new String[readUnsignedShort(u + 6)];
/*  566 */     u += 8;
/*  567 */     for (int i = 0; i < interfaces.length; i++) {
/*  568 */       interfaces[i] = readClass(u, c);
/*  569 */       u += 2;
/*      */     }
/*      */     
/*      */ 
/*  573 */     String signature = null;
/*  574 */     String sourceFile = null;
/*  575 */     String sourceDebug = null;
/*  576 */     String enclosingOwner = null;
/*  577 */     String enclosingName = null;
/*  578 */     String enclosingDesc = null;
/*  579 */     String moduleMainClass = null;
/*  580 */     int anns = 0;
/*  581 */     int ianns = 0;
/*  582 */     int tanns = 0;
/*  583 */     int itanns = 0;
/*  584 */     int innerClasses = 0;
/*  585 */     int module = 0;
/*  586 */     int packages = 0;
/*  587 */     Attribute attributes = null;
/*      */     
/*  589 */     u = getAttributes();
/*  590 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/*  591 */       String attrName = readUTF8(u + 2, c);
/*      */       
/*      */ 
/*  594 */       if ("SourceFile".equals(attrName)) {
/*  595 */         sourceFile = readUTF8(u + 8, c);
/*  596 */       } else if ("InnerClasses".equals(attrName)) {
/*  597 */         innerClasses = u + 8;
/*  598 */       } else if ("EnclosingMethod".equals(attrName)) {
/*  599 */         enclosingOwner = readClass(u + 8, c);
/*  600 */         int item = readUnsignedShort(u + 10);
/*  601 */         if (item != 0) {
/*  602 */           enclosingName = readUTF8(this.items[item], c);
/*  603 */           enclosingDesc = readUTF8(this.items[item] + 2, c);
/*      */         }
/*  605 */       } else if ("Signature".equals(attrName)) {
/*  606 */         signature = readUTF8(u + 8, c);
/*      */       }
/*  608 */       else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*  609 */         anns = u + 8;
/*      */       }
/*  611 */       else if ("RuntimeVisibleTypeAnnotations".equals(attrName)) {
/*  612 */         tanns = u + 8;
/*  613 */       } else if ("Deprecated".equals(attrName)) {
/*  614 */         access |= 0x20000;
/*  615 */       } else if ("Synthetic".equals(attrName)) {
/*  616 */         access |= 0x41000;
/*      */       }
/*  618 */       else if ("SourceDebugExtension".equals(attrName)) {
/*  619 */         int len = readInt(u + 4);
/*  620 */         sourceDebug = readUTF(u + 8, len, new char[len]);
/*      */       }
/*  622 */       else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/*  623 */         ianns = u + 8;
/*      */       }
/*  625 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attrName)) {
/*  626 */         itanns = u + 8;
/*  627 */       } else if ("Module".equals(attrName)) {
/*  628 */         module = u + 8;
/*  629 */       } else if ("ModuleMainClass".equals(attrName)) {
/*  630 */         moduleMainClass = readClass(u + 8, c);
/*  631 */       } else if ("ModulePackages".equals(attrName)) {
/*  632 */         packages = u + 10;
/*  633 */       } else if ("BootstrapMethods".equals(attrName)) {
/*  634 */         int[] bootstrapMethods = new int[readUnsignedShort(u + 8)];
/*  635 */         int j = 0; for (int v = u + 10; j < bootstrapMethods.length; j++) {
/*  636 */           bootstrapMethods[j] = v;
/*  637 */           v += (2 + readUnsignedShort(v + 2) << 1);
/*      */         }
/*  639 */         context.bootstrapMethods = bootstrapMethods;
/*      */       } else {
/*  641 */         Attribute attr = readAttribute(attrs, attrName, u + 8, 
/*  642 */           readInt(u + 4), c, -1, null);
/*  643 */         if (attr != null) {
/*  644 */           attr.next = attributes;
/*  645 */           attributes = attr;
/*      */         }
/*      */       }
/*  648 */       u += 6 + readInt(u + 4);
/*      */     }
/*      */     
/*      */ 
/*  652 */     classVisitor.visit(readInt(this.items[1] - 7), access, name, signature, superClass, interfaces);
/*      */     
/*      */ 
/*      */ 
/*  656 */     if (((flags & 0x2) == 0) && ((sourceFile != null) || (sourceDebug != null)))
/*      */     {
/*  658 */       classVisitor.visitSource(sourceFile, sourceDebug);
/*      */     }
/*      */     
/*      */ 
/*  662 */     if (module != 0) {
/*  663 */       readModule(classVisitor, context, module, moduleMainClass, packages);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  668 */     if (enclosingOwner != null) {
/*  669 */       classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  674 */     if (anns != 0) {
/*  675 */       int i = readUnsignedShort(anns); for (int v = anns + 2; i > 0; i--) {
/*  676 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  677 */           .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     }
/*  680 */     if (ianns != 0) {
/*  681 */       int i = readUnsignedShort(ianns); for (int v = ianns + 2; i > 0; i--) {
/*  682 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  683 */           .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     }
/*  686 */     if (tanns != 0) {
/*  687 */       int i = readUnsignedShort(tanns); for (int v = tanns + 2; i > 0; i--) {
/*  688 */         v = readAnnotationTarget(context, v);
/*  689 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  690 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  691 */           readUTF8(v, c), true));
/*      */       }
/*      */     }
/*  694 */     if (itanns != 0) {
/*  695 */       int i = readUnsignedShort(itanns); for (int v = itanns + 2; i > 0; i--) {
/*  696 */         v = readAnnotationTarget(context, v);
/*  697 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  698 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  699 */           readUTF8(v, c), false));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  704 */     while (attributes != null) {
/*  705 */       Attribute attr = attributes.next;
/*  706 */       attributes.next = null;
/*  707 */       classVisitor.visitAttribute(attributes);
/*  708 */       attributes = attr;
/*      */     }
/*      */     
/*      */ 
/*  712 */     if (innerClasses != 0) {
/*  713 */       int v = innerClasses + 2;
/*  714 */       for (int i = readUnsignedShort(innerClasses); i > 0; i--) {
/*  715 */         classVisitor.visitInnerClass(readClass(v, c), 
/*  716 */           readClass(v + 2, c), readUTF8(v + 4, c), 
/*  717 */           readUnsignedShort(v + 6));
/*  718 */         v += 8;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  723 */     u = this.header + 10 + 2 * interfaces.length;
/*  724 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  725 */       u = readField(classVisitor, context, u);
/*      */     }
/*  727 */     u += 2;
/*  728 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  729 */       u = readMethod(classVisitor, context, u);
/*      */     }
/*      */     
/*      */ 
/*  733 */     classVisitor.visitEnd();
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readModule(ClassVisitor classVisitor, Context context, int u, String mainClass, int packages)
/*      */   {
/*  754 */     char[] buffer = context.buffer;
/*      */     
/*      */ 
/*  757 */     String name = readModule(u, buffer);
/*  758 */     int flags = readUnsignedShort(u + 2);
/*  759 */     String version = readUTF8(u + 4, buffer);
/*  760 */     u += 6;
/*      */     
/*  762 */     ModuleVisitor mv = classVisitor.visitModule(name, flags, version);
/*  763 */     if (mv == null) {
/*  764 */       return;
/*      */     }
/*      */     
/*      */ 
/*  768 */     if (mainClass != null) {
/*  769 */       mv.visitMainClass(mainClass);
/*      */     }
/*      */     
/*  772 */     if (packages != 0) {
/*  773 */       for (int i = readUnsignedShort(packages - 2); i > 0; i--) {
/*  774 */         String packaze = readPackage(packages, buffer);
/*  775 */         mv.visitPackage(packaze);
/*  776 */         packages += 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  781 */     u += 2;
/*  782 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  783 */       String module = readModule(u, buffer);
/*  784 */       int access = readUnsignedShort(u + 2);
/*  785 */       String requireVersion = readUTF8(u + 4, buffer);
/*  786 */       mv.visitRequire(module, access, requireVersion);
/*  787 */       u += 6;
/*      */     }
/*      */     
/*      */ 
/*  791 */     u += 2;
/*  792 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  793 */       String export = readPackage(u, buffer);
/*  794 */       int access = readUnsignedShort(u + 2);
/*  795 */       int exportToCount = readUnsignedShort(u + 4);
/*  796 */       u += 6;
/*  797 */       String[] tos = null;
/*  798 */       if (exportToCount != 0) {
/*  799 */         tos = new String[exportToCount];
/*  800 */         for (int j = 0; j < tos.length; j++) {
/*  801 */           tos[j] = readModule(u, buffer);
/*  802 */           u += 2;
/*      */         }
/*      */       }
/*  805 */       mv.visitExport(export, access, tos);
/*      */     }
/*      */     
/*      */ 
/*  809 */     u += 2;
/*  810 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  811 */       String open = readPackage(u, buffer);
/*  812 */       int access = readUnsignedShort(u + 2);
/*  813 */       int openToCount = readUnsignedShort(u + 4);
/*  814 */       u += 6;
/*  815 */       String[] tos = null;
/*  816 */       if (openToCount != 0) {
/*  817 */         tos = new String[openToCount];
/*  818 */         for (int j = 0; j < tos.length; j++) {
/*  819 */           tos[j] = readModule(u, buffer);
/*  820 */           u += 2;
/*      */         }
/*      */       }
/*  823 */       mv.visitOpen(open, access, tos);
/*      */     }
/*      */     
/*      */ 
/*  827 */     u += 2;
/*  828 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  829 */       mv.visitUse(readClass(u, buffer));
/*  830 */       u += 2;
/*      */     }
/*      */     
/*      */ 
/*  834 */     u += 2;
/*  835 */     for (int i = readUnsignedShort(u - 2); i > 0; i--) {
/*  836 */       String service = readClass(u, buffer);
/*  837 */       int provideWithCount = readUnsignedShort(u + 2);
/*  838 */       u += 4;
/*  839 */       String[] withs = new String[provideWithCount];
/*  840 */       for (int j = 0; j < withs.length; j++) {
/*  841 */         withs[j] = readClass(u, buffer);
/*  842 */         u += 2;
/*      */       }
/*  844 */       mv.visitProvide(service, withs);
/*      */     }
/*      */     
/*  847 */     mv.visitEnd();
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readField(ClassVisitor classVisitor, Context context, int u)
/*      */   {
/*  864 */     char[] c = context.buffer;
/*  865 */     int access = readUnsignedShort(u);
/*  866 */     String name = readUTF8(u + 2, c);
/*  867 */     String desc = readUTF8(u + 4, c);
/*  868 */     u += 6;
/*      */     
/*      */ 
/*  871 */     String signature = null;
/*  872 */     int anns = 0;
/*  873 */     int ianns = 0;
/*  874 */     int tanns = 0;
/*  875 */     int itanns = 0;
/*  876 */     Object value = null;
/*  877 */     Attribute attributes = null;
/*      */     
/*  879 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/*  880 */       String attrName = readUTF8(u + 2, c);
/*      */       
/*      */ 
/*  883 */       if ("ConstantValue".equals(attrName)) {
/*  884 */         int item = readUnsignedShort(u + 8);
/*  885 */         value = item == 0 ? null : readConst(item, c);
/*  886 */       } else if ("Signature".equals(attrName)) {
/*  887 */         signature = readUTF8(u + 8, c);
/*  888 */       } else if ("Deprecated".equals(attrName)) {
/*  889 */         access |= 0x20000;
/*  890 */       } else if ("Synthetic".equals(attrName)) {
/*  891 */         access |= 0x41000;
/*      */ 
/*      */       }
/*  894 */       else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*  895 */         anns = u + 8;
/*      */       }
/*  897 */       else if ("RuntimeVisibleTypeAnnotations".equals(attrName)) {
/*  898 */         tanns = u + 8;
/*      */       }
/*  900 */       else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/*  901 */         ianns = u + 8;
/*      */       }
/*  903 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attrName)) {
/*  904 */         itanns = u + 8;
/*      */       } else {
/*  906 */         Attribute attr = readAttribute(context.attrs, attrName, u + 8, 
/*  907 */           readInt(u + 4), c, -1, null);
/*  908 */         if (attr != null) {
/*  909 */           attr.next = attributes;
/*  910 */           attributes = attr;
/*      */         }
/*      */       }
/*  913 */       u += 6 + readInt(u + 4);
/*      */     }
/*  915 */     u += 2;
/*      */     
/*      */ 
/*  918 */     FieldVisitor fv = classVisitor.visitField(access, name, desc, signature, value);
/*      */     
/*  920 */     if (fv == null) {
/*  921 */       return u;
/*      */     }
/*      */     
/*      */ 
/*  925 */     if (anns != 0) {
/*  926 */       int i = readUnsignedShort(anns); for (int v = anns + 2; i > 0; i--) {
/*  927 */         v = readAnnotationValues(v + 2, c, true, fv
/*  928 */           .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     }
/*  931 */     if (ianns != 0) {
/*  932 */       int i = readUnsignedShort(ianns); for (int v = ianns + 2; i > 0; i--) {
/*  933 */         v = readAnnotationValues(v + 2, c, true, fv
/*  934 */           .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     }
/*  937 */     if (tanns != 0) {
/*  938 */       int i = readUnsignedShort(tanns); for (int v = tanns + 2; i > 0; i--) {
/*  939 */         v = readAnnotationTarget(context, v);
/*  940 */         v = readAnnotationValues(v + 2, c, true, fv
/*  941 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  942 */           readUTF8(v, c), true));
/*      */       }
/*      */     }
/*  945 */     if (itanns != 0) {
/*  946 */       int i = readUnsignedShort(itanns); for (int v = itanns + 2; i > 0; i--) {
/*  947 */         v = readAnnotationTarget(context, v);
/*  948 */         v = readAnnotationValues(v + 2, c, true, fv
/*  949 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  950 */           readUTF8(v, c), false));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  955 */     while (attributes != null) {
/*  956 */       Attribute attr = attributes.next;
/*  957 */       attributes.next = null;
/*  958 */       fv.visitAttribute(attributes);
/*  959 */       attributes = attr;
/*      */     }
/*      */     
/*      */ 
/*  963 */     fv.visitEnd();
/*      */     
/*  965 */     return u;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readMethod(ClassVisitor classVisitor, Context context, int u)
/*      */   {
/*  982 */     char[] c = context.buffer;
/*  983 */     context.access = readUnsignedShort(u);
/*  984 */     context.name = readUTF8(u + 2, c);
/*  985 */     context.desc = readUTF8(u + 4, c);
/*  986 */     u += 6;
/*      */     
/*      */ 
/*  989 */     int code = 0;
/*  990 */     int exception = 0;
/*  991 */     String[] exceptions = null;
/*  992 */     String signature = null;
/*  993 */     int methodParameters = 0;
/*  994 */     int anns = 0;
/*  995 */     int ianns = 0;
/*  996 */     int tanns = 0;
/*  997 */     int itanns = 0;
/*  998 */     int dann = 0;
/*  999 */     int mpanns = 0;
/* 1000 */     int impanns = 0;
/* 1001 */     int firstAttribute = u;
/* 1002 */     Attribute attributes = null;
/*      */     
/* 1004 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 1005 */       String attrName = readUTF8(u + 2, c);
/*      */       
/*      */ 
/* 1008 */       if ("Code".equals(attrName)) {
/* 1009 */         if ((context.flags & 0x1) == 0) {
/* 1010 */           code = u + 8;
/*      */         }
/* 1012 */       } else if ("Exceptions".equals(attrName)) {
/* 1013 */         exceptions = new String[readUnsignedShort(u + 8)];
/* 1014 */         exception = u + 10;
/* 1015 */         for (int j = 0; j < exceptions.length; j++) {
/* 1016 */           exceptions[j] = readClass(exception, c);
/* 1017 */           exception += 2;
/*      */         }
/* 1019 */       } else if ("Signature".equals(attrName)) {
/* 1020 */         signature = readUTF8(u + 8, c);
/* 1021 */       } else if ("Deprecated".equals(attrName)) {
/* 1022 */         context.access |= 0x20000;
/*      */       }
/* 1024 */       else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/* 1025 */         anns = u + 8;
/*      */       }
/* 1027 */       else if ("RuntimeVisibleTypeAnnotations".equals(attrName)) {
/* 1028 */         tanns = u + 8;
/* 1029 */       } else if ("AnnotationDefault".equals(attrName)) {
/* 1030 */         dann = u + 8;
/* 1031 */       } else if ("Synthetic".equals(attrName)) {
/* 1032 */         context.access |= 0x41000;
/*      */ 
/*      */       }
/* 1035 */       else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/* 1036 */         ianns = u + 8;
/*      */       }
/* 1038 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attrName)) {
/* 1039 */         itanns = u + 8;
/*      */       }
/* 1041 */       else if ("RuntimeVisibleParameterAnnotations".equals(attrName)) {
/* 1042 */         mpanns = u + 8;
/*      */       }
/* 1044 */       else if ("RuntimeInvisibleParameterAnnotations".equals(attrName)) {
/* 1045 */         impanns = u + 8;
/* 1046 */       } else if ("MethodParameters".equals(attrName)) {
/* 1047 */         methodParameters = u + 8;
/*      */       } else {
/* 1049 */         Attribute attr = readAttribute(context.attrs, attrName, u + 8, 
/* 1050 */           readInt(u + 4), c, -1, null);
/* 1051 */         if (attr != null) {
/* 1052 */           attr.next = attributes;
/* 1053 */           attributes = attr;
/*      */         }
/*      */       }
/* 1056 */       u += 6 + readInt(u + 4);
/*      */     }
/* 1058 */     u += 2;
/*      */     
/*      */ 
/* 1061 */     MethodVisitor mv = classVisitor.visitMethod(context.access, context.name, context.desc, signature, exceptions);
/*      */     
/* 1063 */     if (mv == null) {
/* 1064 */       return u;
/*      */     }
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
/*      */ 
/* 1077 */     if ((mv instanceof MethodWriter)) {
/* 1078 */       MethodWriter mw = (MethodWriter)mv;
/* 1079 */       if ((mw.cw.cr == this) && (signature != null ? signature
/* 1080 */         .equals(mw.signature) : mw.signature == null)) {
/* 1081 */         boolean sameExceptions = false;
/* 1082 */         if (exceptions == null) {
/* 1083 */           sameExceptions = mw.exceptionCount == 0;
/* 1084 */         } else if (exceptions.length == mw.exceptionCount) {
/* 1085 */           sameExceptions = true;
/* 1086 */           for (int j = exceptions.length - 1; j >= 0; j--) {
/* 1087 */             exception -= 2;
/* 1088 */             if (mw.exceptions[j] != readUnsignedShort(exception)) {
/* 1089 */               sameExceptions = false;
/* 1090 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1094 */         if (sameExceptions)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1100 */           mw.classReaderOffset = firstAttribute;
/* 1101 */           mw.classReaderLength = (u - firstAttribute);
/* 1102 */           return u;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1108 */     if (methodParameters != 0) {
/* 1109 */       int i = this.b[methodParameters] & 0xFF; for (int v = methodParameters + 1; i > 0; v += 4) {
/* 1110 */         mv.visitParameter(readUTF8(v, c), readUnsignedShort(v + 2));i--;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1115 */     if (dann != 0) {
/* 1116 */       AnnotationVisitor dv = mv.visitAnnotationDefault();
/* 1117 */       readAnnotationValue(dann, c, null, dv);
/* 1118 */       if (dv != null) {
/* 1119 */         dv.visitEnd();
/*      */       }
/*      */     }
/* 1122 */     if (anns != 0) {
/* 1123 */       int i = readUnsignedShort(anns); for (int v = anns + 2; i > 0; i--) {
/* 1124 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1125 */           .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     }
/* 1128 */     if (ianns != 0) {
/* 1129 */       int i = readUnsignedShort(ianns); for (int v = ianns + 2; i > 0; i--) {
/* 1130 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1131 */           .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     }
/* 1134 */     if (tanns != 0) {
/* 1135 */       int i = readUnsignedShort(tanns); for (int v = tanns + 2; i > 0; i--) {
/* 1136 */         v = readAnnotationTarget(context, v);
/* 1137 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1138 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/* 1139 */           readUTF8(v, c), true));
/*      */       }
/*      */     }
/* 1142 */     if (itanns != 0) {
/* 1143 */       int i = readUnsignedShort(itanns); for (int v = itanns + 2; i > 0; i--) {
/* 1144 */         v = readAnnotationTarget(context, v);
/* 1145 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1146 */           .visitTypeAnnotation(context.typeRef, context.typePath, 
/* 1147 */           readUTF8(v, c), false));
/*      */       }
/*      */     }
/* 1150 */     if (mpanns != 0) {
/* 1151 */       readParameterAnnotations(mv, context, mpanns, true);
/*      */     }
/* 1153 */     if (impanns != 0) {
/* 1154 */       readParameterAnnotations(mv, context, impanns, false);
/*      */     }
/*      */     
/*      */ 
/* 1158 */     while (attributes != null) {
/* 1159 */       Attribute attr = attributes.next;
/* 1160 */       attributes.next = null;
/* 1161 */       mv.visitAttribute(attributes);
/* 1162 */       attributes = attr;
/*      */     }
/*      */     
/*      */ 
/* 1166 */     if (code != 0) {
/* 1167 */       mv.visitCode();
/* 1168 */       readCode(mv, context, code);
/*      */     }
/*      */     
/*      */ 
/* 1172 */     mv.visitEnd();
/*      */     
/* 1174 */     return u;
/*      */   }
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
/*      */ 
/*      */   private void readCode(MethodVisitor mv, Context context, int u)
/*      */   {
/* 1189 */     byte[] b = this.b;
/* 1190 */     char[] c = context.buffer;
/* 1191 */     int maxStack = readUnsignedShort(u);
/* 1192 */     int maxLocals = readUnsignedShort(u + 2);
/* 1193 */     int codeLength = readInt(u + 4);
/* 1194 */     u += 8;
/*      */     
/*      */ 
/* 1197 */     int codeStart = u;
/* 1198 */     int codeEnd = u + codeLength;
/* 1199 */     Label[] labels = context.labels = new Label[codeLength + 2];
/* 1200 */     readLabel(codeLength + 1, labels);
/* 1201 */     while (u < codeEnd) {
/* 1202 */       int offset = u - codeStart;
/* 1203 */       int opcode = b[u] & 0xFF;
/* 1204 */       switch (ClassWriter.TYPE[opcode]) {
/*      */       case 0: 
/*      */       case 4: 
/* 1207 */         u++;
/* 1208 */         break;
/*      */       case 9: 
/* 1210 */         readLabel(offset + readShort(u + 1), labels);
/* 1211 */         u += 3;
/* 1212 */         break;
/*      */       case 18: 
/* 1214 */         readLabel(offset + readUnsignedShort(u + 1), labels);
/* 1215 */         u += 3;
/* 1216 */         break;
/*      */       case 10: 
/*      */       case 19: 
/* 1219 */         readLabel(offset + readInt(u + 1), labels);
/* 1220 */         u += 5;
/* 1221 */         break;
/*      */       case 17: 
/* 1223 */         opcode = b[(u + 1)] & 0xFF;
/* 1224 */         if (opcode == 132) {
/* 1225 */           u += 6;
/*      */         } else {
/* 1227 */           u += 4;
/*      */         }
/* 1229 */         break;
/*      */       
/*      */       case 14: 
/* 1232 */         u = u + 4 - (offset & 0x3);
/*      */         
/* 1234 */         readLabel(offset + readInt(u), labels);
/* 1235 */         for (int i = readInt(u + 8) - readInt(u + 4) + 1; i > 0; i--) {
/* 1236 */           readLabel(offset + readInt(u + 12), labels);
/* 1237 */           u += 4;
/*      */         }
/* 1239 */         u += 12;
/* 1240 */         break;
/*      */       
/*      */       case 15: 
/* 1243 */         u = u + 4 - (offset & 0x3);
/*      */         
/* 1245 */         readLabel(offset + readInt(u), labels);
/* 1246 */         for (int i = readInt(u + 4); i > 0; i--) {
/* 1247 */           readLabel(offset + readInt(u + 12), labels);
/* 1248 */           u += 8;
/*      */         }
/* 1250 */         u += 8;
/* 1251 */         break;
/*      */       case 1: 
/*      */       case 3: 
/*      */       case 11: 
/* 1255 */         u += 2;
/* 1256 */         break;
/*      */       case 2: 
/*      */       case 5: 
/*      */       case 6: 
/*      */       case 12: 
/*      */       case 13: 
/* 1262 */         u += 3;
/* 1263 */         break;
/*      */       case 7: 
/*      */       case 8: 
/* 1266 */         u += 5;
/* 1267 */         break;
/*      */       case 16: 
/*      */       default: 
/* 1270 */         u += 4;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/* 1276 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 1277 */       Label start = readLabel(readUnsignedShort(u + 2), labels);
/* 1278 */       Label end = readLabel(readUnsignedShort(u + 4), labels);
/* 1279 */       Label handler = readLabel(readUnsignedShort(u + 6), labels);
/* 1280 */       String type = readUTF8(this.items[readUnsignedShort(u + 8)], c);
/* 1281 */       mv.visitTryCatchBlock(start, end, handler, type);
/* 1282 */       u += 8;
/*      */     }
/* 1284 */     u += 2;
/*      */     
/*      */ 
/* 1287 */     int[] tanns = null;
/* 1288 */     int[] itanns = null;
/* 1289 */     int tann = 0;
/* 1290 */     int itann = 0;
/* 1291 */     int ntoff = -1;
/* 1292 */     int nitoff = -1;
/* 1293 */     int varTable = 0;
/* 1294 */     int varTypeTable = 0;
/* 1295 */     boolean zip = true;
/* 1296 */     boolean unzip = (context.flags & 0x8) != 0;
/* 1297 */     int stackMap = 0;
/* 1298 */     int stackMapSize = 0;
/* 1299 */     int frameCount = 0;
/* 1300 */     Context frame = null;
/* 1301 */     Attribute attributes = null;
/*      */     
/* 1303 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 1304 */       String attrName = readUTF8(u + 2, c);
/* 1305 */       if ("LocalVariableTable".equals(attrName)) {
/* 1306 */         if ((context.flags & 0x2) == 0) {
/* 1307 */           varTable = u + 8;
/* 1308 */           int j = readUnsignedShort(u + 8); for (int v = u; j > 0; j--) {
/* 1309 */             int label = readUnsignedShort(v + 10);
/* 1310 */             if (labels[label] == null) {
/* 1311 */               readLabel(label, labels).status |= 0x1;
/*      */             }
/* 1313 */             label += readUnsignedShort(v + 12);
/* 1314 */             if (labels[label] == null) {
/* 1315 */               readLabel(label, labels).status |= 0x1;
/*      */             }
/* 1317 */             v += 10;
/*      */           }
/*      */         }
/* 1320 */       } else if ("LocalVariableTypeTable".equals(attrName)) {
/* 1321 */         varTypeTable = u + 8;
/* 1322 */       } else if ("LineNumberTable".equals(attrName)) {
/* 1323 */         if ((context.flags & 0x2) == 0) {
/* 1324 */           int j = readUnsignedShort(u + 8); for (int v = u; j > 0; j--) {
/* 1325 */             int label = readUnsignedShort(v + 10);
/* 1326 */             if (labels[label] == null) {
/* 1327 */               readLabel(label, labels).status |= 0x1;
/*      */             }
/* 1329 */             Label l = labels[label];
/* 1330 */             while (l.line > 0) {
/* 1331 */               if (l.next == null) {
/* 1332 */                 l.next = new Label();
/*      */               }
/* 1334 */               l = l.next;
/*      */             }
/* 1336 */             l.line = readUnsignedShort(v + 12);
/* 1337 */             v += 4;
/*      */           }
/*      */         }
/*      */       }
/* 1341 */       else if ("RuntimeVisibleTypeAnnotations".equals(attrName)) {
/* 1342 */         tanns = readTypeAnnotations(mv, context, u + 8, true);
/*      */         
/* 1344 */         ntoff = (tanns.length == 0) || (readByte(tanns[0]) < 67) ? -1 : readUnsignedShort(tanns[0] + 1);
/*      */       }
/* 1346 */       else if ("RuntimeInvisibleTypeAnnotations".equals(attrName)) {
/* 1347 */         itanns = readTypeAnnotations(mv, context, u + 8, false);
/*      */         
/* 1349 */         nitoff = (itanns.length == 0) || (readByte(itanns[0]) < 67) ? -1 : readUnsignedShort(itanns[0] + 1);
/* 1350 */       } else if ("StackMapTable".equals(attrName)) {
/* 1351 */         if ((context.flags & 0x4) == 0) {
/* 1352 */           stackMap = u + 10;
/* 1353 */           stackMapSize = readInt(u + 4);
/* 1354 */           frameCount = readUnsignedShort(u + 8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 1374 */       else if ("StackMap".equals(attrName)) {
/* 1375 */         if ((context.flags & 0x4) == 0) {
/* 1376 */           zip = false;
/* 1377 */           stackMap = u + 10;
/* 1378 */           stackMapSize = readInt(u + 4);
/* 1379 */           frameCount = readUnsignedShort(u + 8);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1387 */         for (int j = 0; j < context.attrs.length; j++) {
/* 1388 */           if (context.attrs[j].type.equals(attrName)) {
/* 1389 */             Attribute attr = context.attrs[j].read(this, u + 8, 
/* 1390 */               readInt(u + 4), c, codeStart - 8, labels);
/* 1391 */             if (attr != null) {
/* 1392 */               attr.next = attributes;
/* 1393 */               attributes = attr;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1398 */       u += 6 + readInt(u + 4);
/*      */     }
/* 1400 */     u += 2;
/*      */     
/*      */ 
/* 1403 */     if (stackMap != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1409 */       frame = context;
/* 1410 */       frame.offset = -1;
/* 1411 */       frame.mode = 0;
/* 1412 */       frame.localCount = 0;
/* 1413 */       frame.localDiff = 0;
/* 1414 */       frame.stackCount = 0;
/* 1415 */       frame.local = new Object[maxLocals];
/* 1416 */       frame.stack = new Object[maxStack];
/* 1417 */       if (unzip) {
/* 1418 */         getImplicitFrame(context);
/*      */       }
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
/*      */ 
/* 1431 */       for (int i = stackMap; i < stackMap + stackMapSize - 2; i++) {
/* 1432 */         if (b[i] == 8) {
/* 1433 */           int v = readUnsignedShort(i + 1);
/* 1434 */           if ((v >= 0) && (v < codeLength) && 
/* 1435 */             ((b[(codeStart + v)] & 0xFF) == 187)) {
/* 1436 */             readLabel(v, labels);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1442 */     if (((context.flags & 0x100) != 0) && ((context.flags & 0x8) != 0))
/*      */     {
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
/*      */ 
/* 1455 */       mv.visitFrame(-1, maxLocals, null, 0, null);
/*      */     }
/*      */     
/*      */ 
/* 1459 */     int opcodeDelta = (context.flags & 0x100) == 0 ? -33 : 0;
/* 1460 */     boolean insertFrame = false;
/* 1461 */     u = codeStart;
/* 1462 */     for (; u < codeEnd; 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1712 */         goto 2996)
/*      */     {
/* 1463 */       int offset = u - codeStart;
/*      */       
/*      */ 
/* 1466 */       Label l = labels[offset];
/* 1467 */       if (l != null) {
/* 1468 */         Label next = l.next;
/* 1469 */         l.next = null;
/* 1470 */         mv.visitLabel(l);
/* 1471 */         if (((context.flags & 0x2) == 0) && (l.line > 0)) {
/* 1472 */           mv.visitLineNumber(l.line, l);
/* 1473 */           while (next != null) {
/* 1474 */             mv.visitLineNumber(next.line, l);
/* 1475 */             next = next.next;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1481 */       while ((frame != null) && ((frame.offset == offset) || (frame.offset == -1)))
/*      */       {
/*      */ 
/*      */ 
/* 1485 */         if (frame.offset != -1) {
/* 1486 */           if ((!zip) || (unzip)) {
/* 1487 */             mv.visitFrame(-1, frame.localCount, frame.local, frame.stackCount, frame.stack);
/*      */           }
/*      */           else {
/* 1490 */             mv.visitFrame(frame.mode, frame.localDiff, frame.local, frame.stackCount, frame.stack);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1495 */           insertFrame = false;
/*      */         }
/* 1497 */         if (frameCount > 0) {
/* 1498 */           stackMap = readFrame(stackMap, zip, unzip, frame);
/* 1499 */           frameCount--;
/*      */         } else {
/* 1501 */           frame = null;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1507 */       if (insertFrame) {
/* 1508 */         mv.visitFrame(256, 0, null, 0, null);
/* 1509 */         insertFrame = false;
/*      */       }
/*      */       
/*      */ 
/* 1513 */       int opcode = b[u] & 0xFF;
/* 1514 */       switch (ClassWriter.TYPE[opcode]) {
/*      */       case 0: 
/* 1516 */         mv.visitInsn(opcode);
/* 1517 */         u++;
/* 1518 */         break;
/*      */       case 4: 
/* 1520 */         if (opcode > 54) {
/* 1521 */           opcode -= 59;
/* 1522 */           mv.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
/*      */         }
/*      */         else {
/* 1525 */           opcode -= 26;
/* 1526 */           mv.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
/*      */         }
/* 1528 */         u++;
/* 1529 */         break;
/*      */       case 9: 
/* 1531 */         mv.visitJumpInsn(opcode, labels[(offset + readShort(u + 1))]);
/* 1532 */         u += 3;
/* 1533 */         break;
/*      */       case 10: 
/* 1535 */         mv.visitJumpInsn(opcode + opcodeDelta, labels[
/* 1536 */           (offset + readInt(u + 1))]);
/* 1537 */         u += 5;
/* 1538 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 18: 
/* 1543 */         opcode = opcode < 218 ? opcode - 49 : opcode - 20;
/* 1544 */         Label target = labels[(offset + readUnsignedShort(u + 1))];
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */         if ((opcode == 167) || (opcode == 168)) {
/* 1551 */           mv.visitJumpInsn(opcode + 33, target);
/*      */         } else {
/* 1553 */           opcode = opcode <= 166 ? (opcode + 1 ^ 0x1) - 1 : opcode ^ 0x1;
/*      */           
/* 1555 */           Label endif = readLabel(offset + 3, labels);
/* 1556 */           mv.visitJumpInsn(opcode, endif);
/* 1557 */           mv.visitJumpInsn(200, target);
/*      */           
/*      */ 
/*      */ 
/* 1561 */           insertFrame = true;
/*      */         }
/* 1563 */         u += 3;
/* 1564 */         break;
/*      */       
/*      */ 
/*      */       case 19: 
/* 1568 */         mv.visitJumpInsn(200, labels[(offset + readInt(u + 1))]);
/*      */         
/*      */ 
/*      */ 
/* 1572 */         insertFrame = true;
/* 1573 */         u += 5;
/* 1574 */         break;
/*      */       
/*      */       case 17: 
/* 1577 */         opcode = b[(u + 1)] & 0xFF;
/* 1578 */         if (opcode == 132) {
/* 1579 */           mv.visitIincInsn(readUnsignedShort(u + 2), readShort(u + 4));
/* 1580 */           u += 6;
/*      */         } else {
/* 1582 */           mv.visitVarInsn(opcode, readUnsignedShort(u + 2));
/* 1583 */           u += 4;
/*      */         }
/* 1585 */         break;
/*      */       
/*      */       case 14: 
/* 1588 */         u = u + 4 - (offset & 0x3);
/*      */         
/* 1590 */         int label = offset + readInt(u);
/* 1591 */         int min = readInt(u + 4);
/* 1592 */         int max = readInt(u + 8);
/* 1593 */         Label[] table = new Label[max - min + 1];
/* 1594 */         u += 12;
/* 1595 */         for (int i = 0; i < table.length; i++) {
/* 1596 */           table[i] = labels[(offset + readInt(u))];
/* 1597 */           u += 4;
/*      */         }
/* 1599 */         mv.visitTableSwitchInsn(min, max, labels[label], table);
/* 1600 */         break;
/*      */       
/*      */ 
/*      */       case 15: 
/* 1604 */         u = u + 4 - (offset & 0x3);
/*      */         
/* 1606 */         int label = offset + readInt(u);
/* 1607 */         int len = readInt(u + 4);
/* 1608 */         int[] keys = new int[len];
/* 1609 */         Label[] values = new Label[len];
/* 1610 */         u += 8;
/* 1611 */         for (int i = 0; i < len; i++) {
/* 1612 */           keys[i] = readInt(u);
/* 1613 */           values[i] = labels[(offset + readInt(u + 4))];
/* 1614 */           u += 8;
/*      */         }
/* 1616 */         mv.visitLookupSwitchInsn(labels[label], keys, values);
/* 1617 */         break;
/*      */       
/*      */       case 3: 
/* 1620 */         mv.visitVarInsn(opcode, b[(u + 1)] & 0xFF);
/* 1621 */         u += 2;
/* 1622 */         break;
/*      */       case 1: 
/* 1624 */         mv.visitIntInsn(opcode, b[(u + 1)]);
/* 1625 */         u += 2;
/* 1626 */         break;
/*      */       case 2: 
/* 1628 */         mv.visitIntInsn(opcode, readShort(u + 1));
/* 1629 */         u += 3;
/* 1630 */         break;
/*      */       case 11: 
/* 1632 */         mv.visitLdcInsn(readConst(b[(u + 1)] & 0xFF, c));
/* 1633 */         u += 2;
/* 1634 */         break;
/*      */       case 12: 
/* 1636 */         mv.visitLdcInsn(readConst(readUnsignedShort(u + 1), c));
/* 1637 */         u += 3;
/* 1638 */         break;
/*      */       case 6: 
/*      */       case 7: 
/* 1641 */         int cpIndex = this.items[readUnsignedShort(u + 1)];
/* 1642 */         boolean itf = b[(cpIndex - 1)] == 11;
/* 1643 */         String iowner = readClass(cpIndex, c);
/* 1644 */         cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
/* 1645 */         String iname = readUTF8(cpIndex, c);
/* 1646 */         String idesc = readUTF8(cpIndex + 2, c);
/* 1647 */         if (opcode < 182) {
/* 1648 */           mv.visitFieldInsn(opcode, iowner, iname, idesc);
/*      */         } else {
/* 1650 */           mv.visitMethodInsn(opcode, iowner, iname, idesc, itf);
/*      */         }
/* 1652 */         if (opcode == 185) {
/* 1653 */           u += 5;
/*      */         } else {
/* 1655 */           u += 3;
/*      */         }
/* 1657 */         break;
/*      */       
/*      */       case 8: 
/* 1660 */         int cpIndex = this.items[readUnsignedShort(u + 1)];
/* 1661 */         int bsmIndex = context.bootstrapMethods[readUnsignedShort(cpIndex)];
/* 1662 */         Handle bsm = (Handle)readConst(readUnsignedShort(bsmIndex), c);
/* 1663 */         int bsmArgCount = readUnsignedShort(bsmIndex + 2);
/* 1664 */         Object[] bsmArgs = new Object[bsmArgCount];
/* 1665 */         bsmIndex += 4;
/* 1666 */         for (int i = 0; i < bsmArgCount; i++) {
/* 1667 */           bsmArgs[i] = readConst(readUnsignedShort(bsmIndex), c);
/* 1668 */           bsmIndex += 2;
/*      */         }
/* 1670 */         cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
/* 1671 */         String iname = readUTF8(cpIndex, c);
/* 1672 */         String idesc = readUTF8(cpIndex + 2, c);
/* 1673 */         mv.visitInvokeDynamicInsn(iname, idesc, bsm, bsmArgs);
/* 1674 */         u += 5;
/* 1675 */         break;
/*      */       
/*      */       case 5: 
/* 1678 */         mv.visitTypeInsn(opcode, readClass(u + 1, c));
/* 1679 */         u += 3;
/* 1680 */         break;
/*      */       case 13: 
/* 1682 */         mv.visitIincInsn(b[(u + 1)] & 0xFF, b[(u + 2)]);
/* 1683 */         u += 3;
/* 1684 */         break;
/*      */       case 16: 
/*      */       default: 
/* 1687 */         mv.visitMultiANewArrayInsn(readClass(u + 1, c), b[(u + 3)] & 0xFF);
/* 1688 */         u += 4;
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 1693 */       while ((tanns != null) && (tann < tanns.length) && (ntoff <= offset)) {
/* 1694 */         if (ntoff == offset) {
/* 1695 */           int v = readAnnotationTarget(context, tanns[tann]);
/* 1696 */           readAnnotationValues(v + 2, c, true, mv
/* 1697 */             .visitInsnAnnotation(context.typeRef, context.typePath, 
/* 1698 */             readUTF8(v, c), true));
/*      */         }
/* 1700 */         tann++;
/* 1701 */         ntoff = (tann >= tanns.length) || (readByte(tanns[tann]) < 67) ? -1 : readUnsignedShort(tanns[tann] + 1);
/*      */       }
/* 1703 */       if ((itanns != null) && (itann < itanns.length) && (nitoff <= offset)) {
/* 1704 */         if (nitoff == offset) {
/* 1705 */           int v = readAnnotationTarget(context, itanns[itann]);
/* 1706 */           readAnnotationValues(v + 2, c, true, mv
/* 1707 */             .visitInsnAnnotation(context.typeRef, context.typePath, 
/* 1708 */             readUTF8(v, c), false));
/*      */         }
/* 1710 */         itann++;
/*      */         
/* 1712 */         nitoff = (itann >= itanns.length) || (readByte(itanns[itann]) < 67) ? -1 : readUnsignedShort(itanns[itann] + 1);
/*      */       }
/*      */     }
/* 1715 */     if (labels[codeLength] != null) {
/* 1716 */       mv.visitLabel(labels[codeLength]);
/*      */     }
/*      */     
/*      */ 
/* 1720 */     if (((context.flags & 0x2) == 0) && (varTable != 0)) {
/* 1721 */       int[] typeTable = null;
/* 1722 */       int i; if (varTypeTable != 0) {
/* 1723 */         u = varTypeTable + 2;
/* 1724 */         typeTable = new int[readUnsignedShort(varTypeTable) * 3];
/* 1725 */         for (i = typeTable.length; i > 0;) {
/* 1726 */           typeTable[(--i)] = (u + 6);
/* 1727 */           typeTable[(--i)] = readUnsignedShort(u + 8);
/* 1728 */           typeTable[(--i)] = readUnsignedShort(u);
/* 1729 */           u += 10;
/*      */         }
/*      */       }
/* 1732 */       u = varTable + 2;
/* 1733 */       for (int i = readUnsignedShort(varTable); i > 0; i--) {
/* 1734 */         int start = readUnsignedShort(u);
/* 1735 */         int length = readUnsignedShort(u + 2);
/* 1736 */         int index = readUnsignedShort(u + 8);
/* 1737 */         String vsignature = null;
/* 1738 */         if (typeTable != null) {
/* 1739 */           for (int j = 0; j < typeTable.length; j += 3) {
/* 1740 */             if ((typeTable[j] == start) && (typeTable[(j + 1)] == index)) {
/* 1741 */               vsignature = readUTF8(typeTable[(j + 2)], c);
/* 1742 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1746 */         mv.visitLocalVariable(readUTF8(u + 4, c), readUTF8(u + 6, c), vsignature, labels[start], labels[(start + length)], index);
/*      */         
/*      */ 
/* 1749 */         u += 10;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1754 */     if (tanns != null) {
/* 1755 */       for (int i = 0; i < tanns.length; i++) {
/* 1756 */         if (readByte(tanns[i]) >> 1 == 32) {
/* 1757 */           int v = readAnnotationTarget(context, tanns[i]);
/* 1758 */           v = readAnnotationValues(v + 2, c, true, mv
/* 1759 */             .visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, 
/*      */             
/* 1761 */             readUTF8(v, c), true));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1766 */     if (itanns != null) {
/* 1767 */       for (int i = 0; i < itanns.length; i++) {
/* 1768 */         if (readByte(itanns[i]) >> 1 == 32) {
/* 1769 */           int v = readAnnotationTarget(context, itanns[i]);
/* 1770 */           v = readAnnotationValues(v + 2, c, true, mv
/* 1771 */             .visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, 
/*      */             
/* 1773 */             readUTF8(v, c), false));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1780 */     while (attributes != null) {
/* 1781 */       Attribute attr = attributes.next;
/* 1782 */       attributes.next = null;
/* 1783 */       mv.visitAttribute(attributes);
/* 1784 */       attributes = attr;
/*      */     }
/*      */     
/*      */ 
/* 1788 */     mv.visitMaxs(maxStack, maxLocals);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] readTypeAnnotations(MethodVisitor mv, Context context, int u, boolean visible)
/*      */   {
/* 1809 */     char[] c = context.buffer;
/* 1810 */     int[] offsets = new int[readUnsignedShort(u)];
/* 1811 */     u += 2;
/* 1812 */     for (int i = 0; i < offsets.length; i++) {
/* 1813 */       offsets[i] = u;
/* 1814 */       int target = readInt(u);
/* 1815 */       switch (target >>> 24) {
/*      */       case 0: 
/*      */       case 1: 
/*      */       case 22: 
/* 1819 */         u += 2;
/* 1820 */         break;
/*      */       case 19: 
/*      */       case 20: 
/*      */       case 21: 
/* 1824 */         u++;
/* 1825 */         break;
/*      */       case 64: 
/*      */       case 65: 
/* 1828 */         for (int j = readUnsignedShort(u + 1); j > 0; j--) {
/* 1829 */           int start = readUnsignedShort(u + 3);
/* 1830 */           int length = readUnsignedShort(u + 5);
/* 1831 */           readLabel(start, context.labels);
/* 1832 */           readLabel(start + length, context.labels);
/* 1833 */           u += 6;
/*      */         }
/* 1835 */         u += 3;
/* 1836 */         break;
/*      */       case 71: 
/*      */       case 72: 
/*      */       case 73: 
/*      */       case 74: 
/*      */       case 75: 
/* 1842 */         u += 4;
/* 1843 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       default: 
/* 1854 */         u += 3;
/*      */       }
/*      */       
/* 1857 */       int pathLength = readByte(u);
/* 1858 */       if (target >>> 24 == 66) {
/* 1859 */         TypePath path = pathLength == 0 ? null : new TypePath(this.b, u);
/* 1860 */         u += 1 + 2 * pathLength;
/* 1861 */         u = readAnnotationValues(u + 2, c, true, mv
/* 1862 */           .visitTryCatchAnnotation(target, path, 
/* 1863 */           readUTF8(u, c), visible));
/*      */       } else {
/* 1865 */         u = readAnnotationValues(u + 3 + 2 * pathLength, c, true, null);
/*      */       }
/*      */     }
/* 1868 */     return offsets;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readAnnotationTarget(Context context, int u)
/*      */   {
/* 1886 */     int target = readInt(u);
/* 1887 */     switch (target >>> 24) {
/*      */     case 0: 
/*      */     case 1: 
/*      */     case 22: 
/* 1891 */       target &= 0xFFFF0000;
/* 1892 */       u += 2;
/* 1893 */       break;
/*      */     case 19: 
/*      */     case 20: 
/*      */     case 21: 
/* 1897 */       target &= 0xFF000000;
/* 1898 */       u++;
/* 1899 */       break;
/*      */     case 64: 
/*      */     case 65: 
/* 1902 */       target &= 0xFF000000;
/* 1903 */       int n = readUnsignedShort(u + 1);
/* 1904 */       context.start = new Label[n];
/* 1905 */       context.end = new Label[n];
/* 1906 */       context.index = new int[n];
/* 1907 */       u += 3;
/* 1908 */       for (int i = 0; i < n; i++) {
/* 1909 */         int start = readUnsignedShort(u);
/* 1910 */         int length = readUnsignedShort(u + 2);
/* 1911 */         context.start[i] = readLabel(start, context.labels);
/* 1912 */         context.end[i] = readLabel(start + length, context.labels);
/* 1913 */         context.index[i] = readUnsignedShort(u + 4);
/* 1914 */         u += 6;
/*      */       }
/* 1916 */       break;
/*      */     
/*      */     case 71: 
/*      */     case 72: 
/*      */     case 73: 
/*      */     case 74: 
/*      */     case 75: 
/* 1923 */       target &= 0xFF0000FF;
/* 1924 */       u += 4;
/* 1925 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     default: 
/* 1936 */       target &= (target >>> 24 < 67 ? 65280 : -16777216);
/* 1937 */       u += 3;
/*      */     }
/*      */     
/* 1940 */     int pathLength = readByte(u);
/* 1941 */     context.typeRef = target;
/* 1942 */     context.typePath = (pathLength == 0 ? null : new TypePath(this.b, u));
/* 1943 */     return u + 1 + 2 * pathLength;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readParameterAnnotations(MethodVisitor mv, Context context, int v, boolean visible)
/*      */   {
/* 1962 */     int n = this.b[(v++)] & 0xFF;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1969 */     int synthetics = Type.getArgumentTypes(context.desc).length - n;
/*      */     
/* 1971 */     for (int i = 0; i < synthetics; i++)
/*      */     {
/* 1973 */       AnnotationVisitor av = mv.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
/* 1974 */       if (av != null) {
/* 1975 */         av.visitEnd();
/*      */       }
/*      */     }
/* 1978 */     char[] c = context.buffer;
/* 1979 */     for (; i < n + synthetics; i++) {
/* 1980 */       int j = readUnsignedShort(v);
/* 1981 */       v += 2;
/* 1982 */       for (; j > 0; j--) {
/* 1983 */         AnnotationVisitor av = mv.visitParameterAnnotation(i, readUTF8(v, c), visible);
/* 1984 */         v = readAnnotationValues(v + 2, c, true, av);
/*      */       }
/*      */     }
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readAnnotationValues(int v, char[] buf, boolean named, AnnotationVisitor av)
/*      */   {
/* 2008 */     int i = readUnsignedShort(v);
/* 2009 */     v += 2;
/* 2010 */     if (named) {
/* 2011 */       for (; i > 0; i--) {
/* 2012 */         v = readAnnotationValue(v + 2, buf, readUTF8(v, buf), av);
/*      */       }
/*      */     }
/* 2015 */     for (; i > 0; i--) {
/* 2016 */       v = readAnnotationValue(v, buf, null, av);
/*      */     }
/*      */     
/* 2019 */     if (av != null) {
/* 2020 */       av.visitEnd();
/*      */     }
/* 2022 */     return v;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readAnnotationValue(int v, char[] buf, String name, AnnotationVisitor av)
/*      */   {
/* 2044 */     if (av == null) {
/* 2045 */       switch (this.b[v] & 0xFF) {
/*      */       case 101: 
/* 2047 */         return v + 5;
/*      */       case 64: 
/* 2049 */         return readAnnotationValues(v + 3, buf, true, null);
/*      */       case 91: 
/* 2051 */         return readAnnotationValues(v + 1, buf, false, null);
/*      */       }
/* 2053 */       return v + 3;
/*      */     }
/*      */     
/* 2056 */     switch (this.b[(v++)] & 0xFF) {
/*      */     case 68: 
/*      */     case 70: 
/*      */     case 73: 
/*      */     case 74: 
/* 2061 */       av.visit(name, readConst(readUnsignedShort(v), buf));
/* 2062 */       v += 2;
/* 2063 */       break;
/*      */     case 66: 
/* 2065 */       av.visit(name, Byte.valueOf((byte)readInt(this.items[readUnsignedShort(v)])));
/* 2066 */       v += 2;
/* 2067 */       break;
/*      */     case 90: 
/* 2069 */       av.visit(name, 
/* 2070 */         readInt(this.items[readUnsignedShort(v)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
/*      */       
/* 2072 */       v += 2;
/* 2073 */       break;
/*      */     case 83: 
/* 2075 */       av.visit(name, Short.valueOf((short)readInt(this.items[readUnsignedShort(v)])));
/* 2076 */       v += 2;
/* 2077 */       break;
/*      */     case 67: 
/* 2079 */       av.visit(name, Character.valueOf((char)readInt(this.items[readUnsignedShort(v)])));
/* 2080 */       v += 2;
/* 2081 */       break;
/*      */     case 115: 
/* 2083 */       av.visit(name, readUTF8(v, buf));
/* 2084 */       v += 2;
/* 2085 */       break;
/*      */     case 101: 
/* 2087 */       av.visitEnum(name, readUTF8(v, buf), readUTF8(v + 2, buf));
/* 2088 */       v += 4;
/* 2089 */       break;
/*      */     case 99: 
/* 2091 */       av.visit(name, Type.getType(readUTF8(v, buf)));
/* 2092 */       v += 2;
/* 2093 */       break;
/*      */     case 64: 
/* 2095 */       v = readAnnotationValues(v + 2, buf, true, av
/* 2096 */         .visitAnnotation(name, readUTF8(v, buf)));
/* 2097 */       break;
/*      */     case 91: 
/* 2099 */       int size = readUnsignedShort(v);
/* 2100 */       v += 2;
/* 2101 */       if (size == 0) {
/* 2102 */         return readAnnotationValues(v - 2, buf, false, av
/* 2103 */           .visitArray(name));
/*      */       }
/* 2105 */       switch (this.b[(v++)] & 0xFF) {
/*      */       case 66: 
/* 2107 */         byte[] bv = new byte[size];
/* 2108 */         for (int i = 0; i < size; i++) {
/* 2109 */           bv[i] = ((byte)readInt(this.items[readUnsignedShort(v)]));
/* 2110 */           v += 3;
/*      */         }
/* 2112 */         av.visit(name, bv);
/* 2113 */         v--;
/* 2114 */         break;
/*      */       case 90: 
/* 2116 */         boolean[] zv = new boolean[size];
/* 2117 */         for (int i = 0; i < size; i++) {
/* 2118 */           zv[i] = (readInt(this.items[readUnsignedShort(v)]) != 0 ? 1 : false);
/* 2119 */           v += 3;
/*      */         }
/* 2121 */         av.visit(name, zv);
/* 2122 */         v--;
/* 2123 */         break;
/*      */       case 83: 
/* 2125 */         short[] sv = new short[size];
/* 2126 */         for (int i = 0; i < size; i++) {
/* 2127 */           sv[i] = ((short)readInt(this.items[readUnsignedShort(v)]));
/* 2128 */           v += 3;
/*      */         }
/* 2130 */         av.visit(name, sv);
/* 2131 */         v--;
/* 2132 */         break;
/*      */       case 67: 
/* 2134 */         char[] cv = new char[size];
/* 2135 */         for (int i = 0; i < size; i++) {
/* 2136 */           cv[i] = ((char)readInt(this.items[readUnsignedShort(v)]));
/* 2137 */           v += 3;
/*      */         }
/* 2139 */         av.visit(name, cv);
/* 2140 */         v--;
/* 2141 */         break;
/*      */       case 73: 
/* 2143 */         int[] iv = new int[size];
/* 2144 */         for (int i = 0; i < size; i++) {
/* 2145 */           iv[i] = readInt(this.items[readUnsignedShort(v)]);
/* 2146 */           v += 3;
/*      */         }
/* 2148 */         av.visit(name, iv);
/* 2149 */         v--;
/* 2150 */         break;
/*      */       case 74: 
/* 2152 */         long[] lv = new long[size];
/* 2153 */         for (int i = 0; i < size; i++) {
/* 2154 */           lv[i] = readLong(this.items[readUnsignedShort(v)]);
/* 2155 */           v += 3;
/*      */         }
/* 2157 */         av.visit(name, lv);
/* 2158 */         v--;
/* 2159 */         break;
/*      */       case 70: 
/* 2161 */         float[] fv = new float[size];
/* 2162 */         for (int i = 0; i < size; i++)
/*      */         {
/* 2164 */           fv[i] = Float.intBitsToFloat(readInt(this.items[readUnsignedShort(v)]));
/* 2165 */           v += 3;
/*      */         }
/* 2167 */         av.visit(name, fv);
/* 2168 */         v--;
/* 2169 */         break;
/*      */       case 68: 
/* 2171 */         double[] dv = new double[size];
/* 2172 */         for (int i = 0; i < size; i++)
/*      */         {
/* 2174 */           dv[i] = Double.longBitsToDouble(readLong(this.items[readUnsignedShort(v)]));
/* 2175 */           v += 3;
/*      */         }
/* 2177 */         av.visit(name, dv);
/* 2178 */         v--;
/* 2179 */         break;
/*      */       case 69: case 71: case 72: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 84: case 85: case 86: case 87: case 88: case 89: default: 
/* 2181 */         v = readAnnotationValues(v - 3, buf, false, av.visitArray(name));
/*      */       }
/*      */       break; }
/* 2184 */     return v;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getImplicitFrame(Context frame)
/*      */   {
/* 2195 */     String desc = frame.desc;
/* 2196 */     Object[] locals = frame.local;
/* 2197 */     int local = 0;
/* 2198 */     if ((frame.access & 0x8) == 0) {
/* 2199 */       if ("<init>".equals(frame.name)) {
/* 2200 */         locals[(local++)] = Opcodes.UNINITIALIZED_THIS;
/*      */       } else {
/* 2202 */         locals[(local++)] = readClass(this.header + 2, frame.buffer);
/*      */       }
/*      */     }
/* 2205 */     int i = 1;
/*      */     for (;;) {
/* 2207 */       int j = i;
/* 2208 */       switch (desc.charAt(i++)) {
/*      */       case 'B': 
/*      */       case 'C': 
/*      */       case 'I': 
/*      */       case 'S': 
/*      */       case 'Z': 
/* 2214 */         locals[(local++)] = Opcodes.INTEGER;
/* 2215 */         break;
/*      */       case 'F': 
/* 2217 */         locals[(local++)] = Opcodes.FLOAT;
/* 2218 */         break;
/*      */       case 'J': 
/* 2220 */         locals[(local++)] = Opcodes.LONG;
/* 2221 */         break;
/*      */       case 'D': 
/* 2223 */         locals[(local++)] = Opcodes.DOUBLE;
/* 2224 */         break;
/*      */       case '[': 
/* 2226 */         while (desc.charAt(i) == '[') {
/* 2227 */           i++;
/*      */         }
/* 2229 */         if (desc.charAt(i) == 'L') {
/* 2230 */           i++;
/* 2231 */           while (desc.charAt(i) != ';') {
/* 2232 */             i++;
/*      */           }
/*      */         }
/* 2235 */         locals[(local++)] = desc.substring(j, ++i);
/* 2236 */         break;
/*      */       case 'L': 
/* 2238 */         while (desc.charAt(i) != ';') {
/* 2239 */           i++;
/*      */         }
/* 2241 */         locals[(local++)] = desc.substring(j + 1, i++);
/* 2242 */         break;
/*      */       case 'E': case 'G': case 'H': case 'K': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
/*      */         break label371; }
/*      */     }
/*      */     label371:
/* 2247 */     frame.localCount = local;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readFrame(int stackMap, boolean zip, boolean unzip, Context frame)
/*      */   {
/* 2266 */     char[] c = frame.buffer;
/* 2267 */     Label[] labels = frame.labels;
/*      */     int tag;
/*      */     int tag;
/* 2270 */     if (zip) {
/* 2271 */       tag = this.b[(stackMap++)] & 0xFF;
/*      */     } else {
/* 2273 */       tag = 255;
/* 2274 */       frame.offset = -1;
/*      */     }
/* 2276 */     frame.localDiff = 0;
/* 2277 */     int delta; if (tag < 64) {
/* 2278 */       int delta = tag;
/* 2279 */       frame.mode = 3;
/* 2280 */       frame.stackCount = 0;
/* 2281 */     } else if (tag < 128) {
/* 2282 */       int delta = tag - 64;
/* 2283 */       stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
/* 2284 */       frame.mode = 4;
/* 2285 */       frame.stackCount = 1;
/*      */     } else {
/* 2287 */       delta = readUnsignedShort(stackMap);
/* 2288 */       stackMap += 2;
/* 2289 */       if (tag == 247) {
/* 2290 */         stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
/* 2291 */         frame.mode = 4;
/* 2292 */         frame.stackCount = 1;
/* 2293 */       } else if ((tag >= 248) && (tag < 251))
/*      */       {
/* 2295 */         frame.mode = 2;
/* 2296 */         frame.localDiff = (251 - tag);
/* 2297 */         frame.localCount -= frame.localDiff;
/* 2298 */         frame.stackCount = 0;
/* 2299 */       } else if (tag == 251) {
/* 2300 */         frame.mode = 3;
/* 2301 */         frame.stackCount = 0;
/* 2302 */       } else if (tag < 255) {
/* 2303 */         int local = unzip ? frame.localCount : 0;
/* 2304 */         for (int i = tag - 251; i > 0; i--) {
/* 2305 */           stackMap = readFrameType(frame.local, local++, stackMap, c, labels);
/*      */         }
/*      */         
/* 2308 */         frame.mode = 1;
/* 2309 */         frame.localDiff = (tag - 251);
/* 2310 */         frame.localCount += frame.localDiff;
/* 2311 */         frame.stackCount = 0;
/*      */       } else {
/* 2313 */         frame.mode = 0;
/* 2314 */         int n = readUnsignedShort(stackMap);
/* 2315 */         stackMap += 2;
/* 2316 */         frame.localDiff = n;
/* 2317 */         frame.localCount = n;
/* 2318 */         for (int local = 0; n > 0; n--) {
/* 2319 */           stackMap = readFrameType(frame.local, local++, stackMap, c, labels);
/*      */         }
/*      */         
/* 2322 */         n = readUnsignedShort(stackMap);
/* 2323 */         stackMap += 2;
/* 2324 */         frame.stackCount = n;
/* 2325 */         for (int stack = 0; n > 0; n--) {
/* 2326 */           stackMap = readFrameType(frame.stack, stack++, stackMap, c, labels);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2331 */     frame.offset += delta + 1;
/* 2332 */     readLabel(frame.offset, labels);
/* 2333 */     return stackMap;
/*      */   }
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
/*      */   private int readFrameType(Object[] frame, int index, int v, char[] buf, Label[] labels)
/*      */   {
/* 2357 */     int type = this.b[(v++)] & 0xFF;
/* 2358 */     switch (type) {
/*      */     case 0: 
/* 2360 */       frame[index] = Opcodes.TOP;
/* 2361 */       break;
/*      */     case 1: 
/* 2363 */       frame[index] = Opcodes.INTEGER;
/* 2364 */       break;
/*      */     case 2: 
/* 2366 */       frame[index] = Opcodes.FLOAT;
/* 2367 */       break;
/*      */     case 3: 
/* 2369 */       frame[index] = Opcodes.DOUBLE;
/* 2370 */       break;
/*      */     case 4: 
/* 2372 */       frame[index] = Opcodes.LONG;
/* 2373 */       break;
/*      */     case 5: 
/* 2375 */       frame[index] = Opcodes.NULL;
/* 2376 */       break;
/*      */     case 6: 
/* 2378 */       frame[index] = Opcodes.UNINITIALIZED_THIS;
/* 2379 */       break;
/*      */     case 7: 
/* 2381 */       frame[index] = readClass(v, buf);
/* 2382 */       v += 2;
/* 2383 */       break;
/*      */     default: 
/* 2385 */       frame[index] = readLabel(readUnsignedShort(v), labels);
/* 2386 */       v += 2;
/*      */     }
/* 2388 */     return v;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Label readLabel(int offset, Label[] labels)
/*      */   {
/* 2406 */     if (offset >= labels.length) {
/* 2407 */       return new Label();
/*      */     }
/*      */     
/* 2410 */     if (labels[offset] == null) {
/* 2411 */       labels[offset] = new Label();
/*      */     }
/* 2413 */     return labels[offset];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getAttributes()
/*      */   {
/* 2423 */     int u = this.header + 8 + readUnsignedShort(this.header + 6) * 2;
/*      */     
/* 2425 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 2426 */       for (int j = readUnsignedShort(u + 8); j > 0; j--) {
/* 2427 */         u += 6 + readInt(u + 12);
/*      */       }
/* 2429 */       u += 8;
/*      */     }
/* 2431 */     u += 2;
/* 2432 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 2433 */       for (int j = readUnsignedShort(u + 8); j > 0; j--) {
/* 2434 */         u += 6 + readInt(u + 12);
/*      */       }
/* 2436 */       u += 8;
/*      */     }
/*      */     
/* 2439 */     return u + 2;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels)
/*      */   {
/* 2478 */     for (int i = 0; i < attrs.length; i++) {
/* 2479 */       if (attrs[i].type.equals(type)) {
/* 2480 */         return attrs[i].read(this, off, len, buf, codeOff, labels);
/*      */       }
/*      */     }
/* 2483 */     return new Attribute(type).read(this, off, len, null, -1, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getItemCount()
/*      */   {
/* 2496 */     return this.items.length;
/*      */   }
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
/*      */   public int getItem(int item)
/*      */   {
/* 2510 */     return this.items[item];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStringLength()
/*      */   {
/* 2521 */     return this.maxStringLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readByte(int index)
/*      */   {
/* 2534 */     return this.b[index] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readUnsignedShort(int index)
/*      */   {
/* 2547 */     byte[] b = this.b;
/* 2548 */     return (b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public short readShort(int index)
/*      */   {
/* 2561 */     byte[] b = this.b;
/* 2562 */     return (short)((b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readInt(int index)
/*      */   {
/* 2575 */     byte[] b = this.b;
/* 2576 */     return (b[index] & 0xFF) << 24 | (b[(index + 1)] & 0xFF) << 16 | (b[(index + 2)] & 0xFF) << 8 | b[(index + 3)] & 0xFF;
/*      */   }
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
/*      */   public long readLong(int index)
/*      */   {
/* 2590 */     long l1 = readInt(index);
/* 2591 */     long l0 = readInt(index + 4) & 0xFFFFFFFF;
/* 2592 */     return l1 << 32 | l0;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readUTF8(int index, char[] buf)
/*      */   {
/* 2609 */     int item = readUnsignedShort(index);
/* 2610 */     if ((index == 0) || (item == 0)) {
/* 2611 */       return null;
/*      */     }
/* 2613 */     String s = this.strings[item];
/* 2614 */     if (s != null) {
/* 2615 */       return s;
/*      */     }
/* 2617 */     index = this.items[item];
/* 2618 */     return this.strings[item] = readUTF(index + 2, readUnsignedShort(index), buf);
/*      */   }
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
/*      */ 
/*      */ 
/*      */   private String readUTF(int index, int utfLen, char[] buf)
/*      */   {
/* 2634 */     int endIndex = index + utfLen;
/* 2635 */     byte[] b = this.b;
/* 2636 */     int strLen = 0;
/*      */     
/* 2638 */     int st = 0;
/* 2639 */     char cc = '\000';
/* 2640 */     while (index < endIndex) {
/* 2641 */       int c = b[(index++)];
/* 2642 */       switch (st) {
/*      */       case 0: 
/* 2644 */         c &= 0xFF;
/* 2645 */         if (c < 128) {
/* 2646 */           buf[(strLen++)] = ((char)c);
/* 2647 */         } else if ((c < 224) && (c > 191)) {
/* 2648 */           cc = (char)(c & 0x1F);
/* 2649 */           st = 1;
/*      */         } else {
/* 2651 */           cc = (char)(c & 0xF);
/* 2652 */           st = 2;
/*      */         }
/* 2654 */         break;
/*      */       
/*      */       case 1: 
/* 2657 */         buf[(strLen++)] = ((char)(cc << '\006' | c & 0x3F));
/* 2658 */         st = 0;
/* 2659 */         break;
/*      */       
/*      */       case 2: 
/* 2662 */         cc = (char)(cc << '\006' | c & 0x3F);
/* 2663 */         st = 1;
/*      */       }
/*      */       
/*      */     }
/* 2667 */     return new String(buf, 0, strLen);
/*      */   }
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
/*      */   private String readStringish(int index, char[] buf)
/*      */   {
/* 2681 */     return readUTF8(this.items[readUnsignedShort(index)], buf);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readClass(int index, char[] buf)
/*      */   {
/* 2698 */     return readStringish(index, buf);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readModule(int index, char[] buf)
/*      */   {
/* 2715 */     return readStringish(index, buf);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readPackage(int index, char[] buf)
/*      */   {
/* 2732 */     return readStringish(index, buf);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object readConst(int item, char[] buf)
/*      */   {
/* 2750 */     int index = this.items[item];
/* 2751 */     switch (this.b[(index - 1)]) {
/*      */     case 3: 
/* 2753 */       return Integer.valueOf(readInt(index));
/*      */     case 4: 
/* 2755 */       return Float.valueOf(Float.intBitsToFloat(readInt(index)));
/*      */     case 5: 
/* 2757 */       return Long.valueOf(readLong(index));
/*      */     case 6: 
/* 2759 */       return Double.valueOf(Double.longBitsToDouble(readLong(index)));
/*      */     case 7: 
/* 2761 */       return Type.getObjectType(readUTF8(index, buf));
/*      */     case 8: 
/* 2763 */       return readUTF8(index, buf);
/*      */     case 16: 
/* 2765 */       return Type.getMethodType(readUTF8(index, buf));
/*      */     }
/* 2767 */     int tag = readByte(index);
/* 2768 */     int[] items = this.items;
/* 2769 */     int cpIndex = items[readUnsignedShort(index + 1)];
/* 2770 */     boolean itf = this.b[(cpIndex - 1)] == 11;
/* 2771 */     String owner = readClass(cpIndex, buf);
/* 2772 */     cpIndex = items[readUnsignedShort(cpIndex + 2)];
/* 2773 */     String name = readUTF8(cpIndex, buf);
/* 2774 */     String desc = readUTF8(cpIndex + 2, buf);
/* 2775 */     return new Handle(tag, owner, name, desc, itf);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\asm\ClassReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */