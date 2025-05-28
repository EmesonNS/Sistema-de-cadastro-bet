# 📌 Sistema de Cadastro Betão.Bet

Sistema JavaFX com persistência via Hibernate no banco de dados mySql para cadastro hierárquico de perfis de uma casa de apostas.

## 🚀 Funcionalidades

- ✔️ Cadastro hierárquico: Supervisor → Gerente → Cambista
- ✔️ CRUD completo com persistência em banco de dados
- ✔️ Edição inline com validação em tempo real
- ✔️ Filtros avançados com seleção dinâmica de campos
- ✔️ Campos obrigatórios destacados em vermelho quando não preenchidos
- ✔️ Limpeza automática dos campos após cadastro bem-sucedido
- ✔️ Fechamento automático do EntityManager ao sair da aplicação

## 🛠️ Tecnologias & Arquitetura

### Backend

- Java 17+
- Hibernate / JPA
- Padrão DAO genérico com Singleton
- Conversores customizados para enums
- Gerenciamento automático de transações

### Frontend

- JavaFX (TableView editável, ComboBox dinâmico)
- CSS customizado para estilização
- Janelas modais para formulários

### Banco de Dados

- MySQL

## 🖥️ Estrutura do Projeto

```bash
src/
├── main/
│   ├── java/org/example/projetoBet/
│   │   ├── Infra/                  # Camada de infraestrutura
│   │   │   ├── DAO.java            # Classe DAO genérica
│   │   │   ├── Situacao.java       # Enum de situação
│   │   │   ├── ConverterSituacao.java       # Conversor Enum
│   │   ├── layout/                 # Telas JavaFX
│   │   │   ├── LayoutPrincipal.java  # Tela principal
│   │   │   ├── TelaAddFactory.java   # Criação dinâmica de formulários
│   │   │   ├── TelaPesquisar.java    # Tela de filtro avançado
│   │   ├── modelo/                 # Entidades JPA
│   │   │   ├── Supervisor.java 
│   │   │   ├── Gerente.java
│   │   │   ├── Cambista.java
│   ├── resources/
│   │   ├── styles
│   │   │   ├── styles.css         # Estilização da interface
│   │   ├── META-INF
│   │   │   ├── persistence.xml    # Configuração do JPA
```

## 🏁 Como Executar

Pré-requisitos:
- JDK 17+
- Maven
- MySQL rodando localmente

Clone e execute:

```bash
git clone https://github.com/seu-usuario/projetoBet.git
cd projetoBet
mvn clean javafx:run
```

## 🎨 Demonstração


## 🖥️ Interface Principal

![image](https://imgur.com/yVjrVxM)


---

## 📝 Formulários de Cadastro

<p align="center">
  <img src="https://imgur.com/a/jRqa5PK" width="250"/>
  <img src="![image](https://github.com/user-attachments/assets/289e2fb4-b09b-46e2-9897-f87fa077b60a)" width="250"/>
  <img src="![image](https://github.com/user-attachments/assets/8320eaa6-73bd-4459-bb2e-894bb974baf4)" width="250"/>
</p>

---

## 🔍 Filtros Avançados

<p align="center">
  <img src="![image](https://github.com/user-attachments/assets/f058c507-eaee-40bc-97ac-2711fea1076b)" width="250"/>
  <img src="![image](https://github.com/user-attachments/assets/83aa1b38-9479-4972-8b33-6c439470f8db)" width="250"/>
  <img src="![image](https://github.com/user-attachments/assets/ade10e40-1ecc-48b6-aa00-df5710d5822b)" width="250"/>
</p>


## 🤝 Padrões de Projeto Utilizados

- Singleton (DAO)
- Factory Method (TelaAddFactory)
- Observer (JavaFX Listeners)
- Layer Supertype (DAO genérico)

## 📞 Contato

- ✉️ Email: emesonneves111@gmail.com
- 🔗 LinkedIn: linkedin.com/in/emeson-santos
