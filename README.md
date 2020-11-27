# Webcrawl
### WebCrawl Microservice

> André Luiz Miranda da Rosa (andremirandarosa@gmail.com)

*Linguagens de Programação: Java (JDK: 8)*

------------

Webcrawl para busca de um Keyword em uma URL Base. 

------------
### WEBSERVICES REST:

** URL REQUISIÇÃO:**

http://localhost:4567/crawl

> Retorna o ID do processo de WebCrawling.

Payload:

>{ "keyword": "keyword para busca" }

** URL CONSULTA:**

http://localhost:4567/crawl/$id
Parâmetro: $id: O ID do processo de WebCrawling gerado na requisição.

> Retorna o estado do processamento do WebCrawling com a lista das URLs que contém a Keyword especificada.

------------
### VARIÁVEIS DE AMBIENTE:

** URL para Busca (MANDATÓRIO):**

BASE_URL=https://targetsite.com

** Número Máximo de Resultados (OPCIONAL): Valor Maior que Zero [Padrão: -1 (Infinito)]**

MAX_RESULTS=-1

------------
### EXECUÇÃO:

** Maven:**

mvn clean verify exec:java

** Docker:**

docker build . -t webcrawl

docker run -e BASE_URL=https://targetsite.com -p 4567:4567 --rm webcrawl

** Docker em Dev (Utiliza respositório local):** 

docker run -v "$HOME/.m2":/root/.m2  -e BASE_URL=https://targetsite.com -p 4567:4567 --rm webcrawl

