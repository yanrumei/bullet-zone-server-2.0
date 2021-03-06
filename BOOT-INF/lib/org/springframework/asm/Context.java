package org.springframework.asm;

class Context
{
  Attribute[] attrs;
  int flags;
  char[] buffer;
  int[] bootstrapMethods;
  int access;
  String name;
  String desc;
  Label[] labels;
  int typeRef;
  TypePath typePath;
  int offset;
  Label[] start;
  Label[] end;
  int[] index;
  int mode;
  int localCount;
  int localDiff;
  Object[] local;
  int stackCount;
  Object[] stack;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\asm\Context.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */