package javax.servlet;

import java.io.IOException;

public abstract interface FilterChain
{
  public abstract void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws IOException, ServletException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\FilterChain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */