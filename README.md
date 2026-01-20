# Backend Challenge - Rits Tecnologia

## Repositório Original
https://github.com/aplaraujo/desafio-rits-tecnologia-backend

## Visão Geral

O sistema deve manter um cadastro de usuário, produtos e pedidos. 
Cada usuário possui nome, e-mail, telefone, endereço e senha. 
Os dados dos produtos são: nome e preço. Os dados dos pedidos são os seguintes: 
código do cliente, código do produto, data de criação e estado do pedido. 
O sistema deve apresentar uma lista de produtos, os quais podem ser filtrados 
pelo respectivo nome. O usuário pode incluir, editar e remover pedidos. 
O estado de um pedido pode ser: pendente, em preparo, em entrega, entregue e cancelado. 
Os usuários do sistema podem ser clientes ou administradores. Usuários não autenticados podem se cadastrar no sistema e navegar no catálogo de produtos. Clientes podem atualizar o cadastro no sistema, registrar pedidos e visualizar seus próprios pedidos. Usuários administradores têm acesso à área administrativa na qual podem ver os cadastros de clientes e produtos.

## Modelo Conceitual

- Cada pedido corresponde a um cliente, enquanto que um cliente pode ter um ou mais pedidos.
- Cada produto corresponde a um pedido, enquanto que um pedido pode um ou mais produtos.
- Um usuário pode ter um ou mais perfis (***roles***), que permitem o acesso ao sistema (***client***, ***admin***)

