package org.springframework.boot.autoconfigure;

import java.util.EventListener;

public abstract interface AutoConfigurationImportListener
  extends EventListener
{
  public abstract void onAutoConfigurationImportEvent(AutoConfigurationImportEvent paramAutoConfigurationImportEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationImportListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */