package ce.mun.siteuser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	@Query(value = "SELECT num, title, author FROM article", nativeQuery=true)
	//@Query("SELECT a.num AS num, a.title AS title, a.author AS author FROM Article a")
	//Iterable<ArticleHeader> findArticleHeaders();
	Page<ArticleHeader> findArticleHeaders(Pageable pageable);
}
