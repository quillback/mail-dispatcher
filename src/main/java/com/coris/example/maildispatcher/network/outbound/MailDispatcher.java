package com.coris.example.maildispatcher.network.outbound;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Component
public class MailDispatcher {
    private final JavaMailSender mailSender;

    @Autowired
    public MailDispatcher(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Mono<Void> dispatchMail(final MailPart mailPart){
        try {
            final var mimeMessage = this.bindData(mailSender.createMimeMessage(), mailPart);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            return Mono.error(e);
        }
        return Mono.empty();
    }


    private MimeMessage bindData(@NotNull MimeMessage orig,
                                 @NotNull MailPart part) throws MessagingException {
        orig.setFrom(part.getFrom());
        if (notEmpty(part.getTo()))
            this.addEmail(orig, Message.RecipientType.TO, part.getTo());

        if (notEmpty(part.getCc()))
            this.addEmail(orig, Message.RecipientType.CC, part.getCc());

        if (notEmpty(part.getBcc()))
            this.addEmail(orig, Message.RecipientType.BCC, part.getBcc());

        if (Strings.isNotEmpty(part.getSubject()))
            orig.setSubject(part.getSubject());

        if (Strings.isNotEmpty(part.getBody()))
            orig.setText(part.getBody());

        if (part.hasAttachment()) {
            final var helper = new MimeMessageHelper(orig, true);

            final var map = part.getPathWithMimeType();
            for (Map.Entry<Path, String> entry : map.entrySet()) {
                final var key = entry.getKey();
                helper.addAttachment(key.getFileName().toString(), key.toFile());
            }
        }

        return orig;
    }

    private boolean notEmpty(String[] arr){
        return Objects.nonNull(arr) && arr.length > 0;
    }

    private void addEmail(@NotNull MimeMessage orig, Message.RecipientType type, String[] email) throws MessagingException {
        for (String c : email) orig.addRecipients(type, c);
    }

}
