package telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "BOT_STATUS")
@NoArgsConstructor
public class BotStatus {

    @Id
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
