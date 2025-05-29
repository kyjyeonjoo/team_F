package ce.mun.siteuser.repository;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Article_review {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long num;
	@Column(length = 10, nullable = false)
	private String author;
	@Column(length = 50, nullable = false)
	private String title;
	@Column(length = 2048, nullable = false)
	private String contents;
	
	@Column(length = 100, nullable = false)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date day =  new java.util.Date();
}
