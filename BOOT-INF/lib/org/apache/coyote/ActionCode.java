package org.apache.coyote;

public enum ActionCode
{
  ACK,  CLOSE,  COMMIT,  CLOSE_NOW,  CLIENT_FLUSH,  IS_ERROR,  IS_IO_ALLOWED,  DISABLE_SWALLOW_INPUT,  REQ_HOST_ATTRIBUTE,  REQ_HOST_ADDR_ATTRIBUTE,  REQ_SSL_ATTRIBUTE,  REQ_SSL_CERTIFICATE,  REQ_REMOTEPORT_ATTRIBUTE,  REQ_LOCALPORT_ATTRIBUTE,  REQ_LOCAL_ADDR_ATTRIBUTE,  REQ_LOCAL_NAME_ATTRIBUTE,  REQ_SET_BODY_REPLAY,  AVAILABLE,  ASYNC_START,  ASYNC_DISPATCH,  ASYNC_DISPATCHED,  ASYNC_RUN,  ASYNC_COMPLETE,  ASYNC_TIMEOUT,  ASYNC_ERROR,  ASYNC_SETTIMEOUT,  ASYNC_IS_ASYNC,  ASYNC_IS_STARTED,  ASYNC_IS_COMPLETING,  ASYNC_IS_DISPATCHING,  ASYNC_IS_TIMINGOUT,  ASYNC_IS_ERROR,  ASYNC_POST_PROCESS,  UPGRADE,  NB_READ_INTEREST,  NB_WRITE_INTEREST,  REQUEST_BODY_FULLY_READ,  DISPATCH_READ,  DISPATCH_WRITE,  DISPATCH_EXECUTE,  IS_PUSH_SUPPORTED,  PUSH_REQUEST;
  
  private ActionCode() {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ActionCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */