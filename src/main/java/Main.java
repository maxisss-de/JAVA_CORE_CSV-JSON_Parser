import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvBeanIntrospectionException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};     // 1. Cоздал массив строчек columnMapping, содержащий информацию о предназначении колонок в CVS файле.

        String fileName = "data.csv";                                                  // 2. Определили имя для считываемого CSV файла
        List<Employee> list = parseCSV(columnMapping, fileName);                       // 3.1 То есть в метод parseCSV(), прочитали файл и передали его в массив (№А) "List<Employee> list" где мы будем хранить список сотрудников. / "columnMapping" - это номер см. 1. мы передали ; "fileName" - это номер 2. мы передали.
        writeString(list);                                                             // 3.2 В метод writeString() в скобки передали json (который стоит в 12.1.)
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> users = new ArrayList<>();                                                     // 4.1. Создал новый ArrayList<> в return будем возращать staff
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {                            // 4.2 Передал конструктор файловый ридер FileReader файла fileName. Создаём CSVReader, который принимает в (FileReader()) и в FileReader в скобках указываем файл, который нам нужно прочитать "fileName" - это см.: 2. "data.csv"
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>(); // 6. Класса ColumnPositionMappingStrategy. Создаём класс new ColumnPositionMappingStrategy<>(), тут класс джинализированный <T> дженерик-  <Employee>
            strategy.setType(Employee.class);                                                         // 7. Используя объект стратегии, указал тип setType(). В этой стратегии мы задаём тип: "strategy.setType()" , то есть Employee.class
            strategy.setColumnMapping(columnMapping);                                                 // 8. Используя объект стратегии, указал тип setType() и тип колонок setColumnMapping()
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)                          // 9. Далее создал экземпляр CsvToBean с использованием билдера CsvToBeanBuilder. При постройке CsvToBean использовал ранее созданный объект стратегии ColumnPositionMappingStrategy. Нам нужен CsvToBean, потому-что мы читаем из csv. Bean во многих случаях обозначает, что мы хотим менять объект java. Тут опять используем new CsvToBeanBuilder, то есть Builder в который мы передаём в скобки () объект CSV (reader)
                    .withMappingStrategy(strategy)
                    .build();
            users = csv.parse();
        } catch (IOException | CsvBeanIntrospectionException e) {                                                                     // 5. try-catch с ресурсами
            e.printStackTrace();
        }
        return users;
    }

    public static String listToJson(List<Employee> list) {             // 11. Метод listToJson() -этот метод преобразуйт в строчку в формате JSON.
        GsonBuilder builder = new GsonBuilder();                       // 13.1. Создал объект типа GsonBuilder. Создайм "GsonBuilder = new GsonBuilder()"
        Gson gson = builder.create();                                   // 13.2. Создал объект Gson
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();  // 14. Преобразования списка объектов в JSON, требуется определить тип этого спика, сделал
        String json = gson.toJson(list, listType);                     // 15. Получить JSON из экземпляра класса Gson можно с помощтю метода toJson(), передав в качестве аргументов список сотрудников и тип списка:
        return json;
    }

    public static void writeString(List<Employee> list) throws CsvBeanIntrospectionException {
        String json = listToJson(list);                 // 12.1. В метод listToJson мы в скобки передали массив (list), который (см. 3.1 №А) \ Метод listToJson() -это преобразуйт в строчку в формате JSON и передаём всё в "String json" - (точнее "json"), теперь у нас преобразование в JSON храниться в "json"
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(String.valueOf(json));           // 12.2. Тут в скобках указываем, что в "String json"
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
