package ua.iot.labs.jdbcProjects.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.utils.TablePrintable;

@Data
@AllArgsConstructor
public class Album implements TablePrintable {
    Integer id;
    String name;
    List<Integer> authors;
    Integer yearOfPublishing;
    Optional<Integer> label = Optional.empty();
    BigDecimal price;
    Integer numberOfDownloads;

    private final String FORMAT = "%4s %30s %40s %5s %4s %6s %8s%n";

    @Override
    public String getHeader() {
        return String.format(FORMAT, "id", "name", "authors", "year", "labl", "pric", "downloads");
    }

    @Override
    public String getRow() {
        return String.format(FORMAT, id.toString(), name, authors.toString(), yearOfPublishing.toString(),
                label.orElse(-1).toString(),
                price.toString(), numberOfDownloads.toString());
    }
}
