package ce.mun.siteuser.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(length=20, nullable=false)
	private String name;
	@Column(length=50, nullable=false, unique=true)
	private String email;
	@Column(length=20, nullable=false, name="password")
	private String passwd;
	private String nickname;
	private String phone;
	private String address;
	public SiteUser(String name, String email, String passwd, String nickname, String phone, String address) {
		this.name = name;
		this.email=email;
		this.passwd=passwd;
		this.nickname=nickname;
		this.phone=phone;
		this.address=address;
	}
}
