package org.springframework.asm;

class Edge
{
  static final int NORMAL = 0;
  static final int EXCEPTION = Integer.MAX_VALUE;
  int info;
  Label successor;
  Edge next;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\asm\Edge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */