/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeFlow
/*     */   implements Opcodes
/*     */ {
/*     */   private final String className;
/*     */   private final ClassWriter classWriter;
/*     */   private final Stack<ArrayList<String>> compilationScopes;
/*     */   private List<FieldAdder> fieldAdders;
/*     */   private List<ClinitAdder> clinitAdders;
/*  80 */   private int nextFieldId = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private int nextFreeVariableId = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CodeFlow(String className, ClassWriter classWriter)
/*     */   {
/*  95 */     this.className = className;
/*  96 */     this.classWriter = classWriter;
/*  97 */     this.compilationScopes = new Stack();
/*  98 */     this.compilationScopes.add(new ArrayList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadTarget(MethodVisitor mv)
/*     */   {
/* 108 */     mv.visitVarInsn(25, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadEvaluationContext(MethodVisitor mv)
/*     */   {
/* 118 */     mv.visitVarInsn(25, 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pushDescriptor(String descriptor)
/*     */   {
/* 126 */     Assert.notNull(descriptor, "Descriptor must not be null");
/* 127 */     ((ArrayList)this.compilationScopes.peek()).add(descriptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enterCompilationScope()
/*     */   {
/* 136 */     this.compilationScopes.push(new ArrayList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void exitCompilationScope()
/*     */   {
/* 145 */     this.compilationScopes.pop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String lastDescriptor()
/*     */   {
/* 152 */     ArrayList<String> scopes = (ArrayList)this.compilationScopes.peek();
/* 153 */     return !scopes.isEmpty() ? (String)scopes.get(scopes.size() - 1) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unboxBooleanIfNecessary(MethodVisitor mv)
/*     */   {
/* 162 */     if ("Ljava/lang/Boolean".equals(lastDescriptor())) {
/* 163 */       mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void finish()
/*     */   {
/*     */     Iterator localIterator;
/*     */     
/* 173 */     if (this.fieldAdders != null)
/* 174 */       for (localIterator = this.fieldAdders.iterator(); localIterator.hasNext();) { fieldAdder = (FieldAdder)localIterator.next();
/* 175 */         fieldAdder.generateField(this.classWriter, this);
/*     */       }
/*     */     FieldAdder fieldAdder;
/* 178 */     if (this.clinitAdders != null) {
/* 179 */       MethodVisitor mv = this.classWriter.visitMethod(9, "<clinit>", "()V", null, null);
/* 180 */       mv.visitCode();
/* 181 */       this.nextFreeVariableId = 0;
/* 182 */       for (ClinitAdder clinitAdder : this.clinitAdders) {
/* 183 */         clinitAdder.generateCode(mv, this);
/*     */       }
/* 185 */       mv.visitInsn(177);
/* 186 */       mv.visitMaxs(0, 0);
/* 187 */       mv.visitEnd();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerNewField(FieldAdder fieldAdder)
/*     */   {
/* 197 */     if (this.fieldAdders == null) {
/* 198 */       this.fieldAdders = new ArrayList();
/*     */     }
/* 200 */     this.fieldAdders.add(fieldAdder);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerNewClinit(ClinitAdder clinitAdder)
/*     */   {
/* 209 */     if (this.clinitAdders == null) {
/* 210 */       this.clinitAdders = new ArrayList();
/*     */     }
/* 212 */     this.clinitAdders.add(clinitAdder);
/*     */   }
/*     */   
/*     */   public int nextFieldId() {
/* 216 */     return this.nextFieldId++;
/*     */   }
/*     */   
/*     */   public int nextFreeVariableId() {
/* 220 */     return this.nextFreeVariableId++;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 224 */     return this.className;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String getClassname() {
/* 229 */     return this.className;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertUnboxInsns(MethodVisitor mv, char ch, String stackDescriptor)
/*     */   {
/* 241 */     switch (ch) {
/*     */     case 'Z': 
/* 243 */       if (!stackDescriptor.equals("Ljava/lang/Boolean")) {
/* 244 */         mv.visitTypeInsn(192, "java/lang/Boolean");
/*     */       }
/* 246 */       mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/* 247 */       break;
/*     */     case 'B': 
/* 249 */       if (!stackDescriptor.equals("Ljava/lang/Byte")) {
/* 250 */         mv.visitTypeInsn(192, "java/lang/Byte");
/*     */       }
/* 252 */       mv.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
/* 253 */       break;
/*     */     case 'C': 
/* 255 */       if (!stackDescriptor.equals("Ljava/lang/Character")) {
/* 256 */         mv.visitTypeInsn(192, "java/lang/Character");
/*     */       }
/* 258 */       mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
/* 259 */       break;
/*     */     case 'D': 
/* 261 */       if (!stackDescriptor.equals("Ljava/lang/Double")) {
/* 262 */         mv.visitTypeInsn(192, "java/lang/Double");
/*     */       }
/* 264 */       mv.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
/* 265 */       break;
/*     */     case 'F': 
/* 267 */       if (!stackDescriptor.equals("Ljava/lang/Float")) {
/* 268 */         mv.visitTypeInsn(192, "java/lang/Float");
/*     */       }
/* 270 */       mv.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
/* 271 */       break;
/*     */     case 'I': 
/* 273 */       if (!stackDescriptor.equals("Ljava/lang/Integer")) {
/* 274 */         mv.visitTypeInsn(192, "java/lang/Integer");
/*     */       }
/* 276 */       mv.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
/* 277 */       break;
/*     */     case 'J': 
/* 279 */       if (!stackDescriptor.equals("Ljava/lang/Long")) {
/* 280 */         mv.visitTypeInsn(192, "java/lang/Long");
/*     */       }
/* 282 */       mv.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
/* 283 */       break;
/*     */     case 'S': 
/* 285 */       if (!stackDescriptor.equals("Ljava/lang/Short")) {
/* 286 */         mv.visitTypeInsn(192, "java/lang/Short");
/*     */       }
/* 288 */       mv.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
/* 289 */       break;
/*     */     case 'E': case 'G': case 'H': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
/* 291 */       throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + ch + "'");
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertUnboxNumberInsns(MethodVisitor mv, char targetDescriptor, String stackDescriptor)
/*     */   {
/* 302 */     switch (targetDescriptor) {
/*     */     case 'D': 
/* 304 */       if (stackDescriptor.equals("Ljava/lang/Object")) {
/* 305 */         mv.visitTypeInsn(192, "java/lang/Number");
/*     */       }
/* 307 */       mv.visitMethodInsn(182, "java/lang/Number", "doubleValue", "()D", false);
/* 308 */       break;
/*     */     case 'F': 
/* 310 */       if (stackDescriptor.equals("Ljava/lang/Object")) {
/* 311 */         mv.visitTypeInsn(192, "java/lang/Number");
/*     */       }
/* 313 */       mv.visitMethodInsn(182, "java/lang/Number", "floatValue", "()F", false);
/* 314 */       break;
/*     */     case 'J': 
/* 316 */       if (stackDescriptor.equals("Ljava/lang/Object")) {
/* 317 */         mv.visitTypeInsn(192, "java/lang/Number");
/*     */       }
/* 319 */       mv.visitMethodInsn(182, "java/lang/Number", "longValue", "()J", false);
/* 320 */       break;
/*     */     case 'I': 
/* 322 */       if (stackDescriptor.equals("Ljava/lang/Object")) {
/* 323 */         mv.visitTypeInsn(192, "java/lang/Number");
/*     */       }
/* 325 */       mv.visitMethodInsn(182, "java/lang/Number", "intValue", "()I", false);
/* 326 */       break;
/*     */     case 'E': case 'G': 
/*     */     case 'H': default: 
/* 329 */       throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + targetDescriptor + "'");
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertAnyNecessaryTypeConversionBytecodes(MethodVisitor mv, char targetDescriptor, String stackDescriptor)
/*     */   {
/* 340 */     if (isPrimitive(stackDescriptor)) {
/* 341 */       char stackTop = stackDescriptor.charAt(0);
/* 342 */       if ((stackTop == 'I') || (stackTop == 'B') || (stackTop == 'S') || (stackTop == 'C')) {
/* 343 */         if (targetDescriptor == 'D') {
/* 344 */           mv.visitInsn(135);
/*     */         }
/* 346 */         else if (targetDescriptor == 'F') {
/* 347 */           mv.visitInsn(134);
/*     */         }
/* 349 */         else if (targetDescriptor == 'J') {
/* 350 */           mv.visitInsn(133);
/*     */         }
/* 352 */         else if (targetDescriptor != 'I')
/*     */         {
/*     */ 
/*     */ 
/* 356 */           throw new IllegalStateException("cannot get from " + stackTop + " to " + targetDescriptor);
/*     */         }
/*     */       }
/* 359 */       else if (stackTop == 'J') {
/* 360 */         if (targetDescriptor == 'D') {
/* 361 */           mv.visitInsn(138);
/*     */         }
/* 363 */         else if (targetDescriptor == 'F') {
/* 364 */           mv.visitInsn(137);
/*     */         }
/* 366 */         else if (targetDescriptor != 'J')
/*     */         {
/*     */ 
/* 369 */           if (targetDescriptor == 'I') {
/* 370 */             mv.visitInsn(136);
/*     */           }
/*     */           else {
/* 373 */             throw new IllegalStateException("cannot get from " + stackTop + " to " + targetDescriptor);
/*     */           }
/*     */         }
/* 376 */       } else if (stackTop == 'F') {
/* 377 */         if (targetDescriptor == 'D') {
/* 378 */           mv.visitInsn(141);
/*     */         }
/* 380 */         else if (targetDescriptor != 'F')
/*     */         {
/*     */ 
/* 383 */           if (targetDescriptor == 'J') {
/* 384 */             mv.visitInsn(140);
/*     */           }
/* 386 */           else if (targetDescriptor == 'I') {
/* 387 */             mv.visitInsn(139);
/*     */           }
/*     */           else {
/* 390 */             throw new IllegalStateException("cannot get from " + stackTop + " to " + targetDescriptor);
/*     */           }
/*     */         }
/* 393 */       } else if ((stackTop == 'D') && 
/* 394 */         (targetDescriptor != 'D'))
/*     */       {
/*     */ 
/* 397 */         if (targetDescriptor == 'F') {
/* 398 */           mv.visitInsn(144);
/*     */         }
/* 400 */         else if (targetDescriptor == 'J') {
/* 401 */           mv.visitInsn(143);
/*     */         }
/* 403 */         else if (targetDescriptor == 'I') {
/* 404 */           mv.visitInsn(142);
/*     */         }
/*     */         else {
/* 407 */           throw new IllegalStateException("cannot get from " + stackDescriptor + " to " + targetDescriptor);
/*     */         }
/*     */       }
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
/*     */   public static String createSignatureDescriptor(Method method)
/*     */   {
/* 424 */     Class<?>[] params = method.getParameterTypes();
/* 425 */     StringBuilder sb = new StringBuilder();
/* 426 */     sb.append("(");
/* 427 */     for (Class<?> param : params) {
/* 428 */       sb.append(toJvmDescriptor(param));
/*     */     }
/* 430 */     sb.append(")");
/* 431 */     sb.append(toJvmDescriptor(method.getReturnType()));
/* 432 */     return sb.toString();
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
/*     */   public static String createSignatureDescriptor(Constructor<?> ctor)
/*     */   {
/* 445 */     Class<?>[] params = ctor.getParameterTypes();
/* 446 */     StringBuilder sb = new StringBuilder();
/* 447 */     sb.append("(");
/* 448 */     for (Class<?> param : params) {
/* 449 */       sb.append(toJvmDescriptor(param));
/*     */     }
/* 451 */     sb.append(")V");
/* 452 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toJvmDescriptor(Class<?> clazz)
/*     */   {
/* 464 */     StringBuilder sb = new StringBuilder();
/* 465 */     if (clazz.isArray()) {
/* 466 */       while (clazz.isArray()) {
/* 467 */         sb.append("[");
/* 468 */         clazz = clazz.getComponentType();
/*     */       }
/*     */     }
/* 471 */     if (clazz.isPrimitive()) {
/* 472 */       if (clazz == Void.TYPE) {
/* 473 */         sb.append('V');
/*     */       }
/* 475 */       else if (clazz == Integer.TYPE) {
/* 476 */         sb.append('I');
/*     */       }
/* 478 */       else if (clazz == Boolean.TYPE) {
/* 479 */         sb.append('Z');
/*     */       }
/* 481 */       else if (clazz == Character.TYPE) {
/* 482 */         sb.append('C');
/*     */       }
/* 484 */       else if (clazz == Long.TYPE) {
/* 485 */         sb.append('J');
/*     */       }
/* 487 */       else if (clazz == Double.TYPE) {
/* 488 */         sb.append('D');
/*     */       }
/* 490 */       else if (clazz == Float.TYPE) {
/* 491 */         sb.append('F');
/*     */       }
/* 493 */       else if (clazz == Byte.TYPE) {
/* 494 */         sb.append('B');
/*     */       }
/* 496 */       else if (clazz == Short.TYPE) {
/* 497 */         sb.append('S');
/*     */       }
/*     */     }
/*     */     else {
/* 501 */       sb.append("L");
/* 502 */       sb.append(clazz.getName().replace('.', '/'));
/* 503 */       sb.append(";");
/*     */     }
/* 505 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toDescriptorFromObject(Object value)
/*     */   {
/* 515 */     if (value == null) {
/* 516 */       return "Ljava/lang/Object";
/*     */     }
/*     */     
/* 519 */     return toDescriptor(value.getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isBooleanCompatible(String descriptor)
/*     */   {
/* 528 */     return (descriptor != null) && ((descriptor.equals("Z")) || (descriptor.equals("Ljava/lang/Boolean")));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPrimitive(String descriptor)
/*     */   {
/* 536 */     return (descriptor != null) && (descriptor.length() == 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPrimitiveArray(String descriptor)
/*     */   {
/* 544 */     boolean primitive = true;
/* 545 */     int i = 0; for (int max = descriptor.length(); i < max; i++) {
/* 546 */       char ch = descriptor.charAt(i);
/* 547 */       if (ch != '[')
/*     */       {
/*     */ 
/* 550 */         primitive = ch != 'L';
/* 551 */         break;
/*     */       } }
/* 553 */     return primitive;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean areBoxingCompatible(String desc1, String desc2)
/*     */   {
/* 562 */     if (desc1.equals(desc2)) {
/* 563 */       return true;
/*     */     }
/* 565 */     if (desc1.length() == 1) {
/* 566 */       if (desc1.equals("Z")) {
/* 567 */         return desc2.equals("Ljava/lang/Boolean");
/*     */       }
/* 569 */       if (desc1.equals("D")) {
/* 570 */         return desc2.equals("Ljava/lang/Double");
/*     */       }
/* 572 */       if (desc1.equals("F")) {
/* 573 */         return desc2.equals("Ljava/lang/Float");
/*     */       }
/* 575 */       if (desc1.equals("I")) {
/* 576 */         return desc2.equals("Ljava/lang/Integer");
/*     */       }
/* 578 */       if (desc1.equals("J")) {
/* 579 */         return desc2.equals("Ljava/lang/Long");
/*     */       }
/*     */     }
/* 582 */     else if (desc2.length() == 1) {
/* 583 */       if (desc2.equals("Z")) {
/* 584 */         return desc1.equals("Ljava/lang/Boolean");
/*     */       }
/* 586 */       if (desc2.equals("D")) {
/* 587 */         return desc1.equals("Ljava/lang/Double");
/*     */       }
/* 589 */       if (desc2.equals("F")) {
/* 590 */         return desc1.equals("Ljava/lang/Float");
/*     */       }
/* 592 */       if (desc2.equals("I")) {
/* 593 */         return desc1.equals("Ljava/lang/Integer");
/*     */       }
/* 595 */       if (desc2.equals("J")) {
/* 596 */         return desc1.equals("Ljava/lang/Long");
/*     */       }
/*     */     }
/* 599 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPrimitiveOrUnboxableSupportedNumberOrBoolean(String descriptor)
/*     */   {
/* 610 */     if (descriptor == null) {
/* 611 */       return false;
/*     */     }
/* 613 */     if (isPrimitiveOrUnboxableSupportedNumber(descriptor)) {
/* 614 */       return true;
/*     */     }
/* 616 */     return ("Z".equals(descriptor)) || (descriptor.equals("Ljava/lang/Boolean"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPrimitiveOrUnboxableSupportedNumber(String descriptor)
/*     */   {
/* 627 */     if (descriptor == null) {
/* 628 */       return false;
/*     */     }
/* 630 */     if (descriptor.length() == 1) {
/* 631 */       return "DFIJ".contains(descriptor);
/*     */     }
/* 633 */     if (descriptor.startsWith("Ljava/lang/")) {
/* 634 */       String name = descriptor.substring("Ljava/lang/".length());
/* 635 */       if ((name.equals("Double")) || (name.equals("Float")) || (name.equals("Integer")) || (name.equals("Long"))) {
/* 636 */         return true;
/*     */       }
/*     */     }
/* 639 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isIntegerForNumericOp(Number number)
/*     */   {
/* 649 */     return ((number instanceof Integer)) || ((number instanceof Short)) || ((number instanceof Byte));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char toPrimitiveTargetDesc(String descriptor)
/*     */   {
/* 657 */     if (descriptor.length() == 1) {
/* 658 */       return descriptor.charAt(0);
/*     */     }
/* 660 */     if (descriptor.equals("Ljava/lang/Boolean")) {
/* 661 */       return 'Z';
/*     */     }
/* 663 */     if (descriptor.equals("Ljava/lang/Byte")) {
/* 664 */       return 'B';
/*     */     }
/* 666 */     if (descriptor.equals("Ljava/lang/Character")) {
/* 667 */       return 'C';
/*     */     }
/* 669 */     if (descriptor.equals("Ljava/lang/Double")) {
/* 670 */       return 'D';
/*     */     }
/* 672 */     if (descriptor.equals("Ljava/lang/Float")) {
/* 673 */       return 'F';
/*     */     }
/* 675 */     if (descriptor.equals("Ljava/lang/Integer")) {
/* 676 */       return 'I';
/*     */     }
/* 678 */     if (descriptor.equals("Ljava/lang/Long")) {
/* 679 */       return 'J';
/*     */     }
/* 681 */     if (descriptor.equals("Ljava/lang/Short")) {
/* 682 */       return 'S';
/*     */     }
/*     */     
/* 685 */     throw new IllegalStateException("No primitive for '" + descriptor + "'");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertCheckCast(MethodVisitor mv, String descriptor)
/*     */   {
/* 695 */     if (descriptor.length() != 1) {
/* 696 */       if (descriptor.charAt(0) == '[') {
/* 697 */         if (isPrimitiveArray(descriptor)) {
/* 698 */           mv.visitTypeInsn(192, descriptor);
/*     */         }
/*     */         else {
/* 701 */           mv.visitTypeInsn(192, descriptor + ";");
/*     */         }
/*     */         
/*     */       }
/* 705 */       else if (!descriptor.equals("Ljava/lang/Object"))
/*     */       {
/* 707 */         mv.visitTypeInsn(192, descriptor.substring(1));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertBoxIfNecessary(MethodVisitor mv, String descriptor)
/*     */   {
/* 720 */     if (descriptor.length() == 1) {
/* 721 */       insertBoxIfNecessary(mv, descriptor.charAt(0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertBoxIfNecessary(MethodVisitor mv, char ch)
/*     */   {
/* 732 */     switch (ch) {
/*     */     case 'Z': 
/* 734 */       mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
/* 735 */       break;
/*     */     case 'B': 
/* 737 */       mv.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
/* 738 */       break;
/*     */     case 'C': 
/* 740 */       mv.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
/* 741 */       break;
/*     */     case 'D': 
/* 743 */       mv.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
/* 744 */       break;
/*     */     case 'F': 
/* 746 */       mv.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
/* 747 */       break;
/*     */     case 'I': 
/* 749 */       mv.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
/* 750 */       break;
/*     */     case 'J': 
/* 752 */       mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
/* 753 */       break;
/*     */     case 'S': 
/* 755 */       mv.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
/* 756 */       break;
/*     */     case 'L': case 'V': case '[': 
/*     */       break;
/*     */     case 'E': case 'G': case 'H': case 'K': 
/*     */     case 'M': case 'N': case 'O': case 'P': 
/*     */     case 'Q': case 'R': case 'T': case 'U': 
/*     */     case 'W': case 'X': case 'Y': default: 
/* 763 */       throw new IllegalArgumentException("Boxing should not be attempted for descriptor '" + ch + "'");
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toDescriptor(Class<?> type)
/*     */   {
/* 775 */     String name = type.getName();
/* 776 */     if (type.isPrimitive()) {
/* 777 */       switch (name.length()) {
/*     */       case 3: 
/* 779 */         return "I";
/*     */       case 4: 
/* 781 */         if (name.equals("byte")) {
/* 782 */           return "B";
/*     */         }
/* 784 */         if (name.equals("char")) {
/* 785 */           return "C";
/*     */         }
/* 787 */         if (name.equals("long")) {
/* 788 */           return "J";
/*     */         }
/* 790 */         if (name.equals("void")) {
/* 791 */           return "V";
/*     */         }
/*     */         break;
/*     */       case 5: 
/* 795 */         if (name.equals("float")) {
/* 796 */           return "F";
/*     */         }
/* 798 */         if (name.equals("short")) {
/* 799 */           return "S";
/*     */         }
/*     */         break;
/*     */       case 6: 
/* 803 */         if (name.equals("double")) {
/* 804 */           return "D";
/*     */         }
/*     */         break;
/*     */       case 7: 
/* 808 */         if (name.equals("boolean")) {
/* 809 */           return "Z";
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */     else {
/* 815 */       if (name.charAt(0) != '[') {
/* 816 */         return "L" + type.getName().replace('.', '/');
/*     */       }
/*     */       
/* 819 */       if (name.endsWith(";")) {
/* 820 */         return name.substring(0, name.length() - 1).replace('.', '/');
/*     */       }
/*     */       
/* 823 */       return name;
/*     */     }
/*     */     
/*     */ 
/* 827 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] toParamDescriptors(Method method)
/*     */   {
/* 837 */     return toDescriptors(method.getParameterTypes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] toParamDescriptors(Constructor<?> ctor)
/*     */   {
/* 847 */     return toDescriptors(ctor.getParameterTypes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] toDescriptors(Class<?>[] types)
/*     */   {
/* 856 */     int typesCount = types.length;
/* 857 */     String[] descriptors = new String[typesCount];
/* 858 */     for (int p = 0; p < typesCount; p++) {
/* 859 */       descriptors[p] = toDescriptor(types[p]);
/*     */     }
/* 861 */     return descriptors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertOptimalLoad(MethodVisitor mv, int value)
/*     */   {
/* 870 */     if (value < 6) {
/* 871 */       mv.visitInsn(3 + value);
/*     */     }
/* 873 */     else if (value < 127) {
/* 874 */       mv.visitIntInsn(16, value);
/*     */     }
/* 876 */     else if (value < 32767) {
/* 877 */       mv.visitIntInsn(17, value);
/*     */     }
/*     */     else {
/* 880 */       mv.visitLdcInsn(Integer.valueOf(value));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertArrayStore(MethodVisitor mv, String arrayElementType)
/*     */   {
/* 892 */     if (arrayElementType.length() == 1) {
/* 893 */       switch (arrayElementType.charAt(0)) {
/* 894 */       case 'I':  mv.visitInsn(79); break;
/* 895 */       case 'J':  mv.visitInsn(80); break;
/* 896 */       case 'F':  mv.visitInsn(81); break;
/* 897 */       case 'D':  mv.visitInsn(82); break;
/* 898 */       case 'B':  mv.visitInsn(84); break;
/* 899 */       case 'C':  mv.visitInsn(85); break;
/* 900 */       case 'S':  mv.visitInsn(86); break;
/* 901 */       case 'Z':  mv.visitInsn(84); break;
/*     */       case 'E': case 'G': case 'H': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
/* 903 */         throw new IllegalArgumentException("Unexpected arraytype " + arrayElementType.charAt(0));
/*     */       }
/*     */       
/*     */     } else {
/* 907 */       mv.visitInsn(83);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int arrayCodeFor(String arraytype)
/*     */   {
/* 917 */     switch (arraytype.charAt(0)) {
/* 918 */     case 'I':  return 10;
/* 919 */     case 'J':  return 11;
/* 920 */     case 'F':  return 6;
/* 921 */     case 'D':  return 7;
/* 922 */     case 'B':  return 8;
/* 923 */     case 'C':  return 5;
/* 924 */     case 'S':  return 9;
/* 925 */     case 'Z':  return 4;
/*     */     }
/* 927 */     throw new IllegalArgumentException("Unexpected arraytype " + arraytype.charAt(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isReferenceTypeArray(String arraytype)
/*     */   {
/* 935 */     int length = arraytype.length();
/* 936 */     for (int i = 0; i < length; i++) {
/* 937 */       char ch = arraytype.charAt(i);
/* 938 */       if (ch != '[')
/* 939 */         return ch == 'L';
/*     */     }
/* 941 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void insertNewArrayCode(MethodVisitor mv, int size, String arraytype)
/*     */   {
/* 953 */     insertOptimalLoad(mv, size);
/* 954 */     if (arraytype.length() == 1) {
/* 955 */       mv.visitIntInsn(188, arrayCodeFor(arraytype));
/*     */ 
/*     */     }
/* 958 */     else if (arraytype.charAt(0) == '[')
/*     */     {
/*     */ 
/* 961 */       if (isReferenceTypeArray(arraytype)) {
/* 962 */         mv.visitTypeInsn(189, arraytype + ";");
/*     */       }
/*     */       else {
/* 965 */         mv.visitTypeInsn(189, arraytype);
/*     */       }
/*     */     }
/*     */     else {
/* 969 */       mv.visitTypeInsn(189, arraytype.substring(1));
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
/*     */ 
/*     */ 
/*     */   public static void insertNumericUnboxOrPrimitiveTypeCoercion(MethodVisitor mv, String stackDescriptor, char targetDescriptor)
/*     */   {
/* 986 */     if (!isPrimitive(stackDescriptor)) {
/* 987 */       insertUnboxNumberInsns(mv, targetDescriptor, stackDescriptor);
/*     */     }
/*     */     else {
/* 990 */       insertAnyNecessaryTypeConversionBytecodes(mv, targetDescriptor, stackDescriptor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ClinitAdder
/*     */   {
/*     */     public abstract void generateCode(MethodVisitor paramMethodVisitor, CodeFlow paramCodeFlow);
/*     */   }
/*     */   
/*     */   public static abstract interface FieldAdder
/*     */   {
/*     */     public abstract void generateField(ClassWriter paramClassWriter, CodeFlow paramCodeFlow);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\CodeFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */