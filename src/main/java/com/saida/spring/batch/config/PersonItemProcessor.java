package com.saida.spring.batch.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.saida.spring.batch.domain.Person;
import com.saida.spring.batch.domain.PersonRecord;

public class PersonItemProcessor implements ItemProcessor<PersonRecord, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	private static final DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

	public Person process(final PersonRecord personRecord) throws Exception {

		final Person transformedPerson = new Person(personRecord.getSourceId(), personRecord.getFirstName(),
				personRecord.getMiddleInitial(), personRecord.getLastName(), personRecord.getEmailAddress(),
				personRecord.getPhoneNumber(), personRecord.getStreet(), personRecord.getCity(),
				personRecord.getState(), personRecord.getZip(), format.parse(personRecord.getBirthDate()),
				personRecord.getSsn());
		log.info("Converting (" + personRecord + ") into (" + transformedPerson + ")");

		return transformedPerson;

	}
}