![uml](https://raw.githubusercontent.com/aplaraujo/desafio-rits-tecnologia-backend-project-assets/refs/heads/main/teste-uml.png)

## Casos de Uso

O escopo do sistema consiste nos seguintes casos de uso:

| Caso de uso |Visão geral| Acesso  |
|------------|-----------|---------|
| Manter produtos |CRUD de produtos, com a possibilidade de filtrar itens pelo nome|Apenas administrador|
| Manter pedidos |Visualização dos pedidos criados pelos clientes|Apenas administrador|
| Consultar catálogo |Visualização dos produtos disponíveis, com a possibilidade de filtrar itens pelo nome|Público|
| *Sign up*  |Cadastro no sistema|Público|
| *Login*    |Entrada no sistema (desde que já esteja cadastrado)|Público|
|Cadastrar pedido|Cadastro dos próprios pedidos|Apenas cliente|
|Visualizar pedidos|Visualização dos próprios pedidos|Apenas cliente|
|Atualizar pedidos|Atualização dos próprios pedidos|Apenas cliente|
|Excluir pedidos|Exclusão dos próprios pedidos|Apenas cliente|

## Atores

|Ator| Responsabilidade                                                                      |
|----|---------------------------------------------------------------------------------------|
|Usuário anônimo| Pode acessar as áreas públicas do sistema (catálogo de produtos, *login* e *sign up*) |
|Cliente|Responsável por manter os próprios dados pessoais no sistema, além de pode visualizar o histórico dos próprios pedidos. Todo usuário cadastrado por padrão é um cliente.|
|Administrador|Responsável por acessar a área administrativa do sistema com cadastros e relatórios.|

## Casos de Uso (Detalhes)

### Consultar catálogo

|Atores| Precondições | Pós condições | Visão geral |
|------|--------------|---------------|-------------|
|Usuário anônimo, cliente, administrador| -            | -             |Visualizar os produtos disponíveis, com a possibilidade de filtrar itens por nome|

### Cenário de sucesso

1.[Saída] O sistema informa uma lista de nome e preço dos produtos, ordenada por nome.
</br>
2.[Entrada] O usuário informa, como opção, parte do nome de um produto.
</br>
3.[Saída] O sistema informa a lista atualizada.

#### Informações complementares

O número padrão de registros por página deve ser 12. Como a lista é paginada, o usuário pode navegar nas próximas páginas.

### Manter produtos

|Atores|Precondições| Pós condições | Visão geral  |
|------|------------|---------------|--------------|
|Administrador|Usuário autenticado| -             |CRUD de produtos, com a possibilidade de filtrar produtos pelo nome|

#### Cenário de sucesso

1. Executar caso de uso: **Consultar catálogo**
2. O administrador seleciona uma das opções:
   </br>
   2.1 Variante Inserir
   </br>
   2.2 Variante Atualizar
   </br>
   2.3 Variante Excluir

#### Cenário alternativo: variantes

2.1 Variante Inserir
</br>
2.1.1 [Entrada] O administrador informa nome e preço do produto

2.2 Variante Atualizar
</br>
2.2.1 [Entrada] O administrador seleciona um produto para editar
</br>
2.2.2 [Saída] O sistema informa nome e preço do produto
</br>
2.2.3 [Entrada] O administrador informa novos valores para nome e preço do produto

2.3 Variante Excluir
</br>
2.3.1 [Entrada] O administrador seleciona um produto para excluir

#### Cenário alternativo: exceções

2.1.1a Dados inválidos
</br>
2.1.1a.1 [Saída] O sistema informa os erros nos campos inválidos [1]
</br>
2.1.1a.2 Vai para o passo 2.1.1

2.2.3a Dados inválidos
</br>
2.2.3a.1 [Saída] O sistema informa os erros nos campos inválidos [1]
</br>
2.2.3a.2 Vai para o passo 2.2.1

2.2.3b Número de identificação não encontrado
</br>
2.2.3b.1 [Saída] O sistema informa que o número de identificação não existe
</br>
2.2.3b.2 Vai para o passo 2.2.1

2.3.1a Número de identificação não encontrado
</br>
2.3.1b.1 [Saída] O sistema informa que o número de identificação não existe
</br>
2.3.1b.2 Vai para o passo 2.3.1

#### Informações complementares

[1] Validação dos dados
- Nome: deve ter entre 3 e 100 caracteres (campo obrigatório)
- Preço: deve ser positivo (campo obrigatório)

### *Login*

|Atores| Precondições |Pós condições| Visão geral |
|------|--------------|-------------|-------------|
|Usuário anônimo| -            |Usuário autenticado|Efetuar a entrada no sistema|

#### Cenário de sucesso

1. [Entrada] O usuário anônimo informa as credenciais (nome e senha)
2. [Saída] O sistema informa um token válido

#### Cenário alternativo: exceções

1a. Credenciais inválidas
 
1a.1 [Saída] O sistema informa que as credenciais são inválidas
</br>
1a.2 Vai para o passo 1

### Consultar pedidos

|Atores|Precondições| Pós condições | Visão geral                                                                                |
|------|------------|---------------|--------------------------------------------------------------------------------------------|
|Cliente|Usuário autenticado| -             | Visualizar pedidos disponíveis, podendo filtrar pedidos por número de identificação (*id*) |

1. [Saída] O sistema informa uma lista de pedidos, ordenada por número de identificação
2. [Entrada] O cliente informa o número de identificação de um produto
3. [Saída] O sistema informa o pedido que corresponde ao número de identificação fornecido pelo cliente

### Manter pedidos

|Atores|Precondições| Pós condições | Visão geral                                                                                |
|------|------------|---------------|--------------------------------------------------------------------------------------------|
|Cliente|Usuário autenticado| -             | CRUD de pedidos |

#### Cenário de sucesso

1. Executar caso de uso: *Consultar pedidos*
2. O administrador seleciona uma das opções:
   </br>
   2.1 Variante Inserir
   </br>
   2.2 Variante Atualizar
   </br>
   2.3 Variante Excluir

#### Cenário alternativo: variantes

2.1 Variante Inserir
</br>
2.1.1 [Entrada] O cliente informa código do cliente, código do produto, data de criação e estado do pedido

2.2 Variante Atualizar
</br>
2.2.1 [Entrada] O cliente seleciona um pedido para editar
</br>
2.2.2 [Saída] O sistema informa código do cliente, código do produto, data de criação e estado do pedido
</br>
2.2.3 [Entrada] O cliente informa novos valores para código do cliente, código do produto e estado do pedido

2.3 Variante Excluir
</br>
2.3.1 [Entrada] O cliente seleciona um pedido para excluir

#### Cenário alternativo: exceções

2.1.1a Dados inválidos
</br>
2.1.1a.1 [Saída] O sistema informa os erros nos campos inválidos [1]
</br>
2.1.1a.2 Vai para o passo 2.1.1

2.2.3a Dados inválidos
</br>
2.2.3a.1 [Saída] O sistema informa os erros nos campos inválidos [1]
</br>
2.2.3a.2 Vai para o passo 2.2.1

2.2.3b Número de identificação não encontrado
</br>
2.2.3b.1 [Saída] O sistema informa que o número de identificação não existe
</br>
2.2.3b.2 Vai para o passo 2.2.1

2.3.1a Número de identificação não encontrado
</br>
2.3.1b.1 [Saída] O sistema informa que o número de identificação não existe
</br>
2.3.1b.2 Vai para o passo 2.3.1

#### Informações complementares

[1] Validação dos dados
- Código do cliente: deve ser um valor positivo (campo obrigatório)
- Código do produto: deve ser um valor positivo (campo obrigatório)
- Estado do pedido: não deve ser um valor nulo (campo obrigatório)

### Testes

#### Consulta de produtos

1. Consulta de produtos deve retornar 200 quando a lista estiver ordenada por nome

#### Consulta de produtos por número de identificação

1. Consulta de produtos por número de identificação deve retornar 200 quando o administrador estiver autenticado e o respectivo número de identificação fornecido pelo usuário existir no sistema
2. Consulta de produtos por número de identificação deve retornar 404 quando o administrador estiver autenticado e o respectivo número de identificação fornecido pelo usuário não existir no sistema
3. Consulta de produtos por número de identificação deve retornar 401 quando o administrador não estiver autenticado

#### Cadastro de produtos

1. Cadastro de produtos deve retornar 201 quando o administrador estiver autenticado e todos os dados forem válidos
2. Cadastro de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "nome" estiver vazio
3. Cadastro de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "nome" estiver fora do intervalo de 3 a 100 caracteres
4. Cadastro de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "preço" for negativo ou igual a zero
5. Cadastro de produtos deve retornar 401 quando o administrador não estiver autenticado
6. Cadastro de produtos deve retornar 403 quando o cliente estiver autenticado

#### Atualização de produtos

1. Atualização de produtos deve retornar 201 quando o administrador estiver autenticado e todos os campos forem válidos
2. Atualização de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "nome" estiver vazio
3. Atualização de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "nome" estiver fora do intervalo de 3 a 100 caracteres
4. Atualização de produtos deve retornar 422 quando o administrador estiver autenticado e o campo "preço" for negativo ou igual a zero
5. Atualização de produtos deve retornar 401 quando o administrador não estiver autenticado
6. Atualização de produtos deve retornar 403 quando o cliente estiver autenticado

#### Exclusão de produtos

1. Exclusão de produtos deve retornar 204 quando o administrador estiver autenticado e o número de identificação do produto existir no sistema
2. Exclusão de produtos deve retornar 404 quando o administrador estiver autenticado e o número de identificação do produto não existir no sistema
3. Exclusão de produtos deve retornar 401 quando o administrador não estiver autenticado
4. Exclusão de produtos deve retornar 403 quando o cliente estiver autenticado

#### Consulta completa de pedidos (apenas administrador)

1. Consulta completa de pedidos deve retornar 200 quando o administrador estiver autenticado
2. Consulta completa de pedidos deve retornar 401 quando o administrador não estiver autenticado

#### Consulta de pedidos (apenas cliente)

1. Consulta de pedidos deve retornar 200 quando o cliente estiver autenticado
2. Consulta de pedidos deve retornar 401 quando o cliente não estiver autenticado
3. Consulta de pedidos deve retornar 403 quando o administrador estiver autenticado

#### Consulta de pedidos por número de identificação

1. Consulta de pedidos por número de identificação deve retornar 200 quando o cliente estiver autenticado e quando o respectivo número de identificação fornecido existir no sistema
2. Consulta de pedidos por número de identificação deve retornar 404 quando o cliente estiver autenticado e quando o respectivo número de identificação fornecido não existir no sistema
3. Consulta de pedidos por número de identificação deve retornar 401 quando o cliente não estiver autenticado
4. Consulta de pedidos por número de identificação deve retornar 403 quando o administrador estiver autenticado

#### Cadastro de pedidos

1. Cadastro de pedidos deve retornar 201 quando o cliente estiver autenticado e todos os dados forem válidos
2. Cadastro de pedidos deve retornar 422 quando o cliente estiver autenticado e o campo "código do cliente" for negativo ou igual a zero
3. Cadastro de pedidos deve retornar 422 quando o cliente estiver autenticado e o vetor de produtos estiver vazio
4. Cadastro de pedidos deve retornar 422 quando o cliente estiver autenticado e o campo "estado do pedido" for nulo
5. Cadastro de pedidos deve retornar 401 quando o cliente não estiver autenticado
6. Cadastro de pedidos deve retornar 403 quando o administrador estiver autenticado

#### Atualização de pedidos

1. Atualização de pedidos deve retornar 204 quando o cliente estiver autenticado e todos os dados forem válidos
2. Atualização de pedidos deve retornar 422 quando o cliente estiver autenticado e o campo "código do cliente" for negativo ou igual a zero
3. Atualização de pedidos deve retornar 422 quando o cliente estiver autenticado e o vetor de produtos estiver vazio
4. Atualização de pedidos deve retornar 422 quando o cliente estiver autenticado e o campo "estado do pedido" for nulo
5. Atualização de pedidos deve retornar 401 quando o cliente não estiver autenticado
6. Atualização de pedidos deve retornar 403 quando o administrador estiver autenticado

#### Exclusão de pedidos

1. Exclusão de pedidos deve retornar 204 quando o cliente estiver autenticado e o número de identificação do pedido existir no sistema
2. Exclusão de pedidos deve retornar 404 quando o cliente estiver autenticado e o número de identificação do pedido não existir no sistema
3. Exclusão de pedidos deve retornar 401 quando o cliente não estiver autenticado
4. Exclusão de pedidos deve retornar 403 quando o administrador estiver autenticado

## Tecnologias Usadas

- Java
- Spring Boot
- JPA / Hibernate
- Maven
- JUnit

## Implementação em Produção

- Banco de dados: Postgres (Implementação no Render)

## Como executar o projeto

```
# Clone do repositório
https://github.com/aplaraujo/desafio-rits-tecnologia-backend

# Execução do projeto
./mvn spring-boot:run
```

## Funcionamento do Sistema

Autenticação administrador
</br>
https://github.com/user-attachments/assets/ba2d1935-f80e-4804-8206-e7c823e8e01a

Autenticação cliente


Catálogo de produtos (público)


Operações CRUD de produtos (administrador)


Operações CRUD de pedidos (cliente)


## Autora

Ana Paula Lopes Araujo

https://linkedin.com/in/ana-paula-lopes-araujo
