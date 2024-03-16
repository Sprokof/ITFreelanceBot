package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.enums.Language;
import telegramBot.enums.Role;
import telegramBot.service.AdminService;
import telegramBot.service.MessageService;
import telegramBot.util.BotUtil;

import java.util.Map;

public class AdminCommand implements Command {
    private final MessageService messageService;
    private final AdminService adminService;

    public AdminCommand(MessageService messageService, AdminService adminService){
        this.messageService = messageService;
        this.adminService = adminService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String command = Role.isAdmin(chatId) ? buildAdminCommand() : BotUtil.UNKNOWN_COMMAND;
        messageService.sendResponse(chatId, command);
    }

    private String buildAdminCommand() {
        int subscriptionsCount = adminService.subscriptionsCount();
        Map<String, Integer> subscriptionOrdersCount = adminService.subscriptionOrdersCount();
        Map<String, Integer> subscribedUsersCount = adminService.subscribedUsersCount();
        int countActiveUsers = adminService.activeUsersCount();
        int countNonActiveUsers = adminService.nonActiveUsersCount();
        StringBuilder adminCommand = new StringBuilder();
        adminCommand.append("Количество подписок: ")
                .append(subscriptionsCount)
                .append("\n");
        for (Language language : Language.getLanguages()) {
            int count = subscriptionOrdersCount.get(language.getName());
            adminCommand.append("Количество заказов по подписке ")
                    .append(language.getName())
                    .append(": ")
                    .append(count)
                    .append("\n");
        }
        for (Language language : Language.getLanguages()) {
            int count = subscribedUsersCount.get(language.getName());
            adminCommand.append("Количество пользователей с подпиской ")
                    .append(language.getName())
                    .append(": ")
                    .append(count)
                    .append("\n");
        }
        adminCommand.append("Количество активных пользователей: ")
                .append(countActiveUsers)
                .append("\n")
                .append("Количество неактивных пользователей: ")
                .append(countNonActiveUsers);
        return adminCommand.toString();
    }
}
