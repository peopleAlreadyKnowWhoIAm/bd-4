package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class Playlist implements TablePrintable {
    Integer id;
    String name;
    Integer userId;
    List<Integer> songs;

    private static final String FORMAT = "%4s %20s %4s %35s %n";

	@Override
	public String getHeader() {
		return String.format(FORMAT, "id", "name", "user", "songs ids");
	}

	@Override
	public String getRow() {
		return String.format(FORMAT, id.toString(), name, userId.toString(), songs.toString());
	}
}
