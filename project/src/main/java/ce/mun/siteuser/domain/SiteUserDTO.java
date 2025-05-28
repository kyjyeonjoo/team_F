package ce.mun.siteuser.domain;

import lombok.Data;

@Data
public class SiteUserDTO {
	String name;
	String email;
	String passwd;
	String nickname;
	String phone;
	String address;
}
