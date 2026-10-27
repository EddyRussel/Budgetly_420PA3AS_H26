# 💰 Budgetly — Application de gestion budgétaire personnelle

> Application web full-stack de gestion des finances personnelles pour étudiants.  
> Développée dans le cadre du cours de Programmation Orientée Objet — Collège LaSalle.

---

## 📋 Description

Budgetly permet aux étudiants de suivre leurs revenus et dépenses, de définir des budgets par catégorie, et d'obtenir des prévisions financières basées sur leurs habitudes de consommation.

L'application combine un **backend REST Spring Boot** avec une **interface HTML/CSS/JavaScript** moderne, connectée à une base de données **MariaDB**.

---

## ✨ Fonctionnalités

| Fonctionnalité | Description |
|---|---|
| 📊 Tableau de bord | Solde en temps réel, revenus, dépenses, alertes |
| 💸 Transactions | Ajout, modification, suppression de revenus et dépenses |
| 🏷️ Catégories | Gestion personnalisée des catégories (DÉPENSE / REVENU) |
| 💼 Budgets | Limite par catégorie avec barres de progression et alertes |
| 🔮 Prévisions | Estimation du nombre de jours avant épuisement du solde |
| 📈 Rapport | Statistiques mensuelles et graphique d'évolution sur 6 mois |

---

## 🛠️ Technologies

### Backend
- **Java 17**
- **Spring Boot 4.0.5**
- **Spring Data JPA / Hibernate 7.2.7**
- **MariaDB 10.4** (via XAMPP)
- **Maven**

### Frontend
- **HTML5 / CSS3 / JavaScript (ES6+)**
- **Bootstrap 5.3.3**
- **Chart.js 4.4.0**
- **Google Fonts** (Plus Jakarta Sans, DM Mono)

---

## 🏗️ Architecture

```
Budgetly_v1_avecSB/
├── src/main/java/com/example/budgetly_v1_avecsb/
│   ├── model/
│   │   ├── User.java
│   │   ├── Transaction.java        ← classe abstraite
│   │   ├── Revenu.java             ← hérite de Transaction
│   │   ├── Depense.java            ← hérite de Transaction
│   │   ├── Category.java
│   │   └── Budget.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── TransactionRepository.java
│   │   ├── CategoryRepository.java
│   │   └── BudgetRepository.java
│   ├── service/
│   │   └── PredictionService.java
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── TransactionController.java
│   │   ├── CategoryController.java
│   │   └── BudgetController.java
│   └── config/
│       └── CorsConfig.java
└── src/main/resources/
    ├── application.properties
    └── static/
        ├── login.html
        ├── dashboard.html
        ├── transactions.html
        ├── budgets.html
        ├── previsions.html
        ├── rapport.html
        ├── categories.html
        ├── css/
        │   └── budgetly.css
        └── js/
            └── sidebar.js
```

### Concepts POO appliqués

- **Héritage** : `Transaction` (abstraite) → `Revenu` / `Depense`
- **Polymorphisme** : `calculateImpact()` retourne +montant (Revenu) ou -montant (Dépense)
- **Encapsulation** : getters/setters sur toutes les entités
- **Abstraction** : `Transaction` définit le contrat, les sous-classes implémentent
- **Composition** : Un `User` possède une liste de `Transaction`

---

## 🚀 Installation et démarrage

### Prérequis

- Java 17+
- Maven 3.8+
- XAMPP (MariaDB)
- IntelliJ IDEA (recommandé)

### Étapes

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/EddyRussel/Budgetly_420PA3AS_H26.git
   cd Budgetly_420PA3AS_H26
   ```

2. **Démarrer MariaDB** via XAMPP Control Panel → Start MySQL

3. **Créer la base de données**
   ```sql
   CREATE DATABASE budgetly;
   ```

4. **Configurer `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/budgetly
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   ```

5. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

6. **Ouvrir dans le navigateur**
   ```
   http://localhost:8080/login.html
   ```

---

## 🌐 Endpoints REST principaux

| Méthode | Endpoint | Description |
|---|---|---|
| POST | `/api/users/login` | Connexion |
| POST | `/api/users/register` | Inscription |
| GET | `/api/transactions/user/{id}` | Transactions d'un utilisateur |
| GET | `/api/transactions/user/{id}/balance` | Solde, revenus, dépenses |
| GET | `/api/transactions/user/{id}/prediction` | Prévisions |
| POST | `/api/transactions` | Créer une transaction |
| GET | `/api/categories` | Toutes les catégories |
| GET | `/api/budgets/user/{id}` | Budgets d'un utilisateur |

---

## 📁 Pages frontend

| Page | Description |
|---|---|
| `login.html` | Connexion utilisateur |
| `dashboard.html` | Tableau de bord principal |
| `transactions.html` | Gestion des transactions |
| `budgets.html` | Suivi des budgets par catégorie |
| `previsions.html` | Prévisions financières sur 30 jours |
| `rapport.html` | Rapport mensuel avec graphiques |
| `categories.html` | Gestion des catégories |

---

## 👨‍💻 Auteur

**Eddy Russel Ngoue**  
Étudiant en Techniques de l'informatique  
Collège LaSalle — Session 3

---

## 📄 Licence

Projet académique — Collège LaSalle. Tous droits réservés.
