# Finance Manager API – XPTO

## Descrição do Projeto
O **Finance Manager API** é uma aplicação backend desenvolvida em **Java 8 com Spring Boot** para gerenciar clientes, contas bancárias e transações financeiras da empresa fictícia **XPTO**.

O sistema permite:
- Manter clientes (Pessoa Física e Jurídica) com seus dados completos.
- Controlar contas bancárias e transações associadas.
- Gerar relatórios de saldo de clientes, incluindo período específico.
- Calcular a receita da empresa com base na quantidade de movimentações de cada cliente.

O projeto utiliza **Oracle Database** com funções e procedures PL/SQL como (`calcular_valor_cliente_por_periodo`) para cálculo do valor pago pelo cliente no período, demonstrando integração entre Java e PL/SQL.

---

## Tecnologias Utilizadas
- Java 8
- Spring Boot
- Spring Data JPA
- Oracle Database
- Maven
- Lombok
- OpenAPI
- Spring Validation

---

## Funcionalidades Implementadas
- **CRUD de clientes** (PF e PJ), incluindo validações de dados obrigatórios.
- **CRUD de endereços** vinculados aos clientes.
- **CRUD de contas bancárias**, com restrição de alteração caso existam movimentações associadas (apenas exclusão lógica).
- **Movimentações financeiras**: criação de transações iniciais e subsequentes.
- **Relatórios**:
    - Saldo do cliente completo.
    - Saldo do cliente por período específico.
    - Receita da empresa (XPTO) por período, considerando quantidade de movimentações.
- **Integração com PL/SQL**: função para cálculo do valor pago pelo cliente com base em regras de negócio.
- **Tratamento de exceções**: NotFoundException, validação de dados e regras de negócio.

---

## Boas Práticas de Desenvolvimento
Durante o desenvolvimento, foram aplicadas diversas boas práticas para garantir **qualidade, manutenibilidade e escalabilidade**:

- **Organização em camadas**: Controllers, Services e Repositories separados.
- **DTOs**: para entrada e saída de dados, evitando exposição direta das entidades.
- **Validações de entrada**: para garantir integridade de dados e regras de negócio.
- **Tratamento de exceções centralizado**: mensagens claras para recursos não encontrados ou operações inválidas.
- **Nomenclatura consistente**: todas as classes, métodos e variáveis em inglês, seguindo convenções Java.
- **Reuso de código**: métodos auxiliares para cálculos, agregações e formatação de relatórios.
- **Uso de streams e lambdas**: para processamento de coleções de forma concisa e funcional.
- **Transações seguras**: garantindo integridade de dados ao persistir ou atualizar múltiplas entidades.

---

## Padrões de Projeto Utilizados
- **Service Layer Pattern**: centralização da lógica de negócio, deixando controllers limpos.
- **DTO Pattern**: separação entre entidades internas e dados expostos pela API.
- **Builder Pattern (Lombok)**: instanciamento legível de entidades complexas.
- **Exception Handling Pattern**: padronização no tratamento de erros e mensagens retornadas.

---

## Estrutura do Projeto

```
src/main/java
 └── com.xpto.financemanager
      ├── config          # Configurações da API
      ├── controller      # Endpoints da API
      ├── dto             # Data Transfer Objects
      ├── entities        # Modelos de dados (Cliente, Conta, Transação)
      ├── enums           # Enumerações (ex: tipos de transação)
      ├── exceptions      # Tratamento de erros
      ├── repositories    # Acesso a dados (Oracle)
      └── services        # Regras de negócio
```

---

## Exemplos de Endpoints

- **Consultar saldo de cliente**
```
GET /reports/{customerId}
```

- **Consultar saldo de cliente por período**
```
GET /reports/{customerId}/period?initialDate=2025-09-10&finalDate=2025-09-11
```

- **Criar cliente**
```
POST /customer/{PF}
Body: {
  "name": "João da Silva",
  "cpf": "123.456.789-00",
  "phone": "(11) 98765-4321",
  "address": {
    "street": "Rua das Flores",
    "homeNumber": "123",
    "complement": "Apto 45",
    "city": "São Paulo",
    "state": "São Paulo",
    "zipCode": "01001-000",
    "uf": "SP"
  },
  "account": {
    "accountNumber": "987654-3",
    "bank": "Banco XPTO",
    "agency": "1234",
    "balance": 2500.75
  }
}
```

---

## ✅ Como Executar o Projeto

1. **Clonar o repositório**
```bash
git clone https://github.com/euthiagomota/desafio-xpto-mv
```

2. **Entrar no diretório do projeto**
```bash
cd desafio-xpto-mv
```

3. **Compilar e rodar com Maven**
```bash
mvn spring-boot:run
```

4. **Acessar a documentação da API**
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Testes
O projeto utiliza **JUnit e Mockito** para testes unitários.

Rodar testes com Maven:
```bash
mvn test
```

---

## 👨‍💻 Autor
Projeto desenvolvido por **Thiago José** como desafio técnico da empresa MV.

