package org.springframework.boot;

public abstract interface ExitCodeExceptionMapper
{
  public abstract int getExitCode(Throwable paramThrowable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ExitCodeExceptionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */