package ch.qos.logback.core.net.server;

public abstract interface ClientVisitor<T extends Client>
{
  public abstract void visit(T paramT);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\server\ClientVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */