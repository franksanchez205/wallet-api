package com.billetera.intercom;

import com.billetera.config.MailgunConfig;
import com.billetera.dto.EmailResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mailgun-client", url = "${mailgun.base-url}",
        configuration = MailgunConfig.class)
/**
 * Cliente Feign declarativo para la API REST de Mailgun.
 */
public interface MailgunIntercom {

    /**
     * Envia un correo electronico a traves de la API de Mailgun.
     *
     * @param form mapa de campos del formulario (from, to, subject, text)
     * @return respuesta de Mailgun con id y mensaje
     */
    @PostMapping(value = "/${mailgun.domain}/messages",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    EmailResponseDTO sendEmail(@RequestBody MultiValueMap<String, String> form);
}
