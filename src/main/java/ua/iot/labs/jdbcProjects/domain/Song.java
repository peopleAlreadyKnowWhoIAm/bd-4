package ua.iot.labs.jdbcProjects.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class Song implements TablePrintable {
    Integer id;
    String name;
    Integer length;
    List<Integer> authors;
    Optional<Integer> album = Optional.empty();
    Integer genre;
    BigDecimal price;
    Integer numberOfDownloads;

    private static final String FORMAT = "%4s %35s %5s %20s %5s %4s %4s %8s%n";

    @Override
    public String getHeader() {
        return String.format(FORMAT, "id", "name", "length", "authors", "album", "genr", "pric", "downloads");
    }

    @Override
    public String getRow() {
        return String.format(FORMAT, id.toString(), name, String.format("%d:%d", length / 60, length % 60),
                authors.toString(),album.orElse(-1).toString(), genre.toString(), price.toString(), numberOfDownloads.toString());
    }

}
