/*     */ package org.springframework.cglib.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator.Source;
/*     */ import org.springframework.cglib.core.ClassEmitter;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.KeyFactory;
/*     */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StringSwitcher
/*     */ {
/*  29 */   private static final Type STRING_SWITCHER = TypeUtils.parseType("org.springframework.cglib.util.StringSwitcher");
/*     */   
/*  31 */   private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue(String)");
/*     */   
/*  33 */   private static final StringSwitcherKey KEY_FACTORY = (StringSwitcherKey)KeyFactory.create(StringSwitcherKey.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringSwitcher create(String[] strings, int[] ints, boolean fixedInput)
/*     */   {
/*  49 */     Generator gen = new Generator();
/*  50 */     gen.setStrings(strings);
/*  51 */     gen.setInts(ints);
/*  52 */     gen.setFixedInput(fixedInput);
/*  53 */     return gen.create();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int intValue(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Generator
/*     */     extends AbstractClassGenerator
/*     */   {
/*  69 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(StringSwitcher.class.getName());
/*     */     private String[] strings;
/*     */     private int[] ints;
/*     */     private boolean fixedInput;
/*     */     
/*     */     public Generator()
/*     */     {
/*  76 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setStrings(String[] strings)
/*     */     {
/*  85 */       this.strings = strings;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setInts(int[] ints)
/*     */     {
/*  94 */       this.ints = ints;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setFixedInput(boolean fixedInput)
/*     */     {
/* 103 */       this.fixedInput = fixedInput;
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 107 */       return getClass().getClassLoader();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public StringSwitcher create()
/*     */     {
/* 114 */       setNamePrefix(StringSwitcher.class.getName());
/* 115 */       Object key = StringSwitcher.KEY_FACTORY.newInstance(this.strings, this.ints, this.fixedInput);
/* 116 */       return (StringSwitcher)super.create(key);
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws Exception {
/* 120 */       ClassEmitter ce = new ClassEmitter(v);
/* 121 */       ce.begin_class(46, 1, 
/*     */       
/* 123 */         getClassName(), 
/* 124 */         StringSwitcher.STRING_SWITCHER, null, "<generated>");
/*     */       
/*     */ 
/* 127 */       EmitUtils.null_constructor(ce);
/* 128 */       final CodeEmitter e = ce.begin_method(1, StringSwitcher.INT_VALUE, null);
/* 129 */       e.load_arg(0);
/* 130 */       final List stringList = Arrays.asList(this.strings);
/* 131 */       int style = this.fixedInput ? 2 : 1;
/* 132 */       EmitUtils.string_switch(e, this.strings, style, new ObjectSwitchCallback() {
/*     */         public void processCase(Object key, Label end) {
/* 134 */           e.push(StringSwitcher.Generator.this.ints[stringList.indexOf(key)]);
/* 135 */           e.return_value();
/*     */         }
/*     */         
/* 138 */         public void processDefault() { e.push(-1);
/* 139 */           e.return_value();
/*     */         }
/* 141 */       });
/* 142 */       e.end_method();
/* 143 */       ce.end_class();
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 147 */       return (StringSwitcher)ReflectUtils.newInstance(type);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 151 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface StringSwitcherKey
/*     */   {
/*     */     public abstract Object newInstance(String[] paramArrayOfString, int[] paramArrayOfInt, boolean paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cgli\\util\StringSwitcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */