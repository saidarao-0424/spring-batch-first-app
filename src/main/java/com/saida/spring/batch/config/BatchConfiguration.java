package com.saida.spring.batch.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.saida.spring.batch.domain.Person;
import com.saida.spring.batch.domain.PersonRecord;

/**
 * Main configuration class for Spring Batch dependencies.
 *
 * The @EnableBatchProcessing annotation gives you access to a variety of beans
 * related to batch processing. DefaultBatchConfigurer provides a default
 * strategy for the initialization of Spring Batch dependencies.
 */
@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@EnableJpaRepositories(value = "com.saida.spring.batch")
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@PersistenceContext(unitName = "default")
	private EntityManager entityManager;

	@Bean
	JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
		postProcessor.setJobRegistry(jobRegistry);
		return postProcessor;
	}

	@Bean
	public Job job(Step step) throws Exception {
		return this.jobBuilderFactory.get(Constants.JOB_NAME).validator(validator()).incrementer(new RunIdIncrementer())
				.start(step).build();
	}

	@Bean
	public Step step(FlatFileItemReader<PersonRecord> reader, PersonItemProcessor processor,
			JpaItemWriter<Person> writer) throws Exception {
		return this.stepBuilderFactory.get(Constants.STEP_NAME).<PersonRecord, Person>chunk(2).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	@StepScope
	public JpaItemWriter<Person> writer(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<Person> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<PersonRecord> reader() {

		return new FlatFileItemReaderBuilder<PersonRecord>().name("personItemReader")
				.resource(new ClassPathResource("test.csv")).linesToSkip(1).lineMapper(lineMapper()).build();
	}

	@Bean
	public LineMapper<PersonRecord> lineMapper() {
		DefaultLineMapper<PersonRecord> mapper = new DefaultLineMapper<>();
		mapper.setFieldSetMapper((fieldSet) -> new PersonRecord(fieldSet.readString(0), fieldSet.readString(1),
				fieldSet.readString(2), fieldSet.readString(3), fieldSet.readString(4), fieldSet.readString(5),
				fieldSet.readString(6), fieldSet.readString(7), fieldSet.readString(8), fieldSet.readString(9),
				fieldSet.readString(10), fieldSet.readString(11), fieldSet.readString(12)));
		mapper.setLineTokenizer(new DelimitedLineTokenizer());
		return mapper;
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public JobParametersValidator validator() {
		return new JobParametersValidator() {
			@Override
			public void validate(JobParameters parameters) throws JobParametersInvalidException {

			}
		};
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource datasource) {
		LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
		emfBean.setDataSource(datasource);
		emfBean.setPackagesToScan("com.saida.spring.batch");

		emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		return emfBean;
	}

	@Bean(name = "batchTransactionManager")
	public PlatformTransactionManager transactionManager(DataSource datasource) {
		return new JpaTransactionManager(entityManagerFactory(datasource).getObject());
	}
}