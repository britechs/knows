package org.knows.dao;

import org.knows.model.KSSnippet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * more queries at:
 * https://github.com/eugenp/tutorials/tree/master/spring-data-elasticsearch/src/test/java/com/baeldung/spring/data/es
 */
	
public interface SnippetRepository extends ElasticsearchRepository<KSSnippet, String> {
	
	Page<KSSnippet> findByDomain(String domain, Pageable pageable);
	Page<KSSnippet> findByTags(String tag, Pageable pageable);
	Page<KSSnippet> findByTagsIn(String[] tag, Pageable pageable);
    Page<KSSnippet> findByTitle(String name, Pageable pageable);
 
    Page<KSSnippet> findByContentContaining(String content, Pageable pageable);

    Page<KSSnippet> findByContentContainingAndTagsIn(String content, String[] tags, Pageable pageable);
//    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
//    Page<KSSnippet> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
