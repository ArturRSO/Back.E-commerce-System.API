package ecommerce.system.api.tools;

import ecommerce.system.api.models.SimpleMailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class PasswordRecoverHandler {

    @Value("${password.recover.token.expiration.hours}")
    private int expirationHours;

    @Value("${application.front.customer.url}")
    private String baseUrl;

    @Value("${password.recover.token.key}")
    private String tokenKey;

    private final AESCodec aesCodec;
    private final EmailSender emailSender;

    @Autowired
    public PasswordRecoverHandler(AESCodec aesCodec, EmailSender emailSender) {
        this.aesCodec = aesCodec;
        this.emailSender = emailSender;
    }

    private String createBase(int userId) {

        return LocalDateTime.now().plusHours(expirationHours).toString() + "|" + userId;
    }

    public String generateLink(int userId) throws Exception {

        String base = this.createBase(userId);
        String encryptedParameter = this.aesCodec.encryptText(base, this.tokenKey);

        return baseUrl + "/recover/password/" + encryptedParameter;
    }

    public int extractId(String token) throws Exception {

        String decryptedToken = this.aesCodec.decryptText(token, this.tokenKey);

        String[] splitedToken = decryptedToken.split(Pattern.quote("|"), 2);

        int id = Integer.parseInt(splitedToken[1]);

        return id;
    }

    public LocalDateTime extractExpirationDate(String token)
            throws NoSuchPaddingException,
            UnsupportedEncodingException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {

        String decryptedToken = this.aesCodec.decryptText(token, this.tokenKey);

        String[] splitedToken = decryptedToken.split(Pattern.quote("|"), 0);

        LocalDateTime expirationDate = LocalDateTime.parse(splitedToken[0]);

        return expirationDate;
    }

    public boolean validateToken(String token) throws Exception {

        LocalDateTime expirationDate = this.extractExpirationDate(token);

        return LocalDateTime.now().isBefore(expirationDate);
    }

    public SimpleMailModel sendEmail(int userId, String userEmail) throws Exception {

        String link = this.generateLink(userId);

        String emailTemplate = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"https://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"https://www.w3.org/1999/xhtml\"><head><title>Recuperação de senha</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"viewport\" content=\"width=device-width\" /><style type=\"text/css\">@media screen{@font-face{font-family:'Source Sans Pro';font-style:normal;font-weight:400;src:local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff')}@font-face{font-family:'Source Sans Pro';font-style:normal;font-weight:700;src:local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff')}}body,table,td,a{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}table,td{mso-table-rspace:0pt;mso-table-lspace:0pt}img{-ms-interpolation-mode:bicubic}a[x-apple-data-detectors]{font-family:inherit !important;font-size:inherit !important;font-weight:inherit !important;line-height:inherit !important;color:inherit !important;text-decoration:none !important}div[style*=\"margin: 16px 0;\"]{margin:0 !important}body{width:100% !important;height:100% !important;padding:0 !important;margin:0 !important}table{border-collapse:collapse !important}a{color:#1a82e2}img{height:auto;line-height:100%;text-decoration:none;border:0;outline:none}p{margin:0}</style></head><body style=\"background-color: #e9ecef;\"><div style=\"display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;\"> Esqueceu sua senha? Não se preocupe, te ajudamos com isso!</div><table class=\"body\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" data-made-with-foundation><tbody><tr><td class=\"float-center\" align=\"center\" valign=\"top\"><center><table class=\"container\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr align=\"center\" bgcolor=\"#e9ecef\"><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\"><tr><td align=\"center\" valign=\"top\" style=\"padding: 36px 24px;\"></td></tr></table></td></tr><tr><td align=\"center\" bgcolor=\"#e9ecef\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\"><tr><td style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf; background-color: #ffffff;\"><h1 style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\"> Recuperação de senha</h1></td></tr></table></td></tr><tr><td align=\"center\" bgcolor=\"#e9ecef\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\"><tr><td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\"><p> Pressione o botão abaixo para cadastrar uma nova senha.</p></td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 12px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td align=\"center\" bgcolor=\"#1a82e2\" style=\"border-radius: 6px;\"> <a href=\"[[link]]\" target=\"_blank\" style=\"display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 6px;\"> Cadastrar nova senha </a></td></tr></table></td></tr></table></td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;font-size: 16px;line-height: 24px;border-bottom: 3px solid #d4dadf;\"><p> Conte conosco, <br> Seus amigos do E-commerce System</p></td></tr></table></td></tr><tr><td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 24px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\"><tr><td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\"><p> Você recebeu esse e-mail porque recebemos uma solitação de recuperação de senha para a sua conta. <br> Se não foi você, por favor, ignore este e-mail ou entre em contato conosco para mais detalhes.</p></td></tr><tr><td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\"><p> Avenida Paulista, 123, São Paulo - SP <br> contato@ecommercesystem.com</p></td></tr></table></td></tr></tbody></table></center></td></tr></tbody></table></body></html>";

        emailTemplate = emailTemplate.replace("[[link]]", link);

        SimpleMailModel mail = new SimpleMailModel(userEmail, "Recuperação de senha", emailTemplate);

        this.emailSender.sendMimeEmail(mail);

        return mail;
    }
}
