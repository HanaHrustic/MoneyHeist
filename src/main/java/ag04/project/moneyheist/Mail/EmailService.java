package ag04.project.moneyheist.Mail;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
