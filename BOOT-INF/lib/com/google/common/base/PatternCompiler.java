package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract interface PatternCompiler
{
  public abstract CommonPattern compile(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\PatternCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */