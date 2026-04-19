# Loan Approval Process haldussüsteem 
Coop panga back-end arendaja proovitöö

## Projekti eesmärk
Rakenduse eesmärk on automatiseerida kodulaenu taotlemise protsess. Süsteem kontrollib kliendi vastavust reeglitele (vanusepiirang ja isikukoodi kehtivus), arvutab aniteetgraafiku alusel kuumaksed ning salvestab genereeritud maksegraafiku andmebaasi, et panga haldur saaks selle hiljem üle vaadata.

## Komponendid
Rakendus on ehitatud kasutades järgnevaid tehnoloogiaid:
* **Java & Spring Boot**: Rakenduse loogika ja REST API
* **PostgreSQL**: Relatsiooniline andmebaas
* **Liquibase**: Andmebaasi skeemi haldus ja migratsioonid
* **Docker & Docker Compose**: Rakenduse ja andmebaasi konteineriseerimine 
* **Swagger (OpenAPI)**: API dokumentatsioon ja testimisliides.

## Kuidas koodi oma masinas käivitada

Selleks, et rakendus edukalt käivitada, veendu, et sinu arvutis on installitud **Docker** ja **Java 21**.

### 1. Kloonimine
```bash
git clone [https://github.com/Lugerhelm/Coop2026LoanApprovalProcess.git](https://github.com/Lugerhelm/Coop2026LoanApprovalProcess.git)
cd Coop2026LoanApprovalProcess
```

### 2. Andmebaasi käivitamine (Docker)
Käivita PostgreSQL andmebaas Docker konteineris:
```bash
docker-compose up -d db
```

### 3. Rakenduse käivitamine
Käivita fail src/main/java/org/example/Application.java

või
```bash
./mvnw spring-boot:run
```


### 4. API dokumentatsioon
API dokumentatsioon ja testimiskeskkond saadaval aadressil:
http://localhost:8080/swagger-ui.html
