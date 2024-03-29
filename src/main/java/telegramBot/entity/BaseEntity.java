package telegramBot.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

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

    public void setId(Long id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof BaseEntity)) return false;
        BaseEntity entity = (BaseEntity) obj;
        return Objects.equals(this.id, entity.id);
    }
}
