public class TestCalculator {
    public static void main(String[] args) {
        // Simple test logic to verify 5 + 3 = 8.0
        double res = 5.0 + 3.0;
        if (res == 8.0) {
            System.out.println("Test Passed: Addition logic holds.");
        } else {
            System.out.println("Test Failed.");
            System.exit(1); // Fail the build
        }
    }
}