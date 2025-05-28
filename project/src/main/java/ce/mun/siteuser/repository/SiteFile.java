package ce.mun.siteuser.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="site_file")
@Data
public class SiteFile {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long num;
	private String name;
	

}
