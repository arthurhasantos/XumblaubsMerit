package com.merito.service;

import com.merito.entity.Aluno;
import com.merito.entity.EmpresaParceira;
import com.merito.entity.Professor;
import com.merito.entity.Vantagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@xumblaubsmerit.com}")
    private String fromEmail;
    
    @Value("${app.name:XumblaubsMerit}")
    private String appName;
    
    /**
     * Envia e-mail para o aluno quando ele resgata uma vantagem
     */
    public void enviarEmailCupomAluno(Aluno aluno, Vantagem vantagem, String codigoCupom) {
        String emailAluno = aluno.getEmail();
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📧 [EmailService] Iniciando envio de e-mail para aluno");
        System.out.println("   Destinatário: " + emailAluno);
        System.out.println("   From: " + fromEmail);
        System.out.println("   Host: smtp.gmail.com");
        System.out.println("═══════════════════════════════════════════════════════");
        
        if (emailAluno == null || emailAluno.trim().isEmpty()) {
            System.err.println("❌ E-mail do aluno não disponível para envio");
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String from = (fromEmail != null && !fromEmail.trim().isEmpty()) ? fromEmail : "noreply@xumblaubsmerit.com";
            String app = (appName != null && !appName.trim().isEmpty()) ? appName : "XumblaubsMerit";
            
            System.out.println("   Configurando mensagem...");
            helper.setFrom(from != null ? from : "noreply@xumblaubsmerit.com", 
                          app != null ? app : "XumblaubsMerit");
            helper.setTo(emailAluno);
            helper.setSubject("🎉 Cupom de Vantagem Resgatado - " + (appName != null ? appName : "XumblaubsMerit"));
            
            String htmlContent = gerarTemplateEmailCupomAluno(
                aluno.getNome() != null ? aluno.getNome() : "Aluno",
                vantagem.getNome() != null ? vantagem.getNome() : "Vantagem",
                vantagem.getDescricao(),
                codigoCupom != null ? codigoCupom : "",
                vantagem.getCustoEmMoedas() != null ? vantagem.getCustoEmMoedas() : 0.0
            );
            
            if (htmlContent != null) {
                helper.setText(htmlContent, true);
            }
            
            System.out.println("   Enviando e-mail via SMTP...");
            mailSender.send(message);
            System.out.println("✅ E-mail enviado com SUCESSO para: " + emailAluno);
            System.out.println("═══════════════════════════════════════════════════════");
        } catch (Exception e) {
            // Log do erro, mas não interrompe o fluxo
            System.err.println("═══════════════════════════════════════════════════════");
            System.err.println("❌ ERRO ao enviar e-mail para aluno: " + emailAluno);
            System.err.println("   Tipo de erro: " + e.getClass().getSimpleName());
            System.err.println("   Mensagem: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Causa: " + e.getCause().getMessage());
            }
            System.err.println("═══════════════════════════════════════════════════════");
            e.printStackTrace();
        }
    }
    
    /**
     * Envia e-mail para a empresa parceira quando um aluno resgata uma vantagem
     */
    public void enviarEmailCupomEmpresa(EmpresaParceira empresa, Aluno aluno, 
                                        Vantagem vantagem, String codigoCupom) {
        String emailEmpresa = empresa.getEmailContato();
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📧 [EmailService] Iniciando envio de e-mail para EMPRESA");
        System.out.println("   Destinatário: " + emailEmpresa);
        System.out.println("   Empresa: " + empresa.getNome());
        System.out.println("   From: " + fromEmail);
        System.out.println("   Host: smtp.gmail.com");
        System.out.println("═══════════════════════════════════════════════════════");
        
        if (emailEmpresa == null || emailEmpresa.trim().isEmpty()) {
            System.err.println("❌ E-mail de contato da empresa não disponível para envio");
            System.err.println("   Empresa: " + empresa.getNome());
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String from = (fromEmail != null && !fromEmail.trim().isEmpty()) ? fromEmail : "noreply@xumblaubsmerit.com";
            String app = (appName != null && !appName.trim().isEmpty()) ? appName : "XumblaubsMerit";
            
            System.out.println("   Configurando mensagem...");
            helper.setFrom(from != null ? from : "noreply@xumblaubsmerit.com", 
                          app != null ? app : "XumblaubsMerit");
            helper.setTo(emailEmpresa);
            helper.setSubject("📋 Novo Resgate de Vantagem - " + (appName != null ? appName : "XumblaubsMerit"));
            
            String htmlContent = gerarTemplateEmailCupomEmpresa(
                empresa.getNome() != null ? empresa.getNome() : "Empresa",
                aluno.getNome() != null ? aluno.getNome() : "Aluno",
                aluno.getEmail() != null ? aluno.getEmail() : "",
                vantagem.getNome() != null ? vantagem.getNome() : "Vantagem",
                codigoCupom != null ? codigoCupom : "",
                vantagem.getCustoEmMoedas() != null ? vantagem.getCustoEmMoedas() : 0.0
            );
            
            if (htmlContent != null) {
                helper.setText(htmlContent, true);
            }
            
            System.out.println("   Enviando e-mail via SMTP...");
            mailSender.send(message);
            System.out.println("✅ E-mail enviado com SUCESSO para empresa: " + emailEmpresa);
            System.out.println("═══════════════════════════════════════════════════════");
        } catch (Exception e) {
            // Log do erro, mas não interrompe o fluxo
            System.err.println("═══════════════════════════════════════════════════════");
            System.err.println("❌ ERRO ao enviar e-mail para empresa: " + emailEmpresa);
            System.err.println("   Tipo de erro: " + e.getClass().getSimpleName());
            System.err.println("   Mensagem: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Causa: " + e.getCause().getMessage());
            }
            System.err.println("═══════════════════════════════════════════════════════");
            e.printStackTrace();
        }
    }
    
    /**
     * Envia e-mail para o aluno quando recebe moedas de um professor
     */
    public void enviarEmailMoedasRecebidas(Aluno aluno, Professor professor, 
                                           Double quantidade, String motivo) {
        String emailAluno = aluno.getEmail();
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📧 [EmailService] Iniciando envio de e-mail de MOEDAS RECEBIDAS");
        System.out.println("   Destinatário: " + emailAluno);
        System.out.println("   Aluno: " + aluno.getNome());
        System.out.println("   Professor: " + professor.getNome());
        System.out.println("   Quantidade: " + quantidade + " moedas");
        System.out.println("   From: " + fromEmail);
        System.out.println("   Host: smtp.gmail.com");
        System.out.println("═══════════════════════════════════════════════════════");
        
        if (emailAluno == null || emailAluno.trim().isEmpty()) {
            System.err.println("❌ E-mail do aluno não disponível para envio");
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String from = (fromEmail != null && !fromEmail.trim().isEmpty()) ? fromEmail : "noreply@xumblaubsmerit.com";
            String app = (appName != null && !appName.trim().isEmpty()) ? appName : "XumblaubsMerit";
            
            System.out.println("   Configurando mensagem...");
            helper.setFrom(from != null ? from : "noreply@xumblaubsmerit.com", 
                          app != null ? app : "XumblaubsMerit");
            helper.setTo(emailAluno);
            helper.setSubject("💰 Você recebeu " + quantidade + " moedas! - " + (appName != null ? appName : "XumblaubsMerit"));
            
            String htmlContent = gerarTemplateEmailMoedasRecebidas(
                aluno.getNome() != null ? aluno.getNome() : "Aluno",
                professor.getNome() != null ? professor.getNome() : "Professor",
                quantidade != null ? quantidade : 0.0,
                motivo != null ? motivo : "",
                aluno.getSaldoMoedas() != null ? aluno.getSaldoMoedas() : 0.0
            );
            
            if (htmlContent != null) {
                helper.setText(htmlContent, true);
            }
            
            System.out.println("   Enviando e-mail via SMTP...");
            mailSender.send(message);
            System.out.println("✅ E-mail de moedas enviado com SUCESSO para: " + emailAluno);
            System.out.println("═══════════════════════════════════════════════════════");
        } catch (Exception e) {
            // Log do erro, mas não interrompe o fluxo
            System.err.println("═══════════════════════════════════════════════════════");
            System.err.println("❌ ERRO ao enviar e-mail de moedas recebidas para: " + emailAluno);
            System.err.println("   Tipo de erro: " + e.getClass().getSimpleName());
            System.err.println("   Mensagem: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Causa: " + e.getCause().getMessage());
            }
            System.err.println("═══════════════════════════════════════════════════════");
            e.printStackTrace();
        }
    }
    
    /**
     * Gera template HTML para e-mail de cupom ao aluno
     */
    private String gerarTemplateEmailCupomAluno(String nomeAluno, String nomeVantagem, 
                                                String descricaoVantagem, 
                                                String codigoCupom, Double custoMoedas) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".cupom-box { background: white; border: 3px dashed #667eea; padding: 20px; text-align: center; margin: 20px 0; border-radius: 10px; }" +
                ".cupom-code { font-size: 28px; font-weight: bold; color: #667eea; letter-spacing: 3px; margin: 10px 0; }" +
                ".info-box { background: white; padding: 15px; margin: 15px 0; border-radius: 5px; border-left: 4px solid #667eea; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                ".button { display: inline-block; padding: 12px 30px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 Parabéns, " + nomeAluno + "!</h1>" +
                "<p>Você resgatou uma vantagem com sucesso!</p>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Detalhes do Resgate</h2>" +
                "<div class='info-box'>" +
                "<p><strong>Vantagem:</strong> " + nomeVantagem + "</p>" +
                "<p><strong>Descrição:</strong> " + (descricaoVantagem != null ? descricaoVantagem : "Sem descrição") + "</p>" +
                "<p><strong>Custo:</strong> " + custoMoedas + " moedas</p>" +
                "</div>" +
                "<div class='cupom-box'>" +
                "<h3>Seu Código de Cupom</h3>" +
                "<div class='cupom-code'>" + codigoCupom + "</div>" +
                "<p>Apresente este código na empresa parceira para resgatar sua vantagem!</p>" +
                "</div>" +
                "<p><strong>⚠️ Importante:</strong> Guarde este e-mail com cuidado. Você precisará do código do cupom para resgatar sua vantagem na empresa parceira.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>" + appName + " - Sistema de Mérito Acadêmico</p>" +
                "<p>Este é um e-mail automático, por favor não responda.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Gera template HTML para e-mail de cupom à empresa
     */
    private String gerarTemplateEmailCupomEmpresa(String nomeEmpresa, String nomeAluno, 
                                                  String emailAluno, String nomeVantagem,
                                                  String codigoCupom, Double custoMoedas) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".cupom-box { background: white; border: 3px solid #f5576c; padding: 20px; text-align: center; margin: 20px 0; border-radius: 10px; }" +
                ".cupom-code { font-size: 32px; font-weight: bold; color: #f5576c; letter-spacing: 3px; margin: 10px 0; }" +
                ".info-box { background: white; padding: 15px; margin: 15px 0; border-radius: 5px; border-left: 4px solid #f5576c; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "table { width: 100%; border-collapse: collapse; margin: 15px 0; }" +
                "td { padding: 8px; border-bottom: 1px solid #eee; }" +
                "td:first-child { font-weight: bold; width: 40%; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>📋 Novo Resgate de Vantagem</h1>" +
                "<p>Um aluno resgatou uma de suas vantagens!</p>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Olá, " + nomeEmpresa + "!</h2>" +
                "<p>Um aluno resgatou uma vantagem oferecida por sua empresa. Confira os detalhes abaixo:</p>" +
                "<div class='info-box'>" +
                "<table>" +
                "<tr><td>Aluno:</td><td>" + nomeAluno + "</td></tr>" +
                "<tr><td>E-mail do Aluno:</td><td>" + emailAluno + "</td></tr>" +
                "<tr><td>Vantagem:</td><td>" + nomeVantagem + "</td></tr>" +
                "<tr><td>Valor em Moedas:</td><td>" + custoMoedas + " moedas</td></tr>" +
                "</table>" +
                "</div>" +
                "<div class='cupom-box'>" +
                "<h3>Código do Cupom</h3>" +
                "<div class='cupom-code'>" + codigoCupom + "</div>" +
                "<p>Este é o código que o aluno apresentará para resgatar a vantagem.</p>" +
                "</div>" +
                "<p><strong>📌 Próximos Passos:</strong></p>" +
                "<ul>" +
                "<li>Quando o aluno apresentar este código, valide-o no sistema</li>" +
                "<li>Confirme o resgate da vantagem</li>" +
                "<li>Entregue o produto/serviço conforme combinado</li>" +
                "</ul>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>" + appName + " - Sistema de Mérito Acadêmico</p>" +
                "<p>Este é um e-mail automático, por favor não responda.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Envia e-mail de confirmação para o professor quando ele envia moedas
     */
    public void enviarEmailConfirmacaoEnvioMoedas(Professor professor, Aluno aluno, 
                                                  Double quantidade, String motivo,
                                                  Double saldoAtualProfessor) {
        String emailProfessor = professor.getEmail();
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📧 [EmailService] Iniciando envio de e-mail de CONFIRMAÇÃO ao PROFESSOR");
        System.out.println("   Destinatário: " + emailProfessor);
        System.out.println("   Professor: " + professor.getNome());
        System.out.println("   Aluno: " + aluno.getNome());
        System.out.println("   Quantidade: " + quantidade + " moedas");
        System.out.println("   From: " + fromEmail);
        System.out.println("   Host: smtp.gmail.com");
        System.out.println("═══════════════════════════════════════════════════════");
        
        if (emailProfessor == null || emailProfessor.trim().isEmpty()) {
            System.err.println("❌ E-mail do professor não disponível para envio");
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String from = (fromEmail != null && !fromEmail.trim().isEmpty()) ? fromEmail : "noreply@xumblaubsmerit.com";
            String app = (appName != null && !appName.trim().isEmpty()) ? appName : "XumblaubsMerit";
            
            System.out.println("   Configurando mensagem...");
            helper.setFrom(from != null ? from : "noreply@xumblaubsmerit.com", 
                          app != null ? app : "XumblaubsMerit");
            helper.setTo(emailProfessor);
            helper.setSubject("✅ Moedas enviadas com sucesso! - " + (appName != null ? appName : "XumblaubsMerit"));
            
            String htmlContent = gerarTemplateEmailConfirmacaoEnvioMoedas(
                professor.getNome() != null ? professor.getNome() : "Professor",
                aluno.getNome() != null ? aluno.getNome() : "Aluno",
                aluno.getEmail() != null ? aluno.getEmail() : "",
                quantidade != null ? quantidade : 0.0,
                motivo != null ? motivo : "",
                saldoAtualProfessor != null ? saldoAtualProfessor : 0.0
            );
            
            if (htmlContent != null) {
                helper.setText(htmlContent, true);
            }
            
            System.out.println("   Enviando e-mail via SMTP...");
            mailSender.send(message);
            System.out.println("✅ E-mail de confirmação enviado com SUCESSO para professor: " + emailProfessor);
            System.out.println("═══════════════════════════════════════════════════════");
        } catch (Exception e) {
            // Log do erro, mas não interrompe o fluxo
            System.err.println("═══════════════════════════════════════════════════════");
            System.err.println("❌ ERRO ao enviar e-mail de confirmação para professor: " + emailProfessor);
            System.err.println("   Tipo de erro: " + e.getClass().getSimpleName());
            System.err.println("   Mensagem: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Causa: " + e.getCause().getMessage());
            }
            System.err.println("═══════════════════════════════════════════════════════");
            e.printStackTrace();
        }
    }
    
    /**
     * Gera template HTML para e-mail de moedas recebidas
     */
    private String gerarTemplateEmailMoedasRecebidas(String nomeAluno, String nomeProfessor,
                                                      Double quantidade, String motivo,
                                                      Double saldoAtual) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".moedas-box { background: white; border: 3px solid #4facfe; padding: 30px; text-align: center; margin: 20px 0; border-radius: 10px; }" +
                ".moedas-amount { font-size: 48px; font-weight: bold; color: #4facfe; margin: 10px 0; }" +
                ".info-box { background: white; padding: 15px; margin: 15px 0; border-radius: 5px; border-left: 4px solid #4facfe; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>💰 Você recebeu moedas!</h1>" +
                "<p>Parabéns pelo seu reconhecimento!</p>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Olá, " + nomeAluno + "!</h2>" +
                "<p>Você recebeu uma transferência de moedas do professor <strong>" + nomeProfessor + "</strong>.</p>" +
                "<div class='moedas-box'>" +
                "<h3>Moedas Recebidas</h3>" +
                "<div class='moedas-amount'>+" + quantidade + "</div>" +
                "<p>moedas</p>" +
                "</div>" +
                "<div class='info-box'>" +
                "<p><strong>Mensagem do Professor:</strong></p>" +
                "<p style='font-style: italic; color: #555;'>" + motivo + "</p>" +
                "</div>" +
                "<div class='info-box'>" +
                "<p><strong>Seu saldo atual:</strong> " + saldoAtual + " moedas</p>" +
                "</div>" +
                "<p>Continue se esforçando! Você pode usar essas moedas para resgatar vantagens incríveis!</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>" + appName + " - Sistema de Mérito Acadêmico</p>" +
                "<p>Este é um e-mail automático, por favor não responda.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Gera template HTML para e-mail de confirmação de envio de moedas ao professor
     */
    private String gerarTemplateEmailConfirmacaoEnvioMoedas(String nomeProfessor, String nomeAluno,
                                                            String emailAluno, Double quantidade,
                                                            String motivo, Double saldoAtualProfessor) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".success-box { background: white; border: 3px solid #38ef7d; padding: 30px; text-align: center; margin: 20px 0; border-radius: 10px; }" +
                ".moedas-amount { font-size: 48px; font-weight: bold; color: #11998e; margin: 10px 0; }" +
                ".info-box { background: white; padding: 15px; margin: 15px 0; border-radius: 5px; border-left: 4px solid #11998e; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "table { width: 100%; border-collapse: collapse; margin: 15px 0; }" +
                "td { padding: 8px; border-bottom: 1px solid #eee; }" +
                "td:first-child { font-weight: bold; width: 40%; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>✅ Transferência Confirmada!</h1>" +
                "<p>Suas moedas foram enviadas com sucesso!</p>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Olá, " + nomeProfessor + "!</h2>" +
                "<p>Confirmamos que você enviou moedas para um aluno. Confira os detalhes abaixo:</p>" +
                "<div class='success-box'>" +
                "<h3>Transferência Realizada</h3>" +
                "<div class='moedas-amount'>-" + quantidade + "</div>" +
                "<p>moedas enviadas</p>" +
                "</div>" +
                "<div class='info-box'>" +
                "<table>" +
                "<tr><td>Aluno:</td><td>" + nomeAluno + "</td></tr>" +
                "<tr><td>E-mail do Aluno:</td><td>" + emailAluno + "</td></tr>" +
                "<tr><td>Quantidade:</td><td>" + quantidade + " moedas</td></tr>" +
                "<tr><td>Mensagem:</td><td style='font-style: italic; color: #555;'>" + motivo + "</td></tr>" +
                "</table>" +
                "</div>" +
                "<div class='info-box'>" +
                "<p><strong>Seu saldo atual:</strong> " + saldoAtualProfessor + " moedas</p>" +
                "</div>" +
                "<p>O aluno foi notificado por e-mail sobre o recebimento das moedas.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>" + appName + " - Sistema de Mérito Acadêmico</p>" +
                "<p>Este é um e-mail automático, por favor não responda.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
