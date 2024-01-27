package telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Entity
@Table(name = "Bot_status")
@NoArgsConstructor
public class BotStatus extends BaseEntity {

    @Getter
    @Column(name = "status")
    private String status;

    public void setStatus(telegramBot.enums.BotStatus status) {
        this.status = status.getStatus();
    }




}
