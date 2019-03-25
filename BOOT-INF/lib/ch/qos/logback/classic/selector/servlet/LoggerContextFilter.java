package ch.qos.logback.classic.selector.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class LoggerContextFilter
  implements Filter
{
  public void destroy() {}
  
  /* Error */
  public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain)
    throws java.io.IOException, ServletException
  {
    // Byte code:
    //   0: invokestatic 24	org/slf4j/LoggerFactory:getILoggerFactory	()Lorg/slf4j/ILoggerFactory;
    //   3: checkcast 30	ch/qos/logback/classic/LoggerContext
    //   6: astore 4
    //   8: invokestatic 32	ch/qos/logback/classic/util/ContextSelectorStaticBinder:getSingleton	()Lch/qos/logback/classic/util/ContextSelectorStaticBinder;
    //   11: invokevirtual 38	ch/qos/logback/classic/util/ContextSelectorStaticBinder:getContextSelector	()Lch/qos/logback/classic/selector/ContextSelector;
    //   14: astore 5
    //   16: aconst_null
    //   17: astore 6
    //   19: aload 5
    //   21: instanceof 42
    //   24: ifeq +17 -> 41
    //   27: aload 5
    //   29: checkcast 42	ch/qos/logback/classic/selector/ContextJNDISelector
    //   32: astore 6
    //   34: aload 6
    //   36: aload 4
    //   38: invokevirtual 44	ch/qos/logback/classic/selector/ContextJNDISelector:setLocalContext	(Lch/qos/logback/classic/LoggerContext;)V
    //   41: aload_3
    //   42: aload_1
    //   43: aload_2
    //   44: invokeinterface 48 3 0
    //   49: goto +18 -> 67
    //   52: astore 7
    //   54: aload 6
    //   56: ifnull +8 -> 64
    //   59: aload 6
    //   61: invokevirtual 53	ch/qos/logback/classic/selector/ContextJNDISelector:removeLocalContext	()V
    //   64: aload 7
    //   66: athrow
    //   67: aload 6
    //   69: ifnull +8 -> 77
    //   72: aload 6
    //   74: invokevirtual 53	ch/qos/logback/classic/selector/ContextJNDISelector:removeLocalContext	()V
    //   77: return
    // Line number table:
    //   Java source line #59	-> byte code offset #0
    //   Java source line #60	-> byte code offset #8
    //   Java source line #61	-> byte code offset #16
    //   Java source line #63	-> byte code offset #19
    //   Java source line #64	-> byte code offset #27
    //   Java source line #65	-> byte code offset #34
    //   Java source line #69	-> byte code offset #41
    //   Java source line #70	-> byte code offset #49
    //   Java source line #71	-> byte code offset #54
    //   Java source line #72	-> byte code offset #59
    //   Java source line #74	-> byte code offset #64
    //   Java source line #71	-> byte code offset #67
    //   Java source line #72	-> byte code offset #72
    //   Java source line #75	-> byte code offset #77
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	LoggerContextFilter
    //   0	78	1	request	javax.servlet.ServletRequest
    //   0	78	2	response	javax.servlet.ServletResponse
    //   0	78	3	chain	javax.servlet.FilterChain
    //   6	31	4	lc	ch.qos.logback.classic.LoggerContext
    //   14	14	5	selector	ch.qos.logback.classic.selector.ContextSelector
    //   17	56	6	sel	ch.qos.logback.classic.selector.ContextJNDISelector
    //   52	13	7	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   41	52	52	finally
  }
  
  public void init(FilterConfig arg0)
    throws ServletException
  {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\selector\servlet\LoggerContextFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */