package akuchars.domain.user.model;

import akuchars.domain.common.AbstractJpaEntity;
import akuchars.kernel.ApplicationProperties;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = ApplicationProperties.USER_SCHEMA_NAME, name = "users")
public class User extends AbstractJpaEntity {
}
