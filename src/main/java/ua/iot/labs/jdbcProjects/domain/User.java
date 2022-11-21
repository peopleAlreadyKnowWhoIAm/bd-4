package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class User implements TablePrintable {
   Integer id;
   String name;
   String email;
   List<Integer> preferedGenres; 
   List<Integer> playlists;
   List<Integer> albums;
   List<Integer> songs;

   private static final String FORMAT = "%4s %25s %25s %20s %20s %20s %20s";

@Override
public String getHeader() {
	return String.format(FORMAT, "id", "name", "email", "genres", "playlists", "albums", "songs");
}

@Override
public String getRow() {
	return String.format(FORMAT, id.toString(), name, email, preferedGenres.toString(), playlists.toString(), albums.toString(), songs.toString());
}

}
