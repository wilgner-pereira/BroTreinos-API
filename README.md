BroTreinos API

API RESTful em Spring Boot para gerenciamento de treinos, sessões e exercícios com autenticação JWT.

🚀 Tecnologias Utilizadas

Java 8+

Spring Boot

Spring Security + JWT

Spring Data JPA / Hibernate

Maven

Banco de dados relacional MySQL

Bean Validation

Testes automatizados: JUnit e Mockito

Git e GitHub

📌 Funcionalidades

Cadastro e autenticação de usuários com JWT

Criação e gerenciamento de planos de treino

Registro de sessões de treino

Gerenciamento de exercícios

Estrutura de treino com dias e exercícios

Endpoints seguros por usuário autenticado

🗂 Estrutura do Projeto
```
brotreinos/
├── src/main/java/...   # Código-fonte
├── src/main/resources/ # Configurações e application.properties
├── src/test/java/...   # Testes automatizados
├── pom.xml             # Dependências Maven
└── .gitignore
```

⚡ Como Rodar Localmente

Clone o repositório:

git clone [https://github.com/seu-usuario/brotreinos-api.git](https://github.com/wilgner-pereira/BroTreinos-API.git)

cd brotreinos


Configure o banco de dados em application.properties

Rode a aplicação:

mvn spring-boot:run


Acesse a API em http://localhost:8080

🛠 Endpoints Principais

POST /auth/token – Autenticação

POST /register – Criar usuário

GET /trainings – Listar planos de treino

POST /trainings – Criar plano de treino

POST /workout – Registrar sessão de treino

GET /exercicios – Listar exercícios

POST /exercicios – Criar exercício

(Endpoints adicionais podem ser consultados no código ou via documentação Swagger)

📄 Licença

Projeto de portfólio pessoal, uso livre para estudo e demonstração.
