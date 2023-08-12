package telegramBot.entity;

import lombok.Getter;

import javax.persistence.*;


@Entity
@Table(name = "BOT_STATUS")
public class BotStatus {
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "STATUS")
    private String status;

    public void setStatus(telegramBot.enums.BotStatus status) {
        this.status = status.getStatus();
    }




}
