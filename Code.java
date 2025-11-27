import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WeatherAssignment {

    static class WeatherRecord {
        LocalDate date;
        String city;
        double temperature;

        WeatherRecord(LocalDate date, String city, double temperature) {
            this.date = date;
            this.city = city;
            this.temperature = temperature;
        }

        @Override
        public String toString() {
            return String.format("%s %d -> %.2f°C",
                    city, date.getYear(), temperature);
        }
    }

    static class Loader implements Runnable {
        private final String message;
        Loader(String message) { this.message = message; }
        @Override
        public void run() {
            try {
                System.out.print(message);
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(150);
                    System.out.print(".");
                }
                System.out.println();
            } catch (InterruptedException ignored) {}
        }
    }

    private final List<String> cities;
    private final int startYear;
    private final int endYear;
    private final double[][] dense;
    private final Map<String, Double> sparse;
    private final Map<String, Integer> cityToIndex;
    private final Scanner sc;

    public WeatherAssignment(List<String> cities, int startYear, int endYear) {
        this.cities = new ArrayList<>(cities);
        this.startYear = startYear;
        this.endYear = endYear;
        int rows = endYear - startYear + 1;
        int cols = cities.size();
        dense = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            Arrays.fill(dense[i], Double.NaN);
        sparse = new HashMap<>();
        cityToIndex = new HashMap<>();
        for (int i = 0; i < cities.size(); i++) cityToIndex.put(cities.get(i).toLowerCase(), i);
        sc = new Scanner(System.in);
    }

    private boolean validYear(int y) { return y >= startYear && y <= endYear; }

    private Integer cityIndex(String city) {
        if (city == null) return null;
        return cityToIndex.get(city.toLowerCase());
    }

    private String sparseKey(int year, String city) {
        return year + "-" + city.toLowerCase();
    }

    public void interactiveMenu() {
        System.out.println("=== Weather Data System ===");
        System.out.println("Available cities: " + cities);
        while (true) {
            System.out.println();
            System.out.print("Choose option: 1=Retrieve  2=Insert  3=Delete  4=RowTraversal 5=ColTraversal  6=Analysis  7=Exit : ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1" -> handleRetrieve();
                case "2" -> handleInsert();
                case "3" -> handleDelete();
                case "4" -> performRowTraversal(true);
                case "5" -> performColumnTraversal(true);
                case "6" -> showComplexityAndSpace();
                case "7" -> { shutdown(); return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void handleRetrieve() {
        String city = promptCity();
        if (city == null) return;
        Integer y = promptYear();
        if (y == null) return;

        int r = y - startYear;
        Integer c = cityIndex(city);
        if (c == null) { System.out.println("Unknown city."); return; }

        double denseVal = dense[r][c];
        String key = sparseKey(y, city);
        Double sparseVal = sparse.get(key);

        if (Double.isNaN(denseVal)) System.out.println("[Dense] No record found for " + city + " in " + y);
        else System.out.printf("[Dense] %s %d -> %.2f°C%n", city, y, denseVal);

        if (sparseVal == null) System.out.println("[Sparse] No record found for " + city + " in " + y);
        else System.out.printf("[Sparse] %s %d -> %.2f°C%n", city, y, sparseVal);
    }

    private void handleInsert() {
        String city = promptCity();
        if (city == null) return;
        Integer y = promptYear();
        if (y == null) return;
        Double temp = promptTemperature();
        if (temp == null) return;

        int r = y - startYear;
        Integer c = cityIndex(city);
        if (c == null) { System.out.println("Unknown city."); return; }

        runLoader("Inserting");

        dense[r][c] = temp;
        sparse.put(sparseKey(y, city), temp);
        System.out.printf("Inserted %s %d -> %.2f°C%n", city, y, temp);
    }

    private void handleDelete() {
        String city = promptCity();
        if (city == null) return;
        Integer y = promptYear();
        if (y == null) return;

        int r = y - startYear;
        Integer c = cityIndex(city);
        if (c == null) { System.out.println("Unknown city."); return; }

        runLoader("Deleting");

        dense[r][c] = Double.NaN;
        Double removed = sparse.remove(sparseKey(y, city));
        if (removed == null) System.out.println("No record to delete.");
        else System.out.println("Deleted " + city + " " + y);
    }

    private void performRowTraversal(boolean printValues) {
        int rows = dense.length, cols = dense[0].length;
        long start = System.nanoTime();
        for (int i = 0; i < rows; i++) {
            int year = startYear + i;
            for (int j = 0; j < cols; j++) {
                double v = dense[i][j];
                if (printValues && !Double.isNaN(v)) {
                    System.out.printf("[Row] %s %d -> %.2f°C%n", cities.get(j), year, v);
                }
            }
        }
        long end = System.nanoTime();
        long ms = TimeUnit.NANOSECONDS.toMillis(end - start);
        System.out.println("Row-major traversal time: " + ms + " ms");
    }

    private void performColumnTraversal(boolean printValues) {
        int rows = dense.length, cols = dense[0].length;
        long start = System.nanoTime();
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                double v = dense[i][j];
                if (printValues && !Double.isNaN(v)) {
                    System.out.printf("[Col] %s %d -> %.2f°C%n", cities.get(j), startYear + i, v);
                }
            }
        }
        long end = System.nanoTime();
        long ms = TimeUnit.NANOSECONDS.toMillis(end - start);
        System.out.println("Column-major traversal time: " + ms + " ms");
    }

    private void showComplexityAndSpace() {
        System.out.println("\n--- Complexity Analysis ---");
        System.out.println("Insert: O(1)");
        System.out.println("Delete: O(1)");
        System.out.println("Retrieve: O(1)");
        System.out.println("Row/Column Traversal: O(R × C)");

        int rows = dense.length;
        int cols = dense[0].length;
        int denseCells = rows * cols;
        int sparseEntries = sparse.size();

        long denseBytes = (long) denseCells * Double.BYTES;
        System.out.println("\n--- Space Analysis (approx) ---");
        System.out.println("Dense cells (R×C): " + denseCells);
        System.out.println("Approx memory (dense): " + denseBytes + " bytes (" + denseCells + " doubles)");
        System.out.println("Sparse entries (K): " + sparseEntries);
        System.out.println("Approx memory (sparse): O(K) + key overhead (strings)");

        System.out.println("\n--- Traversal timing comparison (print suppressed) ---");
        long t1 = measureRowTraversal();
        long t2 = measureColumnTraversal();
        System.out.println("Row-major (no-print): " + t1 + " ms");
        System.out.println("Column-major (no-print): " + t2 + " ms");
    }

    private long measureRowTraversal() {
        long start = System.nanoTime();
        performRowTraversal(false);
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    private long measureColumnTraversal() {
        long start = System.nanoTime();
        performColumnTraversal(false);
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    private void runLoader(String msg) {
        Thread t = new Thread(new Loader(msg));
        t.start();
        try { t.join(); } catch (InterruptedException ignored) {}
    }

    private String promptCity() {
        System.out.print("Enter city: ");
        String city = sc.nextLine().trim();
        if (city.isEmpty()) { System.out.println("City cannot be empty."); return null; }
        if (!cityToIndex.containsKey(city.toLowerCase())) {
            System.out.println("City not in available list.");
            return null;
        }
        for (String c : cities) if (c.equalsIgnoreCase(city)) return c;
        return city;
    }

    private Integer promptYear() {
        System.out.print("Enter year (" + startYear + "-" + endYear + "): ");
        String ys = sc.nextLine().trim();
        try {
            int y = Integer.parseInt(ys);
            if (!validYear(y)) { System.out.println("Year out of range."); return null; }
            return y;
        } catch (NumberFormatException e) {
            System.out.println("Invalid year."); return null;
        }
    }

    private Double promptTemperature() {
        System.out.print("Enter temperature: ");
        String ts = sc.nextLine().trim();
        try {
            return Double.parseDouble(ts);
        } catch (NumberFormatException e) {
            System.out.println("Invalid temperature."); return null;
        }
    }

    private void shutdown() {
        System.out.println("Exiting. Final complexity & space summary:");
        showComplexityAndSpace();
        sc.close();
    }

    public static void main(String[] args) {
        List<String> cities = Arrays.asList("Delhi", "Mumbai", "Chennai", "Kolkata", "Bengaluru");
        WeatherAssignment app = new WeatherAssignment(cities, 2021, 2025);

        app.dense[2021 - app.startYear][0] = 26.3;
        app.sparse.put(app.sparseKey(2021, "Delhi"), 26.3);
        app.dense[2022 - app.startYear][1] = 28.5;
        app.sparse.put(app.sparseKey(2022, "Mumbai"), 28.5);

        app.interactiveMenu();
    }
}
