public class Calculator {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Calculator <op> <num1> <num2>");
            return; // Exit if not enough args (or handle as test case)
        }
        String op = args[0];
        double num1 = Double.parseDouble(args[1]);
        double num2 = Double.parseDouble(args[2]);
        double result = 0;

        switch (op) {
            case "add": result = num1 + num2; break;
            case "sub": result = num1 - num2; break;
            case "mul": result = num1 * num2; break;
            case "div": 
                if (num2 == 0) { System.out.println("Error: Div by zero"); return; }
                result = num1 / num2; 
                break;
            default: System.out.println("Invalid operation"); return;
        }
        System.out.println("Result: " + result);
    }
}