/*     */ package org.apache.tomcat.util.bcel.classfile;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassParser
/*     */ {
/*     */   private static final int MAGIC = -889275714;
/*     */   private final DataInput dataInputStream;
/*     */   private String class_name;
/*     */   private String superclass_name;
/*     */   private int access_flags;
/*     */   private String[] interface_names;
/*     */   private ConstantPool constant_pool;
/*     */   private Annotations runtimeVisibleAnnotations;
/*     */   private static final int BUFSIZE = 8192;
/*  52 */   private static final String[] INTERFACES_EMPTY_ARRAY = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassParser(InputStream inputStream)
/*     */   {
/*  60 */     this.dataInputStream = new DataInputStream(new BufferedInputStream(inputStream, 8192));
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
/*     */   public JavaClass parse()
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  78 */     readID();
/*     */     
/*  80 */     readVersion();
/*     */     
/*     */ 
/*  83 */     readConstantPool();
/*     */     
/*  85 */     readClassInfo();
/*     */     
/*  87 */     readInterfaces();
/*     */     
/*     */ 
/*  90 */     readFields();
/*     */     
/*  92 */     readMethods();
/*     */     
/*  94 */     readAttributes();
/*     */     
/*     */ 
/*  97 */     return new JavaClass(this.class_name, this.superclass_name, this.access_flags, this.constant_pool, this.interface_names, this.runtimeVisibleAnnotations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readAttributes()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 109 */     int attributes_count = this.dataInputStream.readUnsignedShort();
/* 110 */     for (int i = 0; i < attributes_count; i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */       int name_index = this.dataInputStream.readUnsignedShort();
/* 117 */       ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(name_index, (byte)1);
/*     */       
/* 119 */       String name = c.getBytes();
/*     */       
/* 121 */       int length = this.dataInputStream.readInt();
/*     */       
/* 123 */       if (name.equals("RuntimeVisibleAnnotations")) {
/* 124 */         if (this.runtimeVisibleAnnotations != null) {
/* 125 */           throw new ClassFormatException("RuntimeVisibleAnnotations attribute is not allowed more than once in a class file");
/*     */         }
/*     */         
/* 128 */         this.runtimeVisibleAnnotations = new Annotations(this.dataInputStream, this.constant_pool);
/*     */       }
/*     */       else {
/* 131 */         Utility.skipFully(this.dataInputStream, length);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readClassInfo()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 143 */     this.access_flags = this.dataInputStream.readUnsignedShort();
/*     */     
/*     */ 
/*     */ 
/* 147 */     if ((this.access_flags & 0x200) != 0) {
/* 148 */       this.access_flags |= 0x400;
/*     */     }
/* 150 */     if (((this.access_flags & 0x400) != 0) && ((this.access_flags & 0x10) != 0))
/*     */     {
/* 152 */       throw new ClassFormatException("Class can't be both final and abstract");
/*     */     }
/*     */     
/* 155 */     int class_name_index = this.dataInputStream.readUnsignedShort();
/* 156 */     this.class_name = Utility.getClassName(this.constant_pool, class_name_index);
/*     */     
/* 158 */     int superclass_name_index = this.dataInputStream.readUnsignedShort();
/* 159 */     if (superclass_name_index > 0)
/*     */     {
/* 161 */       this.superclass_name = Utility.getClassName(this.constant_pool, superclass_name_index);
/*     */     } else {
/* 163 */       this.superclass_name = "java.lang.Object";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readConstantPool()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 174 */     this.constant_pool = new ConstantPool(this.dataInputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readFields()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 184 */     int fields_count = this.dataInputStream.readUnsignedShort();
/* 185 */     for (int i = 0; i < fields_count; i++) {
/* 186 */       Utility.swallowFieldOrMethod(this.dataInputStream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readID()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 199 */     if (this.dataInputStream.readInt() != -889275714) {
/* 200 */       throw new ClassFormatException("It is not a Java .class file");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readInterfaces()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 211 */     int interfaces_count = this.dataInputStream.readUnsignedShort();
/* 212 */     if (interfaces_count > 0) {
/* 213 */       this.interface_names = new String[interfaces_count];
/* 214 */       for (int i = 0; i < interfaces_count; i++) {
/* 215 */         int index = this.dataInputStream.readUnsignedShort();
/* 216 */         this.interface_names[i] = Utility.getClassName(this.constant_pool, index);
/*     */       }
/*     */     } else {
/* 219 */       this.interface_names = INTERFACES_EMPTY_ARRAY;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readMethods()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 230 */     int methods_count = this.dataInputStream.readUnsignedShort();
/* 231 */     for (int i = 0; i < methods_count; i++) {
/* 232 */       Utility.swallowFieldOrMethod(this.dataInputStream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readVersion()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 245 */     Utility.skipFully(this.dataInputStream, 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ClassParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */