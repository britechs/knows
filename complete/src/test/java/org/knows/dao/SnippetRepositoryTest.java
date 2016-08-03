package org.knows.dao;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knows.Application;
import org.knows.model.KSSnippet;
import org.knows.model.KSSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SnippetRepositoryTest {
	static Logger logger = Logger.getLogger(SnippetRepositoryTest.class);
	@Autowired
	private SnippetRepository repository;
	
	@Autowired
	private ElasticsearchTemplate template;
	
	//@Test
	public void testAddSnippet() throws Exception {
		logger.info("repository:" + repository);
		
		KSSnippet t1 = new KSSnippet();
		t1.setContent("Elasticsearch is a highly scalable open-source which can be used for data store, text search and analytics engine. Every instance of ElasticSearch is called a node and several nodes can be grouped together in a cluster.");
		t1.setDomain("elasticsearch");
		t1.setDoctype("html");
		t1.setLink1("webapp");
		t1.setReadertype("dev");
		KSSource source = new KSSource();
		source.setLocation("https://examples.javacodegeeks.com/enterprise-java/spring/spring-data-elasticsearch-example/");
		source.setName("spring-data-elasticsearch-example.htm");
		t1.setSource(source);
		t1.setTags(Arrays.asList("elasticsearch","webapp"));
		
		repository.save(t1);
		
		template.putMapping(KSSnippet.class);
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(t1.getId());
		indexQuery.setObject(t1);
		template.index(indexQuery);
		template.refresh("knows");
	}
	
	@Test
	public void testSearchSnippet() throws Exception {
		logger.info("repository:" + repository);
		final Page<KSSnippet> snippets = repository.findByTags("utility", new PageRequest(0, 10));
		for(KSSnippet ks:snippets) {
			logger.info(ks.getId()+":" + ks.getContent());
		}
	}
	
	@Test
	public void testSearchSnippet2() throws Exception {
		logger.info("repository:" + repository);
		final Page<KSSnippet> snippets = repository.findByContentContaining("utility", new PageRequest(0, 10));
		for(KSSnippet ks:snippets) {
			logger.info(ks.getId()+":" + ks.getContent());
		}
	}
}
