package ce.mun.siteuser.service;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ce.mun.siteuser.domain.ArticleDTO;
import ce.mun.siteuser.domain.Article_reviewDTO;
import ce.mun.siteuser.domain.SiteUserDTO;
import ce.mun.siteuser.repository.Article;
import ce.mun.siteuser.repository.ArticleHeader;
import ce.mun.siteuser.repository.ArticleRepository;
import ce.mun.siteuser.repository.Article_review;
import ce.mun.siteuser.repository.Article_reviewHeader;
import ce.mun.siteuser.repository.Article_reviewRepository;
import ce.mun.siteuser.repository.SiteUser;
import ce.mun.siteuser.repository.SiteUserRepository;

@Service
public class SiteUserService {
	@Autowired
	private SiteUserRepository userRepository;
	
	
	// 게시판 관련 (맛집)
	@Autowired
	private ArticleRepository articleRepository;
	public Page<ArticleHeader> getArticleHeaders(Pageable pageable){
		return articleRepository.findArticleHeaders(pageable);
	}
	
	public Article getArticle(Long num) {
		return articleRepository.getReferenceById(num);
	}
	
	public void save(ArticleDTO dto) {
		Article article = new Article();
		article.setAuthor(dto.getAuthor());
		article.setTitle(dto.getTitle());
		article.setContents(dto.getContents());
		//article.setDay(dto.getDay());
		articleRepository.save(article);
	}
	
	public Iterable<Article> getArticleAll(){
		return articleRepository.findAll();
	}
	
	@Transactional
	public void edit(Long num, String title, String contents) {
		Article article = articleRepository.findById(num).orElse(null);
		if (article != null) {
			article.setTitle(title);
			article.setContents(contents);
		}
	}

	@Transactional
	public void delete(Long num) {
		articleRepository.deleteById(num);
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 게시판 관련 (후기)
	@Autowired
	private Article_reviewRepository article_reviewRepository;
	public Page<Article_reviewHeader> getArticle_reviewHeaders(Pageable pageable){
		return article_reviewRepository.findArticle_reviewHeaders(pageable);
	}
	
	public Article_review getArticle_review(Long num) {
		return article_reviewRepository.getReferenceById(num);
	}
	
	public void review_save(Article_reviewDTO dto) {
		Article_review article_review = new Article_review();
		article_review.setAuthor(dto.getAuthor());
		article_review.setTitle(dto.getTitle());
		article_review.setContents(dto.getContents());
		//article.setDay(dto.getDay());
		article_reviewRepository.save(article_review);
	}
	
	public Iterable<Article_review> getArticle_reviewAll(){
		return article_reviewRepository.findAll();
	}
	
	@Transactional
	public void editReview(Long num, String title, String contents) {
		Article_review article_review = article_reviewRepository.findById(num).orElse(null);
		if (article_review != null) {
			article_review.setTitle(title);
			article_review.setContents(contents);
		}
	}

	@Transactional
	public void deleteReview(Long num) {
		article_reviewRepository.deleteById(num);
	}
	

	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 사용자 관련
	public void save(SiteUserDTO dto) {
		SiteUser user = new SiteUser(dto.getName(), dto.getEmail(), dto.getPasswd(), dto.getNickname(), dto.getPhone(), dto.getAddress());
		userRepository.save(user);
	}
	public Iterable<SiteUser> getAll(){
		//select * from site_user;
		return userRepository.findAll();
	}
	public SiteUser getEmail(String email) {
		return userRepository.findByEmail(email);
	}
	public SiteUser getName(String name) {
		return userRepository.findByName(name);
	}

	
	@Transactional
	public void updateName(String email, String name, String password, String phone, String address) {
	SiteUser user = userRepository.findByEmail(email);
	if (user != null) {
		user.setName(name);
		user.setPasswd(password);
		user.setPhone(phone);
		user.setAddress(address);
		}
	}
}
