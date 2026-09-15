package com.estudo.notificacao.business;

import com.estudo.notificacao.business.dto.TarefasDTO;
import com.estudo.notificacao.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    private String remetente;

    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;

    public void enviaEmail(TarefasDTO tarefasDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelperhelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            mimeMessageHelperhelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            mimeMessageHelperhelper.setTo(InternetAddress.parse(tarefasDTO.getEmailUsuario()));
            mimeMessageHelperhelper.setSubject("Notificação de Tarefa: " + tarefasDTO.getNomeTarefa());

            Context context = new Context();
            context.setVariable("nomeTarefa", tarefasDTO.getNomeTarefa());
            context.setVariable("descricao", tarefasDTO.getDescricao());
            context.setVariable("dataEvento", tarefasDTO.getDataEvento());
            String htmlContent = templateEngine.process("notificacao", context);
            mimeMessageHelperhelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

        }catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar email", e.getCause());
        }
    }

}
