# Scholar REST Service

API REST para gerenciamento de alunos de cursos de inglês. Permite que professores gerenciem suas turmas, alunos e atividades avaliativas, com cálculo automático de notas baseado em pesos por tipo de atividade.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway
- Lombok
- Springdoc OpenAPI (Swagger)
- Docker

## Arquitetura

O projeto segue organização por domínio (package by feature):

```
src/main/java/io/github/thuliosouza/scholar_rest_service/
├── domain/
│   ├── teacher/
│   ├── school/
│   ├── classgroup/
│   ├── student/
│   └── activity/
├── infra/
│   ├── auth/
│   ├── security/
│   └── exception/
└── ScholarRestServiceApplication.java
```

## Cálculo de Nota

A nota de cada aluno é calculada automaticamente a partir das atividades registradas:

| Atividade | Quantidade | Peso unitário | Total |
|---|---|---|---|
| Avaliação Contínua | 2 | 5% | 10% |
| Lição de Casa | 8 | 1,25% | 10% |
| Lição Web | 8 | 1,25% | 10% |
| Teste Oral MID | 1 | 10% | 10% |
| Teste Oral FINAL | 1 | 20% | 20% |
| Teste Escrito MID | 1 | 20% | 20% |
| Teste Escrito FINAL | 1 | 20% | 20% |

## Endpoints

### Autenticação
```
POST /auth/register
POST /auth/login
```

### Professores
```
GET    /teachers/{id}
PUT    /teachers/{id}
DELETE /teachers/{id}
```

### Turmas
```
POST   /class-groups
GET    /class-groups
GET    /class-groups/{id}
PUT    /class-groups/{id}
DELETE /class-groups/{id}
```

### Alunos
```
POST   /students
GET    /students/{id}
GET    /students/class-group/{classGroupId}
PUT    /students/{id}
DELETE /students/{id}
POST   /students/{id}/transfer
```

### Atividades
```
POST   /activities
GET    /activities/student/{studentId}
GET    /activities/{id}
PUT    /activities/{id}
DELETE /activities/{id}
```

## Pré-requisitos

- JDK 21
- Docker e Docker Compose

## Configuração

1. Clone o repositório:
```bash
git clone https://github.com/tsouzz/scholar-rest-service.git
cd scholar-rest-service
```

2. Crie o arquivo `.env` baseado no `.env.example`:
```
DB_NAME=english_school
DB_USER=scholar_user
DB_PASSWORD=sua_senha

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/english_school
SPRING_DATASOURCE_USERNAME=scholar_user
SPRING_DATASOURCE_PASSWORD=sua_senha

JWT_SECRET=seu_secret_aqui
```

3. Suba a aplicação com Docker:
```bash
docker compose up --build
```

A API estará disponível em `http://localhost:8080`.

## Documentação

Com a aplicação rodando, acesse o Swagger em:

```
http://localhost:8080/swagger-ui/index.html
```

## Autenticação

A API usa JWT. Para acessar endpoints protegidos:

1. Registre um professor em `POST /auth/register`
2. Faça login em `POST /auth/login` e copie o token retornado
3. No Swagger, clique em **Authorize** e insira `Bearer seu_token`
