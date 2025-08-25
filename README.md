# 📦 Catalogo Prodotti – Progetto SIW Settembre 2025

Sistema informativo su Web per la gestione di un catalogo prodotti.  
Progetto sviluppato in **Java + Spring Boot** con **PostgreSQL** come database relazionale.  

---

## 🚀 Funzionalità richieste

- Gestione di un catalogo di prodotti con:
  - Nome
  - Prezzo
  - Descrizione
  - Tipologia
  - Prodotti simili
- Accesso al sistema per:
  - **Utente generico**: navigazione prodotti, visualizzazione e inserimento commenti, modifica/eliminazione solo dei propri commenti.
  - **Amministratore**: autenticazione, inserimento/modifica/rimozione prodotti, gestione relazioni tra prodotti simili.
- Interfaccia web basata su **Spring MVC + Thymeleaf**.

---

## 🛠️ Tecnologie utilizzate

- [Java 17+](https://adoptium.net/)
- [Spring Boot 3.5.x](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [PostgreSQL 17](https://www.postgresql.org/)
- [pgAdmin 4](https://www.pgadmin.org/)
- Thymeleaf
- Spring Security

---

## ⚙️ Prerequisiti

- Installare **Java 17+**
- Installare **Maven**
- Installare **PostgreSQL 17** e creare il database:
  ```sql
  CREATE DATABASE catalogo-prodotti;