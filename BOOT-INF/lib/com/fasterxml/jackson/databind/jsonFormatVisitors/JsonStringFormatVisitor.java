package com.fasterxml.jackson.databind.jsonFormatVisitors;

public abstract interface JsonStringFormatVisitor
  extends JsonValueFormatVisitor
{
  public static class Base
    extends JsonValueFormatVisitor.Base
    implements JsonStringFormatVisitor
  {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonStringFormatVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */