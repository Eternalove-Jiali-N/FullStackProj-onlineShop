package onlineShop.log;

import org.springframework.stereotype.Component;

@Component
public class ServerLogger implements Logger{
    @Override
    public void log(String info) {
        System.out.println("Server log: " + info);
    }
}
