/*      */ package org.springframework.asm;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Frame
/*      */ {
/*      */   static final int DIM = -268435456;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int ARRAY_OF = 268435456;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int ELEMENT_OF = -268435456;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int KIND = 251658240;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int VALUE = 8388607;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int BASE_KIND = 267386880;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int BASE_VALUE = 1048575;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int BASE = 16777216;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int OBJECT = 24117248;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNINITIALIZED = 25165824;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int LOCAL = 33554432;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STACK = 50331648;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int TOP = 16777216;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int BOOLEAN = 16777225;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int BYTE = 16777226;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int CHAR = 16777227;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int SHORT = 16777228;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int INTEGER = 16777217;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int FLOAT = 16777218;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int DOUBLE = 16777219;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int LONG = 16777220;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int NULL = 16777221;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNINITIALIZED_THIS = 16777222;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int[] SIZE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Label owner;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int[] inputLocals;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int[] inputStack;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] outputLocals;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] outputStack;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int outputStackTop;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int initializationCount;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] initializations;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  239 */     int[] b = new int['Ê'];
/*  240 */     String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
/*      */     
/*      */ 
/*      */ 
/*  244 */     for (int i = 0; i < b.length; i++) {
/*  245 */       b[i] = (s.charAt(i) - 'E');
/*      */     }
/*  247 */     SIZE = b;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void set(ClassWriter cw, int nLocal, Object[] local, int nStack, Object[] stack)
/*      */   {
/*  548 */     int i = convert(cw, nLocal, local, this.inputLocals);
/*  549 */     while (i < local.length) {
/*  550 */       this.inputLocals[(i++)] = 16777216;
/*      */     }
/*  552 */     int nStackTop = 0;
/*  553 */     for (int j = 0; j < nStack; j++) {
/*  554 */       if ((stack[j] == Opcodes.LONG) || (stack[j] == Opcodes.DOUBLE)) {
/*  555 */         nStackTop++;
/*      */       }
/*      */     }
/*  558 */     this.inputStack = new int[nStack + nStackTop];
/*  559 */     convert(cw, nStack, stack, this.inputStack);
/*  560 */     this.outputStackTop = 0;
/*  561 */     this.initializationCount = 0;
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
/*      */   private static int convert(ClassWriter cw, int nInput, Object[] input, int[] output)
/*      */   {
/*  589 */     int i = 0;
/*  590 */     for (int j = 0; j < nInput; j++) {
/*  591 */       if ((input[j] instanceof Integer)) {
/*  592 */         output[(i++)] = (0x1000000 | ((Integer)input[j]).intValue());
/*  593 */         if ((input[j] == Opcodes.LONG) || (input[j] == Opcodes.DOUBLE)) {
/*  594 */           output[(i++)] = 16777216;
/*      */         }
/*  596 */       } else if ((input[j] instanceof String)) {
/*  597 */         output[(i++)] = type(cw, Type.getObjectType((String)input[j])
/*  598 */           .getDescriptor());
/*      */       }
/*      */       else {
/*  601 */         output[(i++)] = (0x1800000 | cw.addUninitializedType("", ((Label)input[j]).position));
/*      */       }
/*      */     }
/*      */     
/*  605 */     return i;
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
/*      */   final void set(Frame f)
/*      */   {
/*  618 */     this.inputLocals = f.inputLocals;
/*  619 */     this.inputStack = f.inputStack;
/*  620 */     this.outputLocals = f.outputLocals;
/*  621 */     this.outputStack = f.outputStack;
/*  622 */     this.outputStackTop = f.outputStackTop;
/*  623 */     this.initializationCount = f.initializationCount;
/*  624 */     this.initializations = f.initializations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int get(int local)
/*      */   {
/*  635 */     if ((this.outputLocals == null) || (local >= this.outputLocals.length))
/*      */     {
/*      */ 
/*  638 */       return 0x2000000 | local;
/*      */     }
/*  640 */     int type = this.outputLocals[local];
/*  641 */     if (type == 0)
/*      */     {
/*      */ 
/*  644 */       type = this.outputLocals[local] = 0x2000000 | local;
/*      */     }
/*  646 */     return type;
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
/*      */   private void set(int local, int type)
/*      */   {
/*  660 */     if (this.outputLocals == null) {
/*  661 */       this.outputLocals = new int[10];
/*      */     }
/*  663 */     int n = this.outputLocals.length;
/*  664 */     if (local >= n) {
/*  665 */       int[] t = new int[Math.max(local + 1, 2 * n)];
/*  666 */       System.arraycopy(this.outputLocals, 0, t, 0, n);
/*  667 */       this.outputLocals = t;
/*      */     }
/*      */     
/*  670 */     this.outputLocals[local] = type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void push(int type)
/*      */   {
/*  681 */     if (this.outputStack == null) {
/*  682 */       this.outputStack = new int[10];
/*      */     }
/*  684 */     int n = this.outputStack.length;
/*  685 */     if (this.outputStackTop >= n) {
/*  686 */       int[] t = new int[Math.max(this.outputStackTop + 1, 2 * n)];
/*  687 */       System.arraycopy(this.outputStack, 0, t, 0, n);
/*  688 */       this.outputStack = t;
/*      */     }
/*      */     
/*  691 */     this.outputStack[(this.outputStackTop++)] = type;
/*      */     
/*  693 */     int top = this.owner.inputStackTop + this.outputStackTop;
/*  694 */     if (top > this.owner.outputStackMax) {
/*  695 */       this.owner.outputStackMax = top;
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
/*      */   private void push(ClassWriter cw, String desc)
/*      */   {
/*  710 */     int type = type(cw, desc);
/*  711 */     if (type != 0) {
/*  712 */       push(type);
/*  713 */       if ((type == 16777220) || (type == 16777219)) {
/*  714 */         push(16777216);
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
/*      */   private static int type(ClassWriter cw, String desc)
/*      */   {
/*  730 */     int index = desc.charAt(0) == '(' ? desc.indexOf(')') + 1 : 0;
/*  731 */     switch (desc.charAt(index)) {
/*      */     case 'V': 
/*  733 */       return 0;
/*      */     case 'B': 
/*      */     case 'C': 
/*      */     case 'I': 
/*      */     case 'S': 
/*      */     case 'Z': 
/*  739 */       return 16777217;
/*      */     case 'F': 
/*  741 */       return 16777218;
/*      */     case 'J': 
/*  743 */       return 16777220;
/*      */     case 'D': 
/*  745 */       return 16777219;
/*      */     
/*      */     case 'L': 
/*  748 */       String t = desc.substring(index + 1, desc.length() - 1);
/*  749 */       return 0x1700000 | cw.addType(t);
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  754 */     int dims = index + 1;
/*  755 */     while (desc.charAt(dims) == '[')
/*  756 */       dims++;
/*      */     int data;
/*  758 */     int data; int data; int data; int data; int data; int data; int data; int data; switch (desc.charAt(dims)) {
/*      */     case 'Z': 
/*  760 */       data = 16777225;
/*  761 */       break;
/*      */     case 'C': 
/*  763 */       data = 16777227;
/*  764 */       break;
/*      */     case 'B': 
/*  766 */       data = 16777226;
/*  767 */       break;
/*      */     case 'S': 
/*  769 */       data = 16777228;
/*  770 */       break;
/*      */     case 'I': 
/*  772 */       data = 16777217;
/*  773 */       break;
/*      */     case 'F': 
/*  775 */       data = 16777218;
/*  776 */       break;
/*      */     case 'J': 
/*  778 */       data = 16777220;
/*  779 */       break;
/*      */     case 'D': 
/*  781 */       data = 16777219;
/*  782 */       break;
/*      */     case 'E': case 'G': case 'H': case 'K': case 'L': case 'M': 
/*      */     case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': 
/*      */     case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
/*  786 */       String t = desc.substring(dims + 1, desc.length() - 1);
/*  787 */       data = 0x1700000 | cw.addType(t);
/*      */     }
/*  789 */     return dims - index << 28 | data;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int pop()
/*      */   {
/*  799 */     if (this.outputStackTop > 0) {
/*  800 */       return this.outputStack[(--this.outputStackTop)];
/*      */     }
/*      */     
/*  803 */     return 0x3000000 | ---this.owner.inputStackTop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void pop(int elements)
/*      */   {
/*  814 */     if (this.outputStackTop >= elements) {
/*  815 */       this.outputStackTop -= elements;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  820 */       this.owner.inputStackTop -= elements - this.outputStackTop;
/*  821 */       this.outputStackTop = 0;
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
/*      */   private void pop(String desc)
/*      */   {
/*  834 */     char c = desc.charAt(0);
/*  835 */     if (c == '(') {
/*  836 */       pop((Type.getArgumentsAndReturnSizes(desc) >> 2) - 1);
/*  837 */     } else if ((c == 'J') || (c == 'D')) {
/*  838 */       pop(2);
/*      */     } else {
/*  840 */       pop(1);
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
/*      */   private void init(int var)
/*      */   {
/*  853 */     if (this.initializations == null) {
/*  854 */       this.initializations = new int[2];
/*      */     }
/*  856 */     int n = this.initializations.length;
/*  857 */     if (this.initializationCount >= n) {
/*  858 */       int[] t = new int[Math.max(this.initializationCount + 1, 2 * n)];
/*  859 */       System.arraycopy(this.initializations, 0, t, 0, n);
/*  860 */       this.initializations = t;
/*      */     }
/*      */     
/*  863 */     this.initializations[(this.initializationCount++)] = var;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int init(ClassWriter cw, int t)
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  879 */     if (t == 16777222) {
/*  880 */       s = 0x1700000 | cw.addType(cw.thisName); } else { int s;
/*  881 */       if ((t & 0xFFF00000) == 25165824) {
/*  882 */         String type = cw.typeTable[(t & 0xFFFFF)].strVal1;
/*  883 */         s = 0x1700000 | cw.addType(type);
/*      */       } else {
/*  885 */         return t; } }
/*      */     int s;
/*  887 */     for (int j = 0; j < this.initializationCount; j++) {
/*  888 */       int u = this.initializations[j];
/*  889 */       int dim = u & 0xF0000000;
/*  890 */       int kind = u & 0xF000000;
/*  891 */       if (kind == 33554432) {
/*  892 */         u = dim + this.inputLocals[(u & 0x7FFFFF)];
/*  893 */       } else if (kind == 50331648) {
/*  894 */         u = dim + this.inputStack[(this.inputStack.length - (u & 0x7FFFFF))];
/*      */       }
/*  896 */       if (t == u) {
/*  897 */         return s;
/*      */       }
/*      */     }
/*  900 */     return t;
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
/*      */   final void initInputFrame(ClassWriter cw, int access, Type[] args, int maxLocals)
/*      */   {
/*  918 */     this.inputLocals = new int[maxLocals];
/*  919 */     this.inputStack = new int[0];
/*  920 */     int i = 0;
/*  921 */     if ((access & 0x8) == 0) {
/*  922 */       if ((access & 0x80000) == 0) {
/*  923 */         this.inputLocals[(i++)] = (0x1700000 | cw.addType(cw.thisName));
/*      */       } else {
/*  925 */         this.inputLocals[(i++)] = 16777222;
/*      */       }
/*      */     }
/*  928 */     for (int j = 0; j < args.length; j++) {
/*  929 */       int t = type(cw, args[j].getDescriptor());
/*  930 */       this.inputLocals[(i++)] = t;
/*  931 */       if ((t == 16777220) || (t == 16777219)) {
/*  932 */         this.inputLocals[(i++)] = 16777216;
/*      */       }
/*      */     }
/*  935 */     while (i < maxLocals) {
/*  936 */       this.inputLocals[(i++)] = 16777216;
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
/*      */   void execute(int opcode, int arg, ClassWriter cw, Item item)
/*      */   {
/*  955 */     switch (opcode) {
/*      */     case 0: 
/*      */     case 116: 
/*      */     case 117: 
/*      */     case 118: 
/*      */     case 119: 
/*      */     case 145: 
/*      */     case 146: 
/*      */     case 147: 
/*      */     case 167: 
/*      */     case 177: 
/*      */       break;
/*      */     case 1: 
/*  968 */       push(16777221);
/*  969 */       break;
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*      */     case 16: 
/*      */     case 17: 
/*      */     case 21: 
/*  980 */       push(16777217);
/*  981 */       break;
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 22: 
/*  985 */       push(16777220);
/*  986 */       push(16777216);
/*  987 */       break;
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 23: 
/*  992 */       push(16777218);
/*  993 */       break;
/*      */     case 14: 
/*      */     case 15: 
/*      */     case 24: 
/*  997 */       push(16777219);
/*  998 */       push(16777216);
/*  999 */       break;
/*      */     case 18: 
/* 1001 */       switch (item.type) {
/*      */       case 3: 
/* 1003 */         push(16777217);
/* 1004 */         break;
/*      */       case 5: 
/* 1006 */         push(16777220);
/* 1007 */         push(16777216);
/* 1008 */         break;
/*      */       case 4: 
/* 1010 */         push(16777218);
/* 1011 */         break;
/*      */       case 6: 
/* 1013 */         push(16777219);
/* 1014 */         push(16777216);
/* 1015 */         break;
/*      */       case 7: 
/* 1017 */         push(0x1700000 | cw.addType("java/lang/Class"));
/* 1018 */         break;
/*      */       case 8: 
/* 1020 */         push(0x1700000 | cw.addType("java/lang/String"));
/* 1021 */         break;
/*      */       case 16: 
/* 1023 */         push(0x1700000 | cw.addType("java/lang/invoke/MethodType"));
/* 1024 */         break;
/*      */       case 9: case 10: case 11: case 12: 
/*      */       case 13: case 14: case 15: default: 
/* 1027 */         push(0x1700000 | cw.addType("java/lang/invoke/MethodHandle"));
/*      */       }
/* 1029 */       break;
/*      */     case 25: 
/* 1031 */       push(get(arg));
/* 1032 */       break;
/*      */     case 46: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/* 1037 */       pop(2);
/* 1038 */       push(16777217);
/* 1039 */       break;
/*      */     case 47: 
/*      */     case 143: 
/* 1042 */       pop(2);
/* 1043 */       push(16777220);
/* 1044 */       push(16777216);
/* 1045 */       break;
/*      */     case 48: 
/* 1047 */       pop(2);
/* 1048 */       push(16777218);
/* 1049 */       break;
/*      */     case 49: 
/*      */     case 138: 
/* 1052 */       pop(2);
/* 1053 */       push(16777219);
/* 1054 */       push(16777216);
/* 1055 */       break;
/*      */     case 50: 
/* 1057 */       pop(1);
/* 1058 */       int t1 = pop();
/* 1059 */       push(-268435456 + t1);
/* 1060 */       break;
/*      */     case 54: 
/*      */     case 56: 
/*      */     case 58: 
/* 1064 */       int t1 = pop();
/* 1065 */       set(arg, t1);
/* 1066 */       if (arg > 0) {
/* 1067 */         int t2 = get(arg - 1);
/*      */         
/* 1069 */         if ((t2 == 16777220) || (t2 == 16777219)) {
/* 1070 */           set(arg - 1, 16777216);
/* 1071 */         } else if ((t2 & 0xF000000) != 16777216) {
/* 1072 */           set(arg - 1, t2 | 0x800000);
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 55: 
/*      */     case 57: 
/* 1078 */       pop(1);
/* 1079 */       int t1 = pop();
/* 1080 */       set(arg, t1);
/* 1081 */       set(arg + 1, 16777216);
/* 1082 */       if (arg > 0) {
/* 1083 */         int t2 = get(arg - 1);
/*      */         
/* 1085 */         if ((t2 == 16777220) || (t2 == 16777219)) {
/* 1086 */           set(arg - 1, 16777216);
/* 1087 */         } else if ((t2 & 0xF000000) != 16777216) {
/* 1088 */           set(arg - 1, t2 | 0x800000);
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 79: 
/*      */     case 81: 
/*      */     case 83: 
/*      */     case 84: 
/*      */     case 85: 
/*      */     case 86: 
/* 1098 */       pop(3);
/* 1099 */       break;
/*      */     case 80: 
/*      */     case 82: 
/* 1102 */       pop(4);
/* 1103 */       break;
/*      */     case 87: 
/*      */     case 153: 
/*      */     case 154: 
/*      */     case 155: 
/*      */     case 156: 
/*      */     case 157: 
/*      */     case 158: 
/*      */     case 170: 
/*      */     case 171: 
/*      */     case 172: 
/*      */     case 174: 
/*      */     case 176: 
/*      */     case 191: 
/*      */     case 194: 
/*      */     case 195: 
/*      */     case 198: 
/*      */     case 199: 
/* 1121 */       pop(1);
/* 1122 */       break;
/*      */     case 88: 
/*      */     case 159: 
/*      */     case 160: 
/*      */     case 161: 
/*      */     case 162: 
/*      */     case 163: 
/*      */     case 164: 
/*      */     case 165: 
/*      */     case 166: 
/*      */     case 173: 
/*      */     case 175: 
/* 1134 */       pop(2);
/* 1135 */       break;
/*      */     case 89: 
/* 1137 */       int t1 = pop();
/* 1138 */       push(t1);
/* 1139 */       push(t1);
/* 1140 */       break;
/*      */     case 90: 
/* 1142 */       int t1 = pop();
/* 1143 */       int t2 = pop();
/* 1144 */       push(t1);
/* 1145 */       push(t2);
/* 1146 */       push(t1);
/* 1147 */       break;
/*      */     case 91: 
/* 1149 */       int t1 = pop();
/* 1150 */       int t2 = pop();
/* 1151 */       int t3 = pop();
/* 1152 */       push(t1);
/* 1153 */       push(t3);
/* 1154 */       push(t2);
/* 1155 */       push(t1);
/* 1156 */       break;
/*      */     case 92: 
/* 1158 */       int t1 = pop();
/* 1159 */       int t2 = pop();
/* 1160 */       push(t2);
/* 1161 */       push(t1);
/* 1162 */       push(t2);
/* 1163 */       push(t1);
/* 1164 */       break;
/*      */     case 93: 
/* 1166 */       int t1 = pop();
/* 1167 */       int t2 = pop();
/* 1168 */       int t3 = pop();
/* 1169 */       push(t2);
/* 1170 */       push(t1);
/* 1171 */       push(t3);
/* 1172 */       push(t2);
/* 1173 */       push(t1);
/* 1174 */       break;
/*      */     case 94: 
/* 1176 */       int t1 = pop();
/* 1177 */       int t2 = pop();
/* 1178 */       int t3 = pop();
/* 1179 */       int t4 = pop();
/* 1180 */       push(t2);
/* 1181 */       push(t1);
/* 1182 */       push(t4);
/* 1183 */       push(t3);
/* 1184 */       push(t2);
/* 1185 */       push(t1);
/* 1186 */       break;
/*      */     case 95: 
/* 1188 */       int t1 = pop();
/* 1189 */       int t2 = pop();
/* 1190 */       push(t1);
/* 1191 */       push(t2);
/* 1192 */       break;
/*      */     case 96: 
/*      */     case 100: 
/*      */     case 104: 
/*      */     case 108: 
/*      */     case 112: 
/*      */     case 120: 
/*      */     case 122: 
/*      */     case 124: 
/*      */     case 126: 
/*      */     case 128: 
/*      */     case 130: 
/*      */     case 136: 
/*      */     case 142: 
/*      */     case 149: 
/*      */     case 150: 
/* 1208 */       pop(2);
/* 1209 */       push(16777217);
/* 1210 */       break;
/*      */     case 97: 
/*      */     case 101: 
/*      */     case 105: 
/*      */     case 109: 
/*      */     case 113: 
/*      */     case 127: 
/*      */     case 129: 
/*      */     case 131: 
/* 1219 */       pop(4);
/* 1220 */       push(16777220);
/* 1221 */       push(16777216);
/* 1222 */       break;
/*      */     case 98: 
/*      */     case 102: 
/*      */     case 106: 
/*      */     case 110: 
/*      */     case 114: 
/*      */     case 137: 
/*      */     case 144: 
/* 1230 */       pop(2);
/* 1231 */       push(16777218);
/* 1232 */       break;
/*      */     case 99: 
/*      */     case 103: 
/*      */     case 107: 
/*      */     case 111: 
/*      */     case 115: 
/* 1238 */       pop(4);
/* 1239 */       push(16777219);
/* 1240 */       push(16777216);
/* 1241 */       break;
/*      */     case 121: 
/*      */     case 123: 
/*      */     case 125: 
/* 1245 */       pop(3);
/* 1246 */       push(16777220);
/* 1247 */       push(16777216);
/* 1248 */       break;
/*      */     case 132: 
/* 1250 */       set(arg, 16777217);
/* 1251 */       break;
/*      */     case 133: 
/*      */     case 140: 
/* 1254 */       pop(1);
/* 1255 */       push(16777220);
/* 1256 */       push(16777216);
/* 1257 */       break;
/*      */     case 134: 
/* 1259 */       pop(1);
/* 1260 */       push(16777218);
/* 1261 */       break;
/*      */     case 135: 
/*      */     case 141: 
/* 1264 */       pop(1);
/* 1265 */       push(16777219);
/* 1266 */       push(16777216);
/* 1267 */       break;
/*      */     case 139: 
/*      */     case 190: 
/*      */     case 193: 
/* 1271 */       pop(1);
/* 1272 */       push(16777217);
/* 1273 */       break;
/*      */     case 148: 
/*      */     case 151: 
/*      */     case 152: 
/* 1277 */       pop(4);
/* 1278 */       push(16777217);
/* 1279 */       break;
/*      */     case 168: 
/*      */     case 169: 
/* 1282 */       throw new RuntimeException("JSR/RET are not supported with computeFrames option");
/*      */     
/*      */     case 178: 
/* 1285 */       push(cw, item.strVal3);
/* 1286 */       break;
/*      */     case 179: 
/* 1288 */       pop(item.strVal3);
/* 1289 */       break;
/*      */     case 180: 
/* 1291 */       pop(1);
/* 1292 */       push(cw, item.strVal3);
/* 1293 */       break;
/*      */     case 181: 
/* 1295 */       pop(item.strVal3);
/* 1296 */       pop();
/* 1297 */       break;
/*      */     case 182: 
/*      */     case 183: 
/*      */     case 184: 
/*      */     case 185: 
/* 1302 */       pop(item.strVal3);
/* 1303 */       if (opcode != 184) {
/* 1304 */         int t1 = pop();
/* 1305 */         if ((opcode == 183) && 
/* 1306 */           (item.strVal2.charAt(0) == '<')) {
/* 1307 */           init(t1);
/*      */         }
/*      */       }
/* 1310 */       push(cw, item.strVal3);
/* 1311 */       break;
/*      */     case 186: 
/* 1313 */       pop(item.strVal2);
/* 1314 */       push(cw, item.strVal2);
/* 1315 */       break;
/*      */     case 187: 
/* 1317 */       push(0x1800000 | cw.addUninitializedType(item.strVal1, arg));
/* 1318 */       break;
/*      */     case 188: 
/* 1320 */       pop();
/* 1321 */       switch (arg) {
/*      */       case 4: 
/* 1323 */         push(285212681);
/* 1324 */         break;
/*      */       case 5: 
/* 1326 */         push(285212683);
/* 1327 */         break;
/*      */       case 8: 
/* 1329 */         push(285212682);
/* 1330 */         break;
/*      */       case 9: 
/* 1332 */         push(285212684);
/* 1333 */         break;
/*      */       case 10: 
/* 1335 */         push(285212673);
/* 1336 */         break;
/*      */       case 6: 
/* 1338 */         push(285212674);
/* 1339 */         break;
/*      */       case 7: 
/* 1341 */         push(285212675);
/* 1342 */         break;
/*      */       
/*      */       default: 
/* 1345 */         push(285212676); }
/* 1346 */       break;
/*      */     
/*      */ 
/*      */     case 189: 
/* 1350 */       String s = item.strVal1;
/* 1351 */       pop();
/* 1352 */       if (s.charAt(0) == '[') {
/* 1353 */         push(cw, '[' + s);
/*      */       } else {
/* 1355 */         push(0x11700000 | cw.addType(s));
/*      */       }
/* 1357 */       break;
/*      */     case 192: 
/* 1359 */       String s = item.strVal1;
/* 1360 */       pop();
/* 1361 */       if (s.charAt(0) == '[') {
/* 1362 */         push(cw, s);
/*      */       } else {
/* 1364 */         push(0x1700000 | cw.addType(s));
/*      */       }
/* 1366 */       break;
/*      */     case 19: case 20: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: case 45: case 59: 
/*      */     case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 196: case 197: default: 
/* 1369 */       pop(arg);
/* 1370 */       push(cw, item.strVal1);
/*      */     }
/*      */     
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
/*      */   final boolean merge(ClassWriter cw, Frame frame, int edge)
/*      */   {
/* 1391 */     boolean changed = false;
/*      */     
/*      */ 
/* 1394 */     int nLocal = this.inputLocals.length;
/* 1395 */     int nStack = this.inputStack.length;
/* 1396 */     if (frame.inputLocals == null) {
/* 1397 */       frame.inputLocals = new int[nLocal];
/* 1398 */       changed = true;
/*      */     }
/*      */     
/* 1401 */     for (int i = 0; i < nLocal; i++) { int t;
/* 1402 */       if ((this.outputLocals != null) && (i < this.outputLocals.length)) {
/* 1403 */         int s = this.outputLocals[i];
/* 1404 */         int t; if (s == 0) {
/* 1405 */           t = this.inputLocals[i];
/*      */         } else {
/* 1407 */           int dim = s & 0xF0000000;
/* 1408 */           int kind = s & 0xF000000;
/* 1409 */           int t; if (kind == 16777216) {
/* 1410 */             t = s; } else { int t;
/*      */             int t;
/* 1412 */             if (kind == 33554432) {
/* 1413 */               t = dim + this.inputLocals[(s & 0x7FFFFF)];
/*      */             } else {
/* 1415 */               t = dim + this.inputStack[(nStack - (s & 0x7FFFFF))];
/*      */             }
/* 1417 */             if (((s & 0x800000) != 0) && ((t == 16777220) || (t == 16777219)))
/*      */             {
/* 1419 */               t = 16777216;
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 1424 */         t = this.inputLocals[i];
/*      */       }
/* 1426 */       if (this.initializations != null) {
/* 1427 */         t = init(cw, t);
/*      */       }
/* 1429 */       changed |= merge(cw, t, frame.inputLocals, i);
/*      */     }
/*      */     
/* 1432 */     if (edge > 0) {
/* 1433 */       for (i = 0; i < nLocal; i++) {
/* 1434 */         int t = this.inputLocals[i];
/* 1435 */         changed |= merge(cw, t, frame.inputLocals, i);
/*      */       }
/* 1437 */       if (frame.inputStack == null) {
/* 1438 */         frame.inputStack = new int[1];
/* 1439 */         changed = true;
/*      */       }
/* 1441 */       changed |= merge(cw, edge, frame.inputStack, 0);
/* 1442 */       return changed;
/*      */     }
/*      */     
/* 1445 */     int nInputStack = this.inputStack.length + this.owner.inputStackTop;
/* 1446 */     if (frame.inputStack == null) {
/* 1447 */       frame.inputStack = new int[nInputStack + this.outputStackTop];
/* 1448 */       changed = true;
/*      */     }
/*      */     
/* 1451 */     for (i = 0; i < nInputStack; i++) {
/* 1452 */       int t = this.inputStack[i];
/* 1453 */       if (this.initializations != null) {
/* 1454 */         t = init(cw, t);
/*      */       }
/* 1456 */       changed |= merge(cw, t, frame.inputStack, i);
/*      */     }
/* 1458 */     for (i = 0; i < this.outputStackTop; i++) {
/* 1459 */       int s = this.outputStack[i];
/* 1460 */       int dim = s & 0xF0000000;
/* 1461 */       int kind = s & 0xF000000;
/* 1462 */       int t; int t; if (kind == 16777216) {
/* 1463 */         t = s;
/*      */       } else { int t;
/* 1465 */         if (kind == 33554432) {
/* 1466 */           t = dim + this.inputLocals[(s & 0x7FFFFF)];
/*      */         } else {
/* 1468 */           t = dim + this.inputStack[(nStack - (s & 0x7FFFFF))];
/*      */         }
/* 1470 */         if (((s & 0x800000) != 0) && ((t == 16777220) || (t == 16777219)))
/*      */         {
/* 1472 */           t = 16777216;
/*      */         }
/*      */       }
/* 1475 */       if (this.initializations != null) {
/* 1476 */         t = init(cw, t);
/*      */       }
/* 1478 */       changed |= merge(cw, t, frame.inputStack, nInputStack + i);
/*      */     }
/* 1480 */     return changed;
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
/*      */   private static boolean merge(ClassWriter cw, int t, int[] types, int index)
/*      */   {
/* 1501 */     int u = types[index];
/* 1502 */     if (u == t)
/*      */     {
/* 1504 */       return false;
/*      */     }
/* 1506 */     if ((t & 0xFFFFFFF) == 16777221) {
/* 1507 */       if (u == 16777221) {
/* 1508 */         return false;
/*      */       }
/* 1510 */       t = 16777221;
/*      */     }
/* 1512 */     if (u == 0)
/*      */     {
/* 1514 */       types[index] = t;
/* 1515 */       return true; }
/*      */     int v;
/*      */     int v;
/* 1518 */     if (((u & 0xFF00000) == 24117248) || ((u & 0xF0000000) != 0))
/*      */     {
/* 1520 */       if (t == 16777221)
/*      */       {
/* 1522 */         return false; }
/* 1523 */       int v; if ((t & 0xFFF00000) == (u & 0xFFF00000)) {
/*      */         int v;
/* 1525 */         if ((u & 0xFF00000) == 24117248)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1530 */           v = t & 0xF0000000 | 0x1700000 | cw.getMergedType(t & 0xFFFFF, u & 0xFFFFF);
/*      */         }
/*      */         else
/*      */         {
/* 1534 */           int vdim = -268435456 + (u & 0xF0000000);
/* 1535 */           v = vdim | 0x1700000 | cw.addType("java/lang/Object");
/*      */         } } else { int v;
/* 1537 */         if (((t & 0xFF00000) == 24117248) || ((t & 0xF0000000) != 0))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1542 */           int tdim = (((t & 0xF0000000) == 0) || ((t & 0xFF00000) == 24117248) ? 0 : -268435456) + (t & 0xF0000000);
/*      */           
/* 1544 */           int udim = (((u & 0xF0000000) == 0) || ((u & 0xFF00000) == 24117248) ? 0 : -268435456) + (u & 0xF0000000);
/*      */           
/*      */ 
/* 1547 */           v = Math.min(tdim, udim) | 0x1700000 | cw.addType("java/lang/Object");
/*      */         }
/*      */         else {
/* 1550 */           v = 16777216;
/*      */         } } } else { int v;
/* 1552 */       if (u == 16777221)
/*      */       {
/*      */ 
/* 1555 */         v = ((t & 0xFF00000) == 24117248) || ((t & 0xF0000000) != 0) ? t : 16777216;
/*      */       }
/*      */       else
/* 1558 */         v = 16777216;
/*      */     }
/* 1560 */     if (u != v) {
/* 1561 */       types[index] = v;
/* 1562 */       return true;
/*      */     }
/* 1564 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\asm\Frame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */