package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class Genre implements TablePrintable {
    Integer id;
    String name;
    List<Integer> relatedGenresId;

    private static String FORMAT = "%4s %30s %30s%n";

    @Override
    public String getHeader() {
        return String.format(FORMAT, "id", "name", "related genres");
    }

    @Override
    public String getRow() {
        return String.format(FORMAT, id.toString(), name, relatedGenresId.toString());
    }
}
