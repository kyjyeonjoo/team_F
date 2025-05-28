package ce.mun.siteuser.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ce.mun.siteuser.domain.ArticleDTO;
import ce.mun.siteuser.domain.SiteUserDTO;
import ce.mun.siteuser.repository.Article;
import ce.mun.siteuser.repository.ArticleHeader;
import ce.mun.siteuser.repository.ArticleRepository;
import ce.mun.siteuser.repository.SiteUser;
import ce.mun.siteuser.repository.SiteUserRepository;

@Service
public class SiteUserService {
	@Autowired
	private SiteUserRepository userRepository;
	
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
	/*
	public SiteUser getday(Date day) {
		return userRepository.findByDay(day);
	}
	*/
	
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
