package org.hibernate.validator.spi.resourceloading;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract interface ResourceBundleLocator
{
  public abstract ResourceBundle getResourceBundle(Locale paramLocale);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\spi\resourceloading\ResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */