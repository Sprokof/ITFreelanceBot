package telegramBot.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParseClass {
    private String parent;
    private String children;

    public ParseClass(String parent) {
        this.parent = parent;
    }
}
