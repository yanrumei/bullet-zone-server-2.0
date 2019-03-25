/*     */ package org.springframework.boot.autoconfigure.batch;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.batch.core.BatchStatus;
/*     */ import org.springframework.batch.core.Job;
/*     */ import org.springframework.batch.core.JobExecution;
/*     */ import org.springframework.batch.core.JobExecutionException;
/*     */ import org.springframework.batch.core.JobInstance;
/*     */ import org.springframework.batch.core.JobParameter;
/*     */ import org.springframework.batch.core.JobParameters;
/*     */ import org.springframework.batch.core.JobParametersIncrementer;
/*     */ import org.springframework.batch.core.JobParametersInvalidException;
/*     */ import org.springframework.batch.core.configuration.JobRegistry;
/*     */ import org.springframework.batch.core.converter.DefaultJobParametersConverter;
/*     */ import org.springframework.batch.core.converter.JobParametersConverter;
/*     */ import org.springframework.batch.core.explore.JobExplorer;
/*     */ import org.springframework.batch.core.launch.JobLauncher;
/*     */ import org.springframework.batch.core.launch.JobParametersNotFoundException;
/*     */ import org.springframework.batch.core.launch.NoSuchJobException;
/*     */ import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
/*     */ import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
/*     */ import org.springframework.batch.core.repository.JobRestartException;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.CommandLineRunner;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.ApplicationEventPublisherAware;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JobLauncherCommandLineRunner
/*     */   implements CommandLineRunner, ApplicationEventPublisherAware
/*     */ {
/*  68 */   private static final Log logger = LogFactory.getLog(JobLauncherCommandLineRunner.class);
/*     */   
/*  70 */   private JobParametersConverter converter = new DefaultJobParametersConverter();
/*     */   
/*     */   private JobLauncher jobLauncher;
/*     */   
/*     */   private JobRegistry jobRegistry;
/*     */   
/*     */   private JobExplorer jobExplorer;
/*     */   
/*     */   private String jobNames;
/*     */   
/*  80 */   private Collection<Job> jobs = Collections.emptySet();
/*     */   
/*     */   private ApplicationEventPublisher publisher;
/*     */   
/*     */   public JobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer)
/*     */   {
/*  86 */     this.jobLauncher = jobLauncher;
/*  87 */     this.jobExplorer = jobExplorer;
/*     */   }
/*     */   
/*     */   public void setJobNames(String jobNames) {
/*  91 */     this.jobNames = jobNames;
/*     */   }
/*     */   
/*     */   public void setApplicationEventPublisher(ApplicationEventPublisher publisher)
/*     */   {
/*  96 */     this.publisher = publisher;
/*     */   }
/*     */   
/*     */   @Autowired(required=false)
/*     */   public void setJobRegistry(JobRegistry jobRegistry) {
/* 101 */     this.jobRegistry = jobRegistry;
/*     */   }
/*     */   
/*     */   @Autowired(required=false)
/*     */   public void setJobParametersConverter(JobParametersConverter converter) {
/* 106 */     this.converter = converter;
/*     */   }
/*     */   
/*     */   @Autowired(required=false)
/*     */   public void setJobs(Collection<Job> jobs) {
/* 111 */     this.jobs = jobs;
/*     */   }
/*     */   
/*     */   public void run(String... args) throws JobExecutionException
/*     */   {
/* 116 */     logger.info("Running default command line with: " + Arrays.asList(args));
/* 117 */     launchJobFromProperties(StringUtils.splitArrayElementsIntoProperties(args, "="));
/*     */   }
/*     */   
/*     */   protected void launchJobFromProperties(Properties properties) throws JobExecutionException
/*     */   {
/* 122 */     JobParameters jobParameters = this.converter.getJobParameters(properties);
/* 123 */     executeLocalJobs(jobParameters);
/* 124 */     executeRegisteredJobs(jobParameters);
/*     */   }
/*     */   
/*     */   private JobParameters getNextJobParameters(Job job, JobParameters additionalParameters)
/*     */   {
/* 129 */     String name = job.getName();
/* 130 */     JobParameters parameters = new JobParameters();
/* 131 */     List<JobInstance> lastInstances = this.jobExplorer.getJobInstances(name, 0, 1);
/* 132 */     JobParametersIncrementer incrementer = job.getJobParametersIncrementer();
/* 133 */     Map<String, JobParameter> additionals = additionalParameters.getParameters();
/* 134 */     if (lastInstances.isEmpty())
/*     */     {
/* 136 */       if (incrementer != null) {
/* 137 */         parameters = incrementer.getNext(new JobParameters());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 142 */       List<JobExecution> previousExecutions = this.jobExplorer.getJobExecutions((JobInstance)lastInstances.get(0));
/* 143 */       JobExecution previousExecution = (JobExecution)previousExecutions.get(0);
/* 144 */       if (previousExecution == null)
/*     */       {
/* 146 */         if (incrementer != null) {
/* 147 */           parameters = incrementer.getNext(new JobParameters());
/*     */         }
/*     */       }
/* 150 */       else if ((isStoppedOrFailed(previousExecution)) && (job.isRestartable()))
/*     */       {
/* 152 */         parameters = previousExecution.getJobParameters();
/*     */         
/* 154 */         removeNonIdentifying(additionals);
/*     */       }
/* 156 */       else if (incrementer != null)
/*     */       {
/* 158 */         parameters = incrementer.getNext(previousExecution.getJobParameters());
/*     */       }
/*     */     }
/* 161 */     return merge(parameters, additionals);
/*     */   }
/*     */   
/*     */   private boolean isStoppedOrFailed(JobExecution execution) {
/* 165 */     BatchStatus status = execution.getStatus();
/* 166 */     return (status == BatchStatus.STOPPED) || (status == BatchStatus.FAILED);
/*     */   }
/*     */   
/*     */   private void removeNonIdentifying(Map<String, JobParameter> parameters) {
/* 170 */     HashMap<String, JobParameter> copy = new HashMap(parameters);
/*     */     
/* 172 */     for (Map.Entry<String, JobParameter> parameter : copy.entrySet()) {
/* 173 */       if (!((JobParameter)parameter.getValue()).isIdentifying()) {
/* 174 */         parameters.remove(parameter.getKey());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private JobParameters merge(JobParameters parameters, Map<String, JobParameter> additionals)
/*     */   {
/* 181 */     Map<String, JobParameter> merged = new HashMap();
/* 182 */     merged.putAll(parameters.getParameters());
/* 183 */     merged.putAll(additionals);
/* 184 */     return new JobParameters(merged);
/*     */   }
/*     */   
/*     */   private void executeRegisteredJobs(JobParameters jobParameters) throws JobExecutionException
/*     */   {
/* 189 */     if ((this.jobRegistry != null) && (StringUtils.hasText(this.jobNames))) {
/* 190 */       String[] jobsToRun = this.jobNames.split(",");
/* 191 */       for (String jobName : jobsToRun) {
/*     */         try {
/* 193 */           Job job = this.jobRegistry.getJob(jobName);
/* 194 */           if (!this.jobs.contains(job))
/*     */           {
/*     */ 
/* 197 */             execute(job, jobParameters);
/*     */           }
/*     */         } catch (NoSuchJobException ex) {
/* 200 */           logger.debug("No job found in registry for job name: " + jobName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void execute(Job job, JobParameters jobParameters)
/*     */     throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobParametersNotFoundException
/*     */   {
/* 210 */     JobParameters nextParameters = getNextJobParameters(job, jobParameters);
/* 211 */     JobExecution execution = this.jobLauncher.run(job, nextParameters);
/* 212 */     if (this.publisher != null) {
/* 213 */       this.publisher.publishEvent(new JobExecutionEvent(execution));
/*     */     }
/*     */   }
/*     */   
/*     */   private void executeLocalJobs(JobParameters jobParameters) throws JobExecutionException
/*     */   {
/* 219 */     for (Job job : this.jobs) {
/* 220 */       if (StringUtils.hasText(this.jobNames)) {
/* 221 */         String[] jobsToRun = this.jobNames.split(",");
/* 222 */         if (!PatternMatchUtils.simpleMatch(jobsToRun, job.getName())) {
/* 223 */           logger.debug("Skipped job: " + job.getName());
/* 224 */           continue;
/*     */         }
/*     */       }
/* 227 */       execute(job, jobParameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\JobLauncherCommandLineRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */