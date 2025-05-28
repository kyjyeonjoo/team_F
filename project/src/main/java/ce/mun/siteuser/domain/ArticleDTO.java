package ce.mun.siteuser.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ArticleDTO {
	private String author;
	private String title;
	private String contents;
	private Date day;
}
