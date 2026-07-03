package com.billetera.service;

import com.billetera.dto.EmailResponseDTO;
import com.billetera.intercom.MailgunIntercom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Log4j2
@Service
@RequiredArgsConstructor
/**
 * Servicio para el envio de correos electronicos via Mailgun.
 */
public class EmailService {

    private final MailgunIntercom mailgunIntercom;

    @Value("${mailgun.from}")
    private String from;

    /**
     * Envia un token de confirmacion al correo del cliente.
     *
     * @param to    direccion de correo del destinatario
     * @param token token numerico de 6 digitos
     * @throws RuntimeException si falla el envio del correo
     */
    public void enviarToken(String to, String token) {
        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("from", from);
            form.add("to", to);
            form.add("subject", "Token de confirmacion - Billetera");
            form.add("text", "Su token de confirmacion es: " + token);

            EmailResponseDTO response = mailgunIntercom.sendEmail(form);
            log.info("Email enviado a {}: id={}", to, response.getId());
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Error al enviar el token por email", e);
        }
    }
}
