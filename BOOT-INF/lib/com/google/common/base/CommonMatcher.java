package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher
{
  abstract boolean matches();
  
  abstract boolean find();
  
  abstract boolean find(int paramInt);
  
  abstract String replaceAll(String paramString);
  
  abstract int end();
  
  abstract int start();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\CommonMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */