package ce.mun.siteuser.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Article_reviewDTO {
	private String author;
	private String title;
	private String contents;
	private Date day;
}
