# Finance Manager API – XPTO

## Descrição do Projeto
O **Finance Manager API** é uma aplicação backend desenvolvida em **Java 8 com Spring Boot** para gerenciar clientes, contas bancárias e transações financeiras da empresa fictícia **XPTO**.

O sistema permite:
- Manter clientes (Pessoa Física e Jurídica) com seus dados completos.
- Controlar contas bancárias e transações associadas.
- Gerar relatórios de saldo de clientes, incluindo período específico.
- Calcular a receita da empresa com base na quantidade de movimentações de cada cliente.

O projeto utiliza **Oracle Database** e uma função PL/SQL (`calcular_valor_cliente_por_periodo`) para cálculo do valor pago pelo cliente no período, demonstrando integração entre Java e PL/SQL.

---

## Tecnologias Utilizadas
- Java 8
- Spring Boot
- Spring Data JPA
- Oracle Database
- Maven
- Lombok
- OpenAPI

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
- **Repository Pattern**: abstração do acesso a dados e simplificação de consultas.
- **Service Layer Pattern**: centralização da lógica de negócio, deixando controllers limpos.
- **DTO Pattern**: separação entre entidades internas e dados expostos pela API.
- **Builder Pattern (Lombok)**: instanciamento legível de entidades complexas.
- **Exception Handling Pattern**: padronização no tratamento de erros e mensagens retornadas.
- **Factory/Helper Pattern**: usado para gerar relatórios a partir de dados agregados das contas e transações.

---

## Estrutura do Projeto

```
src/main/java
 └── com.xpto.financemanager
      ├── config          # Configurações da API
      ├── controller      # Endpoints da API
      ├── dto             # Regras de negócio
      ├── entities        # Acesso a dados (Oracle)
      ├── enums           # Modelos de dados (Cliente, Conta, Transação)
      ├── exceptions      # Data Transfer Objects
      ├── repositories    # Enumerações (ex: tipos de transação)
      └── services        # Tratamento de erros
```

---

## Exemplos de Endpoints

- **Consultar saldo de cliente**
```
GET /api/customers/{customerId}/report
```

- **Consultar saldo de cliente por período**
```
GET /api/customers/{customerId}/report?initialDate=2025-09-10&finalDate=2025-09-11
```

- **Criar cliente**
```
POST /api/customers
Body: {
  "name": "string",
  "cpf": "string",
  "cnpj": "string",
  "phone": "string",
  "address": {
    "street": "string",
    "homeNumber": "string",
    "complement": "string",
    "city": "string",
    "state": "string",
    "zipCode": "string",
    "uf": "string"
  },
  "account": {
    "accountNumber": "string",
    "bank": "string",
    "agency": "string",
    "balance": 0
  }
}
```

---

## Regras de Negócio
- Clientes PF e PJ podem ter várias contas bancárias.
- Movimentações iniciais são obrigatórias para cada cliente.
- Alteração de dados bancários não é permitida se houver movimentações associadas.
- Cálculo do valor pago pelo cliente baseado na quantidade de movimentações em 30 dias:
    - Até 10 movimentações: R$ 1,00 por operação
    - De 11 a 20 movimentações: R$ 0,75 por operação
    - Acima de 20 movimentações: R$ 0,50 por operação

---

## Observações
- O projeto foi desenvolvido considerando **facilidade de manutenção e extensão futura**.
- A integração com a função PL/SQL demonstra a capacidade de combinar Java com recursos do Oracle Database.
- Relatórios podem ser impressos no console ou exportados para outros formatos.

