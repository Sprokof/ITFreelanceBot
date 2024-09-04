package telegramBot.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Setter
@MappedSuperclass
public abstract class BaseEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public BaseEntity(){}

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public Long getId() {
        return id;
    }

    public long id() {
        Asserts.notNull(id, "entity must have id");
        return this.id;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.intValue();
    }

    @Override
    // not correct impl
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof BaseEntity entity)) return false;
        return Objects.equals(this.id, entity.id);
    }
}
