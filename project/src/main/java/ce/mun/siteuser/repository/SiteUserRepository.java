package ce.mun.siteuser.repository;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

public interface SiteUserRepository extends CrudRepository<SiteUser, Long> {
	SiteUser findByEmail(String email);

	SiteUser findByName(String name);
}
