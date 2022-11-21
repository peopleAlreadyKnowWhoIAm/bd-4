package ua.iot.labs.jdbcProjects.view;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ua.iot.labs.jdbcProjects.domain.Album;
import ua.iot.labs.jdbcProjects.domain.Author;
import ua.iot.labs.jdbcProjects.domain.Genre;
import ua.iot.labs.jdbcProjects.domain.Label;
import ua.iot.labs.jdbcProjects.domain.Playlist;
import ua.iot.labs.jdbcProjects.domain.Song;
import ua.iot.labs.jdbcProjects.domain.User;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;
import ua.iot.labs.jdbcProjects.services.SongService;
import ua.iot.labs.jdbcProjects.services.UserService;

@Service
@AllArgsConstructor
public class ViewController {
    final SongService songs;
    final UserService users;

    final static LinkedHashMap<String, String> menuOptions = new LinkedHashMap<>(Stream.of(new String[][] {
            { "1", "Operations with albums" },
            { "11", "Write all" },
            { "12", "Find by id" },
            { "13", "Find by name" },
            { "14", "Create" },
            { "15", "Update" },
            { "16", "Delete by id" },
            { "2", "Operations with authors" },
            { "21", "Write all" },
            { "22", "Find by id" },
            { "23", "Find by name" },
            { "24", "Create" },
            { "25", "Update" },
            { "26", "Delete by id" },
            { "3", "Operations with genres" },
            { "31", "Write all" },
            { "32", "Find by id" },
            { "33", "Find by name" },
            { "34", "Create" },
            { "35", "Update" },
            { "36", "Delete by id" },
            { "4", "Operations with labels" },
            { "41", "Write all" },
            { "42", "Find by id" },
            { "43", "Find by name" },
            { "44", "Create" },
            { "45", "Update" },
            { "46", "Delete by id" },
            { "5", "Operations with songs" },
            { "51", "Write all" },
            { "52", "Find by id" },
            { "53", "Find by name" },
            { "54", "Find by genre id" },
            { "55", "Create" },
            { "56", "Update" },
            { "57", "Delete by id" },
            { "6", "Operations with users" },
            { "61", "Write all" },
            { "62", "Find by id" },
            { "63", "Find by name" },
            { "64", "Find by email" },
            { "65", "Create" },
            { "66", "Update" },
            { "67", "Delete by id" },
            { "7", "Operations with playlists" },
            { "71", "Write all" },
            { "72", "Find by id" },
            { "73", "Find by name" },
            { "74", "Find by user id" },
            { "75", "Create" },
            { "76", "Update" },
            { "77", "Delete by id" },
            { "0", "Exit" } }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    final Map<String, CommandLineComponent> functions = Stream.of(
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("11", this::writeAllAlbums),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("12", this::findByIdAlbums),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("13", this::findByNameAlbums),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("14", this::createAlbum),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("15", this::updateAlbum),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("16", this::deleteAlbum),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("21", this::writeAllAuthors),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("22", this::findByIdAuthor),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("23", this::findByNameAuthors),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("24", this::createAuthor),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("25", this::updateAuthor),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("26", this::deleteAuthor),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("31", this::writeAllGenres),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("32", this::findByIdGenre),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("33", this::findByNameGenres),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("34", this::createGenre),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("35", this::updateGenre),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("36", this::deleteGenre),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("41", this::writeAllLabels),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("42", this::findByIdLabel),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("43", this::findByNameLabel),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("44", this::createLabel),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("45", this::updateLabel),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("46", this::deleteLabel),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("51", this::writeAllSongs),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("52", this::findByIdSongs),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("53", this::findByNameSong),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("54", this::findSongByGenreId),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("55", this::createSong),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("56", this::updateSong),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("57", this::deleteSong),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("61", this::writeAllUsers),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("62", this::findByIdUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("63", this::findByNameUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("64", this::findByEmailUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("65", this::createUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("66", this::updateUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("67", this::deleteUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("71", this::writeAllPlaylists),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("72", this::findByIdPlaylist),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("73", this::findByNamePlaylist),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("74", this::findPlaylistsByUser),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("75", this::createPlaylist),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("76", this::updatePlaylist),
            new AbstractMap.SimpleEntry<String, CommandLineComponent>("77", this::deletePlaylist))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    static Scanner commandLine = new Scanner(System.in);
    final static String FORMAT_STRING = "%3s  -  %s%n";
    private static final String NEW_LINE_SYMBOL = ">>> ";
    private static final String SAD_MESSAGE = "There isn't the object";

    public void run() {
        while (true) {
            menuOptions.forEach((k, v) -> {
                if (k.length() == 1) {
                    System.out.printf(FORMAT_STRING, k, v);
                }
            });
            System.out.print(NEW_LINE_SYMBOL);

            String inBuf = commandLine.nextLine();
            if (inBuf.length() == 2 && functions.containsKey(inBuf)) {
                functions.get(inBuf).print();
                continue;
            } else if (inBuf.length() == 1 && menuOptions.containsKey(inBuf)) {
                if (inBuf.compareTo("0") == 0)
                    break;

                System.out.println("Category " + menuOptions.get(inBuf));
                menuOptions.forEach((k, v) -> {
                    if (k.startsWith(inBuf) && k.length() == 2) {
                        System.out.printf(FORMAT_STRING, k, v);
                    }
                });

                System.out.print(NEW_LINE_SYMBOL);
                String inBuf2 = commandLine.nextLine();

                if (functions.containsKey(inBuf2)) {
                    functions.get(inBuf2).print();
                    continue;
                }

            }

            System.out.println("Invalid input");
        }
    }

    Integer getId() {
        System.out.println("Enter id: ");
        return integerScanner();
    }

    String getName() {
        System.out.println("Enter name: ");
        return stringScanner();
    }

    void unwrapOptional(Optional<? extends TablePrintable> opt) {
        if (opt.isPresent()) {
            System.out.println(opt.get().toString());
        } else {
            System.out.println(SAD_MESSAGE);
        }
    }

    void printList(List<? extends TablePrintable> list) {
        if (list.isEmpty()) {
            System.out.println(SAD_MESSAGE);
        } else {
            System.out.println(list.get(0).getHeader());
            for (TablePrintable tablePrintable : list) {
                System.out.println(tablePrintable.getRow());
            }
        }
    }

    // ---------------
    void writeAllAlbums() {
        printList(songs.findAllAlbums());
    }

    void findByIdAlbums() {
        Integer id = getId();
        Optional<Album> res = songs.findAlbumById(id);
        unwrapOptional(res);
    }

    void findByNameAlbums() {
        String name = getName();
        printList(songs.findAlbumsByName(name));
    }

    void createAlbum() {
        System.out.println("Enter name:");
        String name = stringScanner();

        System.out.println("Enter authors ids:");
        List<Integer> authors = integerListScanner();
        System.out.println("Enter year of publishing:");
        Integer year = integerScanner();
        System.out.println("Enter price:");
        BigDecimal price = bigDecimalScanner();
        System.out.println("Enter label id or empty line:");
        Optional<Integer> label = optionalIntegerScanner();
        System.out.println("Enter num of downloads:");
        Integer downloads = integerScanner();

        songs.create(new Album(null, name, authors, year, label, price, downloads));
    }

    void updateAlbum() {

        Integer id = getId();
        Optional<Album> res = songs.findAlbumById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Album out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            System.out.println("Enter authors ids (" + out.getAuthors().toString() + "):");
            List<Integer> authors = integerListScanner();
            System.out.println("Enter year of publishing (" + out.getYearOfPublishing().toString() + "):");
            Integer year = integerScanner();
            System.out.println("Enter price(" + out.getPrice().toString() + "):");
            BigDecimal price = bigDecimalScanner();
            System.out.println("Enter label id or empty line (" + out.getLabel().toString() + "):");
            Optional<Integer> label = optionalIntegerScanner();
            System.out.println("Enter num of downloads (" + out.getNumberOfDownloads().toString() + "):");
            Integer downloads = integerScanner();

            songs.update(new Album(out.getId(), name, authors, year, label, price, downloads));

        }

    }

    void deleteAlbum() {
        Integer id = getId();
        songs.deleteAlbum(id);
    }

    // -----------------------------------
    void writeAllAuthors() {
        printList(songs.findAllAuthors());
    }

    void findByIdAuthor() {
        Integer id = getId();
        Optional<Author> res = songs.findAuthorById(id);
        unwrapOptional(res);
    }

    void findByNameAuthors() {
        String name = getName();
        printList(songs.findAuthorsByName(name));
    }

    void createAuthor() {
        System.out.println("Enter name:");
        String name = stringScanner();

        songs.create(new Author(null, name));
    }

    void updateAuthor() {

        Integer id = getId();
        Optional<Author> res = songs.findAuthorById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Author out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            songs.update(new Author(out.getId(), name));

        }

    }

    void deleteAuthor() {
        Integer id = getId();
        songs.deleteAuthor(id);
    }

    void writeAllGenres() {
        printList(songs.findAllGenres());
    }

    void findByIdGenre() {
        Integer id = getId();
        Optional<Genre> res = songs.findGenreById(id);
        unwrapOptional(res);
    }

    void findByNameGenres() {
        String name = getName();
        printList(songs.findGenreByName(name));
    }

    void createGenre() {
        System.out.println("Enter name:");
        String name = stringScanner();

        System.out.println("Enter related genres ids:");
        List<Integer> related = integerListScanner();
        songs.create(new Genre(null, name, related));
    }

    void updateGenre() {

        Integer id = getId();
        Optional<Genre> res = songs.findGenreById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Genre out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            System.out.println("Enter authors ids (" + out.getRelatedGenresId().toString() + "):");
            List<Integer> related = integerListScanner();

            songs.update(new Genre(out.getId(), name, related));

        }

    }

    void deleteGenre() {
        Integer id = getId();
        songs.deleteGenre(id);
    }

    void writeAllLabels() {
        printList(songs.findAllLabels());
    }

    void findByIdLabel() {
        Integer id = getId();
        Optional<Label> res = songs.findLabelById(id);
        unwrapOptional(res);
    }

    void findByNameLabel() {
        String name = getName();
        printList(songs.findLabelByName(name));
    }

    void createLabel() {
        System.out.println("Enter name:");
        String name = stringScanner();

        songs.create(new Label(null, name));
    }

    void updateLabel() {

        Integer id = getId();
        Optional<Label> res = songs.findLabelById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Label out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            songs.update(new Label(out.getId(), name));

        }

    }

    void deleteLabel() {
        Integer id = getId();
        songs.deleteLabel(id);
    }

    // -----------------------------------
    void writeAllSongs() {
        printList(songs.findAllSongs());
    }

    void findByIdSongs() {
        Integer id = getId();
        Optional<Song> res = songs.findSongById(id);
        unwrapOptional(res);
    }

    void findSongByGenreId() {
        Integer id = getId();
        printList(songs.findSongsByGenreId(id));
    }

    void findByNameSong() {
        String name = getName();
        printList(songs.findSongByName(name));
    }

    void createSong() {
        System.out.println("Enter name:");
        String name = stringScanner();

        System.out.println("Enter authors ids:");
        List<Integer> authors = integerListScanner();
        System.out.println("Enter length in seconds:");
        Integer length = integerScanner();
        System.out.println("Enter genre id:");
        Integer genre = integerScanner();
        System.out.println("Enter price:");
        BigDecimal price = bigDecimalScanner();
        System.out.println("Enter album id or empty line:");
        Optional<Integer> album = optionalIntegerScanner();
        System.out.println("Enter num of downloads:");
        Integer downloads = integerScanner();

        songs.create(new Song(null, name, length, authors, album, genre, price, downloads));
    }

    void updateSong() {

        Integer id = getId();
        Optional<Song> res = songs.findSongById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Song out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            System.out.println("Enter authors ids (" + out.getAuthors().toString() + "):");
            List<Integer> authors = integerListScanner();
            System.out.println("Enter length in seconds (" + out.getLength().toString() + "):");
            Integer length = integerScanner();
            System.out.println("Enter genre id (" + out.getGenre().toString() + "):");
            Integer genre = integerScanner();
            System.out.println("Enter price(" + out.getPrice().toString() + "):");
            BigDecimal price = bigDecimalScanner();
            System.out.println("Enter album id or empty line (" + out.getAlbum().toString() + "):");
            Optional<Integer> album = optionalIntegerScanner();
            System.out.println("Enter num of downloads (" + out.getNumberOfDownloads().toString() + "):");
            Integer downloads = integerScanner();

            songs.update(new Song(out.getId(), name, length, authors, album, genre, price, downloads));

        }

    }

    void deleteSong() {
        Integer id = getId();
        songs.deleteSongById(id);
    }

    // --------------------------------
    void writeAllUsers() {
        printList(users.findAllUsers());
    }

    void findByIdUser() {
        Integer id = getId();
        Optional<User> res = users.findUserById(id);
        unwrapOptional(res);
    }

    void findByEmailUser() {
        System.out.println("Enter email:");
        String email = stringScanner();
        unwrapOptional(users.findUserByEmail(email));
    }

    void findByNameUser() {
        String name = getName();
        printList(users.findUsersByName(name));
    }

    void createUser() {
        System.out.println("Enter name:");
        String name = stringScanner();
        System.out.println("Enter email:");
        String email = stringScanner();

        System.out.println("Enter prefered genre ids:");
        List<Integer> prefered = integerListScanner();

        System.out.println("Enter playlist ids:");
        List<Integer> playlists = integerListScanner();

        System.out.println("Enter album ids:");
        List<Integer> albums = integerListScanner();
        System.out.println("Enter song ids:");
        List<Integer> songs = integerListScanner();

        users.create(new User(null, name, email, prefered, playlists, albums, songs));
    }

    void updateUser() {

        Integer id = getId();
        Optional<User> res = users.findUserById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            User out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            System.out.println("Enter email (" + out.getEmail() + "):");
            String email = stringScanner();

            System.out.println("Enter prefered genres ids (" + out.getPreferedGenres().toString() + "):");
            List<Integer> prefered = integerListScanner();
            System.out.println("Enter playlists ids (" + out.getPlaylists().toString() + "):");
            List<Integer> playlists = integerListScanner();
            System.out.println("Enter album ids (" + out.getAlbums().toString() + "):");
            List<Integer> albums = integerListScanner();
            System.out.println("Enter song ids (" + out.getSongs().toString() + "):");
            List<Integer> songs = integerListScanner();

            users.update(new User(out.getId(), name, email, prefered, playlists, albums, songs));

        }

    }

    void deleteUser() {
        Integer id = getId();
        users.deleteUser(id);
    }

    void writeAllPlaylists() {
        printList(users.findAllPlaylists());
    }

    void findByIdPlaylist() {
        Integer id = getId();
        Optional<Playlist> res = users.findPlaylistsById(id);
        unwrapOptional(res);
    }

    void findPlaylistsByUser() {
        Integer userId = getId();
        printList(users.findPlaylistByUser(userId));
    }

    void findByNamePlaylist() {
        String name = getName();
        printList(users.findPlaylistsByName(name));
    }

    void createPlaylist() {
        System.out.println("Enter name:");
        String name = stringScanner();
        System.out.println("Enter user id:");
        Integer userId = integerScanner();

        System.out.println("Enter song ids:");
        List<Integer> songs = integerListScanner();

        users.create(new Playlist(null, name, userId, songs));
    }

    void updatePlaylist() {

        Integer id = getId();
        Optional<Playlist> res = users.findPlaylistsById(id);
        unwrapOptional(res);
        if (res.isPresent()) {
            Playlist out = res.get();
            System.out.println("Enter name (" + out.getName() + "):");
            String name = stringScanner();

            System.out.println("Enter user id (" + out.getUserId().toString() + "):");
            Integer userId = integerScanner();

            System.out.println("Enter song ids (" + out.getSongs().toString() + "):");
            List<Integer> songs = integerListScanner();

            users.update(new Playlist(out.getId(), name, userId, songs));

        }

    }

    void deletePlaylist() {
        Integer id = getId();
        users.deletePlaylist(id);
    }

    // ----------------------------------
    String stringScanner() {
        String buf;
        do {
            System.out.print(">>> ");
            buf = commandLine.nextLine();
            if (!buf.isBlank())
                break;
            System.out.println("Invalid string");
        } while (true);
        return buf;
    }

    Optional<Integer> optionalIntegerScanner() {
        Optional<Integer> out = Optional.empty();
        String buf = "";
        do {
            System.out.print(">>> ");
            buf = commandLine.nextLine();
            if (buf.isBlank())
                break;
            try {
                out = Optional.of(Integer.parseInt(buf));
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invlalid num");
            }

        } while (true);
        return out;
    }

    Integer integerScanner() {
        Integer out = 0;
        Optional<Integer> buf;
        do {
            buf = optionalIntegerScanner();
            if (buf.isPresent()) {
                out = buf.get();
                break;
            }
            System.out.println("Invlalid num");

        } while (true);
        return out;
    }

    List<Integer> integerListScanner() {
        System.out.println("Send empty line to stop entering");

        List<Integer> out = new LinkedList<>();
        String buf = "";
        do {
            System.out.print(">>> ");
            buf = commandLine.nextLine();
            if (buf.isBlank())
                break;
            try {
                Integer bufInt = Integer.parseInt(buf);
                out.add(bufInt);
            } catch (NumberFormatException e) {
                System.out.println("Invlalid num");
            }

        } while (true);

        return out;
    }

    BigDecimal bigDecimalScanner() {
        Double out = 0.0;
        String buf = "";
        do {
            System.out.print(">>> ");
            buf = commandLine.nextLine();
            try {
                out = Double.parseDouble(buf);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invlalid num");
            }

        } while (true);
        return BigDecimal.valueOf(out);
    }
}

@FunctionalInterface
interface CommandLineComponent {
    void print();
}