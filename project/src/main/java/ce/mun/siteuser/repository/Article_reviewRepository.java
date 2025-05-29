package ce.mun.siteuser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Article_reviewRepository extends JpaRepository<Article_review, Long>{
	@Query(value = "SELECT num, title, author FROM article_review", nativeQuery=true)
	//@Query("SELECT a.num AS num, a.title AS title, a.author AS author FROM Article a")
	//Iterable<ArticleHeader> findArticleHeaders();
	Page<Article_reviewHeader> findArticle_reviewHeaders(Pageable pageable);

}
