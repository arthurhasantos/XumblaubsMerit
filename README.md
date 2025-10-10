# XumblaubsMerit

Integrantes:
- Arthur Henrique Araújo Santos 
- Lucas Jácome Magalhães de Jesus
- Victor Ferreira de Almeida

# 🧩 Histórias de Usuário — Sistema XumblaubsMerit

## 👨‍🎓 Aluno

### US01 – Cadastro de Aluno
**Como** aluno interessado em participar do sistema,  
**quero** realizar meu cadastro informando meus dados pessoais e acadêmicos,  
**para** poder acessar o sistema e começar a receber moedas.  

**Critérios de Aceitação:**  
- Deve ser possível inserir: nome, e-mail, CPF, RG, endereço, instituição e curso.  
- A instituição deve ser selecionada de uma lista pré-cadastrada.  
- O sistema deve validar CPF e e-mail únicos.  
- Após cadastro, o aluno deve receber e-mail de confirmação.  

---

### US02 – Consulta de Extrato
**Como** aluno,  
**quero** visualizar meu extrato de transações,  
**para** acompanhar as moedas recebidas e as trocas realizadas.  

**Critérios de Aceitação:**  
- O extrato deve exibir: data, tipo de operação (recebimento/troca), valor e descrição.  
- Deve mostrar o saldo total atual.  
- Deve permitir filtrar por período.  

---

### US03 – Troca de Moedas por Vantagens
**Como** aluno,  
**quero** trocar minhas moedas por vantagens oferecidas por parceiros,  
**para** obter descontos ou produtos.  

**Critérios de Aceitação:**  
- Deve listar todas as vantagens disponíveis com descrição, imagem e custo.  
- O sistema deve verificar se o aluno tem saldo suficiente.  
- Ao confirmar, o saldo é descontado e um código de cupom é gerado.  
- O aluno e o parceiro devem receber um e-mail com o código da troca.  

---

## 👨‍🏫 Professor

### US04 – Distribuição de Moedas
**Como** professor,  
**quero** enviar moedas a meus alunos como reconhecimento,  
**para** premiar bom comportamento e desempenho acadêmico.  

**Critérios de Aceitação:**  
- O professor deve possuir saldo suficiente.  
- Deve indicar o aluno e a quantidade de moedas.  
- Uma mensagem obrigatória de reconhecimento deve ser informada.  
- O aluno deve ser notificado por e-mail.  
- O saldo do professor deve ser atualizado automaticamente.  

---

### US05 – Consulta de Extrato
**Como** professor,  
**quero** visualizar meu extrato de transações,  
**para** acompanhar o envio de moedas e meu saldo atual.  

**Critérios de Aceitação:**  
- O extrato deve exibir: data, aluno destinatário, quantidade e mensagem.  
- O saldo acumulado deve ser mostrado.  
- Deve permitir filtragem por semestre.  

---

## 🏢 Empresa Parceira

### US06 – Cadastro de Empresa Parceira
**Como** empresa interessada em oferecer vantagens,  
**quero** cadastrar meus dados e produtos,  
**para** disponibilizar recompensas aos alunos.  

**Critérios de Aceitação:**  
- A empresa deve informar nome, e-mail, CNPJ e descrição.  
- Deve cadastrar as vantagens com nome, descrição, imagem e custo em moedas.  
- O sistema deve validar o CNPJ e o e-mail.  
- O cadastro deve passar por aprovação do administrador.  

---

### US07 – Receber Notificação de Resgate
**Como** empresa parceira,  
**quero** receber e-mails com os códigos das trocas realizadas,  
**para** validar os cupons apresentados pelos alunos.  

**Critérios de Aceitação:**  
- Cada troca deve gerar um código único.  
- O e-mail deve conter o nome do aluno, data e valor da troca.  
- Deve haver um link para confirmar a utilização do cupom.  

---

## 🔐 Autenticação

### US08 – Login e Autenticação
**Como** usuário (aluno, professor ou parceiro),  
**quero** acessar o sistema com login e senha,  
**para** usar as funcionalidades correspondentes ao meu perfil.  

**Critérios de Aceitação:**  
- Deve haver autenticação segura com criptografia de senha.  
- Login deve ser validado por e-mail e senha.  
- O sistema deve distinguir o tipo de usuário (aluno, professor, parceiro).  
- Acesso a rotas e funcionalidades deve ser restrito conforme o perfil.  

---

### US09 – Recuperação de Senha
**Como** usuário,  
**quero** redefinir minha senha,  
**para** recuperar o acesso em caso de esquecimento.  

**Critérios de Aceitação:**  
- Deve ser possível solicitar recuperação via e-mail.  
- O sistema deve enviar um link temporário e seguro.  
- A nova senha deve atender a regras mínimas de segurança.  

---

## ⚙️ Administração

### US10 – Gerenciar Instituições e Parcerias
**Como** administrador do sistema,  
**quero** cadastrar e gerenciar instituições e parceiros,  
**para** manter o controle e integridade das informações.  

**Critérios de Aceitação:**  
- Deve permitir aprovar ou rejeitar novos cadastros.  
- Deve exibir listas de instituições e empresas ativas.  
- Deve permitir edição ou remoção de registros.  

---
