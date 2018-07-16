package com.saida.spring.batch.config;

public final class Constants {

	/**
	 * Default system account used for all database interaction by the batch
	 * processor
	 */
	public static final String SYSTEM_ACCOUNT = "system";

	/**
	 * Assure locale-specific instances leverage the same default
	 */
	public static final String DEFAULT_LANG_KEY = "en";

	/**
	 * Name of the job
	 */
	public static final String JOB_NAME = "person-batch-loader";

	/**
	 * Name of the step
	 */
	public static final String STEP_NAME = "process-persons-step";

	/**
	 * Job parameter name for job file name
	 */
	public static final String JOB_PARAM_FILE_NAME = "person-batch-loader.fileName";



	private Constants() {
	}
}