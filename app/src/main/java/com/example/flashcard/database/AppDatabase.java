package com.example.flashcard.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.flashcard.database.dao.QuizDao;
import com.example.flashcard.database.entities.Answer;
import com.example.flashcard.database.entities.Category;
import com.example.flashcard.database.entities.Question;

import java.util.concurrent.Executors;

@Database(entities = { Category.class, Question.class, Answer.class }, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract QuizDao quizDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "flashcard_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void preload(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = getInstance(context);
            if (db.quizDao().getAllCategories().isEmpty()) {
                populateDatabase(db.quizDao());
            }
        });
    }

    private static void populateDatabase(QuizDao dao) {
        // ==========================================
        // 1. ZWIERZĘTA (10 pytań)
        // ==========================================
        long animalsId = dao.insertCategory(new Category("Zwierzęta"));
        insertQuestion(dao, animalsId, "Które zwierzę jest najszybsze na lądzie?", "Gepard", "Lew", "Zając",
                "Antylopa");
        insertQuestion(dao, animalsId, "Co jedzą pandy?", "Bambus", "Mięso", "Owoce", "Ryby");
        insertQuestion(dao, animalsId, "Ile nóg ma pająk?", "8", "6", "4", "10");
        insertQuestion(dao, animalsId, "Który ptak nie lata?", "Struś", "Orzeł", "Wróbel", "Gołąb");
        insertQuestion(dao, animalsId, "Największy ssak to:", "Płetwal błękitny", "Słoń", "Żyrafa", "Nosorożec");
        insertQuestion(dao, animalsId, "Ile serc ma ośmiornica?", "3", "1", "2", "0");
        insertQuestion(dao, animalsId, "Który ssak potrafi latać?", "Nietoperz", "Wiewiórka", "Struś", "Pingwin");
        insertQuestion(dao, animalsId, "Co jest przysmakiem koali?", "Eukaliptus", "Bambus", "Akacja", "Trawa");
        insertQuestion(dao, animalsId, "Ile żołądków ma krowa?", "4", "1", "2", "3");
        insertQuestion(dao, animalsId, "Które zwierzę śpi na stojąco?", "Koń", "Pies", "Kot", "Królik");

        // ==========================================
        // 2. GEOGRAFIA (10 pytań)
        // ==========================================
        long geoId = dao.insertCategory(new Category("Geografia"));
        insertQuestion(dao, geoId, "Stolica Polski to?", "Warszawa", "Kraków", "Gdańsk", "Poznań");
        insertQuestion(dao, geoId, "Najdłuższa rzeka świata?", "Amazonka", "Nil", "Wisła", "Missisipi");
        insertQuestion(dao, geoId, "Ile kontynentów jest na Ziemi?", "7", "6", "5", "8");
        insertQuestion(dao, geoId, "W jakim kraju leży Paryż?", "Francja", "Hiszpania", "Włochy", "Niemcy");
        insertQuestion(dao, geoId, "Najwyższa góra świata?", "Mount Everest", "K2", "Rysy", "Kilimandżaro");
        insertQuestion(dao, geoId, "Stolica USA to:", "Waszyngton", "Nowy Jork", "Los Angeles", "Chicago");
        insertQuestion(dao, geoId, "Największy ocean to:", "Pacyfik", "Atlantyk", "Indyjski", "Arktyczny");
        insertQuestion(dao, geoId, "Gdzie leżą piramidy?", "Egipt", "Meksyk", "Chiny", "Indie");
        insertQuestion(dao, geoId, "Stolica Japonii to:", "Tokio", "Pekin", "Seul", "Bangkok");
        insertQuestion(dao, geoId, "Które państwo ma największą powierzchnię?", "Rosja", "Kanada", "Chiny", "USA");

        // ==========================================
        // 3. HISTORIA (10 pytań)
        // ==========================================
        long histId = dao.insertCategory(new Category("Historia"));
        insertQuestion(dao, histId, "W którym roku była Bitwa pod Grunwaldem?", "1410", "1939", "1000", "1525");
        insertQuestion(dao, histId, "Kto był pierwszym królem Polski?", "Bolesław Chrobry", "Mieszko I",
                "Kazimierz Wielki", "Władysław Jagiełło");
        insertQuestion(dao, histId, "Rozpoczęcie II Wojny Światowej?", "1939", "1914", "1945", "1920");
        insertQuestion(dao, histId, "Kto odkrył Amerykę?", "Krzysztof Kolumb", "Vasco da Gama", "Magellan", "Kopernik");
        insertQuestion(dao, histId, "Pierwszy człowiek na księżycu?", "Neil Armstrong", "Jurij Gagarin", "Buzz Aldrin",
                "Mirosław Hermaszewski");
        insertQuestion(dao, histId, "Kto namalował Mona Lisę?", "Da Vinci", "Picasso", "Van Gogh", "Matejko");
        insertQuestion(dao, histId, "Rok odzyskania niepodległości PL?", "1918", "1945", "1795", "1989");
        insertQuestion(dao, histId, "Bogini mądrości to:", "Atena", "Wenus", "Hera", "Afrodyta");
        insertQuestion(dao, histId, "Kto zbudował piramidy?", "Egipcjanie", "Grecy", "Rzymianie", "Persowie");
        insertQuestion(dao, histId, "Która wojna trwała 100 lat?", "Angielsko-francuska", "Polsko-krzyżacka",
                "Grecko-perska", "Rzymsko-punicka");

        // ==========================================
        // 4. NAUKA (10 pytań)
        // ==========================================
        long scienceId = dao.insertCategory(new Category("Nauka"));
        insertQuestion(dao, scienceId, "Ile planet jest w Układzie Słonecznym?", "8", "9", "7", "10");
        insertQuestion(dao, scienceId, "Jaki gaz jest niezbędny do oddychania?", "Tlen", "Azot", "Wodór", "Hel");
        insertQuestion(dao, scienceId, "Symbol chemiczny wody to:", "H2O", "CO2", "O2", "NaCl");
        insertQuestion(dao, scienceId, "Ile kości ma dorosły człowiek?", "206", "306", "106", "256");
        insertQuestion(dao, scienceId, "Która planeta jest najbliżej Słońca?", "Merkury", "Wenus", "Mars", "Ziemia");
        insertQuestion(dao, scienceId, "Prędkość światła wynosi około:", "300 000 km/s", "30 000 km/s", "3 000 km/s",
                "3 000 000 km/s");
        insertQuestion(dao, scienceId, "DNA to skrót od:", "Kwas dezoksyrybonukleinowy", "Kwas rybonukleinowy",
                "Dwuazot", "Dwutlenek");
        insertQuestion(dao, scienceId, "Ile chromów ma człowiek?", "46", "23", "48", "44");
        insertQuestion(dao, scienceId, "Która planeta ma pierścienie?", "Saturn", "Jowisz", "Mars", "Wenus");
        insertQuestion(dao, scienceId, "Co wynalazł Thomas Edison?", "Żarówkę", "Telefon", "Radio", "Telewizor");

        // ==========================================
        // 5. SPORT (10 pytań)
        // ==========================================
        long sportId = dao.insertCategory(new Category("Sport"));
        insertQuestion(dao, sportId, "Ile graczy ma drużyna piłkarska?", "11", "10", "12", "9");
        insertQuestion(dao, sportId, "Ile setów gra się w tenisie (mężczyźni, Grand Slam)?", "5", "3", "4", "6");
        insertQuestion(dao, sportId, "Który kraj wygrał najwięcej MŚ w piłce nożnej?", "Brazylia", "Niemcy", "Włochy",
                "Argentyna");
        insertQuestion(dao, sportId, "Ile punktów wart jest touchdown w futbolu?", "6", "3", "7", "1");
        insertQuestion(dao, sportId, "Który sport używa krążka?", "Hokej", "Curling", "Golf", "Tenis");
        insertQuestion(dao, sportId, "Ile okrążeń ma tor 400m na stadionie?", "1", "2", "4", "0.5");
        insertQuestion(dao, sportId, "Robert Lewandowski gra na jakiej pozycji?", "Napastnik", "Bramkarz", "Obrońca",
                "Pomocnik");
        insertQuestion(dao, sportId, "Ile rund ma walka bokserska (zawodowa)?", "12", "10", "15", "8");
        insertQuestion(dao, sportId, "Która dyscyplina NIE jest olimpijska?", "Squash", "Badminton", "Tenis stołowy",
                "Siatkówka");
        insertQuestion(dao, sportId, "Kto zdobył najwięcej Złotych Piłek?", "Messi", "Ronaldo", "Platini", "Cruyff");

        // ==========================================
        // 6. FILMY I SERIALE (10 pytań)
        // ==========================================
        long moviesId = dao.insertCategory(new Category("Filmy i Seriale"));
        insertQuestion(dao, moviesId, "Kto reżyserował 'Titanica'?", "James Cameron", "Steven Spielberg",
                "Christopher Nolan", "Martin Scorsese");
        insertQuestion(dao, moviesId, "Ile filmów ma seria 'Harry Potter'?", "8", "7", "9", "6");
        insertQuestion(dao, moviesId, "Kto grał Jokera w 'Mroczny Rycerz'?", "Heath Ledger", "Joaquin Phoenix",
                "Jack Nicholson", "Jared Leto");
        insertQuestion(dao, moviesId, "W którym roku wyszedł pierwszy 'Star Wars'?", "1977", "1980", "1983", "1975");
        insertQuestion(dao, moviesId, "Jak ma na imię główny bohater 'Shreka'?", "Shrek", "Osioł", "Fiona", "Kot");
        insertQuestion(dao, moviesId, "Ile sezonów ma 'Gra o Tron'?", "8", "7", "9", "10");
        insertQuestion(dao, moviesId, "Kto grał Iron Mana w MCU?", "Robert Downey Jr.", "Chris Evans",
                "Chris Hemsworth", "Mark Ruffalo");
        insertQuestion(dao, moviesId, "Który film zdobył Oscara 2024 za najlepszy film?", "Oppenheimer", "Barbie",
                "Killers of the Flower Moon", "Poor Things");
        insertQuestion(dao, moviesId, "Ile jest filmów 'Szybcy i Wściekli' (do 2024)?", "10", "8", "9", "11");
        insertQuestion(dao, moviesId, "Kto stworzył Myszką Miki?", "Walt Disney", "Pixar", "DreamWorks", "Warner Bros");

        // ==========================================
        // 7. TECHNOLOGIA (10 pytań)
        // ==========================================
        long techId = dao.insertCategory(new Category("Technologia"));
        insertQuestion(dao, techId, "Kto założył Apple?", "Steve Jobs", "Bill Gates", "Elon Musk", "Mark Zuckerberg");
        insertQuestion(dao, techId, "Ile bitów ma 1 bajt?", "8", "16", "4", "2");
        insertQuestion(dao, techId, "Co oznacza skrót WWW?", "World Wide Web", "Wide World Web", "Web World Wide",
                "World Web Wide");
        insertQuestion(dao, techId, "Który język programowania stworzył Google?", "Go", "Java", "Python", "C++");
        insertQuestion(dao, techId, "W którym roku powstał Facebook?", "2004", "2006", "2008", "2002");
        insertQuestion(dao, techId, "Co to jest RAM?", "Pamięć operacyjna", "Dysk twardy", "Procesor",
                "Karta graficzna");
        insertQuestion(dao, techId, "Kto jest CEO Tesli?", "Elon Musk", "Jeff Bezos", "Tim Cook", "Sundar Pichai");
        insertQuestion(dao, techId, "Android został stworzony przez:", "Google", "Apple", "Microsoft", "Samsung");
        insertQuestion(dao, techId, "Ile wynoszi 2^10?", "1024", "1000", "512", "2048");
        insertQuestion(dao, techId, "HTML to język do:", "Tworzenia stron WWW", "Programowania", "Baz danych",
                "Grafiki");

        // ==========================================
        // 8. JĘZYK POLSKI (10 pytań)
        // ==========================================
        long polishId = dao.insertCategory(new Category("Język Polski"));
        insertQuestion(dao, polishId, "Kto napisał 'Pana Tadeusza'?", "Adam Mickiewicz", "Juliusz Słowacki",
                "Henryk Sienkiewicz", "Bolesław Prus");
        insertQuestion(dao, polishId, "Ile jest przypadków w języku polskim?", "7", "6", "5", "8");
        insertQuestion(dao, polishId, "Która część mowy odpowiada na pytanie 'kto? co?'?", "Rzeczownik", "Czasownik",
                "Przymiotnik", "Przysłówek");
        insertQuestion(dao, polishId, "Kto napisał 'Quo Vadis'?", "Henryk Sienkiewicz", "Stefan Żeromski",
                "Władysław Reymont", "Bolesław Prus");
        insertQuestion(dao, polishId, "Co to jest metafora?", "Przenośnia", "Porównanie", "Epitet", "Onomatopeja");
        insertQuestion(dao, polishId, "Ile liter ma polski alfabet?", "32", "26", "30", "35");
        insertQuestion(dao, polishId, "Kto napisał 'Dziady'?", "Adam Mickiewicz", "Juliusz Słowacki",
                "Zygmunt Krasiński", "Cyprian Norwid");
        insertQuestion(dao, polishId, "Co to jest synonim?", "Wyraz bliskoznaczny", "Wyraz przeciwstawny", "Wyraz obcy",
                "Wyraz złożony");
        insertQuestion(dao, polishId, "Który czas opisuje zdarzenia przeszłe?", "Czas przeszły", "Czas teraźniejszy",
                "Czas przyszły", "Tryb rozkazujący");
        insertQuestion(dao, polishId, "Kto napisał 'Lalka'?", "Bolesław Prus", "Eliza Orzeszkowa", "Maria Konopnicka",
                "Stefan Żeromski");
    }

    private static void insertQuestion(QuizDao dao, long catId, String text, String correct, String w1, String w2,
            String w3) {
        long qId = dao.insertQuestion(new Question((int) catId, text));
        dao.insertAnswer(new Answer((int) qId, correct, true));
        dao.insertAnswer(new Answer((int) qId, w1, false));
        dao.insertAnswer(new Answer((int) qId, w2, false));
        dao.insertAnswer(new Answer((int) qId, w3, false));
    }
}
