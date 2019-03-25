package org.springframework.beans.factory.parsing;

public abstract interface ProblemReporter
{
  public abstract void fatal(Problem paramProblem);
  
  public abstract void error(Problem paramProblem);
  
  public abstract void warning(Problem paramProblem);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\ProblemReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */