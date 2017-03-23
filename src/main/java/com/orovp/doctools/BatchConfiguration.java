package com.orovp.doctools;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Value("${default.input.dir.before}") String beforeDirPath;
	@Value("${default.input.dir.after}") String afterDirPath;
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
    @Autowired
    public DataSource dataSource;
    
	@Bean
	public ItemReader<Document> documentReader() {
		return new DocumentReader(beforeDirPath, afterDirPath);
	}

	@Bean
	public DiffProcessor processor() {
		return new DiffProcessor();
	}
	
	@Bean
	public ItemWriter<Document> documentWriter() {
		return new DocumentWithDiffWriter();
	}
	
	@Bean
	public Job makeDiffJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("makeDiffJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
	}
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1")
				.<Document, Document> chunk(1)
				.reader(documentReader())
				.processor(processor())
				.writer(documentWriter())
				.build();
	}
	
}
