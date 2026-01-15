# Platformă pentru Rezervarea Locurilor de Parcare
### Proiect MPAI - Academia de Studii Economice din București
**Facultatea de Cibernetică, Statistică și Informatică Economică**

---

## Descriere Proiect
Această aplicație Java (console-based) este concepută pentru gestionarea eficientă a locurilor de parcare. Sistemul permite utilizatorilor să vizualizeze, rezerve și să ocupe locuri de parcare, asigurând persistența datelor și un flux logic bazat pe design pattern-uri moderne.

### Scopul aplicației
* **Vizualizare:** Consultarea locurilor de parcare disponibile în timp real.
* **Rezervare:** Blocarea unui loc pentru un anumit interval de timp.
* **Ocupare:** Validarea accesului la locul rezervat.
* **Eliberare:** Calcularea automată a costului și eliberarea resursei.
* **Persistență:** Salvarea datelor într-un sistem de gestiune a bazelor de date (H2).

---

## Arhitectura Soluției
Proiectul este structurat modular pentru a permite extensibilitatea (ex. adăugarea unei interfețe grafice ulterior).

### Structura Pachetelor
* `ro.ase.parking.model`: Entitățile de bază (`User`, `ParkingSpot`, `Reservation`, `ParkingSession`).
* `ro.ase.parking.state`: Implementarea **State Pattern** pentru stările locului de parcare.
* `ro.ase.parking.specification`: Reguli de validare prin **Specification Pattern**.
* `ro.ase.parking.observer`: Sistemul de notificări prin **Observer Pattern**.
* `ro.ase.parking.db`: Gestiunea bazei de date H2.
* `ro.ase.parking`: Clasa principală (`Main`).

---

## Modele de Proiectare Utilizate

### 1. State Pattern
Gestionează comportamentul locului de parcare în funcție de status: `AvailableState`, `ReservedState`, `OccupiedState`.
* **Beneficiu:** Elimină logica complexă de tip `if-else` și face codul mai ușor de întreținut.

### 2. Observer Pattern
Notifică utilizatorii în mod automat la schimbări majore de sistem (rezervări sau eliberări de locuri).
* **Interfețe:** `Observer` și `UserNotificationObserver`.

### 3. Singleton Pattern
Implementat în clasa `ParkingManager` pentru a asigura un punct unic de control asupra notificărilor și a stării globale.

### 4. Specification Pattern
Izolează regulile de validare (ex: `SpotAvailableSpecification`, `TimeIntervalSpecification`) de restul logicii de business.

---

## Persistența Datelor
Aplicația utilizează **H2 Database** în mod persistent. Datele nu se pierd la închiderea aplicației.

**Tabele Principale:**
- `users`
- `parking_spots`
- `reservations`
- `parking_sessions`

---

## Fluxul Principal
1. **Autentificare** utilizator.
2. **Vizualizare** locuri parcare.
3. **Selecție & Rezervare** (dacă locul este liber).
4. **Ocupare** (doar de către cel care a rezervat).
5. **Eliberare & Plată**: Calcularea costului și actualizarea bazei de date.

---

## Echipa de Proiect
* **Profesor coordonator:** Prof. univ. dr. Pocatilu Paul
* **Studenți:**
    * Dumitrescu Vlad-Eduard
    * Mărgineanu Matei
    * Stănică Florin-Alexandru

**Locație & Dată:** București, 2025