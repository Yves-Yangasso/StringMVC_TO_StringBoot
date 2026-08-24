# StringMVC_TO_StringBoot

Portage en **Spring Boot 3** du projet `diti4_spring_mvc` (Spring MVC sans Boot) : gestion de **produits** et de **types de produits**, persistance **JPA/Hibernate** sur **PostgreSQL**, exposée en **API REST** documentee par **Swagger UI**.

## Stack technique

| Composant | diti4_spring_mvc | StringMVC_TO_StringBoot |
|-----------|------------------|-------------------------|
| Framework | Spring 5.3.30 (XML + web.xml) | Spring Boot 3.5.16 |
| Java | 8 (source/target) | 21 |
| Namespace | `javax.*` | `jakarta.*` |
| Persistance | Hibernate 5 + `EntityManager` a la main | Spring Data JPA (Hibernate 6) |
| Serveur | Tomcat 9 externe (WAR + Cargo) | Tomcat embarque (JAR executable) |
| Vues | JSP (`/produit`) | supprimees — API REST uniquement |
| Swagger | `openapi.json` ecrit a la main | springdoc-openapi (spec generee) |

## Prerequis

- JDK 21+
- Maven 3.9+
- PostgreSQL en ecoute locale

## Configuration base de donnees

Dans `src/main/resources/application.properties` (au lieu de `AppConfig.java` dans le projet MVC) :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/diti4_spring_mvc
spring.datasource.username=postgres
spring.datasource.password=****
spring.jpa.hibernate.ddl-auto=update
```

La base pointe sur **la meme base que le projet Spring MVC** — les tables `products` et `type_produits` sont partagees.

## Demarrer l'application

```bash
mvn spring-boot:run
```

ou bien :

```bash
mvn clean package
java -jar target/StringMVC_TO_StringBoot.jar
```

L'application ecoute sur **http://localhost:8080**.

> Le projet Spring MVC utilise aussi le port 8080 : n'en lancer qu'un seul a la fois, ou changer `server.port`.

## API REST

Endpoints **identiques** a ceux du projet Spring MVC.

### Produits — `/api/produit`

| Methode | URL | Description |
|---------|-----|-------------|
| GET | `/api/produit?page=0&size=5` | Lister les produits (pagine) |
| GET | `/api/produit/{id}` | Detail d'un produit |
| POST | `/api/produit` | Creer un produit |
| DELETE | `/api/produit/delete/{id}` | Supprimer un produit |

Corps du POST (`ProduitDTO`) :

```json
{ "libelle": "Laptop", "prix": 999.9, "typeProduitId": 1 }
```

### Types de produit — `/api/typeproduit`

| Methode | URL | Description |
|---------|-----|-------------|
| GET | `/api/typeproduit` | Lister les types |
| GET | `/api/typeproduit/{id}` | Detail d'un type |
| POST | `/api/typeproduit` | Creer un type |
| DELETE | `/api/typeproduit/delete/{id}` | Supprimer un type |

```json
{ "libelle": "Electronique" }
```

### Erreurs

`RestExceptionHandler` renvoie :

- **400** avec `{ "champ": "message" }` sur une validation en echec ;
- **404** avec `{ "message": "..." }` sur `ResourceNotFoundException`.

## Documentation Swagger

- UI : http://localhost:8080/swagger-ui.html
- Spec generee : http://localhost:8080/v3/api-docs

## Structure du projet

```
src/main/java/diti/
├── Application.java   point d'entree Spring Boot
├── config/            SecurityConfig (SecurityFilterChain, tout en permitAll)
├── entity/            Produit, TypeProduit
├── dto/               ProduitDTO (validation)
├── mapper/            ProduitMapper (MapStruct)
├── repository/        ProductRepository, TypeProduitRepository (JpaRepository)
├── service/           interfaces + impl/ (logique metier, inchangee)
└── REST/              ProduitRestController, TypeProduitRestController, RestExceptionHandler
src/main/resources/application.properties
```

## Ce qui a disparu par rapport au projet Spring MVC

| Fichier MVC | Devenu |
|-------------|--------|
| `web.xml`, `spring-servlet.xml` | auto-configuration Boot |
| `AppConfig.java` (DataSource, EntityManagerFactory, TransactionManager) | `application.properties` |
| `repository/impl/*RepositoryImpl.java` | Spring Data JPA |
| `controller/ProduitController.java` + JSP | supprimes (API REST uniquement) |
| `config/SwaggerController.java` + `openapi.json` | springdoc-openapi |
