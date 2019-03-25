package org.springframework.boot;

import java.util.List;
import java.util.Set;

public abstract interface ApplicationArguments
{
  public abstract String[] getSourceArgs();
  
  public abstract Set<String> getOptionNames();
  
  public abstract boolean containsOption(String paramString);
  
  public abstract List<String> getOptionValues(String paramString);
  
  public abstract List<String> getNonOptionArgs();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ApplicationArguments.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */