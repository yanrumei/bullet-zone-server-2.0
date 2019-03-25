package org.apache.catalina.valves;

public final class Constants
{
  public static final String Package = "org.apache.catalina.valves";
  
  public static final class AccessLog
  {
    public static final String COMMON_ALIAS = "common";
    public static final String COMMON_PATTERN = "%h %l %u %t \"%r\" %s %b";
    public static final String COMBINED_ALIAS = "combined";
    public static final String COMBINED_PATTERN = "%h %l %u %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\"";
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */