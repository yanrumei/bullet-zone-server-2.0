package javax.websocket;

public abstract interface MessageHandler
{
  public static abstract interface Whole<T>
    extends MessageHandler
  {
    public abstract void onMessage(T paramT);
  }
  
  public static abstract interface Partial<T>
    extends MessageHandler
  {
    public abstract void onMessage(T paramT, boolean paramBoolean);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\MessageHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */