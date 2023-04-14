package telegramBot.services;

import telegramBot.entity.InitStatus;
import telegramBot.repositories.InitStatusRepo;
import telegramBot.repositories.InitStatusRepoImpl;

public class InitStatusService {

    private static final InitStatusRepo repo = new InitStatusRepoImpl();

    public static boolean init(){
       return repo.getStatus().isInit();
    }

    public static void setInit(){
        InitStatus status = repo.getStatus();
        status.setInit(true);
        repo.update(status);
    }
}
