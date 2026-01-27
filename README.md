# Backend Challenge - Rits Tecnologia

API REST desenvolvida em Java + Spring Boot como solução para um teste técnico. 
O projeto implementa autenticação, autorização por papéis (roles) e regras de negócio 
para gerenciamento de usuários, produtos e pedidos.

## Repositório Original
https://github.com/aplaraujo/desafio-rits-tecnologia-backend

## Documento de requisitos
https://github.com/aplaraujo/desafio-rits-tecnologia-backend-project-assets/blob/main/rits-documentacao-requisitos.pdf

## Tecnologias Usadas

- Java
- Spring Boot
- JPA / Hibernate
- Maven
- JUnit
- Docker & Docker Compose
- Postgres

## Regras de Negócio

- Usuários podem ter diferentes perfis (ADMIN e CLIENT)

- Apenas usuários autorizados podem acessar determinados ***endpoints***

- Clientes visualizam apenas os seus próprios pedidos

- Administradores podem gerenciar produtos e pedidos

## Autenticação e autorização

A API utiliza autenticação baseada em JWT.

Fluxo resumido:
- Usuário realiza login
- A API retorna um token JWT
- O ***token*** deve ser enviado no cabeçalho de autorização (**Authorization**) das requisições protegidas

```
Authorization: Bearer <token>
```

## Endpoints Principais

### Autenticação

- `POST /auth/token` — *login* do usuário

- `POST /auth/user` — cadastro de usuário

### Produtos

- `GET /catalog/products` — lista de produtos (público)

- `POST /products` — criar produto (ADMIN)

- `PUT /products/{id}` — atualizar produto (ADMIN)

- `DELETE /products/{id}` — remover produto (ADMIN)

### Pedidos

- `GET /order-list` — lista geral de pedidos (ADMIN)

- `POST /orders` — criar pedido (CLIENT)

- `PUT /orders/{id}` — atualizar pedido (CLIENT)

- `DELETE /orders/{id}` — remover pedido (CLIENT)

## Implementação em Produção

- Banco de dados: Postgres (Implementação no Render)

## Como executar o projeto

#### Clone do repositório

```
https://github.com/aplaraujo/desafio-rits-tecnologia-backend
```

#### Ambiente de produção

A aplicação ficará disponível em:

```
https://desafio-rits-tecnologia-backend.onrender.com
```

##### Execução do projeto

```
mvn clean install
mvn spring-boot:run
```

#### Ambiente de desenvolvimento

##### Com Docker

```
docker-compose up --build
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

##### Execução do projeto (também para ambiente de testes)

```
mvn clean install
mvn spring-boot:run
```

## Estrutura do sistema

```
src/
├── config
├── controllers
├── dto
├── entities
├── mappers
├── repositories
├── security
├── services
```


## Funcionamento do Sistema

Autenticação administrador
</br>
[![Video Title](https://img.youtube.com/vi/OM1rE6U9kwY/0.jpg)](https://www.youtube.com/watch?v=OM1rE6U9kwY)

Autenticação cliente
</br>
[![Video Title](https://img.youtube.com/vi/SfhusM1DWMc/0.jpg)](https://www.youtube.com/watch?v=SfhusM1DWMc)

Catálogo de produtos (público)
</br>
[![Video Title](https://img.youtube.com/vi/vEn5qTvfSRQ/0.jpg)](https://www.youtube.com/watch?v=vEn5qTvfSRQ)

Operações CRUD de produtos (administrador)
</br>
[![Video Title](https://img.youtube.com/vi/P_aaXRjWsY4/0.jpg)](https://www.youtube.com/watch?v=P_aaXRjWsY4)

Operações CRUD de pedidos (cliente)
</br>
[![Video Title](https://img.youtube.com/vi/RNbNB2kYveg/0.jpg)](https://www.youtube.com/watch?v=RNbNB2kYveg)

## Autora

Ana Paula Lopes Araujo

https://github.com/aplaraujo
</br>
https://linkedin.com/in/ana-paula-lopes-araujo
