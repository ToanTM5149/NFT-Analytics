package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateIO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Lấy ngày tháng hiện tại chuyển sang dạng chuỗi format "dd/MM/yyyy"
    public static String getCurrentDateAsString() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(FORMATTER);
    }

    // Chuyển đổi giữa các dạng format ngày tháng và trả về String
    public static String formatDateAsString(LocalDate date) {
        return date.format(FORMATTER);
    }

    // Chuyển đổi từ chuỗi sang dạng LocalDate
    public static LocalDate parseStringToDate(String dateString, String inputFormat) {
    	DateTimeFormatter intputFormater = DateTimeFormatter.ofPattern(inputFormat);
        return LocalDate.parse(dateString, intputFormater);
    }
}