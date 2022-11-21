package ua.iot.labs.jdbcProjects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class Label implements TablePrintable {
    Integer id;
    String name;

    private static String FORMAT = "%4s %10s%n";
	@Override
	public String getHeader() {
		return String.format(FORMAT, "id", "name");
	}
	@Override
	public String getRow() {
		return String.format(FORMAT, id.toString(), name);
	}
}
