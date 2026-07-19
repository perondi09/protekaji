# Protekaji API

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

A **Protekaji API** é um sistema back-end desenvolvido em Java com Spring Boot para o gerenciamento e rastreamento de equipamentos de proteção contra incêndio. A aplicação permite cadastrar empresas de manutenção, gerenciar lotes de inspeção e controlar individualmente extintores de incêndio, suas validades e especificações.

## 🚀 Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 4.1.0** (Web, Data JPA, Validation)
*   **PostgreSQL** (Banco de dados relacional)
*   **Hibernate / JPA** (Mapeamento Objeto-Relacional)
*   **Lombok** (Redução de boilerplate)
*   **SpringDoc OpenAPI (Swagger)** (Documentação interativa da API)
*   **Padrão DTO** usando Java Records
*   **Logs** usando SL4J

## 🏗️ Arquitetura e Modelagem

O projeto segue a arquitetura em camadas (Controllers, Services, Repositories e DTOs) e adota boas práticas RESTful. 
Todas as chaves primárias do sistema utilizam **UUID** para maior segurança e escalabilidade.

### Relacionamentos do Banco de Dados
A regra de negócios obedece a seguinte hierarquia (Deleção em Cascata gerenciada via BD):
1.  **Company (Empresa)**: Entidade raiz, identificada unicamente por seu CNPJ.
2.  **Batch (Lote)**: Um lote de manutenção sempre pertence a uma Empresa.
3.  **Extinguisher (Extintor)**: Um extintor (identificado pelo número de série único) sempre pertence a um Lote.

## ⚙️ Pré-requisitos

Para rodar o projeto localmente, você precisará de:
*   [JDK 21+](https://jdk.java.net/) (ou equivalente configurado no seu ambiente)
*   [PostgreSQL](https://www.postgresql.org/) rodando na porta `5432`
*   Maven (ou utilizar a IDE de sua preferência, como IntelliJ IDEA)

## 🛠️ Como Executar o Projeto

**1. Clone o repositório:**
```bash
git clone [https://github.com/seu-usuario/protekaji.git](https://github.com/seu-usuario/protekaji.git)
cd protekaji

```

**2. Configure o Banco de Dados:**
No PostgreSQL, crie um banco de dados chamado `protekaji_db`.
Verifique se as credenciais no arquivo `src/main/resources/application.properties` estão corretas para o seu ambiente local:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/protekaji_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Na primeira execução, mantenha 'create' para gerar as tabelas. 
# Depois, mude para 'update' para não perder os dados salvos.
spring.jpa.hibernate.ddl-auto=update

```

**3. Rode a aplicação:**
Execute a classe principal `ProtekajiApplication.java` na sua IDE ou use o Maven:

```bash
./mvnw spring-boot:run

```

A API estará disponível em: `http://localhost:8080`

## 📖 Documentação da API (Swagger)

A API é totalmente documentada pelo Swagger. Com a aplicação rodando, você pode visualizar todos os endpoints, schemas de DTOs e testar as rotas diretamente pelo navegador.

🔗 **Acesse o Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🔗 Principais Endpoints

Abaixo estão os endpoints disponíveis. Note que as requisições de atualização (PATCH) suportam **atualizações parciais** de dados.

### Empresas (`/companies`)

* `POST /companies` - Cadastra uma nova empresa.
* `GET /companies` - Lista todas as empresas.
* `GET /companies/{id}` - Busca empresa por UUID.
* `PATCH /companies/{id}` - Atualiza parcialmente os dados da empresa.
* `DELETE /companies/{id}` - Deleta a empresa (e todos os lotes/extintores em cascata).

### Lotes (`/batches`)

* `POST /batches` - Cria um novo lote para uma empresa (via CNPJ).
* `GET /batches` - Lista todos os lotes.
* `GET /batches?company_id={id}` - Lista todos os lotes de uma empresa específica.
* `GET /batches/{id}` - Busca lote por UUID.
* `DELETE /batches/{id}` - Deleta o lote (e seus extintores em cascata).

### Extintores (`/extinguishers`)

* `POST /extinguishers` - Cadastra um extintor atrelado a um lote.
* `GET /extinguishers` - Lista todos os extintores.
* `GET /extinguishers?batch_id={id}` - Lista extintores de um lote específico.
* `GET /extinguishers/{id}` - Busca extintor pelo UUID.
* `GET /extinguishers/serie/{serieNumber}` - Busca extintor pelo número de série único.
* `PATCH /extinguishers/{id}` - Atualiza parcialmente (ex: altera apenas tipo ou peso).
* `DELETE /extinguishers/{id}` - Remove um extintor.

## 👨‍💻 Autor

**Guilherme Perondi**

* [LinkedIn](https://www.google.com/search?q=https://www.linkedin.com/in/guilherme-perondi)
* [GitHub](https://www.google.com/search?q=https://github.com/perondi09)
